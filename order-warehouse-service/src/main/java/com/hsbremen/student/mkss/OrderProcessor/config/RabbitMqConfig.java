package com.hsbremen.student.mkss.OrderProcessor.config;

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

    @Value("${my.rabbitmq.direct.exchange}")
    String directExchange;

    @Value("${my.rabbitmq.a.queue}")
    String newOrderQueue;

    @Value("${my.rabbitmq.reply.queue}")
    String replyQueue;

    @Value("${my.rabbitmq.a.routing.key}")
    String aRoutingKeyName;

    @Value("${my.rabbitmq.reply.routing.key}")
    String replyRoutingKey;

    @Bean("directExchange")
    DirectExchange directExchange() {
        return new DirectExchange(directExchange);
    }

    @Bean("newOrderQueue")
    Queue newOrderQueue() { return new Queue(newOrderQueue, false);}

    @Bean("WarehouseReplyQueue")
    Queue replyQueue() {
        return new Queue(replyQueue, false);
    }


    @Bean
    Binding newOrderBinding(@Qualifier("newOrderQueue") Queue queue, @Qualifier("directExchange") DirectExchange directExchange) {
        System.out.println("binded: " + directExchange + newOrderQueue + aRoutingKeyName);
        return BindingBuilder.bind(queue).to(directExchange).with(aRoutingKeyName);
    }

    @Bean
    Binding WarehouseReplyBinding(@Qualifier("WarehouseReplyQueue") Queue queue, @Qualifier("directExchange") DirectExchange directExchange) {
        System.out.println("binded: " + directExchange + replyQueue + replyRoutingKey);
        return BindingBuilder.bind(queue).to(directExchange).with(replyRoutingKey);
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
