package com.hsbremen.student.mkss.OrderProcessor.eventsproducer;

import com.hsbremen.student.mkss.OrderProcessor.eventsproducer.EventsProducer;
import com.hsbremen.student.mkss.restservice.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import de.hsbremen.mkss.events.CrudEventProducer;
import de.hsbremen.mkss.events.EventWithPayload;
import de.hsbremen.mkss.events.Event;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


@Service
public class EventsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsConsumer.class);

    private EventsProducer eventsProducer;

    public EventsConsumer(EventsProducer eventsProducer) {
        this.eventsProducer = eventsProducer;
    }

    @RabbitListener(queues = {"${my.rabbitmq.a.queue}"})
    public void receiveMessage(EventWithPayload<Order> event) {
        LOGGER.info(String.format("Received message -> %s", event.toString()));
        eventsProducer.processOrder(event.getPayload());
    }
}
