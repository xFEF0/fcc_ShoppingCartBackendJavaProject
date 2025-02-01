package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.dto.CartItemDTO;

public interface ICartItemService {

    CartItemDTO addCartItem(Long cartId, Long productId, int quantity);
    CartItemDTO updateItemQuantity(Long cartId, Long productId, int quantity);
    void removeCartItem(Long cartId, Long productId);
}
