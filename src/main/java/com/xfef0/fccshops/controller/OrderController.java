package com.xfef0.fccshops.controller;


import com.xfef0.fccshops.dto.OrderDTO;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.response.ApiResponse;
import com.xfef0.fccshops.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            OrderDTO order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Success.", order));
        } catch (ResourceNotFoundException e) {
            return getExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Success.", order));
        } catch (ResourceNotFoundException e) {
            return getExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDTO> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Success.", orders));
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
