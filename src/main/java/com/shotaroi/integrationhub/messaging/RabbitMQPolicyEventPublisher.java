package com.shotaroi.integrationhub.messaging;

import com.shotaroi.integrationhub.dto.PolicyResponseDTO;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ implementation of PolicyEventPublisher.
 * Publishes policy lifecycle events to the message queue (simulating IBM MQ).
 */
@Component
@ConditionalOnBean(RabbitTemplate.class)
public class RabbitMQPolicyEventPublisher implements PolicyEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQPolicyEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${integration.messaging.exchange:policy.events}")
    private String exchange;

    @Value("${integration.messaging.routing-key.created:policy.created}")
    private String routingKeyCreated;

    @Value("${integration.messaging.routing-key.updated:policy.updated}")
    private String routingKeyUpdated;

    @Value("${integration.messaging.routing-key.cancelled:policy.cancelled}")
    private String routingKeyCancelled;

    public RabbitMQPolicyEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishPolicyCreated(PolicyResponseDTO policy) {
        PolicyEventMessage message = new PolicyEventMessage("POLICY_CREATED", policy);
        rabbitTemplate.convertAndSend(exchange, routingKeyCreated, message);
        log.info("Published POLICY_CREATED event for policy {} (id={})", policy.policyNumber(), policy.id());
    }

    @Override
    public void publishPolicyUpdated(PolicyResponseDTO policy, PolicyStatus previousStatus) {
        PolicyEventMessage message = new PolicyEventMessage("POLICY_UPDATED", policy, previousStatus);
        rabbitTemplate.convertAndSend(exchange, routingKeyUpdated, message);
        log.info("Published POLICY_UPDATED event for policy {} (id={}), previousStatus={}",
                policy.policyNumber(), policy.id(), previousStatus);
    }

    @Override
    public void publishPolicyCancelled(PolicyResponseDTO policy) {
        PolicyEventMessage message = new PolicyEventMessage("POLICY_CANCELLED", policy);
        rabbitTemplate.convertAndSend(exchange, routingKeyCancelled, message);
        log.info("Published POLICY_CANCELLED event for policy {} (id={})", policy.policyNumber(), policy.id());
    }
}
