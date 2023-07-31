package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.util.MoneySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanApplicationRequestDTO{
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
    private String passportSeries;
    private String passportNumber;
}
