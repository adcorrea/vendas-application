package com.adcorreajr.vendas.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CredentialsDTO {

    private String login;
    private String senha;
}
