package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.dto.CartDTO;
import com.xfef0.fccshops.model.Cart;
import com.xfef0.fccshops.model.User;

import java.math.BigDecimal;

public interface ICartService {

    CartDTO getCartDTO(Long id);
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeCart(User user);
    Cart getCartByUserId(Long userId);
}
