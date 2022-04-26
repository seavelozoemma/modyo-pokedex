package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PokeDomainEvolutionChain {
    private Boolean isBaby;
    private PokeDomainSource species;
    private List<PokeDomainEvolutionChain> evolvesTo;
}
