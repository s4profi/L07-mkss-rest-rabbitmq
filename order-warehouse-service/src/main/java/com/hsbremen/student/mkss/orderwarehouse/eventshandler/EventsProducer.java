package com.hsbremen.student.mkss.orderwarehouse.eventshandler;

import com.hsbremen.student.mkss.restservice.model.Order;
import com.hsbremen.student.mkss.restservice.util.Status;
import de.hsbremen.mkss.events.Event;
import de.hsbremen.mkss.events.EventWithPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Random;
import de.hsbremen.mkss.events.CrudEventProducer;


@Component
public class EventsProducer implements CrudEventProducer<Order> {
    int randomNumber;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsProducer.class);

    private AmqpTemplate amqpTemplate;

    @Value("${my.rabbitmq.direct.exchange}")
    String directExchange;

    @Value("${my.rabbitmq.reply.routing.key}")
    String replyRoutingKey;

    public EventsProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }


    private EventWithPayload<Order> buildEvent(Event.EventType type, Order payload) {
        EventWithPayload<Order> event = EventWithPayload.<Order> builder()
                .type(type)
                .payload(payload)
                .build();
        return event;
    }

    public void processOrder(Order payload) {
        //System.out.println(payload);
        Random random = new Random();
        randomNumber = random.nextInt(2);
        if (randomNumber == 0) {
            payload.setStatus(Status.REJECTED);
            emitUpdateEvent(payload);
        } else {
            payload.setStatus(Status.ACCEPTED);
            emitUpdateEvent(payload);
        }
    }

    @Override
    public void emitUpdateEvent(Order payload) {
        EventWithPayload<Order> event = buildEvent(Event.EventType.CHANGED, payload);
        amqpTemplate.convertAndSend(directExchange, replyRoutingKey, event);
        LOGGER.info(String.format("Send Update with -> Type: %s, Order Id: %s, Date: %s, Routing Key: %s",
                event.getType(), event.getPayload().getId(), event.getDate().toString(), replyRoutingKey));
    }

    @Override
    public void emitDeleteEvent(Order payload) {
        EventWithPayload<Order> event = buildEvent(Event.EventType.DELETED, payload);
        amqpTemplate.convertAndSend(directExchange, replyRoutingKey, event);
    }

    @Override
    public void emitCreateEvent(Order payload) {
        EventWithPayload<Order> event = buildEvent(Event.EventType.CREATED, payload);
        amqpTemplate.convertAndSend(directExchange, replyRoutingKey, event);
    }
}
