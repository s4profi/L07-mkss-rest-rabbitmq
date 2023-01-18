package com.hsbremen.student.mkss.restservice.eventshandler;

import com.hsbremen.student.mkss.restservice.model.Order;
import com.hsbremen.student.mkss.restservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.hsbremen.mkss.events.EventWithPayload;
import de.hsbremen.mkss.events.Event;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


@Service
public class EventsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsConsumer.class);

    private OrderService orderService;
    public EventsConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = {"${my.rabbitmq.reply.queue}"} )
    public void receiveMessage(EventWithPayload<Order> event) {

        if(Event.EventType.CHANGED == event.getType()) {
                orderService.saveOrder(event.getPayload());
                LOGGER.info(String.format("Order updated -> Status: %s, Type: %s, Order Id: %s, Date: %s",
                        event.getPayload().getStatus(), event.getType(), event.getPayload().getId(), event.getDate().toString()));
        } else {
            LOGGER.info(String.format("Wrong Event Type. Order with Id: " + event.getPayload().getId() + " is ignored"));
        }
    }
}
