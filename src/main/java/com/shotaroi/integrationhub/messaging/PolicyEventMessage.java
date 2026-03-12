package com.shotaroi.integrationhub.messaging;

import com.shotaroi.integrationhub.dto.PolicyResponseDTO;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;

/**
 * Message payload for policy events published to RabbitMQ.
 * Serialized as JSON via Jackson for interoperability.
 */
public record PolicyEventMessage(
        String eventType,
        PolicyResponseDTO policy,
        PolicyStatus previousStatus
) {

    public PolicyEventMessage(String eventType, PolicyResponseDTO policy) {
        this(eventType, policy, null);
    }
}
