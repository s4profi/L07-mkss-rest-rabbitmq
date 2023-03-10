package com.hsbremen.student.mkss.restservice.model;


import com.hsbremen.student.mkss.restservice.util.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Schema(description = "Model object for orders.")
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<LineItem> items = new ArrayList<>();
    public Status status = Status.EMPTY;

    private String customerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void addProduct(Product product) {
        this.items.add(product);
    }

    public void deleteItem(int itemId) {
        int index = 0;
        for (LineItem i : items){
            if (i.getId() == itemId){break;};
            index = index + 1;
        }
        items.remove(index);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus(){ return status;};
}
