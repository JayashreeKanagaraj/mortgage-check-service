package com.ing.mortgagecheckservice.controller;

import com.ing.mortgagecheckservice.modal.response.InterestRateResponse;
import com.ing.mortgagecheckservice.service.InterestRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/interest-rates")
public class InterestRateController {
    private final InterestRateService interestRateService;

    public InterestRateController(InterestRateService interestRateService) {
        this.interestRateService = interestRateService;
    }

    /**
     * Fetches list of available interest rates.
     *
     * @return Response entity containing the list of interest rates.
     */
    @Operation(summary = "Get available interest rates",
            description = "Returns a list of interest rates with maturity periods and last update timestamps.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved interest rates",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InterestRateResponse.class))),
            @ApiResponse(responseCode = "404", description = "No interest rates available"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<InterestRateResponse> getInterestRates() {
        log.info("Received request to fetch interest rates");
        return interestRateService.getInterestRates();
    }
}
