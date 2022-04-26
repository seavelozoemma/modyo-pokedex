package com.pokedex.pokemon.application.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PokeDomainPaginator {
    private Integer count;
    private String next;
    private String previus;
    private List<PokeDomainSource> list;

    @JsonCreator
    public PokeDomainPaginator(
            @JsonProperty("count") Integer count,
            @JsonProperty("next") String next,
            @JsonProperty("previus") String previus,
            @JsonProperty("results") List<PokeDomainSource> list) {
        this.count = count;
        this.next = next;
        this.previus = previus;
        this.list = list;
    }
}
