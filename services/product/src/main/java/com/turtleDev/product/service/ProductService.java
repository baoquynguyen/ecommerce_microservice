package com.turtleDev.product.service;

import com.turtleDev.product.dto.ProductPurchaseRequest;
import com.turtleDev.product.dto.ProductPurchaseResponse;
import com.turtleDev.product.dto.ProductRequest;
import com.turtleDev.product.dto.ProductResponse;
import com.turtleDev.product.exception.ProductPurchaseException;
import com.turtleDev.product.mapper.ProductMapper;
import com.turtleDev.product.model.Product;
import com.turtleDev.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(@Valid ProductRequest request) {
        var product = productMapper.toProduct(request);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        //extract list product id from request
        var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        //check all product want to purchase whether already existed in the db or not
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);
        //compare products in request with products in db
        if(productIds.size() != storedProducts.size()){
            throw new ProductPurchaseException("One or more products does not exists");
        }

        //extract list stored request
        var storesRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for(int i=0; i<storedProducts.size(); i++){
            var product = storedProducts.get(i);
            var productRequest = storesRequest.get(i);
            //compare available quantity of product in db with in request
            if(product.getAvailableQuantity() < productRequest.quantity()){
                throw new ProductPurchaseException("Insufficient stock quantity for product with " + productRequest.productId());
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);
            purchasedProducts.add(productMapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts;
    }

    public ProductResponse findProductById(Integer productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the ID " + productId));
    }

    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
