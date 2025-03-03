package com.turtleDev.order.mapper;

import com.turtleDev.order.dto.OrderLineRequest;
import com.turtleDev.order.model.Order;
import com.turtleDev.order.model.OrderLine;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.id())
                .quantity(request.quantity())
                .order(Order.builder().id(request.orderId()).build())
                .productId(request.productId())
                .build();
    }
}
