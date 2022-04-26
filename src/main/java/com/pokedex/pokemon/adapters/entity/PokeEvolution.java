package com.pokedex.pokemon.adapters.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pokedex.pokemon.application.domain.PokeDomainEvolution;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeEvolution {
    private Integer id;
    private PokeEvolutionChain chain;

    public static PokeDomainEvolution fromEntity(PokeEvolution entity) {
        return PokeDomainEvolution
                .builder()
                .id(entity.id)
                .chain(PokeEvolutionChain.fromEntity(entity.chain))
                .build();
    }
}
