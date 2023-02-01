package com.hsbremen.student.mkss.restservice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Order Input Data", description = "DTO for either order to be created or line item to be added. " +
        "To add order, customerName is required. To add LineItem name, price and quantity is required.")
public class OrderInputData {

    @JsonProperty
    private String customerName;

    @JsonProperty
    private String name;

    @JsonProperty
    private int price;

    @JsonProperty
    private int quantity;

    /**
     * Builder Pattern
     */
    public OrderInputData() {
        //empty constructor
    }

    public OrderInputData(String name) {
        if(name == null)
            throw new IllegalArgumentException("Invalid name");
        this.name = name;
    }

    public OrderInputData price(int price) {
        this.price = price;
        return this;
    }

    public OrderInputData quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @NotNull
    public String getName() { return name; }
    @NotNull
    public int getPrice() { return price; }
    @NotNull
    public int getQuantity() { return quantity; }
    @NotNull
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}