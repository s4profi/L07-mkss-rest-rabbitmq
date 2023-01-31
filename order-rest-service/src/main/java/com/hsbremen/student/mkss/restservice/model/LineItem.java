package com.hsbremen.student.mkss.restservice.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Line item for orders.")
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@JsonDeserialize(as=Product.class)
public abstract class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void setPrice(int price);

    public abstract int getPrice();

    public abstract void print();
}