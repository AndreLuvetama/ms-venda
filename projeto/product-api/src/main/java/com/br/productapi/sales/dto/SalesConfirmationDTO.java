package com.br.productapi.sales.dto;

import com.br.productapi.sales.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesConfirmationDTO {
    private String salesId;
    private SalesStatus status;
}
