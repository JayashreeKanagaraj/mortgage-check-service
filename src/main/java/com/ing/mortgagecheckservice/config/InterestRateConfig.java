package com.ing.mortgagecheckservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "interest-rates")
@EnableConfigurationProperties
public class InterestRateConfig {

    private List<InterestRateEntry> defaultRates;
    @Getter
    @Setter
    @AllArgsConstructor
    public static class InterestRateEntry {
        private int maturityPeriod;
        private BigDecimal interestRate;
    }

}