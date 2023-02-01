package com.hsbremen.student.mkss.restservice.eventshandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import de.hsbremen.mkss.events.CrudEventProducer;
import de.hsbremen.mkss.events.EventWithPayload;
import de.hsbremen.mkss.events.Event;
import com.hsbremen.student.mkss.restservice.model.Order;


@Component
public class EventsProducer implements CrudEventProducer<Order> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsProducer.class);

    private AmqpTemplate amqpTemplate;

    @Value("${my.rabbitmq.direct.exchange}")
    String directExchange;

    @Value("${my.rabbitmq.newOrder.routing.key}")
    String newOrderRoutingKey;

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

    @Override
    public void emitCreateEvent(Order payload) {
        EventWithPayload<Order> event = buildEvent(Event.EventType.CREATED, payload);
        amqpTemplate.convertAndSend(directExchange, newOrderRoutingKey, event);
        LOGGER.info(String.format("Send Event with -> Type: %s, Order Id: %s, Date: %s, Routing Key: %s",
                event.getType(), event.getPayload().getId(), event.getDate().toString(), newOrderRoutingKey));
    }

    @Override
    public void emitUpdateEvent(Order payload) {
        EventWithPayload<Order> event = buildEvent(Event.EventType.CHANGED, payload);
        amqpTemplate.convertAndSend(directExchange, newOrderRoutingKey, event);
    }

    @Override
    public void emitDeleteEvent(Order payload) {
        EventWithPayload<Order> event = buildEvent(Event.EventType.DELETED, payload);
        amqpTemplate.convertAndSend(directExchange, newOrderRoutingKey, event);
    }
}
