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

    @Value("${my.rabbitmq.an.exchange}")
    String anExchangeName;

    @Value("${my.rabbitmq.a.queue}")
    String aQueueName;

    @Value("${my.rabbitmq.a.routing.key}")
    String aRoutingKeyName;

    @Bean("someExchange")
    DirectExchange someExchange() {
        return new DirectExchange(anExchangeName);
    }

    @Bean("someQueue")
    Queue someQueue() {
        return new Queue(aQueueName, false);
    }


    @Bean
    Binding someBinding(@Qualifier("someQueue") Queue queue, @Qualifier("someExchange") DirectExchange exchange) {
        System.out.println("binded: " + anExchangeName + aQueueName + aRoutingKeyName);
        return BindingBuilder.bind(queue).to(exchange).with(aRoutingKeyName);
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
