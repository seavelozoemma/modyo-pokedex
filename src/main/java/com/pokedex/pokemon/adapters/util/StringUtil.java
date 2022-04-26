package com.pokedex.pokemon.adapters.util;

public class StringUtil {

    public static Integer getId(String url) {
        if (url != null) {
            String[] array = url.split("/");
            int length = array.length;
            return Integer.parseInt(array[length-1]);
        } else {
            return null;
        }
    }

}
