package com.hsbremen.student.mkss.restservice.controller;

import java.util.List;

import com.hsbremen.student.mkss.restservice.eventshandler.EventsProducer;
import com.hsbremen.student.mkss.restservice.model.LineItem;
import com.hsbremen.student.mkss.restservice.model.Order;
import com.hsbremen.student.mkss.restservice.service.OrderService;
import com.hsbremen.student.mkss.restservice.util.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Order", description = "Routes for order rest service.")
@RestController
public class OrderController {

    private final OrderService orderService;

    private final EventsProducer eventsProducer;
    public OrderController(OrderService orderService, EventsProducer eventsProducer){
        this.orderService = orderService;
        this.eventsProducer = eventsProducer;
    }
    @Operation(summary = "List all orders", description = "Return all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all orders",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Order.class)))
            })
    })
    @GetMapping("/orders")
    public ResponseEntity listOrders() {
        List orders = this.orderService.findAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @Operation(summary = "Get order by id", description = "Return a single order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No order found with this",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/orders/{orderId}")
    public ResponseEntity getOrder(@PathVariable Integer orderId) {
        Order order = this.orderService.findOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>("No order found with this id", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @Operation(summary = "Get line items from order", description = "Return all line items of an order with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all line items of order with id",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LineItem.class)))
                    }),
            @ApiResponse(responseCode = "404", description = "No order found with this id",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity getLineItems(@PathVariable Integer orderId) {
        Order order = this.orderService.findOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>("No order found with this id.",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order.items, HttpStatus.OK);
    }

    @Operation(summary = "Create new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created order with new id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))
                    })
    })
    @PostMapping("/orders")
    public ResponseEntity<Order> addOrder(@RequestBody OrderInputData orderInputData){
        Order order = new Order();
        order.setCustomerName(orderInputData.getCustomerName());
        this.orderService.saveOrder(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @Operation(summary = "Add line item to order", description = "Returns the order with the added line item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added line item to order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No order found with this id",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "406", description = "Line items may only be added or removed as " +
                    "long as the order has not been committed",
                    content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("/orders/{orderId}/items")
    public ResponseEntity addItemToOrder(@PathVariable Integer orderId,@RequestBody OrderInputData orderInputData) {
        Order order = this.orderService.findOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>("No order found with this id.", HttpStatus.NOT_FOUND);
        }
        order = orderService.addProduct(orderId, orderInputData);
        if(order == null) {
            return new ResponseEntity<>("Line items may only be added or removed as long as the order has not been committed",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @Operation(summary = "Delete item from order", description = "Return order after deleting line item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted line item from order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No order found with this id",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "406", description = "Line items may only be added or removed as " +
                    "long as the order has not been committed",
                    content = @Content(mediaType = "text/plain"))
    })
    @Transactional
    @PutMapping("/orders/{orderId}/{itemId}")  // Put for updating order
    public ResponseEntity deleteItemFromOrder(@PathVariable Integer orderId, @PathVariable Integer itemId){
        Order order = this.orderService.findOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>("No order found with this id.", HttpStatus.NOT_FOUND);
        }
        order = this.orderService.deleteItem(orderId, itemId);
        if(order == null) {
            return new ResponseEntity<>("Line items may only be added or removed as long as the order has not been committed",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @Operation(summary = "Delete item from order", description = "Return order with new status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Committed order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No order found with this id",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "406", description = "An order may only be purchased in its\\n\" +\n" +
                    "                    \"IN_PREPARATION status!",
                    content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("/orders/{orderId}/commit") // PutMapping because not completely processed yet
    public ResponseEntity commitOrder(@PathVariable Integer orderId) {
        Order order = this.orderService.findOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>("No order found with this id.", HttpStatus.NOT_FOUND);
        }
        if (!order.status.equals(Status.IN_PREPARATION)){
            return new ResponseEntity<>("An order may only be purchased in its\n" +
                    "IN_PREPARATION status! ", HttpStatus.NOT_ACCEPTABLE);
        }
        order.setStatus(Status.COMMITTED);
        this.orderService.saveOrder(order);
        eventsProducer.emitCreateEvent(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @Operation(summary = "Delete item from order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "No order found with this id",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "406", description = "An order may only be purchased in its IN_PREPARATION status!",
                    content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable Integer orderId){
        Order order = this.orderService.findOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>("No order found with this id.", HttpStatus.NOT_FOUND);
        }
        if(order.status.equals(Status.COMMITTED)) {
            return new ResponseEntity<>("Order may only be deleted when order has not been committed",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        this.orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }
}
