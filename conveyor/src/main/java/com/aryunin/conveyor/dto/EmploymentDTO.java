package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.util.DecimalSerializer;
import com.aryunin.conveyor.dto.enums.EmploymentStatus;
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
    @NotNull(message = "field must not be null")
    private EmploymentStatus employmentStatus;
    @NotNull(message = "field must not be null")
    @Pattern(regexp = "\\d{20}")
    private String employerINN;
    @JsonSerialize(using = DecimalSerializer.class)
    @NotNull(message = "field must not be null")
    @Min(value = 0, message = "negative salary")
    private BigDecimal salary;
    @NotNull(message = "field must not be null")
    private Position position;
    @NotNull(message = "field must not be null")
    @Min(value = 0, message = "negative total work experience")
    private Integer workExperienceTotal;
    @NotNull(message = "field must not be null")
    @Min(value = 0, message = "negative work experience")
    private Integer workExperienceCurrent;
}
