package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokeDomainDetail {
    private Integer id;
    private String name;
}
