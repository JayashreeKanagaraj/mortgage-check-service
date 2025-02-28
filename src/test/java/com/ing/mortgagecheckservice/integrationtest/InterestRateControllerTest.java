package com.ing.mortgagecheckservice.integrationtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
class InterestRateControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnInterestRatesSuccessfully() throws Exception {

        mockMvc.perform(get("/api/interest-rates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].maturityPeriod").value(15))
                .andExpect(jsonPath("$[0].interestRate").value(3.5))
                .andExpect(jsonPath("$[1].maturityPeriod").value(20))
                .andExpect(jsonPath("$[1].interestRate").value(3.8))
                .andExpect(jsonPath("$[2].maturityPeriod").value(30))
                .andExpect(jsonPath("$[2].interestRate").value(4.0));
    }

//    @Test
//    void shouldReturnNotFoundWhenNoInterestRatesAvailable() throws Exception {
//        // Arrange
//        when(interestRateService.getInterestRates()).thenThrow(new InterestRateNotFoundException("No interest rates available"));
//
//
//        mockMvc.perform(get("/api/interest-rates")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.message").value("No interest rates available"));
//    }
}

