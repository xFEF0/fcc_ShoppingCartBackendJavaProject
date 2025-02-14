package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.dto.CartDTO;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Cart;
import com.xfef0.fccshops.model.CartItem;
import com.xfef0.fccshops.model.User;
import com.xfef0.fccshops.repository.CartItemRepository;
import com.xfef0.fccshops.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private static final Long USER_ID = 1L;
    private static final Long CART_ID = 2L;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ModelMapper modelMapper;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepository, cartItemRepository, modelMapper);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullInGetCartByUserId() {
        assertThatThrownBy(() -> cartService.getCartByUserId(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldGetCartByUserId() {
        Cart cart = new Cart();
        when(cartRepository.findByUserId(eq(USER_ID))).thenReturn(cart);

        assertThat(cartService.getCartByUserId(USER_ID))
                .isEqualTo(cart);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNullInInitializeCart() {
        assertThatThrownBy(() -> cartService.initializeCart(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldInitializeCart() {
        User user = new User();
        user.setId(USER_ID);
        Cart cart = new Cart();
        when(cartRepository.findByUserId(eq(USER_ID))).thenReturn(cart);

        assertThat(cartService.initializeCart(user))
                .isEqualTo(cart);

        verify(cartRepository, times(1)).findByUserId(eq(USER_ID));
    }

    @Test
    void shouldInitializeNewCart() {
        User user = new User();
        user.setId(USER_ID);
        Cart cart = new Cart();
        when(cartRepository.findByUserId(eq(USER_ID))).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        assertThat(cartService.initializeCart(user))
                .isEqualTo(cart);

        verify(cartRepository, times(1)).findByUserId(eq(USER_ID));
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullInGetCart() {
        assertThatThrownBy(() -> cartService.getCart(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenCartIdNotInRepositoryForGetCart() {
        when(cartRepository.findById(eq(CART_ID))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCart(CART_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cart not found");
    }

    @Test
    void shouldGetCart() {
        Cart cart = new Cart();
        cart.setTotalAmount(BigDecimal.ONE);
        when(cartRepository.findById(eq(CART_ID))).thenReturn(Optional.of(cart));
        when(cartRepository.save(eq(cart))).thenReturn(cart);

        assertThat(cartService.getCart(CART_ID))
                .isEqualTo(cart);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullInGetCartDTO() {
        assertThatThrownBy(() -> cartService.getCartDTO(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenCartIdNotInRepositoryForGetCartDTO() {
        when(cartRepository.findById(eq(CART_ID))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCartDTO(CART_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cart not found");
    }

    @Test
    void shouldGetCartDTO() {
        Cart cart = new Cart();
        cart.setTotalAmount(BigDecimal.ONE);
        Set<CartItem> cartItemSet = new HashSet<>();
        cartItemSet.add(new CartItem());
        cart.setItems(cartItemSet);
        when(cartRepository.findById(eq(CART_ID))).thenReturn(Optional.of(cart));
        when(cartRepository.save(eq(cart))).thenReturn(cart);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(5L);
        when(modelMapper.map(eq(cart), eq(CartDTO.class))).thenReturn(cartDTO);

        assertThat(cartService.getCartDTO(CART_ID))
                .isEqualTo(cartDTO);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullInGetTotalPrice() {
        assertThatThrownBy(() -> cartService.getTotalPrice(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenCartIdNotInRepositoryForGetTotalPrice() {
        when(cartRepository.findById(eq(CART_ID))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getTotalPrice(CART_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cart not found");
    }

    @Test
    void shouldGetTotalPrice() {
        Cart cart = new Cart();
        cart.setTotalAmount(BigDecimal.TEN);
        when(cartRepository.findById(eq(CART_ID))).thenReturn(Optional.of(cart));
        when(cartRepository.save(eq(cart))).thenReturn(cart);

        assertThat(cartService.getTotalPrice(CART_ID))
                .isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullInClearCart() {
        assertThatThrownBy(() -> cartService.clearCart(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenCartIdNotInRepositoryForClearCart() {
        when(cartRepository.findById(eq(CART_ID))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getTotalPrice(CART_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cart not found");
    }

    @Test
    void shouldClearCart() {
        when(cartRepository.findById(eq(CART_ID))).thenReturn(Optional.of(new Cart()));

        cartService.clearCart(CART_ID);

        verify(cartItemRepository, times(1)).deleteAllByCartId(eq(CART_ID));
        verify(cartRepository, times(1)).deleteById(eq(CART_ID));
    }
}