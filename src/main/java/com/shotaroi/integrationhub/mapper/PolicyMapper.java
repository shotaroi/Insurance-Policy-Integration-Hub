package com.shotaroi.integrationhub.mapper;

import com.shotaroi.integrationhub.dto.ExternalPolicyRequestDTO;
import com.shotaroi.integrationhub.dto.PolicyResponseDTO;
import com.shotaroi.integrationhub.entity.PolicyEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between external DTOs and internal domain entities.
 * Centralizes data transformation logic for the integration layer.
 */
@Component
public class PolicyMapper {

    /**
     * Converts external API request to internal entity for persistence.
     */
    public PolicyEntity toEntity(ExternalPolicyRequestDTO dto) {
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyNumber(dto.policyNumber());
        entity.setCustomerId(dto.customerId());
        entity.setPolicyType(dto.policyType());
        entity.setPremiumAmount(dto.premiumAmount());
        entity.setStartDate(dto.startDate());
        entity.setStatus(dto.status());
        return entity;
    }

    /**
     * Converts entity to response DTO for API consumers.
     */
    public PolicyResponseDTO toResponseDTO(PolicyEntity entity) {
        return new PolicyResponseDTO(
                entity.getId(),
                entity.getPolicyNumber(),
                entity.getCustomerId(),
                entity.getPolicyType(),
                entity.getPremiumAmount(),
                entity.getStartDate(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
