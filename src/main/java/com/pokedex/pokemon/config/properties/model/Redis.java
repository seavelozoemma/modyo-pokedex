package com.pokedex.pokemon.config.properties.model;

import lombok.Data;

@Data
public class Redis {
    private String host;
    private String port;
    private String secret;
}
