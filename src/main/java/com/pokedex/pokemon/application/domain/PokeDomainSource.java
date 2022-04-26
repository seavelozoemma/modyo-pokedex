package com.pokedex.pokemon.application.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PokeDomainSource {
    private String name;
    private Integer id;
    @JsonCreator
    public PokeDomainSource(
            @JsonProperty("name") String name,
            @JsonProperty("id") Integer id
    ) {
        this.name = name;
        this.id = id;
    }
}
