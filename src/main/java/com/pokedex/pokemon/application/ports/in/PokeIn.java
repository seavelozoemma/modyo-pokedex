package com.pokedex.pokemon.application.ports.in;

import com.pokedex.pokemon.application.domain.PokeDomain;
import com.pokedex.pokemon.application.domain.PokeDomainBasic;

import java.util.List;

public interface PokeIn {

    PokeDomainBasic findById(int id);

    PokeDomain findByPaginate(int page);
}
