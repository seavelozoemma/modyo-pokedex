package com.pokedex.pokemon.adapters.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokemon.adapters.entity.*;
import com.pokedex.pokemon.adapters.exception.NotFoundRestClientException;
import com.pokedex.pokemon.adapters.exception.ServiceUnavailableException;
import com.pokedex.pokemon.adapters.exception.TimeoutRestClientException;
import com.pokedex.pokemon.adapters.integration.PokeDetailRestAdapter;
import com.pokedex.pokemon.adapters.util.RestUtilsProps;
import com.pokedex.pokemon.application.domain.PokeDomainBasic;
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

@DisplayName("PokeDetailTestRestAdapter")
@RestClientTest({PokeDetailRestAdapter.class})
@Import({TestConfig.class, RestUtilsProps.class})
@ContextConfiguration(initializers = TestConfig.class)
public class PokeDetailTestRestAdapter {

    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private GlobalProperties globalProperties;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PokeDetailRestAdapter pokeDetailRestAdapter;

    @Test
    @DisplayName("Cuando intengo cargar detalle de pokemon y este funciona")
    public void getDetailOk() throws JsonProcessingException {
        String JSON = objectMapper.writeValueAsString(getMockEntity());
        this.server.expect(requestTo(buildURL())).andRespond(withSuccess(JSON, MediaType.APPLICATION_JSON));
        PokeDomainBasic evolution = this.pokeDetailRestAdapter.findById(1);
        assertThat(evolution).isEqualTo(getMockDomain());
    }

    @Test
    @DisplayName("Cuando intengo cargar detalle de pokemon y obtengo un 404")
    public void getDetail404() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.NOT_FOUND));
        Throwable thrown = catchThrowable(()->this.pokeDetailRestAdapter.findById(1));
        assertThat(thrown)
                .isInstanceOf(NotFoundRestClientException.class)
                .hasMessage(ErrorCode.POKE_DETAIL_NOT_FOUND.getReasonPhrase());
    }

    @Test
    @DisplayName("Cuando intengo cargar detalle de pokemon y obtengo un 500")
    public void getDetail500() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        Throwable thrown = catchThrowable(()->this.pokeDetailRestAdapter.findById(1));
        assertThat(thrown)
                .isInstanceOf(ServiceUnavailableException.class)
                .hasMessage(ErrorCode.POKE_DETAIL_SERVER_ERROR.getReasonPhrase());
    }

    @Test
    @DisplayName("Cuando intengo cargar detalle de pokemon y obtengo un 408")
    public void getDetail408() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.REQUEST_TIMEOUT));
        Throwable thrown = catchThrowable(()->this.pokeDetailRestAdapter.findById(1));
        assertThat(thrown)
                .isInstanceOf(TimeoutRestClientException.class)
                .hasMessage(ErrorCode.POKE_DETAIL_TIMEOUT.getReasonPhrase());
    }
    
    private PokeDomainBasic getMockDomain() {
        List<PokeDomainSource> types = new ArrayList(){{
            add(
                    PokeDomainSource
                            .builder()
                            .name("Specie")
                            .id(1)
                            .build()
            );
        }};

        List<PokeDomainSource> abilities = new ArrayList() {{
            add(
                    PokeDomainSource
                            .builder()
                            .name("Fat Shoot")
                            .id(1)
                            .build()
            );
        }};

        List<PokeDomainSource> moves = new ArrayList(){{
            add(
                    PokeDomainSource
                            .builder()
                            .name("Fat Shoot")
                            .id(1)
                            .build()

            );
        }};
        return PokeDomainBasic
                .builder()
                .id(1)
                .height(1)
                .name("Pikachu")
                .weight(1)
                .order(1)
                .types(types)
                .abilities(abilities)
                .moves(moves)
                .build();
    }
    
    private PokeBasic getMockEntity() {
        
        List<PokeTypes> types = new ArrayList(){{
           add(
                   PokeTypes
                           .builder()
                           .type(
                                   PokeSource
                                           .builder()
                                           .url("https://www.pokemon/1/")
                                           .name("Specie")
                                           .build()
                           )
                           .build()
           ); 
        }};
        
        List<PokeListAbilities> abilities = new ArrayList() {{
           add(
                   PokeListAbilities
                           .builder()
                           .ability(
                                   PokeSource
                                           .builder()
                                           .name("Fat Shoot")
                                           .url("http://www.pokemon/1/")
                                           .build()
                           )
                           .build()
           );
        }};
        
        List<PokeMoves> moves = new ArrayList(){{
           add(
                   PokeMoves
                           .builder()
                           .move(
                                   PokeSource
                                           .builder()
                                           .name("Fat Shoot")
                                           .url("http://www.pokemon/1/")
                                           .build()
                           )
                           .build()
                           
           );
        }};
        
        return PokeBasic
                .builder()
                .id(1)
                .height(1)
                .name("Pikachu")
                .weight(1)
                .order(1)
                .types(types)
                .abilities(abilities)
                .moves(moves)
                .build();
    }

    private URI buildURL() {
        String url = apiProperties.getPoke().getDetail();
        Map<String,String> mapper = new HashMap<>();
        mapper.put("id","1");
        return new DefaultUriBuilderFactory(url)
                .builder()
                .build(mapper);
    }

}
