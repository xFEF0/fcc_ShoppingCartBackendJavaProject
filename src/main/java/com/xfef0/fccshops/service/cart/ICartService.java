package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.dto.CartDTO;
import com.xfef0.fccshops.model.Cart;

import java.math.BigDecimal;

public interface ICartService {

    CartDTO getCartDTO(Long id);
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Long initializeCart();
    Cart getCartByUserId(Long userId);
}
