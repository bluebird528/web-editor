package com.webeditor.api.repository;

import com.webeditor.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for UserRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role("ROLE_USER")
                .build();
    }

    @Test
    void whenSaveUser_thenUserIsSaved() {
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void whenFindByUsername_thenReturnUser() {
        userRepository.save(testUser);

        Optional<User> found = userRepository.findByUsername("testuser");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void whenExistsByUsername_thenReturnTrue() {
        userRepository.save(testUser);

        boolean exists = userRepository.existsByUsername("testuser");

        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByEmail_thenReturnTrue() {
        userRepository.save(testUser);

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }
}
