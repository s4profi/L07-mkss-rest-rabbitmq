package com.hsbremen.student.mkss.OrderProcessor.eventsproducer;

import com.hsbremen.student.mkss.restservice.model.Order;
import de.hsbremen.mkss.events.Event;
import de.hsbremen.mkss.events.EventWithPayload;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import de.hsbremen.mkss.events.CrudEventProducer;


@Component
public class EventsProducer {

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

    public void emitCreateEvent(String payload) {
        //Status[] answer = {Status.ACCEPTED, Status.REJECTED};
        Order order = new Order();
        System.out.println(order);
        String[] answer = {"Status.ACCEPTED", "Status.REJECTED"};
        //Status updatedStatus = answer[(int) Math.round( Math.random())];
        //payload.setStatus(updatedStatus);
        EventWithPayload<String> event = buildEvent(Event.EventType.CREATED, payload);
        amqpTemplate.convertAndSend(anExchangeName, aRoutingKeyName, event);
        System.out.println("Sent event = " + event  + " using exchange " + anExchangeName + " with routing key " + aRoutingKeyName);
    }


}
