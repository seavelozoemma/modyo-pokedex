package com.pokedex.pokemon.adapters.integration;

import com.pokedex.pokemon.adapters.entity.PokeEvolution;
import com.pokedex.pokemon.adapters.exception.NotFoundRestClientException;
import com.pokedex.pokemon.adapters.exception.ServiceUnavailableException;
import com.pokedex.pokemon.adapters.exception.TimeoutRestClientException;
import com.pokedex.pokemon.adapters.handler.RestTemplateErrorHandler;
import com.pokedex.pokemon.adapters.util.RestUtilsProps;
import com.pokedex.pokemon.application.domain.PokeDomainEvolution;
import com.pokedex.pokemon.application.ports.out.PokeEvolutionRepository;
import com.pokedex.pokemon.config.ErrorCode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class PokeEvolutionRestAdapter implements PokeEvolutionRepository {

    private final RestTemplate restTemplate;
    private final RestUtilsProps restUtilsProps;

    public PokeEvolutionRestAdapter(RestTemplate restTemplate, RestUtilsProps restUtilsProps) {
        this.restTemplate = restTemplate;
        this.restUtilsProps = restUtilsProps;
        Map<HttpStatus, RuntimeException> map = new HashMap<>();
        map.put(HttpStatus.NOT_FOUND, new NotFoundRestClientException(ErrorCode.POKE_EVOLUTION_NOT_FOUND));
        map.put(HttpStatus.INTERNAL_SERVER_ERROR, new ServiceUnavailableException(ErrorCode.POKE_EVOLUTION_SERVER_ERROR));
        map.put(HttpStatus.REQUEST_TIMEOUT, new TimeoutRestClientException(ErrorCode.POKE_EVOLUTION_TIMEOUT));
        this.restTemplate.setErrorHandler(new RestTemplateErrorHandler(map));
    }

    @Override
    public PokeDomainEvolution findEvolutionById(Integer id) {
        PokeEvolution evolution = Optional.of(
                        restTemplate.exchange(
                                this.restUtilsProps.getApiProperties().getPoke().getEvolution(),
                                HttpMethod.GET,
                                this.restUtilsProps.getHeadersAcceptJson(),
                                new ParameterizedTypeReference<PokeEvolution>(){},
                                id))
                .map(HttpEntity::getBody)
                .orElseThrow(()->new NotFoundRestClientException(ErrorCode.POKE_EVOLUTION_NOT_FOUND));
        return PokeEvolution.fromEntity(evolution);
    }
}
