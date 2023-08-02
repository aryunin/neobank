package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.service.calculation.CalculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculation")
@RequiredArgsConstructor
@Slf4j
public class CalculationController {
    private final CalculationService service;

    @PostMapping
    public CreditDTO getCredit(@RequestBody @Valid ScoringDataDTO request) {
        log.info("processing credit for " + request.getLastName() + " " + request.getFirstName());
        var result = service.getCredit(request);
        log.info("credit offered to " + request.getLastName() + " " + request.getFirstName());
        return result;
    }
}
