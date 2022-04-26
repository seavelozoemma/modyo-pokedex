package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PokeDomain {
    private Integer count;
    private Integer next;
    private Integer previus;
    private List<PokeDomainBasic> list;
}
