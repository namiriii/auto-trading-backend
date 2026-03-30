package com.namil.autotrading.repository;

import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);


}
