package com.br.productapi.dto;

import com.br.productapi.model.Product;
import com.br.productapi.model.Supplier;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SupplierResponse {
    private Integer id;
    private String name;

    //Convertemos a classe category para a classe response
    public static SupplierResponse of(Supplier supplier){
        var response = new SupplierResponse();
        BeanUtils.copyProperties(supplier, response); //objeto origem/destino
        return response;
    }
}
