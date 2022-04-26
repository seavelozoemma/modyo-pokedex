package com.pokedex.pokemon.adapters.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokemon.adapters.entity.PokeCharacteristic;
import com.pokedex.pokemon.adapters.entity.PokeDescription;
import com.pokedex.pokemon.adapters.entity.PokeSource;
import com.pokedex.pokemon.adapters.exception.NotFoundRestClientException;
import com.pokedex.pokemon.adapters.exception.ServiceUnavailableException;
import com.pokedex.pokemon.adapters.exception.TimeoutRestClientException;
import com.pokedex.pokemon.adapters.integration.PokeCharacteristicRestAdapter;
import com.pokedex.pokemon.adapters.util.RestUtilsProps;
import com.pokedex.pokemon.application.domain.PokeDomainCharacteristic;
import com.pokedex.pokemon.application.domain.PokeDomainDescription;
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

@DisplayName("PokeCharacteristicTestRestAdapter")
@RestClientTest({PokeCharacteristicRestAdapter.class})
@Import({TestConfig.class, RestUtilsProps.class})
@ContextConfiguration(initializers = TestConfig.class)
public class PokeCharacteristicTestRestAdapter {

    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private GlobalProperties globalProperties;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PokeCharacteristicRestAdapter pokeCharacteristicRestAdapter;

    @Test
    @DisplayName("Cuando intengo cargar caracteristicas de pokemon y este funciona")
    public void getCharacteristicOk() throws JsonProcessingException {
        String JSON = objectMapper.writeValueAsString(getMockEntity());
        this.server.expect(requestTo(buildURL())).andRespond(withSuccess(JSON, MediaType.APPLICATION_JSON));
        PokeDomainCharacteristic evolution = this.pokeCharacteristicRestAdapter.findById(1);
        assertThat(evolution).isEqualTo(getMockDomain());
    }

    @Test
    @DisplayName("Cuando intengo cargar caracteristicas de pokemon y obtengo un 404")
    public void getCharacteristic404() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.NOT_FOUND));
        Throwable thrown = catchThrowable(()->this.pokeCharacteristicRestAdapter.findById(1));
        assertThat(thrown)
                .isInstanceOf(NotFoundRestClientException.class)
                .hasMessage(ErrorCode.POKE_CHARACTERISTIC_NOT_FOUND.getReasonPhrase());
    }

    @Test
    @DisplayName("Cuando intengo cargar caracteristicas de pokemon y obtengo un 500")
    public void getCharacteristic500() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        Throwable thrown = catchThrowable(()->this.pokeCharacteristicRestAdapter.findById(1));
        assertThat(thrown)
                .isInstanceOf(ServiceUnavailableException.class)
                .hasMessage(ErrorCode.POKE_CHARACTERISTIC_SERVER_ERROR.getReasonPhrase());
    }

    @Test
    @DisplayName("Cuando intengo cargar caracteristicas de pokemon y obtengo un 408")
    public void getCharacteristic408() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.REQUEST_TIMEOUT));
        Throwable thrown = catchThrowable(()->this.pokeCharacteristicRestAdapter.findById(1));
        assertThat(thrown)
                .isInstanceOf(TimeoutRestClientException.class)
                .hasMessage(ErrorCode.POKE_CHARACTERISTIC_TIMEOUT.getReasonPhrase());
    }

    private PokeDomainCharacteristic getMockDomain() {
        PokeDomainSource language = PokeDomainSource
                .builder()
                .id(1)
                .name("es")
                .build();

        List<PokeDomainDescription> descriptions = new ArrayList(){{
            add(
                    PokeDomainDescription
                            .builder()
                            .language(language)
                            .description("Pokemon trucha")
                            .build()
            );
        }};

        return PokeDomainCharacteristic
                .builder()
                .id(1)
                .descriptions(descriptions)
                .build();
    }

    private PokeCharacteristic getMockEntity() {

        PokeSource language = PokeSource
                .builder()
                .url("http://www.pokemon/1/")
                .name("es")
                .build();

        List<PokeDescription> descriptions = new ArrayList(){{
           add(
                   PokeDescription
                           .builder()
                           .description("Pokemon trucha")
                           .language(language)
                           .build()
           );
        }};

        return PokeCharacteristic
                .builder()
                .id(1)
                .descriptions(descriptions)
                .build();
    }

    private URI buildURL() {
        String url = apiProperties.getPoke().getCharacteristic();
        Map<String,String> mapper = new HashMap<>();
        mapper.put("id","1");
        return new DefaultUriBuilderFactory(url)
                .builder()
                .build(mapper);
    }

}
