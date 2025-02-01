package com.xfef0.fccshops.service.order;

import com.xfef0.fccshops.dto.OrderDTO;

import java.util.List;

public interface IOrderService {
    OrderDTO placeOrder(Long userId);
    OrderDTO getOrder(Long orderId);
    List<OrderDTO> getUserOrders(Long userId);
}
