package com.example.hr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentDTO {

    private Long id;

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotBlank(message = "Loại tài liệu không được để trống")
    private String documentType;

    @NotBlank(message = "Tên file không được để trống")
    private String fileName;

    private String fileUrl;
    private String fileSize;
    private String mimeType;
    private String description;
    private Boolean isConfidential;
}
