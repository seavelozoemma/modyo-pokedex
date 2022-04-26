package com.pokedex.pokemon.adapters.exception;

import com.pokedex.pokemon.config.ErrorCode;
import com.pokedex.pokemon.config.exception.GenericException;

public class ServiceUnavailableException extends GenericException {
    public ServiceUnavailableException(ErrorCode errorCode) {
        super(errorCode);
    }
}