package com.springboot.coding.securityApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

        private Long userId;
//        private String username;
        private String accessToken;
        private String refreshToken;

}
