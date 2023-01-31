package com.hsbremen.student.mkss.restservice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Order Input Data", description = "DTO for either order to be created or line item to be added. " +
        "To add order, customerName is required. To add LineItem name, price and quantity is required.")
public class OrderInputData {

    @NotBlank
    @JsonProperty
    private String customerName;
    @NotBlank
    @JsonProperty
    private String name;
    @NotBlank
    @JsonProperty
    private int price;
    @NotBlank
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


    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}