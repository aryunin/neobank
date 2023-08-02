package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;
import com.aryunin.conveyor.service.offer.OffersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
@Slf4j
public class OffersController {
    private final OffersService service;

    @PostMapping
    public List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO request) {
        log.info("processing offers for " + request.getEmail());
        var result = service.getOffers(request);
        log.info("offers for " + request.getEmail() + " processed");
        return result;
    }
}
