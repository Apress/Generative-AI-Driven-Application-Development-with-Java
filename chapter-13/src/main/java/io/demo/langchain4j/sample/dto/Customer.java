package io.demo.langchain4j.sample.dto;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

// 1. Customer Entity
//   - Represents the database table for customer data.
//   - Uses PanacheEntity for simplified JPA with Quarkus.
@Entity
public class Customer extends PanacheEntity {

    public String name;
    public String address;

    // Add constructors, getters, and setters as needed.
    public Customer() {
    }

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
