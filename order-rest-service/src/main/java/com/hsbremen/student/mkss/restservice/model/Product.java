package com.hsbremen.student.mkss.restservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Product of our shop.")
@Entity
public class Product extends LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank
    private int unitPrice;
    @NotBlank
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product(String name, int unitPrice, int quantity) { // here name was called name other its n
        setName(name);
        setPrice(unitPrice);
        setQuantity(quantity);
    }

    public Product() {
        super();
    }

    public void setPrice(int price)
    {
        if(price < 0) throw new IllegalArgumentException("Invalid price!");
        this.unitPrice = price;
    }

    public int getPrice() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return quantity + " * " + super.getName();
    }

    public void print() {
        System.out.println(toString());
    }

    public void setQuantity(int quantity)
    {
        if(quantity < 0) throw new IllegalArgumentException("Invalid quantity!");
        this.quantity = quantity;
    }

    public int getQuantity(){ return quantity; };

}