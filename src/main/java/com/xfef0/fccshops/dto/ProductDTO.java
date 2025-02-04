package com.xfef0.fccshops.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xfef0.fccshops.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;
    private Category category;
    private List<ImageDTO> images;
}
