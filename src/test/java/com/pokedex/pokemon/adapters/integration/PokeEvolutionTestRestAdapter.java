package com.pokedex.pokemon.adapters.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokemon.adapters.entity.PokeEvolution;
import com.pokedex.pokemon.adapters.entity.PokeEvolutionChain;
import com.pokedex.pokemon.adapters.entity.PokeSource;
import com.pokedex.pokemon.adapters.exception.NotFoundRestClientException;
import com.pokedex.pokemon.adapters.exception.ServiceUnavailableException;
import com.pokedex.pokemon.adapters.exception.TimeoutRestClientException;
import com.pokedex.pokemon.adapters.integration.PokeEvolutionRestAdapter;
import com.pokedex.pokemon.adapters.util.RestUtilsProps;
import com.pokedex.pokemon.application.domain.PokeDomainEvolution;
import com.pokedex.pokemon.application.domain.PokeDomainEvolutionChain;
import com.pokedex.pokemon.application.domain.PokeDomainSource;
import com.pokedex.pokemon.config.ErrorCode;
import com.pokedex.pokemon.config.TestConfig;
import com.pokedex.pokemon.config.properties.ApiProperties;
import com.pokedex.pokemon.config.properties.GlobalProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("PokeListRestAdapter")
@RestClientTest({PokeEvolutionRestAdapter.class})
@Import({TestConfig.class, RestUtilsProps.class})
@ContextConfiguration(initializers = TestConfig.class)
public class PokeEvolutionTestRestAdapter {

    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private GlobalProperties globalProperties;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PokeEvolutionRestAdapter pokeEvolutionRestAdapter;

    @Test
    @DisplayName("Cuando intengo cargar evoluciones de pokemon y este funciona")
    public void getEvolutionOk() throws JsonProcessingException {
        String JSON = objectMapper.writeValueAsString(getMockEntity());
        this.server.expect(requestTo(buildURL())).andRespond(withSuccess(JSON, MediaType.APPLICATION_JSON));
        PokeDomainEvolution evolution = this.pokeEvolutionRestAdapter.findEvolutionById(1);
        assertThat(evolution).isEqualTo(getMockDomain());
    }

    @Test
    @DisplayName("Cuando intengo cargar evoluciones de pokemon y obtengo un 404")
    public void getEvolution404() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.NOT_FOUND));
        Throwable thrown = catchThrowable(()->this.pokeEvolutionRestAdapter.findEvolutionById(1));
        assertThat(thrown)
                .isInstanceOf(NotFoundRestClientException.class)
                .hasMessage(ErrorCode.POKE_EVOLUTION_NOT_FOUND.getReasonPhrase());
    }

    @Test
    @DisplayName("Cuando intengo cargar evoluciones de pokemon y obtengo un 500")
    public void getEvolution500() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        Throwable thrown = catchThrowable(()->this.pokeEvolutionRestAdapter.findEvolutionById(1));
        assertThat(thrown)
                .isInstanceOf(ServiceUnavailableException.class)
                .hasMessage(ErrorCode.POKE_EVOLUTION_SERVER_ERROR.getReasonPhrase());
    }

    @Test
    @DisplayName("Cuando intengo cargar evoluciones de pokemon y obtengo un 408")
    public void getEvolution408() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.REQUEST_TIMEOUT));
        Throwable thrown = catchThrowable(()->this.pokeEvolutionRestAdapter.findEvolutionById(1));
        assertThat(thrown)
                .isInstanceOf(TimeoutRestClientException.class)
                .hasMessage(ErrorCode.POKE_EVOLUTION_TIMEOUT.getReasonPhrase());
    }

    private PokeEvolution getMockEntity() {

        PokeSource specie = PokeSource
                .builder()
                .name("pikachu")
                .url("http://www.pikachu.cl/1/")
                .build();

        List<PokeEvolutionChain> evolves = new ArrayList(){{
            add(
                    PokeEvolutionChain
                            .builder()
                            .isBaby(Boolean.FALSE)
                            .species(specie)
                            .evolvesTo(new ArrayList<>())
                            .build()
            );
        }};

        List<PokeEvolutionChain> evolvesTo = new ArrayList(){{
            add(
                    PokeEvolutionChain
                            .builder()
                            .isBaby(Boolean.FALSE)
                            .species(specie)
                            .evolvesTo(evolves)
                            .build()
            );
        }};

        PokeEvolutionChain chain = PokeEvolutionChain
                .builder()
                .isBaby(Boolean.TRUE)
                .species(specie)
                .evolvesTo(evolvesTo)
                .build();

        return PokeEvolution
                .builder()
                .chain(chain)
                .id(1)
                .build();

    }

    private PokeDomainEvolution getMockDomain() {

        PokeDomainSource specie = PokeDomainSource
                .builder()
                    .name("pikachu")
                    .id(1)
                .build();

        List<PokeDomainEvolutionChain> evolves = new ArrayList(){{
            add(
                    PokeDomainEvolutionChain
                            .builder()
                            .isBaby(Boolean.FALSE)
                            .species(specie)
                            .evolvesTo(new ArrayList<>())
                            .build()
            );
        }};

        List<PokeDomainEvolutionChain> evolvesTo = new ArrayList(){{
            add(
                    PokeDomainEvolutionChain
                            .builder()
                            .isBaby(Boolean.FALSE)
                            .species(specie)
                            .evolvesTo(evolves)
                            .build()
            );
        }};

       PokeDomainEvolutionChain chain = PokeDomainEvolutionChain
                   .builder()
                           .isBaby(Boolean.TRUE)
                           .species(specie)
                           .evolvesTo(evolvesTo)
                   .build();

        return PokeDomainEvolution
                .builder()
                .id(1)
                .chain(chain)
                .build();
    }

    private URI buildURL() {
        String url = apiProperties.getPoke().getEvolution();
        Map<String,String> mapper = new HashMap<>();
        mapper.put("id","1");
        return new DefaultUriBuilderFactory(url)
                .builder()
                .build(mapper);
    }

}
