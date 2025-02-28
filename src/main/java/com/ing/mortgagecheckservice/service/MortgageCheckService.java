package com.ing.mortgagecheckservice.service;

import com.ing.mortgagecheckservice.modal.request.MortgageCheckRequest;
import com.ing.mortgagecheckservice.modal.response.MortgageCheckResponse;

public interface MortgageCheckService {

    /**
     * Processes mortgage check request.
     *
     * @param request The mortgage check request containing income, loan value, maturity period, and home value.
     * @return A MortgageCheckResponse containing feasibility status, monthly cost, and any error it will be thrown.
     */
    MortgageCheckResponse processMortgageCheck(MortgageCheckRequest request);
}
