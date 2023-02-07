package com.br.productapi.model.jwt.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Integer id;
    private String name;
    private String email;

    //Claims dados do user usuários que estão armazenados no Token de acesso
    public static JwtResponse getUser(Claims jwtClaims){
        try{
            return new ObjectMapper().convertValue(jwtClaims.get("authUser"), JwtResponse.class);

        } catch (Exception ex){
            ex.printStackTrace(); //printa o erro
            return null;
        }

    }
}
