package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.service.calculation.CalculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculation")
@RequiredArgsConstructor
public class CalculationController {
    private final CalculationService service;

    @PostMapping
    @ResponseBody
    public CreditDTO getCredit(@RequestBody @Valid ScoringDataDTO request) {
        return service.getCredit(request);
    }
}
