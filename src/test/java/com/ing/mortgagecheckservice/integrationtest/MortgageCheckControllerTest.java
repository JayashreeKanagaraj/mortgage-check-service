package com.ing.mortgagecheckservice.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.mortgagecheckservice.modal.request.MortgageCheckRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MortgageCheckControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnMortgageCheckResponseSuccessfully() throws Exception {
        MortgageCheckRequest validRequest = new MortgageCheckRequest(
                BigDecimal.valueOf(50000),
                20,
                BigDecimal.valueOf(150000),
                BigDecimal.valueOf(200000)
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(true))
                .andExpect(jsonPath("$.monthlyCost").value(893));
    }

    @Test
    void shouldReturnBadRequestForInvalidRequest() throws Exception {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest(
                BigDecimal.valueOf(50000),
                20,
                BigDecimal.valueOf(-150000),
                BigDecimal.valueOf(200000)
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnNotFeasibleWhenLoanExceedsHomeValue() throws Exception {
        MortgageCheckRequest request = new MortgageCheckRequest(
                BigDecimal.valueOf(70000),
                20,
                BigDecimal.valueOf(250000),
                BigDecimal.valueOf(200000)
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(false));
    }

    @Test
    void shouldReturnNotFeasibleWhenLoanExceeds4TimesIncomeValue() throws Exception {
        MortgageCheckRequest request = new MortgageCheckRequest(
                BigDecimal.valueOf(40000),
                20,
                BigDecimal.valueOf(190000),
                BigDecimal.valueOf(200000)
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(false));
    }

    @Test
    void shouldReturnBadRequestWhenIncomeIsZero() throws Exception {
        MortgageCheckRequest request = new MortgageCheckRequest(
                BigDecimal.valueOf(0),
                20,
                BigDecimal.valueOf(150000),
                BigDecimal.valueOf(200000)
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void shouldReturnBadRequestWhenInterestNotFound() throws Exception {
        MortgageCheckRequest request = new MortgageCheckRequest(
                BigDecimal.valueOf(5000),
                200,
                BigDecimal.valueOf(15000),
                BigDecimal.valueOf(20000)
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No interest rate found for maturity period: 200"));
    }
}

