package com.shotaroi.integrationhub.messaging;

import com.shotaroi.integrationhub.dto.PolicyResponseDTO;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;

/**
 * Interface for publishing policy lifecycle events.
 * Allows test implementations without RabbitMQ.
 */
public interface PolicyEventPublisher {

    void publishPolicyCreated(PolicyResponseDTO policy);

    void publishPolicyUpdated(PolicyResponseDTO policy, PolicyStatus previousStatus);

    void publishPolicyCancelled(PolicyResponseDTO policy);
}
