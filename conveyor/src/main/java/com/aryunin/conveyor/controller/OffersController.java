package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;
import com.aryunin.conveyor.service.offer.OffersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OffersController {
    private final OffersService service;

    @PostMapping
    @ResponseBody
    public List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO request) {
        return service.getOffers(request);
    }
}
