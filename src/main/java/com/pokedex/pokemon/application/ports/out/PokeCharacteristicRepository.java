package com.pokedex.pokemon.application.ports.out;

import com.pokedex.pokemon.application.domain.PokeDomainCharacteristic;

public interface PokeCharacteristicRepository {
    PokeDomainCharacteristic findById(int id);
}
