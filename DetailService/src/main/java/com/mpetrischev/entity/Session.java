package com.mpetrischev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "sessions", indexes = {
        @Index(name = "idx_ctn_uuid", columnList = "cell_id, ctn")
})
public class Session implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Column(name = "cell_id")
    @NotBlank
    private String cell;

    @Column(name = "ctn")
    private String ctn;


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getCtn() {
        return ctn;
    }

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }
}
