package com.pokedex.pokemon.adapters.exception;

import com.pokedex.pokemon.config.ErrorCode;
import com.pokedex.pokemon.config.exception.GenericException;

public class TimeoutRestClientException extends GenericException {
    public TimeoutRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}