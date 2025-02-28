package com.ing.mortgagecheckservice.unittest.service;

import com.ing.mortgagecheckservice.datastore.InterestRateDataStore;
import com.ing.mortgagecheckservice.exception.InterestRateNotFoundException;
import com.ing.mortgagecheckservice.modal.entity.InterestRate;
import com.ing.mortgagecheckservice.modal.response.InterestRateResponse;
import com.ing.mortgagecheckservice.service.InterestRateServiceImpl;
import com.ing.mortgagecheckservice.unittest.mock.ApplicationMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestRateServiceImplTest {

    @Mock
    private InterestRateDataStore interestRateDataStore;

    @InjectMocks
    private InterestRateServiceImpl interestRateService;


    @Test
    void shouldReturnInterestRates_whenAvailable() {
        when(interestRateDataStore.getInterestRates()).thenReturn(ApplicationMock.getInterestRates());

        List<InterestRateResponse> responses = interestRateService.getInterestRates();

        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).maturityPeriod()).isEqualTo(15);
        assertThat(responses.get(0).interestRate()).isEqualTo(BigDecimal.valueOf(3.5));
        verify(interestRateDataStore, times(1)).getInterestRates();
    }

    @Test
    void shouldThrowException_whenNoInterestRatesAvailable() {
        when(interestRateDataStore.getInterestRates()).thenReturn(List.of());

        assertThatThrownBy(() -> interestRateService.getInterestRates())
                .isInstanceOf(InterestRateNotFoundException.class)
                .hasMessage("No interest rates available.");

        verify(interestRateDataStore, times(1)).getInterestRates();
    }

    @Test
    void shouldMapInterestRateToResponseCorrectly() {
        List<InterestRate> interestRates = ApplicationMock.getInterestRates();
        when(interestRateDataStore.getInterestRates()).thenReturn(interestRates);

        List<InterestRateResponse> responses = interestRateService.getInterestRates();

        assertThat(responses).allSatisfy(response ->
                assertThat(interestRates.stream()
                        .anyMatch(rate -> rate.getMaturityPeriod() == response.maturityPeriod() &&
                                rate.getInterestRate().compareTo(response.interestRate()) == 0 &&
                                rate.getLastUpdate().equals(response.lastUpdate())))
                        .isTrue()
        );
    }
}

