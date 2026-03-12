package com.shotaroi.integrationhub.dto;

import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for PATCH /policies/{id}/status requests.
 */
public record PolicyStatusUpdateDTO(
        @NotNull(message = "Status is required")
        PolicyStatus status
) {}
