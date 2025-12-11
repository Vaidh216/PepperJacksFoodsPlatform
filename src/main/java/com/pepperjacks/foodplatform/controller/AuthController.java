package com.pepperjacks.foodplatform.controller;

import com.pepperjacks.foodplatform.dto.auth.*;
import com.pepperjacks.foodplatform.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody SendOtpRequestDto request) {
        authService.sendOtp(request);
        return ResponseEntity.ok("OTP sent successfully");
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponseDto> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto request) {
        AuthResponseDto response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/google-signin")
    public ResponseEntity<AuthResponseDto> googleSignIn(@Valid @RequestBody GoogleSignInRequestDto request) {
        AuthResponseDto response = authService.googleSignIn(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        AuthResponseDto response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequestDto request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }
}

