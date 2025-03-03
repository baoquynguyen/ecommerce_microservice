package com.turtleDev.order.mapper;

import com.turtleDev.order.dto.OrderRequest;
import com.turtleDev.order.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public Order toOrder(OrderRequest request){
        return Order.builder()
                .id(request.id())
                .reference(request.reference())
                .customerId(request.customerId())
                .totalAmount(request.amount())
                .paymentMethod(request.paymentMethod())
                .build();
    }
}
