package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PokeDomainEvolution {
    private Integer id;
    private PokeDomainEvolutionChain chain;
}

