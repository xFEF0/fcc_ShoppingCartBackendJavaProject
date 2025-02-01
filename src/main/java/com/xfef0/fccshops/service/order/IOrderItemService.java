package com.xfef0.fccshops.service.order;

import com.xfef0.fccshops.dto.OrderItemDTO;
import com.xfef0.fccshops.model.OrderItem;

public interface IOrderItemService {

    OrderItemDTO addItem(Long orderId, Long productId, int quantity);
    OrderItemDTO updateItem();
    void removeItem(Long orderId, Long productId);
}
