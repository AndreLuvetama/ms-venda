package com.br.productapi.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesProductResponse {
    List<String> salesIds; // Vai receber um conjunto de Ids de venda
}
