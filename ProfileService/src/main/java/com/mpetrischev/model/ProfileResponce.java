package com.mpetrischev.model;

import java.util.List;

public class ProfileResponce {
    private List<ProfileInfo> results;
    private Info info;

    public List<ProfileInfo> getResults() {
        return results;
    }

    public void setResults(List<ProfileInfo> results) {
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
