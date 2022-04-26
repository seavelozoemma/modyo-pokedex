package com.pokedex.pokemon.adapters.util;

import com.pokedex.pokemon.config.properties.ApiProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RestUtilsProps {

    @Getter
    private final ApiProperties apiProperties;

    @Autowired
    public RestUtilsProps(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }
    public HttpEntity getHeadersAcceptJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(headers);
    }

}
