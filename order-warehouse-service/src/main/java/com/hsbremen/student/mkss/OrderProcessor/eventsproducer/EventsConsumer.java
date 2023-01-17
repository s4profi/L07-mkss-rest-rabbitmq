package com.hsbremen.student.mkss.OrderProcessor.eventsproducer;

import com.hsbremen.student.mkss.OrderProcessor.eventsproducer.EventsProducer;
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

    private AmqpTemplate amqpTemplate;

    private EventsProducer eventsProducer;

    public EventsConsumer(AmqpTemplate amqpTemplate, EventsProducer eventsProducer) {
        this.amqpTemplate = amqpTemplate;
        this.eventsProducer = eventsProducer;
    }


    private EventWithPayload<String> buildEvent(Event.EventType type, String payload) {
        EventWithPayload<String> event = EventWithPayload.<String> builder()
                .type(type)
                .payload(payload)
                .build();
        return event;
    }

    @RabbitListener(queues="${my.rabbitmq.a.rest.queue}")
    public void receiveMessage(EventWithPayload<String> event) {
        System.out.print("Hallo");
        System.out.println(event);
        eventsProducer.emitCreateEvent("Antwort vom Listener");
    }
}
