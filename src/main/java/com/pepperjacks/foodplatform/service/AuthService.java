package com.pepperjacks.foodplatform.service;

import com.pepperjacks.foodplatform.dto.auth.*;

public interface AuthService {
    
    void sendOtp(SendOtpRequestDto request);
    
    AuthResponseDto verifyOtp(VerifyOtpRequestDto request);
    
    AuthResponseDto googleSignIn(GoogleSignInRequestDto request);
    
    AuthResponseDto refreshToken(RefreshTokenRequestDto request);
    
    void logout(String refreshToken);
}

