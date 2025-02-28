package com.ing.mortgagecheckservice.datastore;

import com.ing.mortgagecheckservice.config.InterestRateConfig;
import com.ing.mortgagecheckservice.modal.entity.InterestRate;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@Getter
@Setter
public class InterestRateDataStore {
    private final InterestRateConfig interestRateConfig;
    private final List<InterestRate> interestRates = new CopyOnWriteArrayList<>();

    public InterestRateDataStore(InterestRateConfig interestRateConfig) {
        this.interestRateConfig = interestRateConfig;
    }

    /**
     * Retrieves the interest rate for a given maturity period.
     *
     * @param maturityPeriod The loan maturity period in years.
     * @return The applicable interest rate wrapped in an Optional.
     */
    public Optional<BigDecimal> getInterestRateBasedOnMaturityPeriod(int maturityPeriod) {
        log.info("Fetching interest rate for maturity period: {} years", maturityPeriod);

        return interestRates.stream()
                .filter(rate -> rate.getMaturityPeriod() == maturityPeriod)
                .map(InterestRate::getInterestRate)
                .findFirst();
    }

    /**
     * Loads initial interest rates into memory from the configuration at application startup.
     * This ensures the application has predefined rates available for mortgage calculations.
     */
    @PostConstruct
    public void loadInitialRates() {
        log.info("Loading initial interest rates from configuration...");
        List<InterestRate> defaultRates = interestRateConfig.getDefaultRates().stream()
                    .map(interestRate -> new InterestRate(interestRate.getMaturityPeriod(), interestRate.getInterestRate(), Instant.now()))
                    .toList();

        if (defaultRates.isEmpty()) {
            log.warn("No interest rates found in configuration. The system might not function as expected.");
        } else {
            log.info("Loaded {} initial interest rates successfully.", defaultRates.size());
            interestRates.addAll(defaultRates);
        }
    }
}
