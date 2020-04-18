package com.mpetrischev.model;

import com.mpetrischev.data.jpa.domain.Abonent;

import java.util.ArrayList;
import java.util.List;

public class ResponseAbonents {
    private int total;
    private List<Abonent> results;

    public ResponseAbonents(int total){
        this.total=total;
        results= new ArrayList<>(total);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Abonent> getResults() {
        return results;
    }

    public void setResults(List<Abonent> results) {
        this.results = results;
    }
}
