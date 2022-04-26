package com.pokedex.pokemon.application.ports.out;

import com.pokedex.pokemon.application.domain.PokeDomainBasic;

public interface PokeDetailRepository {
    PokeDomainBasic findById(Integer id);
}
