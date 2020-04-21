package com.mpetrischev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "abonentid", indexes = {
       @Index(name = "idx_ctn", columnList = "ctn")
})
public class Abonent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(name = "ctn")
    @NotBlank
    private String ctn ;

    @Transient
    private String name;

    @Transient
    private String email;

    public Abonent() {}

    public Abonent(String ctn) {
        this.ctn = ctn;
    }

    public Abonent(UUID id, String ctn, String name, String email) {
        this.id = id;
        this.ctn = ctn;
        this.name = name;
        this.email = email;
    }

    public String getCtn() {
        return ctn;
    }

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Abonent{" +
                "id=" + id +
                ", ctn='" + ctn + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
