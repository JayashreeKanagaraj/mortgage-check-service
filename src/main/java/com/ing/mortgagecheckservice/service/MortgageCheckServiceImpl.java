package com.ing.mortgagecheckservice.service;

import com.ing.mortgagecheckservice.datastore.InterestRateDataStore;
import com.ing.mortgagecheckservice.exception.InterestRateNotFoundException;
import com.ing.mortgagecheckservice.helper.MortgageCalculator;
import com.ing.mortgagecheckservice.helper.MortgageRuleValidator;
import com.ing.mortgagecheckservice.modal.request.MortgageCheckRequest;
import com.ing.mortgagecheckservice.modal.response.MortgageCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class MortgageCheckServiceImpl implements MortgageCheckService{

    private final InterestRateDataStore interestRateDataStore;

    public MortgageCheckServiceImpl(InterestRateDataStore interestRateDataStore) {
        this.interestRateDataStore = interestRateDataStore;
    }

    @Override
    public MortgageCheckResponse processMortgageCheck(MortgageCheckRequest request) {
        log.info("Processing mortgage check for loanValue: {}, income: {}, homeValue: {}, maturityPeriod: {}",
                request.loanValue(), request.income(), request.homeValue(), request.maturityPeriod());

        boolean isFeasible = isMortgageFeasible(request);
        Optional<BigDecimal> monthlyCost = calculateMonthlyCost(isFeasible, request);

        return new MortgageCheckResponse(isFeasible, monthlyCost);
    }

    /**
     * Determines if the mortgage request is feasible based on loan-to-income and loan-to-home value rules.
     *
     * @param request The mortgage request containing loan and income details.
     * @return True if the mortgage is feasible, false otherwise.
     */
    private boolean isMortgageFeasible(MortgageCheckRequest request) {
        boolean feasible = MortgageRuleValidator.isLoanWithinIncomeLimit(request.loanValue(), request.income()) &&
                MortgageRuleValidator.isLoanWithinHomeValue(request.loanValue(), request.homeValue());
        log.debug("Mortgage feasibility check result: {}", feasible);
        return feasible;
    }

    /**
     * Calculates the monthly cost only if the mortgage is feasible.
     *
     * @param isFeasible Indicates whether the mortgage is feasible.
     * @param request    The mortgage check request details.
     * @return The calculated monthly cost, or an empty value if the mortgage is not feasible.
     */
    private Optional<BigDecimal> calculateMonthlyCost(boolean isFeasible, MortgageCheckRequest request) {
        if (!isFeasible) {
            log.warn("Mortgage check declined. Loan does not meet feasibility criteria.");
            return Optional.empty();
        }
        BigDecimal interestRate = getInterestRateBasedOnMaturityPeriod(request.maturityPeriod());
        log.debug("Interest rate fetched for maturity period {}: {}", request.maturityPeriod(), interestRate);

        BigDecimal monthlyCost = MortgageCalculator.calculateMonthlyPayment(
                request.loanValue(), request.maturityPeriod(), interestRate);

        log.info("Mortgage check approved. Calculated monthly cost: {}", monthlyCost);
        return Optional.of(monthlyCost);
    }

    /**
     * Fetches the interest rate for a given maturity period.
     *
     * @param maturityPeriod The loan maturity period in years.
     * @return The applicable interest rate.
     * @throws InterestRateNotFoundException if no matching rate is found.
     */
    private BigDecimal getInterestRateBasedOnMaturityPeriod(int maturityPeriod) {
        return interestRateDataStore.getInterestRateBasedOnMaturityPeriod(maturityPeriod)
                .orElseThrow(() -> new InterestRateNotFoundException(
                        "No interest rate found for maturity period: " + maturityPeriod));
    }
}
