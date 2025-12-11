package com.pepperjacks.foodplatform.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private Boolean isActive;
    private LocalDateTime createdAt;
}

