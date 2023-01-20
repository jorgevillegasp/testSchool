package org.viamatica.school.utility;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Metodos {

    @Inject
    ObjectMapper objectMapper;

    /**
     * Recorre el eString y me trae el ultimo valor,
     * debido a que mi JWT al decodificar me trae un String
     *
     * @param value
     * @return int
     */
    public int lastValue(String value){

            Gson gson = new Gson();
            JsonElement json = gson.fromJson(value, JsonElement.class);
            int res =  json.getAsJsonObject().get("idRol").getAsInt(); // retorna "John"

        return res;
    }
}
