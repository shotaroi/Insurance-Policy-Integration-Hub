package com.shotaroi.integrationhub.messaging;

import com.shotaroi.integrationhub.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * Consumes policy events from RabbitMQ.
 * Simulates downstream systems (audit, analytics, legacy sync) processing events.
 */
@Component
@ConditionalOnBean(RabbitTemplate.class)
public class PolicyEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PolicyEventConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.POLICY_EVENTS_QUEUE)
    public void handlePolicyEvent(PolicyEventMessage message) {
        log.info("Received policy event: type={}, policyNumber={}, policyId={}",
                message.eventType(),
                message.policy() != null ? message.policy().policyNumber() : "N/A",
                message.policy() != null ? message.policy().id() : "N/A");

        switch (message.eventType()) {
            case "POLICY_CREATED" -> processPolicyCreated(message);
            case "POLICY_UPDATED" -> processPolicyUpdated(message);
            case "POLICY_CANCELLED" -> processPolicyCancelled(message);
            default -> log.warn("Unknown event type: {}", message.eventType());
        }
    }

    private void processPolicyCreated(PolicyEventMessage message) {
        log.info("Processing POLICY_CREATED: policy {} created for customer {}",
                message.policy().policyNumber(), message.policy().customerId());
        // In production: sync to legacy systems, update analytics, send notifications
    }

    private void processPolicyUpdated(PolicyEventMessage message) {
        log.info("Processing POLICY_UPDATED: policy {} status changed, previousStatus={}",
                message.policy().policyNumber(), message.previousStatus());
        // In production: propagate to downstream systems
    }

    private void processPolicyCancelled(PolicyEventMessage message) {
        log.info("Processing POLICY_CANCELLED: policy {} cancelled",
                message.policy().policyNumber());
        // In production: trigger refund workflows, update billing
    }
}
