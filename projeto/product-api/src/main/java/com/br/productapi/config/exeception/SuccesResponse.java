package com.br.productapi.config.exeception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccesResponse {
    private Integer status;
    private String message;


    public  static  SuccesResponse create(String message){
        return SuccesResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }
}
