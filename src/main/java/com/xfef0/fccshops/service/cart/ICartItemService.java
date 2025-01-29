package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.model.CartItem;

public interface ICartItemService {

    CartItem addCartItem(Long cartId, Long productId, int quantity);
    void removeCartItem(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
}
