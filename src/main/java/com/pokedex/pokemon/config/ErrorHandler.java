package com.pokedex.pokemon.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pokedex.pokemon.adapters.exception.NotFoundRestClientException;
import com.pokedex.pokemon.adapters.exception.RestClientGenericException;
import com.pokedex.pokemon.adapters.exception.ServiceUnavailableException;
import com.pokedex.pokemon.adapters.exception.TimeoutRestClientException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundRestClientException.class)
    public ResponseEntity<ApiErrorResponse> handle(NotFoundRestClientException ex) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.getCode());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiErrorResponse> handle(ServiceUnavailableException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getCode());
    }

    @ExceptionHandler(TimeoutRestClientException.class)
    public ResponseEntity<ApiErrorResponse> handle(TimeoutRestClientException ex) {
        log.error(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.REQUEST_TIMEOUT, ex, ex.getCode());
    }


    @ExceptionHandler(RestClientGenericException.class)
    public ResponseEntity<ApiErrorResponse> handle(RestClientGenericException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getCode());
    }

    private ResponseEntity<ApiErrorResponse> buildResponseError(HttpStatus httpStatus, Throwable ex, ErrorCode errorCode) {

        ApiErrorResponse apiErrorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.getReasonPhrase())
                .statusCode(httpStatus.value())
                .code(errorCode.value())
                .message(ex.getMessage())
                .type(ex.getClass().getCanonicalName())
                .build();

        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

    @Builder
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class ApiErrorResponse {

        @JsonProperty
        private String status;
        @JsonProperty
        private Integer statusCode;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private LocalDateTime timestamp;
        @JsonProperty
        private String message;
        @JsonProperty
        private Integer code;
        @JsonProperty
        private String type;
        @JsonProperty
        private String xB3traceId;
        @JsonProperty
        private String debugMessage;

    }

}
