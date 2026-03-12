package com.shotaroi.integrationhub.dto;

import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import com.shotaroi.integrationhub.entity.enums.PolicyType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for policy API responses.
 * Exposes only the fields appropriate for external consumers.
 */
public record PolicyResponseDTO(
        Long id,
        String policyNumber,
        String customerId,
        PolicyType policyType,
        BigDecimal premiumAmount,
        LocalDate startDate,
        PolicyStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
