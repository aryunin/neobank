package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;
import com.aryunin.conveyor.service.offer.OffersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OffersController.class)
class OffersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OffersService offersService;

    @Test
    void getOffers_success() throws Exception {
        var request = LoanApplicationRequestDTO.builder().build();
        var answer = List.of(
                LoanOfferDTO.builder().rate(new BigDecimal("9")).build(),
                LoanOfferDTO.builder().rate(new BigDecimal("8")).build(),
                LoanOfferDTO.builder().rate(new BigDecimal("6")).build(),
                LoanOfferDTO.builder().rate(new BigDecimal("5")).build()
        );

        Mockito.when(offersService.getOffers(any())).thenReturn(answer);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$.[1].rate", is(8.0)));
    }
}