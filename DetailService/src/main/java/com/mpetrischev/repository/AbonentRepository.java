package com.mpetrischev.repository;

import com.mpetrischev.entity.Abonent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonentRepository extends CrudRepository<Abonent, String> {
    Abonent getByCtn(String ctn);
}