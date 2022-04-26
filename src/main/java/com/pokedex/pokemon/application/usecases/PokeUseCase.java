package com.pokedex.pokemon.application.usecases;

import com.pokedex.pokemon.application.domain.*;
import com.pokedex.pokemon.application.ports.in.PokeIn;
import com.pokedex.pokemon.application.ports.out.PokeCharacteristicRepository;
import com.pokedex.pokemon.application.ports.out.PokeDetailRepository;
import com.pokedex.pokemon.application.ports.out.PokeEvolutionRepository;
import com.pokedex.pokemon.application.ports.out.PokeListRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PokeUseCase implements PokeIn {

    private final PokeDetailRepository pokeDetailRepository;
    private final PokeListRepository pokeListRepository;
    private final PokeEvolutionRepository pokeEvolutionRepository;
    private final PokeCharacteristicRepository pokeCharacteristicRepository;

    public PokeUseCase(PokeDetailRepository pokeDetailRepository, PokeListRepository pokeListRepository, PokeEvolutionRepository pokeEvolutionRepository, PokeCharacteristicRepository pokeCharacteristicRepository) {
        this.pokeDetailRepository = pokeDetailRepository;
        this.pokeListRepository = pokeListRepository;
        this.pokeEvolutionRepository = pokeEvolutionRepository;
        this.pokeCharacteristicRepository = pokeCharacteristicRepository;
    }


    @Override
    public PokeDomainBasic findById(int id) {
        PokeDomainBasic poke = this.pokeDetailRepository.findById(id);
        PokeDomainEvolution evolutions = this.pokeEvolutionRepository.findEvolutionById(id);
        PokeDomainCharacteristic characteristic = this.pokeCharacteristicRepository.findById(id);
        return poke.withCharacteristic(characteristic).withEvolutions(evolutions);
    }

    @Override
    public PokeDomain findByPaginate(int page) {
        int rows = 5;
        int limit = page * rows;
        int offset = limit - rows;
        List<PokeDomainBasic> pokemons = new ArrayList<>();
        PokeDomainPaginator paginator = this.pokeListRepository.findByOffset(limit, offset);
        List<PokeDomainSource> list = paginator.getList();
        list.forEach(e-> {
            PokeDomainBasic poke = this.pokeDetailRepository.findById(e.getId());
            pokemons.add(poke);
        });
        return PokeDomain
                .builder()
                .count(paginator.getCount())
                .next(page+1)
                .previus((page<=1)?0:page-1)
                .list(pokemons)
                .build();
    }
}
