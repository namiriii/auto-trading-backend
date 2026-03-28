package com.namil.autotrading.repository;

import com.namil.autotrading.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {


}
