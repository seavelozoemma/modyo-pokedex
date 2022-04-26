package com.pokedex.pokemon.application.ports.out;

import com.pokedex.pokemon.application.domain.PokeDomainPaginator;

public interface PokeListRepository {
    PokeDomainPaginator findByOffset(int limit, int offset);
}
