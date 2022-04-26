package com.pokedex.pokemon.config.properties;

import com.pokedex.pokemon.config.properties.model.Poke;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private Poke poke;
}
