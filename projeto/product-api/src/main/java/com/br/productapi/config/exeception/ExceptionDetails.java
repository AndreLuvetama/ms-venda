package com.br.productapi.config.exeception;

import lombok.Data;

@Data
public class ExceptionDetails {
    private int status;
    private String message;
}
