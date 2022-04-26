package com.pokedex.pokemon.adapters.exception;

import com.pokedex.pokemon.config.ErrorCode;
import com.pokedex.pokemon.config.exception.GenericException;

public class RestClientGenericException extends GenericException {
    public RestClientGenericException(ErrorCode errorCode) {
        super(errorCode);
    }
}