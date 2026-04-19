package com.example.hr.dto;

import com.example.hr.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentDTO {

    private Integer id;

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotNull(message = "Loại tài liệu không được để trống")
    private DocumentType documentType;

    @NotBlank(message = "Tên tài liệu không được để trống")
    private String documentName;

    private String fileUrl;
    private Long fileSize;
    private String mimeType;
    private String description;
    private LocalDate expiryDate;
}
