package com.example.hr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAssetDTO {

    private Integer id;

    @NotBlank(message = "Tên tài sản không được để trống")
    private String assetName;

    private String assetCode;

    @NotBlank(message = "Danh mục không được để trống")
    private String category;

    private String serialNumber;
    private String description;
    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    private String location;
    private LocalDate warrantyExpiry;
}
