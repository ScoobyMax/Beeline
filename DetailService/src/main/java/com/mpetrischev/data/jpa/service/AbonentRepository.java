package com.mpetrischev.data.jpa.service;

import com.mpetrischev.data.jpa.domain.Abonent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonentRepository extends CrudRepository<Abonent, String> {
    Abonent getByCtn(String ctn);

//    String getIdByCtn(String ctn);
}