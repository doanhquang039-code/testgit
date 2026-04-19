package com.example.hr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetAssignmentDTO {

    private Integer id;

    @NotNull(message = "Asset ID không được để trống")
    private Integer assetId;

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotNull(message = "Ngày giao không được để trống")
    private LocalDate assignedDate;

    private LocalDate expectedReturn;
    private Integer assignedById;
    private String conditionOnAssign;
    private String notes;

    // Cho trả tài sản
    private String conditionOnReturn;
}
