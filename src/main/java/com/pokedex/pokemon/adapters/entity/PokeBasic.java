package com.pokedex.pokemon.adapters.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pokedex.pokemon.application.domain.PokeDomainBasic;
import com.pokedex.pokemon.application.domain.PokeDomainSource;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeBasic {
    private Integer id;
    private String name;
    private Integer weight;
    private Integer height;
    private Integer order;
    private List<PokeTypes> types;
    private List<PokeListAbilities> abilities;
    private List<PokeMoves> moves;

    public static PokeDomainBasic fromEntity(PokeBasic entity) {

        List<PokeDomainSource> types = new ArrayList<>();
        List<PokeDomainSource> moves = new ArrayList<>();
        List<PokeDomainSource> abilities = new ArrayList<>();

        if(entity.abilities!=null && !entity.abilities.isEmpty()) {
            entity.abilities.forEach(e->{
                moves.add(PokeSource.fromEntity(e.getAbility()));
            });
        }

        if(entity.moves!=null && !entity.moves.isEmpty()) {
            entity.moves.forEach(e->{
                abilities.add(PokeSource.fromEntity(e.getMove()));
            });
        }

        if(entity.types!=null && !entity.types.isEmpty()) {
            entity.types.forEach(e->{
                types.add(PokeSource.fromEntity(e.getType()));
            });
        }

        return PokeDomainBasic
                .builder()
                .id(entity.id)
                .height(entity.height)
                .name(entity.name)
                .weight(entity.weight)
                .order(entity.order)
                .types(types)
                .abilities(abilities)
                .moves(moves)
                .build();

    }

}
