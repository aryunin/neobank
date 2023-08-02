package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.EmploymentDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.dto.enums.EmploymentStatus;
import com.aryunin.conveyor.dto.enums.Gender;
import com.aryunin.conveyor.dto.enums.MaterialStatus;
import com.aryunin.conveyor.dto.enums.Position;
import com.aryunin.conveyor.service.calculation.CalculationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculationController.class)
class CalculationControllerTest {
    @MockBean
    private CalculationService calculationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCredit_success() throws Exception {
        EmploymentDTO employment = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .employerINN("12345678901234567890")
                .salary(new BigDecimal("20000"))
                .position(Position.WORKER)
                .workExperienceTotal(32)
                .workExperienceCurrent(20)
                .build();

        ScoringDataDTO request = ScoringDataDTO.builder()
                .amount(new BigDecimal(100000))
                .term(6)
                .firstName("Artem")
                .lastName("Yunin")
                .gender(Gender.MALE)
                .birthDate(LocalDate.now().minusYears(21))
                .passportNumber("123456")
                .passportSeries("1234")
                .passportIssueDate(LocalDate.now().minusDays(1))
                .passportIssueBranch("123-123")
                .materialStatus(MaterialStatus.DIVORCED)
                .dependentAmount(0)
                .account("12345678901234567890")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .employment(employment)
                .build();

        CreditDTO answer = CreditDTO.builder().amount(new BigDecimal("100000.00")).build();

        Mockito.when(calculationService.getCredit(any())).thenReturn(answer);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount", is(100000.00)));
    }

    @Test
    void getCredit_validationException() throws Exception {

        ScoringDataDTO request = ScoringDataDTO.builder().build();


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException)
                );
    }
}