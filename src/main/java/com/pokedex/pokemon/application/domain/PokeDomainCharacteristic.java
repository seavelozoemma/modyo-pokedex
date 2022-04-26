package com.pokedex.pokemon.application.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PokeDomainCharacteristic {

    private Integer id;
    private List<PokeDomainDescription> descriptions;

}
