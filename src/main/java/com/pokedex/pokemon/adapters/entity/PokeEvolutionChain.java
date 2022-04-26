package com.pokedex.pokemon.adapters.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pokedex.pokemon.application.domain.PokeDomainEvolutionChain;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeEvolutionChain {
    private Boolean isBaby;
    private PokeSource species;
    private List<PokeEvolutionChain> evolvesTo;

    public static PokeDomainEvolutionChain fromEntity(PokeEvolutionChain entity) {
        return PokeDomainEvolutionChain
                .builder()
                    .isBaby(entity.isBaby)
                    .species(PokeSource.fromEntity(entity.species))
                    .evolvesTo(PokeEvolutionChain.fromList(entity.evolvesTo))
                .build();
    }

    private static List<PokeDomainEvolutionChain> fromList(List<PokeEvolutionChain> list) {
        if(list!=null && !list.isEmpty()) {
            return list.stream().map(PokeEvolutionChain::fromEntity).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

}
