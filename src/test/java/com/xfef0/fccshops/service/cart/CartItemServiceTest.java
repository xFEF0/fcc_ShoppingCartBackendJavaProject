package com.xfef0.fccshops.service.cart;

import com.xfef0.fccshops.dto.CartItemDTO;
import com.xfef0.fccshops.exception.QuantityNotValidException;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Cart;
import com.xfef0.fccshops.model.CartItem;
import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.repository.CartRepository;
import com.xfef0.fccshops.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    public static final long CART_ID = 1L;
    public static final long PRODUCT_ID = 2L;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartService cartService;
    @Mock
    private ProductService productService;
    @Mock
    private ModelMapper modelMapper;
    @Captor
    private ArgumentCaptor<CartItem> cartItemCaptor;
    @Captor
    private ArgumentCaptor<Cart> cartCaptor;

    private CartItemService cartItemService;

    @BeforeEach
    public void setup() {
        cartItemService = new CartItemService(cartRepository, cartService, productService, modelMapper);
    }

    @Test
    void shouldThrowExceptionWhenCartIdIsNullInAddCartItem() {
        assertThatThrownBy(() -> cartItemService.addCartItem(null, 21L, 0))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNullInAddCartItem() {
        assertThatThrownBy(() -> cartItemService.addCartItem(12L, null, 0))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegativeInAddCartItem() {
        assertThatThrownBy(() -> cartItemService.addCartItem(12L, 21L, -1))
                .isInstanceOf(QuantityNotValidException.class);
    }

    @Test
    void shouldAddNewItemToCart() {
        int quantity = 2;
        Cart cart = getCart();
        when(cartService.getCart(anyLong())).thenReturn(cart);
        Product product = getProduct();
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(cartRepository.save(isA(Cart.class))).thenReturn(cart);
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setQuantity(quantity);
        when(modelMapper.map(isA(CartItem.class), eq(CartItemDTO.class))).thenReturn(cartItemDTO);

        CartItemDTO returnedCartItem = cartItemService.addCartItem(CART_ID, PRODUCT_ID + 1, quantity);

        assertThat(returnedCartItem).isEqualTo(cartItemDTO);
        assertThat(returnedCartItem.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void shouldAddQuantityToCart() {
        int quantity = 2;
        Cart cart = getCart();
        cart.getItems().forEach(item -> {
            item.setQuantity(1);
            item.setId(1L);
        });
        when(cartService.getCart(anyLong())).thenReturn(cart);
        when(cartRepository.save(isA(Cart.class))).thenReturn(cart);
        CartItemDTO cartItemDTO = new CartItemDTO();
        when(modelMapper.map(isA(CartItem.class), eq(CartItemDTO.class))).thenReturn(cartItemDTO);

        CartItemDTO returnedCartItem = cartItemService.addCartItem(CART_ID, PRODUCT_ID, quantity);

        verify(modelMapper, times(1)).map(cartItemCaptor.capture(), eq(CartItemDTO.class));
        CartItem cartItem = cartItemCaptor.getValue();
        assertThat(cartItem.getQuantity()).isEqualTo(quantity + 1);
        assertThat(returnedCartItem).isEqualTo(cartItemDTO);
    }

    @Test
    void shouldThrowExceptionWhenCartIdIsNullInRemoveCartItem() {
        assertThatThrownBy(() -> cartItemService.removeCartItem(null, 21L))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNullInRemoveCartItem() {
        assertThatThrownBy(() -> cartItemService.removeCartItem(12L, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenItemToBeRemovedIsNotFound() {
        Cart cart = mock(Cart.class);
        when(cartService.getCart(eq(CART_ID))).thenReturn(cart);
        when(cart.getItems()).thenReturn(getCartItems());

        assertThatThrownBy(() -> cartItemService.removeCartItem(CART_ID, PRODUCT_ID+1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not in cart!");
    }

    @Test
    void shouldRemoveCartItem() {
        Cart cart = mock(Cart.class);
        when(cartService.getCart(eq(CART_ID))).thenReturn(cart);
        when(cart.getItems()).thenReturn(getCartItems());

        cartItemService.removeCartItem(CART_ID, PRODUCT_ID);

        verify(cart, times(1)).removeItem(isA(CartItem.class));
        verify(cartRepository, times(1)).save(eq(cart));
    }

    @Test
    void shouldThrowExceptionWhenCartIdIsNullInUpdateItemQuantity() {
        assertThatThrownBy(() -> cartItemService.updateItemQuantity(null, 21L, 1))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNullInUpdateItemQuantity() {
        assertThatThrownBy(() -> cartItemService.updateItemQuantity(12L, null, 2))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegativeInUpdateItemQuantity() {
        assertThatThrownBy(() -> cartItemService.updateItemQuantity(12L, 21L, -1))
                .isInstanceOf(QuantityNotValidException.class);
    }

    @Test
    void shouldThrowExceptionWhenItemToBeUpdatedIsNotFound() {
        Cart cart = mock(Cart.class);
        when(cartService.getCart(eq(CART_ID))).thenReturn(cart);
        when(cart.getItems()).thenReturn(getCartItems());

        assertThatThrownBy(() -> cartItemService.updateItemQuantity(CART_ID, PRODUCT_ID+1, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not in cart!");
    }

    @Test
    void shouldUpdateItemQuantity() {
        Cart cart = new Cart();
        Set<CartItem> cartItems = getCartItems();
        cartItems.forEach(item -> item.setQuantity(100));
        cart.setItems(cartItems);
        when(cartService.getCart(eq(CART_ID))).thenReturn(cart);

        int quantity = 2;
        cartItemService.updateItemQuantity(CART_ID, PRODUCT_ID, quantity);

        verify(cartRepository, times(1)).save(cartCaptor.capture());
        Cart cartSaved = cartCaptor.getValue();
        Optional<CartItem> cartItem = cartSaved.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(PRODUCT_ID))
                .findFirst();
        assertThat(cartItem.isPresent()).isTrue();
        assertThat(cartItem.get().getQuantity()).isEqualTo(quantity);

    }

    private static Cart getCart() {
        Cart cart = new Cart();
        cart.setItems(getCartItems());
        return cart;
    }

    private static Set<CartItem> getCartItems() {
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(getCartItem());
        return cartItems;
    }

    private static CartItem getCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(getProduct());
        cartItem.setUnitPrice(BigDecimal.ONE);
        cartItem.setTotalPrice(BigDecimal.TEN);
        return cartItem;
    }

    private static Product getProduct() {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setPrice(BigDecimal.ONE);
        return product;
    }
}