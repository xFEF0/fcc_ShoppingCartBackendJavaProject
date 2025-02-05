package com.xfef0.fccshops.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xfef0.fccshops.model.Cart;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDTO {
    private Long cartId;
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private ProductDTO product;
}
