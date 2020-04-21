package com.mpetrischev.dto;

public class Profile {
    private String ctn;
    private String email;
    private String name;

    public Profile(String ctn) {
        setCtn(ctn);
    }

    public String getCtn() {
        return ctn;
    }

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
