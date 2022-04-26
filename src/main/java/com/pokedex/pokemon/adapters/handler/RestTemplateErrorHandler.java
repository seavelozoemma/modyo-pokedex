package com.pokedex.pokemon.adapters.handler;
import com.pokedex.pokemon.adapters.exception.RestClientGenericException;
import com.pokedex.pokemon.config.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    private final Map<HttpStatus, RuntimeException> exceptionsMap;

    public RestTemplateErrorHandler(Map<HttpStatus, RuntimeException> exceptionsMap) {
        this.exceptionsMap = exceptionsMap;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.info("Handle Error Request body : {}", new String(response.getBody().toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        throw exceptionsMap.getOrDefault(response.getStatusCode(),
                new RestClientGenericException(ErrorCode.WEB_CLIENT_GENERIC));
    }
}
