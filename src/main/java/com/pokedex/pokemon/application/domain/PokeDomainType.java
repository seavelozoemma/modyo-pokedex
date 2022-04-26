package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PokeDomainType {
    private Integer id;
    private String name;
}
