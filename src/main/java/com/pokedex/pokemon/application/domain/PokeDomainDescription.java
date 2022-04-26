package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PokeDomainDescription {
    private String description;
    private PokeDomainSource language;
}
