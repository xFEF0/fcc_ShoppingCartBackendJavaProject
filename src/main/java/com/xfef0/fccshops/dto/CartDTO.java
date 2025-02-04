package com.xfef0.fccshops.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDTO {
    private Long id;
    private BigDecimal totalAmount;
    private List<CartItemDTO> cartItems;
}
