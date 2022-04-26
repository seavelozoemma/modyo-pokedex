package com.pokedex.pokemon.adapters.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokemon.adapters.entity.PokePaginator;
import com.pokedex.pokemon.adapters.entity.PokeSource;
import com.pokedex.pokemon.adapters.exception.NotFoundRestClientException;
import com.pokedex.pokemon.adapters.exception.ServiceUnavailableException;
import com.pokedex.pokemon.adapters.exception.TimeoutRestClientException;
import com.pokedex.pokemon.adapters.integration.PokeListRestAdapter;
import com.pokedex.pokemon.adapters.util.RestUtilsProps;
import com.pokedex.pokemon.application.domain.PokeDomainPaginator;
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
@RestClientTest({PokeListRestAdapter.class})
@Import({TestConfig.class, RestUtilsProps.class})
@ContextConfiguration(initializers = TestConfig.class)
public class PokeListTestRestAdapter {

    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private GlobalProperties globalProperties;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PokeListRestAdapter pokeListRestAdapter;

    @Test
    @DisplayName("Cuando intengo cargar un listado de pokemon y este funciona")
    public void getListOk() throws JsonProcessingException {
        String JSON = objectMapper.writeValueAsString(getMockEntity());
        this.server.expect(requestTo(buildURL())).andRespond(withSuccess(JSON, MediaType.APPLICATION_JSON));
        PokeDomainPaginator paginator = this.pokeListRestAdapter.findByOffset(5, 0);
        assertThat(paginator).isEqualTo(getMockDomain());
    }

    @Test
    @DisplayName("Cuando intengo cargar un listado de pokemon y obtengo un 404")
    public void getList404() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.NOT_FOUND));
        Throwable thrown = catchThrowable(()->this.pokeListRestAdapter.findByOffset(5, 0));
        assertThat(thrown)
                .isInstanceOf(NotFoundRestClientException.class)
                .hasMessage(ErrorCode.POKE_LIST_NOT_FOUND.getReasonPhrase());
    }

    @Test
    @DisplayName("Cuando intengo cargar un listado de pokemon y obtengo un 500")
    public void getList500() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        Throwable thrown = catchThrowable(()->this.pokeListRestAdapter.findByOffset(5, 0));
        assertThat(thrown)
                .isInstanceOf(ServiceUnavailableException.class)
                .hasMessage(ErrorCode.POKE_LIST_SERVER_ERROR.getReasonPhrase());
    }

    @Test
    @DisplayName("Cuando intengo cargar un listado de pokemon y obtengo un 408")
    public void getList408() {
        this.server.expect(requestTo(buildURL())).andRespond(withStatus(HttpStatus.REQUEST_TIMEOUT));
        Throwable thrown = catchThrowable(()->this.pokeListRestAdapter.findByOffset(5, 0));
        assertThat(thrown)
                .isInstanceOf(TimeoutRestClientException.class)
                .hasMessage(ErrorCode.POKE_LIST_TIMEOUT.getReasonPhrase());
    }

    private PokeDomainPaginator getMockDomain() {

        List<PokeDomainSource> list = new ArrayList() {{
            add(
                    PokeDomainSource
                            .builder()
                            .name("pikachu")
                            .id(1)
                            .build()
            );
            add(
                    PokeDomainSource
                            .builder()
                            .name("boulbasour")
                            .id(2)
                            .build()
            );
        }};

        return PokeDomainPaginator
                .builder()
                .count(1)
                .previus("http://www.next.com")
                .next("http://www.next.com")
                .list(list)
                .build();
    }

    private PokePaginator getMockEntity() {

        List<PokeSource> list = new ArrayList(){{
            add(
                    PokeSource
                    .builder()
                        .name("pikachu")
                        .url("https://www.pokeapi/v2/api/pokemon/1/")
                    .build()
            );
            add(
                    PokeSource
                            .builder()
                            .name("boulbasour")
                            .url("https://www.pokeapi/v2/api/pokemon/2/")
                            .build()
            );
        }};

        return PokePaginator
                .builder()
                .count(1)
                .next("http://www.next.com")
                .previus("http://www.next.com")
                .results(list)
                .build();

    }

    private URI buildURL() {
        String url = apiProperties.getPoke().getList();
        Map<String,String> mapper = new HashMap<>();
        mapper.put("limit","5");
        mapper.put("offset","0");
        return new DefaultUriBuilderFactory(url)
                .builder()
                .build(mapper);
    }


}
