package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PokeDomainAbility {
    private Integer id;
    private String name;
    private List<PokeDomainAbilityEffect> effectEntries;
}
