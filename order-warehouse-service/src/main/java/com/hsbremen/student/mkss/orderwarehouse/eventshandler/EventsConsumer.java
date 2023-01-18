package com.hsbremen.student.mkss.orderwarehouse.eventshandler;

import com.hsbremen.student.mkss.restservice.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.hsbremen.mkss.events.EventWithPayload;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


@Service
public class EventsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsConsumer.class);

    private EventsProducer eventsProducer;

    public EventsConsumer(EventsProducer eventsProducer) {
        this.eventsProducer = eventsProducer;
    }

    @RabbitListener(queues = {"${my.rabbitmq.newOrder.queue}"})
    public void receiveMessage(EventWithPayload<Order> event) {
        LOGGER.info(String.format("Received message with -> Type: %s, Order Id: %s, Date: %s",
                event.getType(), event.getPayload().getId(), event.getDate().toString()));
        eventsProducer.processOrder(event.getPayload());
    }
}
