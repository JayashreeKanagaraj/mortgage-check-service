package com.ing.mortgagecheckservice.controller;

import com.ing.mortgagecheckservice.modal.request.MortgageCheckRequest;
import com.ing.mortgagecheckservice.modal.response.MortgageCheckResponse;
import com.ing.mortgagecheckservice.service.MortgageCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/mortgage-check")
public class MortgageCheckController {

    private final MortgageCheckService mortgageCheckService;

    public MortgageCheckController(MortgageCheckService mortgageCheckService) {
        this.mortgageCheckService = mortgageCheckService;
    }

    /**
     * Processes a mortgage check request to determine feasibility and monthly cost.
     *
     * @param request The mortgage check request containing loan details.
     * @return Response entity with mortgage feasibility result and monthly cost.
     */
    @Operation(summary = "Check mortgage feasibility",
            description = "Validates if the requested mortgage is feasible based on income, home value, and loan amount.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mortgage check successful",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = MortgageCheckResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(produces = "application/json")
    public MortgageCheckResponse processMortgageCheck(@Valid @RequestBody MortgageCheckRequest request) {
        log.info("Received mortgage check request: Loan Value={}, Income={}, Home Value={}, Maturity Period={}",
                request.loanValue(), request.income(), request.homeValue(), request.maturityPeriod());

        return mortgageCheckService.processMortgageCheck(request);
    }
}
