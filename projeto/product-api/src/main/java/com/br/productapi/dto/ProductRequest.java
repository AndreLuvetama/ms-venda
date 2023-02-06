package com.br.productapi.dto;

import com.br.productapi.model.Supplier;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    @JsonProperty("quantity_Available")
    private Integer quantityAvailable;
    private Integer supplierId;
    private Integer categoryId;



}
