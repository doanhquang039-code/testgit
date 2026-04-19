package com.example.hr.dto;

import com.example.hr.enums.WarningLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeWarningDTO {

    private Integer id;

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotNull(message = "Issued By không được để trống")
    private Integer issuedById;

    @NotNull(message = "Mức cảnh cáo không được để trống")
    private WarningLevel warningLevel;

    @NotBlank(message = "Lý do không được để trống")
    private String reason;

    private String description;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private String attachmentUrl;
}
