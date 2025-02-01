package com.xfef0.fccshops.dto;

import com.xfef0.fccshops.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private Set<OrderItemDTO> items;
}
