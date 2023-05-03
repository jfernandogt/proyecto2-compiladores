package com.jfbarrios;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;



public class ParserTest {

    /*
    *
    * {
        "nombres": "Juan Fernando",
        "apellidos": "Barrios Barrera",
        "carnet": "202102655",
        "edad": 32,
        "esViudo": false,
        "comidasPreferidas": [
            "pizza",
            "pasta",
            "ramen"
        ],
        "direccion": {
            "calle": "Calle 1",
            "colonia": "Colonia 1",
            "ciudad": "Ciudad 1",
            "pais": "Pais 1"
        },
        "punteoQueDeseaEnTarea": 100.00
    }
    * */
    @Test
    public void testValidJSON() throws Exception {
        String expresion = "{\n" +
                "        \"nombres\": \"Juan Fernando\",\n" +
                "        \"apellidos\": \"Barrios Barrera\",\n" +
                "        \"carnet\": \"202102655\",\n" +
                "        \"edad\": 32,\n" +
                "        \"esViudo\": false,\n" +
                "        \"comidasPreferidas\": [\n" +
                "            \"pizza\",\n" +
                "            \"pasta\",\n" +
                "            \"ramen\"\n" +
                "        ],\n" +
                "        \"direccion\": {\n" +
                "            \"calle\": \"Calle 1\",\n" +
                "            \"colonia\": \"Colonia 1\",\n" +
                "            \"ciudad\": \"Ciudad 1\",\n" +
                "            \"pais\": \"Pais 1\"\n" +
                "        },\n" +
                "        \"punteoQueDeseaEnTarea\": 100.00\n" +
                "    }";
        JSONLexer lexer = new JSONLexer(new StringReader(expresion));

        Parser parser = new Parser(lexer);

        Object result = parser.parse().value;

        assertEquals(true, result);
    }

    @Test
    public void testEmptyObject() throws Exception {
        String expresion = "{}";
        JSONLexer lexer = new JSONLexer(new StringReader(expresion));

        Parser parser = new Parser(lexer);

        Object result = parser.parse().value;

        assertEquals(true, result);
    }

    @Test
    public void testEmptyArray() throws Exception {
        String expresion = "[]";
        JSONLexer lexer = new JSONLexer(new StringReader(expresion));

        Parser parser = new Parser(lexer);

        Object result = parser.parse().value;

        assertEquals(true, result);
    }

    @Test
    public void testNonValidJSONStructure() {
        String expresion = "{]";
        Exception assertThrows = Assertions.assertThrows(Exception.class, () -> {
            JSONLexer lexer = new JSONLexer(new StringReader(expresion));

            Parser parser = new Parser(lexer);

            Object result = parser.parse().value;
        });
    }

    @Test
    public void testNonValidJSONKeys() {
        String expresion = "{key: true}";
        Exception assertThrows = Assertions.assertThrows(Exception.class, () -> {
            JSONLexer lexer = new JSONLexer(new StringReader(expresion));

            Parser parser = new Parser(lexer);

            Object result = parser.parse().value;
        });
    }


    @Test
    public void testInputExam() throws Exception {
        String expresion = "{\n" +
                "\n" +
                "  \"squadName\": \"Super hero squad\",\n" +
                "\n" +
                "  \"homeTown\": \"Metro City\",\n" +
                "\n" +
                "  \"formed\": 2016,\n" +
                "\n" +
                "  \"secretBase\": \"Super tower\",\n" +
                "\n" +
                "  \"active\": true,\n" +
                "\n" +
                "  \"members\": [\n" +
                "\n" +
                "    {\n" +
                "\n" +
                "      \"name\": \"Molecule Man\",\n" +
                "\n" +
                "      \"age\": 29,\n" +
                "\n" +
                "      \"secretIdentity\": \"Dan Jukes\",\n" +
                "\n" +
                "      \"powers\": [\n" +
                "\n" +
                "        \"Radiation resistance\",\n" +
                "\n" +
                "        \"Turning tiny\",\n" +
                "\n" +
                "        \"Radiation blast\"\n" +
                "\n" +
                "      ]\n" +
                "\n" +
                "    },\n" +
                "\n" +
                "    {\n" +
                "\n" +
                "      \"name\": \"Madame Uppercut\",\n" +
                "\n" +
                "      \"age\": 39,\n" +
                "\n" +
                "      \"secretIdentity\": \"Jane Wilson\",\n" +
                "\n" +
                "      \"powers\": [\n" +
                "\n" +
                "        \"Million tonne punch\",\n" +
                "\n" +
                "        \"Damage resistance\",\n" +
                "\n" +
                "        \"Superhuman reflexes\"\n" +
                "\n" +
                "      ]\n" +
                "\n" +
                "    },\n" +
                "\n" +
                "    {\n" +
                "\n" +
                "      \"name\": \"Eternal Flame\",\n" +
                "\n" +
                "      \"age\": 1000000,\n" +
                "\n" +
                "      \"secretIdentity\": \"Unknown\",\n" +
                "\n" +
                "      \"powers\": [\n" +
                "\n" +
                "        \"Immortality\",\n" +
                "\n" +
                "        \"Heat Immunity\",\n" +
                "\n" +
                "        \"Inferno\",\n" +
                "\n" +
                "        \"Teleportation\",\n" +
                "\n" +
                "        \"Interdimensional travel\"\n" +
                "\n" +
                "      ]\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "  ]\n" +
                "\n" +
                "}";
        JSONLexer lexer = new JSONLexer(new StringReader(expresion));

        Parser parser = new Parser(lexer);

        Object result = parser.parse().value;

        assertEquals(true, result);
    }

}
