package com.pokedex.pokemon.adapters.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pokedex.pokemon.application.domain.PokeDomainPaginator;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokePaginator {
    private Integer count;
    private String next;
    private String previus;
    private List<PokeSource> results;

    @JsonCreator
    public PokePaginator(
            @JsonProperty("count") Integer count,
            @JsonProperty("next") String next,
            @JsonProperty("previus") String previus,
            @JsonProperty("results") List<PokeSource> results) {
        this.count = count;
        this.next = next;
        this.previus = previus;
        this.results = results;
    }

    public static PokeDomainPaginator fromEntity(PokePaginator entity) {
        return PokeDomainPaginator
                .builder()
                    .count(entity.count)
                    .next(entity.next)
                    .previus(entity.previus)
                    .list(PokeSource.fromListEntity(entity.results))
                .build();
    }
}
