package com.shotaroi.integrationhub.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for policy event messaging.
 * Declares exchange, queues, bindings, and JSON message converter.
 */
@Configuration
@ConditionalOnBean(ConnectionFactory.class)
public class RabbitMQConfig {

    public static final String POLICY_EVENTS_QUEUE = "policy.events.queue";

    @Value("${integration.messaging.exchange:policy.events}")
    private String exchange;

    @Value("${integration.messaging.routing-key.created:policy.created}")
    private String routingKeyCreated;

    @Value("${integration.messaging.routing-key.updated:policy.updated}")
    private String routingKeyUpdated;

    @Value("${integration.messaging.routing-key.cancelled:policy.cancelled}")
    private String routingKeyCancelled;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange policyEventsExchange() {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public Queue policyEventsQueue() {
        return new Queue(POLICY_EVENTS_QUEUE, true);
    }

    @Bean
    public Binding policyCreatedBinding(Queue policyEventsQueue, DirectExchange policyEventsExchange) {
        return BindingBuilder.bind(policyEventsQueue).to(policyEventsExchange).with(routingKeyCreated);
    }

    @Bean
    public Binding policyUpdatedBinding(Queue policyEventsQueue, DirectExchange policyEventsExchange) {
        return BindingBuilder.bind(policyEventsQueue).to(policyEventsExchange).with(routingKeyUpdated);
    }

    @Bean
    public Binding policyCancelledBinding(Queue policyEventsQueue, DirectExchange policyEventsExchange) {
        return BindingBuilder.bind(policyEventsQueue).to(policyEventsExchange).with(routingKeyCancelled);
    }
}
