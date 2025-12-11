package com.pepperjacks.foodplatform.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleSignInRequestDto {
    
    @NotBlank(message = "Google ID token is required")
    private String idToken;
    
    private String deviceId;
    private String fcmToken;
}

