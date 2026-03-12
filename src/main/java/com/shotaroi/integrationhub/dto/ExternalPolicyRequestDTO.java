package com.shotaroi.integrationhub.dto;

import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import com.shotaroi.integrationhub.entity.enums.PolicyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * External API request DTO for creating a new insurance policy.
 * Represents the contract from external systems calling our integration API.
 */
public record ExternalPolicyRequestDTO(
        @NotBlank(message = "Policy number is required")
        String policyNumber,

        @NotBlank(message = "Customer ID is required")
        String customerId,

        @NotNull(message = "Policy type is required")
        PolicyType policyType,

        @NotNull(message = "Premium amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Premium amount must be positive")
        BigDecimal premiumAmount,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        PolicyStatus status
) {
    /**
     * Default status to PENDING if not provided.
     */
    public ExternalPolicyRequestDTO {
        if (status == null) {
            status = PolicyStatus.PENDING;
        }
    }
}
