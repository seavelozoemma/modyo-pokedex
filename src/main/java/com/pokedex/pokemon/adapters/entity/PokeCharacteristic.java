package com.pokedex.pokemon.adapters.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokedex.pokemon.application.domain.PokeDomainCharacteristic;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PokeCharacteristic {
    private Integer id;
    private List<PokeDescription> descriptions;

    @JsonCreator
    public PokeCharacteristic(
            @JsonProperty("id") Integer id,
            @JsonProperty("descriptions") List<PokeDescription> descriptions
    ) {
        this.id = id;
        this.descriptions = descriptions;
    }

    public static PokeDomainCharacteristic fromEntity(PokeCharacteristic entity) {
        return PokeDomainCharacteristic
                .builder()
                    .descriptions(PokeDescription.fromListEntity(entity.descriptions))
                    .id(entity.id)
                .build();
    }

}
