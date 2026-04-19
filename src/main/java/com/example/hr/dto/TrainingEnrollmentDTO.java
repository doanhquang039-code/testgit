package com.example.hr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEnrollmentDTO {

    private Integer id;

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotNull(message = "Program ID không được để trống")
    private Integer programId;

    private BigDecimal score;
    private String feedback;
    private String certificateUrl;
}
