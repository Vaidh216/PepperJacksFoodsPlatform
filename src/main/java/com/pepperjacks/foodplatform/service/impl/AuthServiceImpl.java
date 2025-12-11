package com.pepperjacks.foodplatform.service.impl;

import com.pepperjacks.foodplatform.dto.auth.*;
import com.pepperjacks.foodplatform.model.*;
import com.pepperjacks.foodplatform.model.IdentityEntity.IdentityProvider;
import com.pepperjacks.foodplatform.repository.*;
import com.pepperjacks.foodplatform.service.AuthService;
import com.pepperjacks.foodplatform.util.JwtUtil;
import com.pepperjacks.foodplatform.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;
    private final OtpRepository otpRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final JwtUtil jwtUtil;
    private final OtpGenerator otpGenerator;
    
    @Value("${otp.validity-minutes}")
    private Integer otpValidityMinutes;
    
    @Value("${jwt.refresh-token-validity-ms}")
    private Long refreshTokenValidity;
    
    @Override
    @Transactional
    public void sendOtp(SendOtpRequestDto request) {
        String otp = otpGenerator.generateOtp();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(otpValidityMinutes);
        
        // Invalidate previous OTPs
        otpRepository.findTopByPhoneAndIsVerifiedFalseOrderByCreatedAtDesc(request.getPhoneNumber())
                .ifPresent(existingOtp -> {
                    existingOtp.setIsVerified(true);
                    otpRepository.save(existingOtp);
                });
        
        // Create new OTP
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setPhone(request.getPhoneNumber());
        otpEntity.setOtpCode(otp);
        otpEntity.setExpiresAt(expiresAt);
        otpEntity.setIsVerified(false);
        otpRepository.save(otpEntity);
        
        // TODO: Send OTP via Twilio/Msg91/Firebase SMS
        log.info("OTP generated for {}: {}", request.getPhoneNumber(), otp);
    }
    
    @Override
    @Transactional
    public AuthResponseDto verifyOtp(VerifyOtpRequestDto request) {
        Optional<OtpEntity> otpOpt = otpRepository.findByPhoneAndOtpCodeAndIsVerifiedFalse(
                request.getPhoneNumber(), request.getOtp());
        
        if (otpOpt.isEmpty()) {
            throw new RuntimeException("Invalid OTP");
        }
        
        OtpEntity otpEntity = otpOpt.get();
        
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }
        
        // Mark OTP as used
        otpEntity.setIsVerified(true);
        otpRepository.save(otpEntity);
        
        // Find or create user
        UserEntity user = findOrCreateUserByPhone(request.getPhoneNumber());
        
        // Find or create identity
        IdentityEntity identity = findOrCreateIdentity(user.getId(), IdentityProvider.PHONE, request.getPhoneNumber());
        identity.setIsVerified(true);
        identityRepository.save(identity);
        
        // Save FCM token if provided
        if (request.getFcmToken() != null && request.getDeviceId() != null) {
            saveDeviceToken(user.getId(), request.getDeviceId(), request.getFcmToken());
        }
        
        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getPhone(), "CUSTOMER");
        String refreshToken = generateRefreshToken(user.getId(), request.getDeviceId());
        
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900000L) // 15 minutes in ms
                .user(mapToUserDto(user))
                .build();
    }
    
    @Override
    @Transactional
    public AuthResponseDto googleSignIn(GoogleSignInRequestDto request) {
        // TODO: Verify Google ID token
        // For now, throw not implemented
        throw new RuntimeException("Google Sign-In not implemented yet");
    }
    
    @Override
    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findByTokenAndRevokedFalse(
                request.getRefreshToken());
        
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        RefreshTokenEntity token = tokenOpt.get();
        
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired");
        }
        
        UserEntity user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Generate new tokens
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getPhone(), "CUSTOMER");
        String newRefreshToken = generateRefreshToken(user.getId(), token.getDeviceId());
        
        // Revoke old refresh token
        token.setRevoked(true);
        refreshTokenRepository.save(token);
        
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(900000L)
                .user(mapToUserDto(user))
                .build();
    }
    
    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }
    
    private UserEntity findOrCreateUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setPhone(phone);
                    newUser.setIsActive(true);
                    return userRepository.save(newUser);
                });
    }
    
    private IdentityEntity findOrCreateIdentity(Long userId, IdentityProvider provider, String providerUserId) {
        return identityRepository.findByUserIdAndProvider(userId, provider)
                .orElseGet(() -> {
                    IdentityEntity identity = new IdentityEntity();
                    identity.setUserId(userId);
                    identity.setProvider(provider);
                    identity.setProviderUserId(providerUserId);
                    identity.setIsVerified(false);
                    return identityRepository.save(identity);
                });
    }
    
    private String generateRefreshToken(Long userId, String deviceId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plus(Duration.ofMillis(refreshTokenValidity));
        
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(token);
        refreshToken.setUserId(userId);
        refreshToken.setDeviceId(deviceId);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);
        
        return token;
    }
    
    private void saveDeviceToken(Long userId, String deviceId, String fcmToken) {
        Optional<DeviceTokenEntity> existingToken = deviceTokenRepository.findByUserIdAndDeviceId(userId, deviceId);
        
        if (existingToken.isPresent()) {
            DeviceTokenEntity token = existingToken.get();
            token.setFcmToken(fcmToken);
            token.setIsActive(true);
            deviceTokenRepository.save(token);
        } else {
            DeviceTokenEntity newToken = new DeviceTokenEntity();
            newToken.setUserId(userId);
            newToken.setDeviceId(deviceId);
            newToken.setFcmToken(fcmToken);
            newToken.setPlatform("ANDROID");
            newToken.setIsActive(true);
            deviceTokenRepository.save(newToken);
        }
    }
    
    private UserDto mapToUserDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
