package com.hsbremen.student.mkss.OrderProcessor.eventsproducer;

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
        System.out.println(payload);
        Random random = new Random();
        randomNumber = random.nextInt(2);
        if (randomNumber == 0) {
            payload.setStatus(Status.REJECTED);
            emitDeleteEvent(payload);
        } else {
            payload.setStatus(Status.ACCEPTED);
            emitUpdateEvent(payload);
        }
        //Status[] answer = {Status.ACCEPTED, Status.REJECTED};
        //String[] answer = {"Status.ACCEPTED", "Status.REJECTED"};
        //Status updatedStatus = answer[(int) Math.round( Math.random())];
        //payload.setStatus(updatedStatus);
       // System.out.println("Sent event = " + event  + " using exchange " + directExchange + " with routing key " + replyRoutingKey);
    }

    @Override
    public void emitUpdateEvent(Order payload) {
        EventWithPayload<Order> event = buildEvent(Event.EventType.CHANGED, payload);
        amqpTemplate.convertAndSend(directExchange, replyRoutingKey, event);
        LOGGER.info(String.format("Send Status -> %s", event.toString()));

    }

    @Override
    public void emitDeleteEvent(Order payload) {
        EventWithPayload<Order> event = buildEvent(Event.EventType.DELETED, payload);
        amqpTemplate.convertAndSend(directExchange, replyRoutingKey, event);
        LOGGER.info(String.format("Send Status -> %s", event.toString()));
    }


    @Override
    public void emitCreateEvent(Order payload) {

    }
}
