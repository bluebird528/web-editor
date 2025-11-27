package com.webeditor.api.service;

import com.webeditor.api.dto.ContentRequest;
import com.webeditor.api.dto.ContentResponse;
import com.webeditor.api.entity.Content;
import com.webeditor.api.entity.User;
import com.webeditor.api.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing content operations.
 */
@Service
@Transactional
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    /**
     * Create new content.
     *
     * @param request the content request
     * @param author  the author user
     * @return created content response
     */
    public ContentResponse createContent(ContentRequest request, User author) {
        Content content = Content.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .tags(request.getTags())
                .author(author)
                .build();

        Content savedContent = contentRepository.save(content);
        return ContentResponse.fromEntity(savedContent);
    }

    /**
     * Update existing content.
     *
     * @param id      the content id
     * @param request the content request
     * @param author  the author user
     * @return updated content response
     * @throws RuntimeException if content not found or unauthorized
     */
    public ContentResponse updateContent(Long id, ContentRequest request, User author) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));

        if (!content.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("You are not authorized to update this content");
        }

        content.setTitle(request.getTitle());
        content.setBody(request.getBody());
        content.setStatus(request.getStatus());
        content.setTags(request.getTags());

        Content updatedContent = contentRepository.save(content);
        return ContentResponse.fromEntity(updatedContent);
    }

    /**
     * Delete content.
     *
     * @param id     the content id
     * @param author the author user
     * @throws RuntimeException if content not found or unauthorized
     */
    public void deleteContent(Long id, User author) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));

        if (!content.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("You are not authorized to delete this content");
        }

        contentRepository.delete(content);
    }

    /**
     * Get content by id.
     *
     * @param id the content id
     * @return content response
     * @throws RuntimeException if content not found
     */
    @Transactional(readOnly = true)
    public ContentResponse getContentById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));
        return ContentResponse.fromEntity(content);
    }

    /**
     * Get all contents with pagination.
     *
     * @param pageable pagination information
     * @return page of content responses
     */
    @Transactional(readOnly = true)
    public Page<ContentResponse> getAllContents(Pageable pageable) {
        return contentRepository.findAll(pageable)
                .map(ContentResponse::fromEntity);
    }

    /**
     * Get contents by author.
     *
     * @param author   the author user
     * @param pageable pagination information
     * @return page of content responses
     */
    @Transactional(readOnly = true)
    public Page<ContentResponse> getContentsByAuthor(User author, Pageable pageable) {
        return contentRepository.findByAuthor(author, pageable)
                .map(ContentResponse::fromEntity);
    }

    /**
     * Get contents by status.
     *
     * @param status   the status
     * @param pageable pagination information
     * @return page of content responses
     */
    @Transactional(readOnly = true)
    public Page<ContentResponse> getContentsByStatus(String status, Pageable pageable) {
        return contentRepository.findByStatus(status, pageable)
                .map(ContentResponse::fromEntity);
    }

    /**
     * Search contents by title.
     *
     * @param keyword  the keyword to search
     * @param pageable pagination information
     * @return page of content responses
     */
    @Transactional(readOnly = true)
    public Page<ContentResponse> searchContentsByTitle(String keyword, Pageable pageable) {
        return contentRepository.findByTitleContaining(keyword, pageable)
                .map(ContentResponse::fromEntity);
    }
}
