package com.webeditor.api.service;

import com.webeditor.api.dto.ContentRequest;
import com.webeditor.api.dto.ContentResponse;
import com.webeditor.api.entity.Content;
import com.webeditor.api.entity.User;
import com.webeditor.api.repository.ContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for ContentService.
 */
@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private ContentService contentService;

    private User testUser;
    private Content testContent;
    private ContentRequest contentRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role("ROLE_USER")
                .build();

        testContent = Content.builder()
                .id(1L)
                .title("Test Content")
                .body("This is a test content body")
                .status("DRAFT")
                .tags("test,unit")
                .author(testUser)
                .build();

        contentRequest = new ContentRequest();
        contentRequest.setTitle("Test Content");
        contentRequest.setBody("This is a test content body");
        contentRequest.setStatus("DRAFT");
        contentRequest.setTags("test,unit");
    }

    @Test
    void whenCreateContent_thenContentIsCreated() {
        when(contentRepository.save(any(Content.class))).thenReturn(testContent);

        ContentResponse response = contentService.createContent(contentRequest, testUser);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Content");
        assertThat(response.getAuthorUsername()).isEqualTo("testuser");
        verify(contentRepository).save(any(Content.class));
    }

    @Test
    void whenUpdateContent_thenContentIsUpdated() {
        when(contentRepository.findById(1L)).thenReturn(Optional.of(testContent));
        when(contentRepository.save(any(Content.class))).thenReturn(testContent);

        ContentResponse response = contentService.updateContent(1L, contentRequest, testUser);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Content");
        verify(contentRepository).findById(1L);
        verify(contentRepository).save(any(Content.class));
    }

    @Test
    void whenUpdateContentByUnauthorizedUser_thenThrowException() {
        User anotherUser = User.builder()
                .id(2L)
                .username("anotheruser")
                .email("another@example.com")
                .password("password123")
                .role("ROLE_USER")
                .build();

        when(contentRepository.findById(1L)).thenReturn(Optional.of(testContent));

        assertThatThrownBy(() -> contentService.updateContent(1L, contentRequest, anotherUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not authorized");
    }

    @Test
    void whenDeleteContent_thenContentIsDeleted() {
        when(contentRepository.findById(1L)).thenReturn(Optional.of(testContent));

        contentService.deleteContent(1L, testUser);

        verify(contentRepository).findById(1L);
        verify(contentRepository).delete(testContent);
    }

    @Test
    void whenGetContentById_thenReturnContent() {
        when(contentRepository.findById(1L)).thenReturn(Optional.of(testContent));

        ContentResponse response = contentService.getContentById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test Content");
        verify(contentRepository).findById(1L);
    }

    @Test
    void whenGetContentByIdNotFound_thenThrowException() {
        when(contentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contentService.getContentById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}
