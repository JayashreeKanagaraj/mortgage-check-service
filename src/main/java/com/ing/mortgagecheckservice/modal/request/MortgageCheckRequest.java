package com.ing.mortgagecheckservice.modal.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

/**
 * DTO representing a mortgage check request with field-level validation rules.
 */
public record MortgageCheckRequest(
        @NotNull(message = "Income cannot be null.")
        @DecimalMin(value = "0.01", message = "Income must be greater than zero.")
        BigDecimal income,

        @Min(value = 1, message = "Maturity period must be at least 1 year.")
        int maturityPeriod,
        @NotNull(message = "Loan value cannot be null.")
        @DecimalMin(value = "0.01", message = "Loan value must be greater than zero.")
        BigDecimal loanValue,
        @NotNull(message = "Home value cannot be null.")
        @DecimalMin(value = "0.01", message = "Home value must be greater than zero.")
        BigDecimal homeValue
) {}

