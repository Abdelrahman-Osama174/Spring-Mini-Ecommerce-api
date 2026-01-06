package com.abdelrahman.EcommerceApp.repository;

import com.abdelrahman.EcommerceApp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
