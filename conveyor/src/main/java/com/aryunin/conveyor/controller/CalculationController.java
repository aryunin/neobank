package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.service.calculation.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Calculation", description = "Loan management APIs")
public class CalculationController {
    private final CalculationService service;

    @Operation(
            summary = "Get loan",
            description = "Validate data, score it and get available loan as response"
    )
    @PostMapping
    public CreditDTO getCredit(@RequestBody @Valid ScoringDataDTO request) {
        log.info("processing credit for " + request.getLastName() + " " + request.getFirstName());
        var result = service.getCredit(request);
        log.info("credit offered to " + request.getLastName() + " " + request.getFirstName());
        return result;
    }
}
