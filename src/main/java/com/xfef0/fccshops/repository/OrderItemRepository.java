package com.xfef0.fccshops.repository;

import com.xfef0.fccshops.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
