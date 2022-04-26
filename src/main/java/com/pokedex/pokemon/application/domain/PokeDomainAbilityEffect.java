package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PokeDomainAbilityEffect {
    private String effect;
    private String shortEffect;
}
