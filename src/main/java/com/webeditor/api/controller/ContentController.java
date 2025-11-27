package com.webeditor.api.controller;

import com.webeditor.api.dto.ContentRequest;
import com.webeditor.api.dto.ContentResponse;
import com.webeditor.api.dto.MessageResponse;
import com.webeditor.api.entity.User;
import com.webeditor.api.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for content management operations.
 */
@Tag(name = "Content", description = "Content management APIs")
@RestController
@RequestMapping("/api/contents")
@SecurityRequirement(name = "bearerAuth")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Operation(summary = "Create new content", description = "Create a new content item")
    @PostMapping
    public ResponseEntity<ContentResponse> createContent(
            @Valid @RequestBody ContentRequest request,
            @AuthenticationPrincipal User author) {
        ContentResponse response = contentService.createContent(request, author);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update content", description = "Update an existing content item")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody ContentRequest request,
            @AuthenticationPrincipal User author) {
        try {
            ContentResponse response = contentService.updateContent(id, request, author);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Delete content", description = "Delete a content item")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContent(
            @PathVariable Long id,
            @AuthenticationPrincipal User author) {
        try {
            contentService.deleteContent(id, author);
            return ResponseEntity.ok(new MessageResponse("Content deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get content by ID", description = "Retrieve a content item by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getContentById(@PathVariable Long id) {
        try {
            ContentResponse response = contentService.getContentById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get all contents", description = "Retrieve all contents with pagination")
    @GetMapping
    public ResponseEntity<Page<ContentResponse>> getAllContents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ContentResponse> contents = contentService.getAllContents(pageable);
        return ResponseEntity.ok(contents);
    }

    @Operation(summary = "Get my contents", description = "Retrieve contents created by the current user")
    @GetMapping("/my")
    public ResponseEntity<Page<ContentResponse>> getMyContents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @AuthenticationPrincipal User author) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ContentResponse> contents = contentService.getContentsByAuthor(author, pageable);
        return ResponseEntity.ok(contents);
    }

    @Operation(summary = "Get contents by status", description = "Retrieve contents by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ContentResponse>> getContentsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ContentResponse> contents = contentService.getContentsByStatus(status, pageable);
        return ResponseEntity.ok(contents);
    }

    @Operation(summary = "Search contents", description = "Search contents by title keyword")
    @GetMapping("/search")
    public ResponseEntity<Page<ContentResponse>> searchContents(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ContentResponse> contents = contentService.searchContentsByTitle(keyword, pageable);
        return ResponseEntity.ok(contents);
    }
}
