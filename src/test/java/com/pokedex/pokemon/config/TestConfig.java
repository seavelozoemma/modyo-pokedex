package com.pokedex.pokemon.config;

import com.pokedex.pokemon.config.properties.ApiProperties;
import com.pokedex.pokemon.config.properties.GlobalProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@TestConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties(value ={ApiProperties.class, GlobalProperties.class} )
public class TestConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils
                .addInlinedPropertiesToEnvironment(applicationContext);
    }
}
