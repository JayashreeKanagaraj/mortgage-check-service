package com.ing.mortgagecheckservice.service;

import com.ing.mortgagecheckservice.datastore.InterestRateDataStore;
import com.ing.mortgagecheckservice.exception.InterestRateNotFoundException;
import com.ing.mortgagecheckservice.modal.entity.InterestRate;
import com.ing.mortgagecheckservice.modal.response.InterestRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InterestRateServiceImpl implements InterestRateService{
    private final InterestRateDataStore interestRateDataStore;

    public InterestRateServiceImpl(InterestRateDataStore interestRateDataStore) {
        this.interestRateDataStore = interestRateDataStore;
    }

    /**
     * Retrieves the list of available interest rates.
     *
     * @return A list of {@link InterestRateResponse} containing maturity periods,
     * interest rates, and last update timestamps.
     * @throws InterestRateNotFoundException if no interest rates are available.
     */
    @Override
    public List<InterestRateResponse> getInterestRates() {
        List<InterestRate> interestRates = interestRateDataStore.getInterestRates();

        if (interestRates.isEmpty()) {
            log.warn("No interest rates found in the datastore.");
            throw new InterestRateNotFoundException("No interest rates available.");
        }

        log.info("Retrieved {} interest rates.", interestRates.size());
        return interestRates.stream()
                .map(interestRate -> new InterestRateResponse(interestRate.getMaturityPeriod(), interestRate.getInterestRate(), interestRate.getLastUpdate()))
                .collect(Collectors.toList());
    }
}
