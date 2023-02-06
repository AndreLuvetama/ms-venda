package com.br.productapi.dto;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.br.productapi.model.Category;
import com.br.productapi.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryResponse {
    private Integer id;
    private String description;
    //private SupplierResponse supplier;
    //private CategoryResponse category;

    //Convertemos a classe category para a classe response
    public static CategoryResponse of(Category category){
        var response = new CategoryResponse();
        BeanUtils.copyProperties(category, response);
        return response;
    }
}
