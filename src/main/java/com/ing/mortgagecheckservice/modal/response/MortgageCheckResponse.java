package com.ing.mortgagecheckservice.modal.response;

import java.math.BigDecimal;
import java.util.Optional;

public record MortgageCheckResponse(
        boolean feasible,
        Optional<BigDecimal> monthlyCost
) {}
