package com.mpetrischev.dto;

import com.mpetrischev.entity.Abonent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseAbonentsTest {

    Abonent abonent;

    @Test
    void testTotal() {
        ResponseAbonents responseAbonents = new ResponseAbonents(2);
        responseAbonents.setTotal(4);
        assertEquals(4, responseAbonents.getTotal());
    }


    @Test
    void testResults() {
        ResponseAbonents responseAbonents = new ResponseAbonents(2);
        List<Abonent> abonents = new ArrayList<>();
        abonents.add(new Abonent("123"));
        abonents.add(new Abonent("321"));

        responseAbonents.setResults(abonents);
        assertEquals("321", responseAbonents.getResults().get(1).getCtn());
    }

}