package com.ing.mortgagecheckservice.helper;

import com.ing.mortgagecheckservice.exception.MortgageCheckException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for mortgage payment calculations.
 * <p>
 * This class provides methods to calculate monthly mortgage payments using
 * the standard loan amortization formula.
 * </p>
 * <p>
 * Formula:
 * M = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
 * Where:
 * - M = Monthly payment
 * - P = Loan principal (amount borrowed)
 * - r = Monthly interest rate (annual rate / 12 / 100)
 * - n = Total number of payments (loan term in years * 12)
 * </p>
 */
public class MortgageCalculator {

    private MortgageCalculator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Calculates the monthly mortgage payment based on loan amount, maturity period, and interest rate.
     *
     * @param loanAmount     The total loan value (principal amount).
     * @param maturityPeriod The loan term in years.
     * @param interestRate   The annual interest rate (as a percentage).
     * @return The calculated monthly payment.
     */
    public static BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, int maturityPeriod, BigDecimal interestRate) {
        if (loanAmount == null || interestRate == null) {
            throw new MortgageCheckException("Loan amount and interest rate must not be null.");
        }

        if (maturityPeriod <= 0) {
            throw new MortgageCheckException("Maturity period must not be zero");
        }

        BigDecimal monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        int totalPayments = maturityPeriod * 12;

        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return loanAmount.divide(BigDecimal.valueOf(totalPayments), 2, RoundingMode.HALF_UP);
        }

        BigDecimal onePlusRatePowN = BigDecimal.ONE.add(monthlyInterestRate).pow(totalPayments);
        BigDecimal numerator = loanAmount.multiply(monthlyInterestRate).multiply(onePlusRatePowN);
        BigDecimal denominator = onePlusRatePowN.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}

