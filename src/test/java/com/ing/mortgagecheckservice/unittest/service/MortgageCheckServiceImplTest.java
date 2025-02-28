package com.ing.mortgagecheckservice.unittest.service;

import com.ing.mortgagecheckservice.datastore.InterestRateDataStore;
import com.ing.mortgagecheckservice.exception.InterestRateNotFoundException;
import com.ing.mortgagecheckservice.modal.request.MortgageCheckRequest;
import com.ing.mortgagecheckservice.modal.response.MortgageCheckResponse;
import com.ing.mortgagecheckservice.service.MortgageCheckServiceImpl;
import com.ing.mortgagecheckservice.unittest.mock.ApplicationMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageCheckServiceImplTest {

    @Mock
    private InterestRateDataStore interestRateDataStore;

    @InjectMocks
    private MortgageCheckServiceImpl classUnderTest;


    @Test
    void testProcessMortgageCheck_FeasibleLoan() {
        when(interestRateDataStore.getInterestRateBasedOnMaturityPeriod(20))
                .thenReturn(Optional.of(BigDecimal.valueOf(3.5)));

        MortgageCheckResponse response = classUnderTest.processMortgageCheck(ApplicationMock.getMortgageCheckRequest());

        assertTrue(response.feasible());
        assertTrue(response.monthlyCost().isPresent());
        verify(interestRateDataStore, times(1)).getInterestRateBasedOnMaturityPeriod(20);
    }

    @Test
    void testProcessMortgageCheck_NotFeasibleLoan() {
        MortgageCheckRequest invalidRequest = new MortgageCheckRequest(
                BigDecimal.valueOf(50000),
                20,
                BigDecimal.valueOf(300000),
                BigDecimal.valueOf(200000)
        );

        MortgageCheckResponse response = classUnderTest.processMortgageCheck(invalidRequest);

        assertFalse(response.feasible());
        assertTrue(response.monthlyCost().isEmpty());
        verify(interestRateDataStore, never()).getInterestRateBasedOnMaturityPeriod(anyInt());
    }

    @Test
    void testProcessMortgageCheck_MissingInterestRate_ShouldThrowException() {
        when(interestRateDataStore.getInterestRateBasedOnMaturityPeriod(20))
                .thenReturn(Optional.empty());

        InterestRateNotFoundException exception = assertThrows(InterestRateNotFoundException.class, () ->
                classUnderTest.processMortgageCheck(ApplicationMock.getMortgageCheckRequest())
        );

        assertEquals("No interest rate found for maturity period: 20", exception.getMessage());
    }

    @Test
    void testProcessMortgageCheck_ZeroIncome_ShouldBeInfeasible() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                BigDecimal.ZERO, 20, BigDecimal.valueOf(50000), BigDecimal.valueOf(100000)
        );

        MortgageCheckResponse response = classUnderTest.processMortgageCheck(request);

        assertFalse(response.feasible());
        assertTrue(response.monthlyCost().isEmpty());
    }

    @Test
    void testProcessMortgageCheck_ZeroLoanValue_ShouldBeInfeasible() {

        MortgageCheckRequest request = new MortgageCheckRequest(
                BigDecimal.valueOf(50000), 20, BigDecimal.ZERO, BigDecimal.valueOf(100000)
        );

        MortgageCheckResponse response = classUnderTest.processMortgageCheck(request);

        assertFalse(response.feasible());
        assertTrue(response.monthlyCost().isEmpty());
    }

    @Test
    void testProcessMortgageCheck_NegativeValues_ShouldBeInfeasible() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                BigDecimal.valueOf(-50000), 20, BigDecimal.valueOf(-150000), BigDecimal.valueOf(-200000)
        );

        MortgageCheckResponse response = classUnderTest.processMortgageCheck(request);

        assertFalse(response.feasible());
        assertTrue(response.monthlyCost().isEmpty());
    }

}

