package com.ing.mortgagecheckservice.modal.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class InterestRate {

    private int maturityPeriod;
    private BigDecimal interestRate;
    private Instant lastUpdate;

}
