package io.demo.langchain4j.sample.dto;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// 2. CustomerRepository
//   -  Extends PanacheRepository to get the basic CRUD operations.
//   -  Implements a custom method to get the customer name by ID.
@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    @Tool("get the customer name for the given customerId")
    public String getCustomerName(long id) {
        return find("id", id).firstResult().name;
    }
}

