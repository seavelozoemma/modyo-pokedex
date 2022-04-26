package com.pokedex.pokemon.adapters.exception;

import com.pokedex.pokemon.config.ErrorCode;
import com.pokedex.pokemon.config.exception.GenericException;

public class NotFoundRestClientException extends GenericException {
    public NotFoundRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
