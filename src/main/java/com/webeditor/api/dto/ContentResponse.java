package com.webeditor.api.dto;

import com.webeditor.api.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for content response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponse {

    private Long id;
    private String title;
    private String body;
    private String status;
    private String authorUsername;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ContentResponse fromEntity(Content content) {
        return ContentResponse.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .status(content.getStatus())
                .authorUsername(content.getAuthor().getUsername())
                .tags(content.getTags())
                .createdAt(content.getCreatedAt())
                .updatedAt(content.getUpdatedAt())
                .build();
    }
}
