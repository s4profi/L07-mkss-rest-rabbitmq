package com.hsbremen.student.mkss.restservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Value("${my.rabbitmq.direct.exchange}")
    String directExchange;

    @Value("${my.rabbitmq.newOrder.queue}")
    String newOrderQueue;

    @Value("${my.rabbitmq.reply.queue}")
    String replyQueue;

    @Value("${my.rabbitmq.newOrder.routing.key}")
    String newOrderRoutingKey;

    @Value("${my.rabbitmq.reply.routing.key}")
    String replyRoutingKey;


    // Exchanges are required for emitting and receiving event messages
    @Bean("directExchange")
    DirectExchange directExchange() {
        return new DirectExchange(directExchange);
    }

    // Queues are required for receiving event messages
    @Bean("newOrderQueue")
    Queue newOrderQueue() {
        return new Queue(newOrderQueue, false);
    }

    @Bean("WarehouseReplyQueue")
    Queue replyQueue() {
        return new Queue(replyQueue, false);
    }

    // Bindings are required for receiving event messages:
    // connecting of a queue to an exchange
    @Bean
    Binding newOrderBinding(@Qualifier("newOrderQueue") Queue queue, @Qualifier("directExchange") DirectExchange exchange) {
        LOGGER.info(String.format("binded: " + directExchange + " " + newOrderQueue + " " + newOrderRoutingKey));
        return BindingBuilder.bind(queue).to(exchange).with(newOrderRoutingKey);
    }

    @Bean
    Binding WarehouseReplyBinding(@Qualifier("WarehouseReplyQueue") Queue queue, @Qualifier("directExchange") DirectExchange exchange) {
        LOGGER.info(String.format("binded: " + directExchange + " " + replyQueue + " " + replyRoutingKey));
        return BindingBuilder.bind(queue).to(exchange).with(replyRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
