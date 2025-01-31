package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.dto.CartDTO;
import com.xfef0.fccshops.model.Cart;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Long initializeCart();
    CartDTO convertToDTO(Cart cart);
}
