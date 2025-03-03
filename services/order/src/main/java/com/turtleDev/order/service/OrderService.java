package com.turtleDev.order.service;

import com.turtleDev.order.customer.CustomerClient;
import com.turtleDev.order.dto.OrderLineRequest;
import com.turtleDev.order.dto.OrderRequest;
import com.turtleDev.order.exception.BusinessException;
import com.turtleDev.order.mapper.OrderMapper;
import com.turtleDev.order.product.ProductClient;
import com.turtleDev.order.product.PurchaseRequest;
import com.turtleDev.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;

    public Integer createOrder(OrderRequest request) {
        //check the  customer -> OpenFein
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order. No customer existed with provider ID"));

        //purchase the products --> product microservice (REST template)
        this.productClient.purchaseProducts(request.product());

        //persist order
        var order = this.orderRepository.save(orderMapper.toOrder(request));

        //persist orderLine
        for(PurchaseRequest purchaseRequest : request.product()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        //todo start payment process

        //send the order conformation -> notification microservice (kafka)
        return null;
    }
}
