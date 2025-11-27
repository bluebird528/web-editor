package com.webeditor.api.security;

import com.webeditor.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for JwtTokenProvider.
 */
@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User testUser;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role("ROLE_USER")
                .build();

        authentication = new UsernamePasswordAuthenticationToken(testUser, null,
                testUser.getAuthorities());
    }

    @Test
    void whenGenerateToken_thenTokenIsGenerated() {
        String token = jwtTokenProvider.generateToken(authentication);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void whenValidateToken_thenReturnTrue() {
        String token = jwtTokenProvider.generateToken(authentication);

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void whenValidateInvalidToken_thenReturnFalse() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertThat(isValid).isFalse();
    }

    @Test
    void whenGetUsernameFromToken_thenReturnUsername() {
        String token = jwtTokenProvider.generateToken(authentication);

        String username = jwtTokenProvider.getUsernameFromToken(token);

        assertThat(username).isEqualTo("testuser");
    }

    @Test
    void whenGenerateTokenFromUsername_thenTokenIsGenerated() {
        String token = jwtTokenProvider.generateTokenFromUsername("testuser");

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();

        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);
        assertThat(extractedUsername).isEqualTo("testuser");
    }
}
