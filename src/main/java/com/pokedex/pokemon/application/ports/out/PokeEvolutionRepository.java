package com.pokedex.pokemon.application.ports.out;

import com.pokedex.pokemon.application.domain.PokeDomainEvolution;

public interface PokeEvolutionRepository {
    PokeDomainEvolution findEvolutionById(Integer id);
}
