package com.webeditor.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for content creation and update requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Body is required")
    private String body;

    private String status = "DRAFT";

    private String tags;
}
