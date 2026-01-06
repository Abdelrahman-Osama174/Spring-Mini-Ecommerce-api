package com.abdelrahman.EcommerceApp.service;

import com.abdelrahman.EcommerceApp.model.Order;
import com.abdelrahman.EcommerceApp.model.OrderItem;
import com.abdelrahman.EcommerceApp.model.Product;
import com.abdelrahman.EcommerceApp.model.dto.OrderItemRequest;
import com.abdelrahman.EcommerceApp.model.dto.OrderItemResponse;
import com.abdelrahman.EcommerceApp.model.dto.OrderRequest;
import com.abdelrahman.EcommerceApp.model.dto.OrderResponse;
import com.abdelrahman.EcommerceApp.repository.OrderRepository;
import com.abdelrahman.EcommerceApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;

    public OrderResponse placeOrder(OrderRequest request) {
        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setCustomerEmail(request.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));

            // Update the stock quantity of the product
            if (itemReq.quantity() > product.getStockQuantity()) {
                throw new RuntimeException("Product Quantity Not Enough");
            } else {
                product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
                productRepo.save(product);
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepo.save(order);

        // Mapped the savedOrder to orderResponse
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem orderItem : savedOrder.getOrderItems()) {
            OrderItemResponse ItemResponse = new OrderItemResponse(
                    orderItem.getProduct().getName(),
                    orderItem.getQuantity(),
                    orderItem.getTotalPrice()
            );

            itemResponses.add(ItemResponse);
        }

        OrderResponse orderResponse = new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getCustomerEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses
        );

        return orderResponse;
    }

    public List<OrderResponse> getAllOrderResponses() {
        List<Order> orders = orderRepo.findAll();

        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemResponse itemResponse = new OrderItemResponse(
                        orderItem.getProduct().getName(),
                        orderItem.getQuantity(),
                        orderItem.getTotalPrice()
                );
                itemResponses.add(itemResponse);
            }

            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getCustomerEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponses
            );
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }
}
