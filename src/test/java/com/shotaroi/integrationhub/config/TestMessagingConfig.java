package com.shotaroi.integrationhub.config;

import com.shotaroi.integrationhub.dto.PolicyResponseDTO;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import com.shotaroi.integrationhub.messaging.PolicyEventPublisher;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration that provides a no-op PolicyEventPublisher
 * so integration tests can run without RabbitMQ.
 */
@TestConfiguration
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class TestMessagingConfig {

    @Bean
    @Primary
    public PolicyEventPublisher noOpPolicyEventPublisher() {
        return new PolicyEventPublisher() {
            @Override
            public void publishPolicyCreated(PolicyResponseDTO policy) {
                // No-op for tests
            }

            @Override
            public void publishPolicyUpdated(PolicyResponseDTO policy, PolicyStatus previousStatus) {
                // No-op for tests
            }

            @Override
            public void publishPolicyCancelled(PolicyResponseDTO policy) {
                // No-op for tests
            }
        };
    }
}
