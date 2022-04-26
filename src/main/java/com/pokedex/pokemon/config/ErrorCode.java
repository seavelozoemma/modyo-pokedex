package com.pokedex.pokemon.config;

public enum ErrorCode {

    WEB_CLIENT_GENERIC(100, "Error on call API"),
    POKE_LIST_NOT_FOUND(101, "Could not load pokemon data"),
    POKE_LIST_SERVER_ERROR(102, "An error occurred loading Pokemon information"),
    POKE_LIST_TIMEOUT(103, "It takes time to load Pokemon information"),
    POKE_EVOLUTION_NOT_FOUND(104, "Could not load Evolution data"),
    POKE_EVOLUTION_SERVER_ERROR(105, "An error occurred loading Evolution information"),
    POKE_EVOLUTION_TIMEOUT(106, "It takes time to load Evolution information"),
    POKE_DETAIL_NOT_FOUND(104, "Could not load Detail data"),
    POKE_DETAIL_SERVER_ERROR(105, "An error occurred loading Detail information"),
    POKE_DETAIL_TIMEOUT(106, "It takes time to load Detail information"),
    POKE_TYPE_NOT_FOUND(107, "Could not load Type data"),
    POKE_TYPE_SERVER_ERROR(108, "An error occurred loading Type information"),
    POKE_TYPE_TIMEOUT(109, "It takes time to load Type information"),
    POKE_ABILITY_NOT_FOUND(110, "Could not load Ability data"),
    POKE_ABILITY_SERVER_ERROR(111, "An error occurred loading Ability information"),
    POKE_ABILITY_TIMEOUT(112, "It takes time to load Ability information"),
    POKE_CHARACTERISTIC_NOT_FOUND(113, "Could not load Characteristic data"),
    POKE_CHARACTERISTIC_SERVER_ERROR(114, "An error occurred loading Characteristic information"),
    POKE_CHARACTERISTIC_TIMEOUT(115, "It takes time to load Characteristic information");

    private final int value;
    private final String reasonPhrase;

    ErrorCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

}
