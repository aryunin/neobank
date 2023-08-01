package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.dto.enums.EmploymentStatus;
import com.aryunin.conveyor.util.DecimalSerializer;
import com.aryunin.conveyor.dto.enums.Position;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDTO {
    @NotNull
    private EmploymentStatus employmentStatus;
    @NotNull
    @Pattern(regexp = "\\d{20}")
    private String employerINN;
    @JsonSerialize(using = DecimalSerializer.class)
    @NotNull
    @Min(value = 0, message = "negative salary")
    private BigDecimal salary;
    @NotNull
    private Position position;
    @NotNull
    @Min(value = 0, message = "negative total work experience")
    private Integer workExperienceTotal;
    @NotNull
    @Min(value = 0, message = "negative work experience")
    private Integer workExperienceCurrent;
}
