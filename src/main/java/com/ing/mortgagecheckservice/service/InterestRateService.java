package com.ing.mortgagecheckservice.service;

import com.ing.mortgagecheckservice.modal.response.InterestRateResponse;

import java.util.List;

public interface InterestRateService {

    /**
     * Retrieves the list of available interest rates.
     *
     * @return A list of {@link InterestRateResponse} containing maturity periods,
     * interest rates, and last update timestamps.
     */
    List<InterestRateResponse> getInterestRates();
}
