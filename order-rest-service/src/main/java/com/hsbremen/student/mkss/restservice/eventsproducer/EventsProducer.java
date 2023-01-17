package com.hsbremen.student.mkss.restservice.eventsproducer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import de.hsbremen.mkss.events.CrudEventProducer;
import de.hsbremen.mkss.events.EventWithPayload;
import de.hsbremen.mkss.events.Event;
import com.hsbremen.student.mkss.restservice.model.Order;


@Component
public class EventsProducer implements CrudEventProducer<String> {

    private AmqpTemplate amqpTemplate;

    @Value("${my.rabbitmq.an.exchange}")
    String anExchangeName;

    @Value("${my.rabbitmq.a.routing.key}")
    String aRoutingKeyName;


    public EventsProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }


    private EventWithPayload<String> buildEvent(Event.EventType type, String payload) {
        EventWithPayload<String> event = EventWithPayload.<String> builder()
                .type(type)
                .payload(payload)
                .build();
        return event;
    }

    @Override
    public void emitCreateEvent(String payload) {
        EventWithPayload<String> event = buildEvent(Event.EventType.CREATED, payload);
        amqpTemplate.convertAndSend(anExchangeName, aRoutingKeyName, event);
        System.out.println("Sent event = " + event + " using exchange " + anExchangeName + " with routing key " + aRoutingKeyName);
    }

    @Override
    public void emitUpdateEvent(String payload) {
        // TODO: Implementation for update events (e.g. changed Order)
    }

    @Override
    public void emitDeleteEvent(String payload) {
        // TODO: Implementation for delete events (e.g. deleted Order)
    }
}
