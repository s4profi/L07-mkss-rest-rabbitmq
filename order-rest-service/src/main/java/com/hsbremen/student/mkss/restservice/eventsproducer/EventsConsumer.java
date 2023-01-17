package com.hsbremen.student.mkss.restservice.eventsproducer;

import com.hsbremen.student.mkss.restservice.model.Order;
import com.hsbremen.student.mkss.restservice.service.OrderService;
import com.hsbremen.student.mkss.restservice.util.Status;
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

    private OrderService orderService;
    public EventsConsumer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @RabbitListener(queues="${my.rabbitmq.a.warehouse.queue}")
    public void receiveMessage(EventWithPayload<String> event) {
        System.out.println(event);
        //System.out.println("was " + event.getStatus());
        //if(event.getStatus() == Status.ACCEPTED){
        if(event.getPayload().equals("Antwort vom Listener")){
            //orderService.saveOrder(event.getpayload());
            System.out.println("Order was updated");
        }
        else{
            System.out.println("Order was ignored");
        }
    }
}
