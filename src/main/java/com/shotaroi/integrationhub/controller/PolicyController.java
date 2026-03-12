package com.shotaroi.integrationhub.controller;

import com.shotaroi.integrationhub.dto.ExternalPolicyRequestDTO;
import com.shotaroi.integrationhub.dto.PolicyResponseDTO;
import com.shotaroi.integrationhub.dto.PolicyStatusUpdateDTO;
import com.shotaroi.integrationhub.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for insurance policy integration API.
 * Exposes endpoints for external systems to create and manage policies.
 */
@RestController
@RequestMapping("/api/integration/policies")
@Tag(name = "Policy Integration", description = "API for insurance policy creation and management")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new insurance policy")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Policy created successfully",
                    content = @Content(schema = @Schema(implementation = PolicyResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Policy number already exists")
    })
    public PolicyResponseDTO createPolicy(@Valid @RequestBody ExternalPolicyRequestDTO request) {
        return policyService.createPolicy(request);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get policy by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Policy found",
                    content = @Content(schema = @Schema(implementation = PolicyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Policy not found")
    })
    public PolicyResponseDTO getPolicy(@Parameter(description = "Policy ID") @PathVariable Long id) {
        return policyService.getPolicyById(id);
    }

    @PatchMapping(value = "/{id}/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update policy status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully",
                    content = @Content(schema = @Schema(implementation = PolicyResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Policy not found")
    })
    public PolicyResponseDTO updatePolicyStatus(
            @Parameter(description = "Policy ID") @PathVariable Long id,
            @Valid @RequestBody PolicyStatusUpdateDTO update) {
        return policyService.updatePolicyStatus(id, update);
    }
}
