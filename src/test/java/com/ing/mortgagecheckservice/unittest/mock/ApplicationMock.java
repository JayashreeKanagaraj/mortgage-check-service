package com.ing.mortgagecheckservice.unittest.mock;

import com.ing.mortgagecheckservice.config.InterestRateConfig;
import com.ing.mortgagecheckservice.modal.entity.InterestRate;
import com.ing.mortgagecheckservice.modal.request.MortgageCheckRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class ApplicationMock {

    private ApplicationMock(){}

    public static MortgageCheckRequest getMortgageCheckRequest(){
       MortgageCheckRequest mortgageCheckRequest = new MortgageCheckRequest(
                BigDecimal.valueOf(50000),
                20,
                BigDecimal.valueOf(150000),
                BigDecimal.valueOf(200000)
        );

       return mortgageCheckRequest;
    }

    public static List<InterestRate> getInterestRates(){

        return List.of(
                new InterestRate(15, BigDecimal.valueOf(3.5), Instant.now()),
                new InterestRate(20, BigDecimal.valueOf(3.8), Instant.now()),
                new InterestRate(30, BigDecimal.valueOf(4.0), Instant.now())
        );
    }


    public static List<InterestRateConfig.InterestRateEntry> getConfigInterestRates(){

        return List.of(
                new InterestRateConfig.InterestRateEntry(15, BigDecimal.valueOf(3.5)),
                new InterestRateConfig.InterestRateEntry(20, BigDecimal.valueOf(3.8)),
                new InterestRateConfig.InterestRateEntry(30, BigDecimal.valueOf(4.0))
        );
    }
}
