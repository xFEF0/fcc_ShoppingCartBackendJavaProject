package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.dto.CartItemDTO;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Cart;
import com.xfef0.fccshops.model.CartItem;
import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.repository.CartItemRepository;
import com.xfef0.fccshops.repository.CartRepository;
import com.xfef0.fccshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ICartService cartService;
    private final IProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public CartItemDTO addCartItem(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartRepository.save(cart);
        return convertToDTO(cartItem);
    }

    @Override
    public void removeCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found!"));
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public CartItemDTO updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not in cart"));

        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(cartItem.getProduct().getPrice());
        cartItem.setTotalPrice();

        cart.updateTotalAmount();
        cartRepository.save(cart);

        return convertToDTO(cartItem);
    }

    private CartItemDTO convertToDTO(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDTO.class);
    }
}
