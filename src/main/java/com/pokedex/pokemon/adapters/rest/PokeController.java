package com.pokedex.pokemon.adapters.rest;

import com.pokedex.pokemon.application.domain.PokeDomain;
import com.pokedex.pokemon.application.domain.PokeDomainBasic;
import com.pokedex.pokemon.application.usecases.PokeUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokeController {

    private final PokeUseCase pokeListUseCase;

    public PokeController(PokeUseCase pokeListUseCase) {
        this.pokeListUseCase = pokeListUseCase;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> findById(@PathVariable("id") int id) {
        PokeDomainBasic response = this.pokeListUseCase.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> findPaginate(
            @RequestParam(value = "page", defaultValue = "5") int page) {
        PokeDomain response = this.pokeListUseCase.findByPaginate(page);
        return ResponseEntity.ok(response);
    }
}
