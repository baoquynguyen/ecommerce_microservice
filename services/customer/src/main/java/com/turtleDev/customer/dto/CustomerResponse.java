package com.turtleDev.customer.dto;

import com.turtleDev.customer.model.Address;

public record CustomerResponse (
        String id,
        String firstName,
        String lastName,
        String email,
        Address address
) {
}
