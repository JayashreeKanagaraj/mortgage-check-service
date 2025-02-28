package com.ing.mortgagecheckservice.unittest.datastore;

import com.ing.mortgagecheckservice.config.InterestRateConfig;
import com.ing.mortgagecheckservice.datastore.InterestRateDataStore;
import com.ing.mortgagecheckservice.modal.entity.InterestRate;
import com.ing.mortgagecheckservice.unittest.mock.ApplicationMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterestRateDataStoreTest {
    @Mock
    private InterestRateConfig interestRateConfig;

    @InjectMocks
    private InterestRateDataStore classUnderTest;

    @BeforeEach
    void setUp() {
        when(interestRateConfig.getDefaultRates()).thenReturn(ApplicationMock.getConfigInterestRates());
        classUnderTest.loadInitialRates(); // Load initial rates
    }

    @Test
    void shouldReturnInterestRate_whenMaturityPeriodExists() {
        // Act
        Optional<BigDecimal> interestRate = classUnderTest.getInterestRateBasedOnMaturityPeriod(15);

        // Assert
        assertThat(interestRate).isPresent();
        assertThat(interestRate.get()).isEqualTo(BigDecimal.valueOf(3.5));
    }

    @Test
    void shouldReturnEmptyOptional_whenMaturityPeriodDoesNotExist() {
        // Act
        Optional<BigDecimal> interestRate = classUnderTest.getInterestRateBasedOnMaturityPeriod(10);

        // Assert
        assertThat(interestRate).isEmpty();
    }

    @Test
    void shouldLoadInitialRatesCorrectly() {
        // Assert
        assertThat(classUnderTest.getInterestRates()).hasSize(3);
        assertThat(classUnderTest.getInterestRates())
                .extracting(InterestRate::getMaturityPeriod)
                .containsExactlyInAnyOrder(15, 20, 30);
    }

}

