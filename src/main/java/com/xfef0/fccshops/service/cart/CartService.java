package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.dto.CartDTO;
import com.xfef0.fccshops.dto.CartItemDTO;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Cart;
import com.xfef0.fccshops.model.User;
import com.xfef0.fccshops.repository.CartItemRepository;
import com.xfef0.fccshops.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.setTotalAmount(cart.getTotalAmount());
        return cartRepository.save(cart);
    }

    @Override
    public CartDTO getCartDTO(Long id) {
        Cart cart = getCart(id);
        return convertToDTO(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        CartDTO cart = getCartDTO(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> createNewCart(user));
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItemDTO> itemDTOS = cart.getItems().stream()
                .map(cartItem -> {
                    CartItemDTO cartItemDTO = modelMapper.map(cartItem, CartItemDTO.class);
                    cartItem.setCart(null);
                    return cartItemDTO;
                })
                .toList();
        cartDTO.setCartItems(itemDTOS);
        return cartDTO;
    }
}
