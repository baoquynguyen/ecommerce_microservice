package com.turtleDev.customer.controller;

import com.turtleDev.customer.dto.CustomerRequest;
import com.turtleDev.customer.dto.CustomerResponse;
import com.turtleDev.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerRequest request) {
        customerService.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> retrieveAllCustomers() {
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    @GetMapping("/exist/{customer-id}/")
    public ResponseEntity<Boolean> existsById(@PathVariable("customer-id") String customerId ){
        return ResponseEntity.ok(customerService.existsById(customerId));
    }

    @GetMapping("/{customer-id}/")
    public ResponseEntity<CustomerResponse> retrieveCustomerById(@PathVariable("customer-id") String customerId ){
        return ResponseEntity.ok(customerService.findCustomerById(customerId));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> removeCustomerById(@PathVariable("customer-id") String customerId){
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.accepted().build();
    }
}
