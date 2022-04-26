package com.pokedex.pokemon.application.domain;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder
public class PokeDomainBasic {
    private Integer id;
    private String name;
    private Integer weight;
    private Integer height;
    private Integer order;
    private List<PokeDomainSource> types;
    private List<PokeDomainSource> abilities;
    private List<PokeDomainSource> moves;
    @With
    private PokeDomainCharacteristic characteristic;
    @With
    private PokeDomainEvolution evolutions;
}
