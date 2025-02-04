package com.xfef0.fccshops.dto;

import com.xfef0.fccshops.model.Cart;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private ProductDTO product;
}
