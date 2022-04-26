package com.pokedex.pokemon.config.properties;

import com.pokedex.pokemon.config.properties.model.Redis;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring")
public class SpringProperties {
    private Redis redis;
}
