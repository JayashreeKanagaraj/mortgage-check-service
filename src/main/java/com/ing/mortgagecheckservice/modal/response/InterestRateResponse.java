package com.ing.mortgagecheckservice.modal.response;

import java.math.BigDecimal;
import java.time.Instant;

public record InterestRateResponse(int maturityPeriod, BigDecimal interestRate, Instant lastUpdate) {
}
