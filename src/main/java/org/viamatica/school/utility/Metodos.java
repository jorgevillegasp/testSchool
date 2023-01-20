package org.viamatica.school.utility;

public class Metodos {


    /**
     * Recorre el eString y me trae el ultimo valor,
     * debido a que mi JWT al decodificar me trae un String
     *
     * @param value
     * @return int
     */
    public int lastValue(String value){
        return Integer.valueOf(value.substring(value.lastIndexOf(",")+1, value.indexOf("]")));
    }
}
