package com.example.hr.dto;

import com.example.hr.enums.BenefitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBenefitDTO {

    private Integer id;

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotNull(message = "Loại phúc lợi không được để trống")
    private BenefitType benefitType;

    @NotBlank(message = "Tên phúc lợi không được để trống")
    private String benefitName;

    private String description;
    private BigDecimal monetaryValue;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    private LocalDate endDate;
    private String provider;
    private String policyNumber;
}
