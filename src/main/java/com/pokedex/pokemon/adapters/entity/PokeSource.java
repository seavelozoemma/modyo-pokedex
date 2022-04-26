package com.pokedex.pokemon.adapters.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pokedex.pokemon.adapters.util.StringUtil;
import com.pokedex.pokemon.application.domain.PokeDomainSource;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeSource {
    private String name;
    private String url;

    public static List<PokeDomainSource> fromListEntity(List<PokeSource> list) {
        return Optional.of(
                list.stream()
                        .map(PokeSource::fromEntity)
                        .collect(Collectors.toList())
        ).orElse(new ArrayList<>());
    }

    public static PokeDomainSource fromEntity(PokeSource entity) {
        return PokeDomainSource
                .builder()
                    .id(StringUtil.getId(entity.url))
                    .name(entity.name)
                .build();
    }

}
