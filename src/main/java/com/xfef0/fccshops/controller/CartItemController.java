package com.xfef0.fccshops.controller;

import com.xfef0.fccshops.dto.CartItemDTO;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.CartItem;
import com.xfef0.fccshops.response.ApiResponse;
import com.xfef0.fccshops.service.cart.ICartItemService;
import com.xfef0.fccshops.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cart-items")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam int quantity) {
        try {
            if (cartId == null) {
                cartId = cartService.initializeCart();
            }
            CartItem cartItem = cartItemService.addCartItem(cartId, productId, quantity);
            CartItemDTO cartItemDTO = cartItemService.convertToDTO(cartItem);
            return ResponseEntity.ok(new ApiResponse("Item added.", cartItemDTO));
        } catch (ResourceNotFoundException e) {
            return getExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestParam Long cartId,
                                                          @RequestParam Long productId) {
        try {
            cartItemService.removeCartItem(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Items removed.", null));
        } catch (ResourceNotFoundException e) {
            return getExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestParam Long cartId,
                                                          @RequestParam Long productId,
                                                          @RequestParam int quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item quantity updated.", null));
        } catch (ResourceNotFoundException e) {
            return getExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static ResponseEntity<ApiResponse> getExceptionResponseEntity(Exception e, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ApiResponse(e.getMessage(), null));
    }
}
