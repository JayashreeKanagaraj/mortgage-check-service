package com.ing.mortgagecheckservice.unittest.helper;

import com.ing.mortgagecheckservice.helper.MortgageRuleValidator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;


class MortgageRuleValidatorTest {

    @Test
    void testLoanWithinIncomeLimit_Valid() {
        BigDecimal loanValue = BigDecimal.valueOf(40000);
        BigDecimal income = BigDecimal.valueOf(10000);

        assertTrue(MortgageRuleValidator.isLoanWithinIncomeLimit(loanValue, income));
    }

    @Test
    void testLoanWithinIncomeLimit_Exceeded() {
        BigDecimal loanValue = BigDecimal.valueOf(50000);
        BigDecimal income = BigDecimal.valueOf(10000);

        assertFalse(MortgageRuleValidator.isLoanWithinIncomeLimit(loanValue, income));
    }

    @Test
    void testLoanWithinHomeValue_Valid() {
        BigDecimal loanValue = BigDecimal.valueOf(250000);
        BigDecimal homeValue = BigDecimal.valueOf(300000);

        assertTrue(MortgageRuleValidator.isLoanWithinHomeValue(loanValue, homeValue));
    }

    @Test
    void testLoanWithinHomeValue_Exceeded() {
        BigDecimal loanValue = BigDecimal.valueOf(350000);
        BigDecimal homeValue = BigDecimal.valueOf(300000);

        assertFalse(MortgageRuleValidator.isLoanWithinHomeValue(loanValue, homeValue));
    }
}
