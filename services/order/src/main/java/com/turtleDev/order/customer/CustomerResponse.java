package com.turtleDev.order.customer;

public record CustomerResponse(
        String id,
        String firsName,
        String lastName,
        String email
) {
}
