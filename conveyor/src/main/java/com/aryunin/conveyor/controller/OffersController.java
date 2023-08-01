package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;
import com.aryunin.conveyor.service.OffersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.util.List;

// default contextPath replaced by /api in the application.yml
@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OffersController {
    private final OffersService service;
    private final int scale = 16; // fixme куда-нибудь вынести

    @PostMapping
    @ResponseBody
    public List<LoanOfferDTO> getOffers(
            @RequestBody LoanApplicationRequestDTO request
            ) {
        upscale(request);
        return service.getOffers(request);
    }

    private void upscale(LoanApplicationRequestDTO request) {
        request.setAmount(request.getAmount().setScale(scale, RoundingMode.HALF_UP));
    }
}
