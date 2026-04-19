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
public class TrainingProgramDTO {

    private Integer id;

    @NotBlank(message = "Tên chương trình không được để trống")
    private String programName;

    private String description;
    private String instructor;
    private Integer departmentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxCapacity;
    private String location;
    private String trainingType;
    private BigDecimal budget;
}
