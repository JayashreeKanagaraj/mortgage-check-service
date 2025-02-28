package com.ing.mortgagecheckservice.helper;

import java.math.BigDecimal;

/**
 * Utility class for mortgage business rule validation.
 */
public class MortgageRuleValidator {
    private static final BigDecimal LOAN_TO_INCOME_RATIO = BigDecimal.valueOf(4);

    private MortgageRuleValidator() {
        // Prevent instantiation
    }

    /**
     * Checks if the loan amount does not exceed 4 times the borrower's income.
     *
     * @param income The borrower's income.
     * @param loanValue The requested loan amount.
     * @return true if the loan is within the allowed limit, otherwise false.
     */
    public static boolean isLoanWithinIncomeLimit(BigDecimal loanValue, BigDecimal income) {
        return loanValue.compareTo(BigDecimal.ZERO)>0 && loanValue.compareTo(income.multiply(LOAN_TO_INCOME_RATIO)) <= 0;
    }

    /**
     * Checks if the loan amount does not exceed the value of the home.
     *
     * @param loanValue The requested loan amount.
     * @param homeValue The home’s market value.
     * @return true if the loan is within the home value, otherwise false.
     */
    public static boolean isLoanWithinHomeValue(BigDecimal loanValue, BigDecimal homeValue) {
        return loanValue.compareTo(homeValue) <= 0;
    }
}

