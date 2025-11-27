package com.webeditor.api.repository;

import com.webeditor.api.entity.Content;
import com.webeditor.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for ContentRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
class ContentRepositoryTest {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Content testContent;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role("ROLE_USER")
                .build();
        testUser = userRepository.save(testUser);

        testContent = Content.builder()
                .title("Test Content")
                .body("This is a test content body")
                .status("DRAFT")
                .tags("test,unit")
                .author(testUser)
                .build();
    }

    @Test
    void whenSaveContent_thenContentIsSaved() {
        Content savedContent = contentRepository.save(testContent);

        assertThat(savedContent).isNotNull();
        assertThat(savedContent.getId()).isNotNull();
        assertThat(savedContent.getTitle()).isEqualTo("Test Content");
        assertThat(savedContent.getCreatedAt()).isNotNull();
        assertThat(savedContent.getUpdatedAt()).isNotNull();
    }

    @Test
    void whenFindByAuthor_thenReturnContents() {
        contentRepository.save(testContent);

        Page<Content> contents = contentRepository.findByAuthor(testUser, PageRequest.of(0, 10));

        assertThat(contents).isNotEmpty();
        assertThat(contents.getContent().get(0).getAuthor().getUsername()).isEqualTo("testuser");
    }

    @Test
    void whenFindByStatus_thenReturnContents() {
        contentRepository.save(testContent);

        Page<Content> contents = contentRepository.findByStatus("DRAFT", PageRequest.of(0, 10));

        assertThat(contents).isNotEmpty();
        assertThat(contents.getContent().get(0).getStatus()).isEqualTo("DRAFT");
    }

    @Test
    void whenFindByTitleContaining_thenReturnContents() {
        contentRepository.save(testContent);

        Page<Content> contents = contentRepository.findByTitleContaining("Test",
                PageRequest.of(0, 10));

        assertThat(contents).isNotEmpty();
        assertThat(contents.getContent().get(0).getTitle()).contains("Test");
    }

    @Test
    void whenCountByAuthor_thenReturnCount() {
        contentRepository.save(testContent);

        long count = contentRepository.countByAuthor(testUser);

        assertThat(count).isEqualTo(1);
    }
}
