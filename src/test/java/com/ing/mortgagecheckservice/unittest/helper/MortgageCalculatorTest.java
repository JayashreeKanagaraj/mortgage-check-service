package com.ing.mortgagecheckservice.unittest.helper;

import com.ing.mortgagecheckservice.exception.MortgageCheckException;
import com.ing.mortgagecheckservice.helper.MortgageCalculator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MortgageCalculatorTest {

    @Test
    void testCalculateMonthlyPayment_ValidCase() {
        BigDecimal loanAmount = BigDecimal.valueOf(200000);
        int maturityPeriod = 30;
        BigDecimal interestRate = BigDecimal.valueOf(4.0);

        BigDecimal expectedMonthlyPayment = BigDecimal.valueOf(954.83);
        BigDecimal actualMonthlyPayment = MortgageCalculator.calculateMonthlyPayment(loanAmount, maturityPeriod, interestRate);

        assertEquals(expectedMonthlyPayment, actualMonthlyPayment);
    }

    @Test
    void testCalculateMonthlyPayment_MinimumLoanPeriod() {
        BigDecimal loanAmount = BigDecimal.valueOf(50000);
        int maturityPeriod = 1;
        BigDecimal interestRate = BigDecimal.valueOf(5.0);

        BigDecimal expectedMonthlyPayment = BigDecimal.valueOf(4280.37);
        BigDecimal actualMonthlyPayment = MortgageCalculator.calculateMonthlyPayment(loanAmount, maturityPeriod, interestRate);

        assertEquals(expectedMonthlyPayment, actualMonthlyPayment);
    }

    @Test
    void testCalculateMonthlyPayment_NullLoanAmount() {
        assertThrows(MortgageCheckException.class, () ->
                MortgageCalculator.calculateMonthlyPayment(null, 15, BigDecimal.valueOf(3.5))
        );
    }

    @Test
    void testCalculateMonthlyPayment_NullInterestRate() {
        assertThrows(MortgageCheckException.class, () ->
                MortgageCalculator.calculateMonthlyPayment(BigDecimal.valueOf(100000), 20, null)
        );
    }

    @Test
    void testCalculateMonthlyPayment_ZeroMaturityPeriod() {
        assertThrows(MortgageCheckException.class, () ->
                MortgageCalculator.calculateMonthlyPayment(BigDecimal.valueOf(100000), 0, BigDecimal.valueOf(3.5))
        );
    }
}

