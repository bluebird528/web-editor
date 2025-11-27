package com.webeditor.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for JWT response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String email;

    public JwtResponse(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }
}
