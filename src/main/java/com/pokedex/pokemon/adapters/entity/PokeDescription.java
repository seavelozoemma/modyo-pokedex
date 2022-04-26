package com.pokedex.pokemon.adapters.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokedex.pokemon.application.domain.PokeDomainDescription;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeDescription {
    private String description;
    private PokeSource language;

    @JsonCreator
    public PokeDescription(
            @JsonProperty("description") String description,
            @JsonProperty("language") PokeSource language
    ) {
        this.description = description;
        this.language = language;
    }

    public static List<PokeDomainDescription> fromListEntity(List<PokeDescription> list) {
        return Optional.of(
                list.stream().map(PokeDescription::fromEntity).collect(Collectors.toList())
        ).orElse(new ArrayList<>());
    }

    private static PokeDomainDescription fromEntity(PokeDescription entity) {
        return PokeDomainDescription
                .builder()
                .description(entity.getDescription())
                .language(PokeSource.fromEntity(entity.language))
                .build();
    }

}
