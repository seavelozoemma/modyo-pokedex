package com.pokedex.pokemon.application;

import com.pokedex.pokemon.adapters.exception.NotFoundRestClientException;
import com.pokedex.pokemon.adapters.exception.ServiceUnavailableException;
import com.pokedex.pokemon.adapters.exception.TimeoutRestClientException;
import com.pokedex.pokemon.application.domain.*;
import com.pokedex.pokemon.application.ports.out.PokeCharacteristicRepository;
import com.pokedex.pokemon.application.ports.out.PokeDetailRepository;
import com.pokedex.pokemon.application.ports.out.PokeEvolutionRepository;
import com.pokedex.pokemon.application.ports.out.PokeListRepository;
import com.pokedex.pokemon.application.usecases.PokeUseCase;
import com.pokedex.pokemon.config.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("PokeTestUseCase")
@ExtendWith(MockitoExtension.class)
public class PokeTestUseCase {

    @Mock
    PokeDetailRepository pokeDetailRepository;

    @Mock
    PokeListRepository pokeListRepository;

    @Mock
    PokeEvolutionRepository pokeEvolutionRepository;

    @Mock
    PokeCharacteristicRepository pokeCharacteristicRepository;

    @Test
    @DisplayName("Obtengo listado de pokemones")
    public void getPokeListOk() {
        when(this.pokeListRepository.findByOffset(5,0)).thenReturn(getMockPaginateEntity());
        when(this.pokeDetailRepository.findById(1)).thenReturn(getMockDetailDomain());
        PokeUseCase useCase = new PokeUseCase(pokeDetailRepository, pokeListRepository, pokeEvolutionRepository, pokeCharacteristicRepository);
        PokeDomain paginate = useCase.findByPaginate(1);
        assertNotNull(paginate);
    }

    @Test
    @DisplayName("Obtengo listado de pokemones y responde 404")
    public void getPokeList404() {
        when(this.pokeListRepository.findByOffset(5,0)).thenThrow(new NotFoundRestClientException(ErrorCode.POKE_LIST_NOT_FOUND));
        PokeUseCase useCase = new PokeUseCase(pokeDetailRepository, pokeListRepository, pokeEvolutionRepository, pokeCharacteristicRepository);
        assertThrows(NotFoundRestClientException.class, () -> useCase.findByPaginate(1));
    }

    @Test
    @DisplayName("Obtengo listado de pokemones y responde 500")
    public void getPokeList500() {
        when(this.pokeListRepository.findByOffset(5,0)).thenThrow(new ServiceUnavailableException(ErrorCode.POKE_LIST_SERVER_ERROR));
        PokeUseCase useCase = new PokeUseCase(pokeDetailRepository, pokeListRepository, pokeEvolutionRepository, pokeCharacteristicRepository);
        assertThrows(ServiceUnavailableException.class, () -> useCase.findByPaginate(1));
    }

    @Test
    @DisplayName("Obtengo listado de pokemones y responde 408")
    public void getPokeList408() {
        when(this.pokeListRepository.findByOffset(5,0)).thenThrow(new TimeoutRestClientException(ErrorCode.POKE_LIST_TIMEOUT));
        PokeUseCase useCase = new PokeUseCase(pokeDetailRepository, pokeListRepository, pokeEvolutionRepository, pokeCharacteristicRepository);
        assertThrows(TimeoutRestClientException.class, () -> useCase.findByPaginate(1));
    }

    @Test
    @DisplayName("Obtengo listado de pokemones")
    public void getPokeDetailOk() {
        when(this.pokeDetailRepository.findById(1)).thenReturn(getMockDetailDomain());
        when(this.pokeEvolutionRepository.findEvolutionById(1)).thenReturn(getMockEvolutionDomain());
        when(this.pokeCharacteristicRepository.findById(1)).thenReturn(getMockCharacteristicDomain());
        PokeUseCase useCase = new PokeUseCase(pokeDetailRepository, pokeListRepository, pokeEvolutionRepository, pokeCharacteristicRepository);
        PokeDomainBasic poke = useCase.findById(1);
        assertNotNull(poke);
    }

    @Test
    @DisplayName("Obtengo listado de pokemones y responde 404")
    public void getPokeDetail404() {
        when(this.pokeDetailRepository.findById(1)).thenThrow(new NotFoundRestClientException(ErrorCode.POKE_DETAIL_NOT_FOUND));
        PokeUseCase useCase = new PokeUseCase(pokeDetailRepository, pokeListRepository, pokeEvolutionRepository, pokeCharacteristicRepository);
        assertThrows(NotFoundRestClientException.class, () -> useCase.findById(1));
    }

    @Test
    @DisplayName("Obtengo listado de pokemones y responde 500")
    public void getPokeDetail500() {
        when(this.pokeDetailRepository.findById(1)).thenThrow(new ServiceUnavailableException(ErrorCode.POKE_DETAIL_SERVER_ERROR));
        PokeUseCase useCase = new PokeUseCase(pokeDetailRepository, pokeListRepository, pokeEvolutionRepository, pokeCharacteristicRepository);
        assertThrows(ServiceUnavailableException.class, () -> useCase.findById(1));
    }

    @Test
    @DisplayName("Obtengo listado de pokemones y responde 408")
    public void getPokeDetail408() {
        when(this.pokeDetailRepository.findById(1)).thenThrow(new TimeoutRestClientException(ErrorCode.POKE_DETAIL_TIMEOUT));
        PokeUseCase useCase = new PokeUseCase(pokeDetailRepository, pokeListRepository, pokeEvolutionRepository, pokeCharacteristicRepository);
        assertThrows(TimeoutRestClientException.class, () -> useCase.findById(1));
    }

    private PokeDomainCharacteristic getMockCharacteristicDomain() {
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

    private PokeDomainEvolution getMockEvolutionDomain() {

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

    private PokeDomainPaginator getMockPaginateEntity() {

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

    private PokeDomainBasic getMockDetailDomain() {
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

}
