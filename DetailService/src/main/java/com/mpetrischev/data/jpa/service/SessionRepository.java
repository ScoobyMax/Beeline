package com.mpetrischev.data.jpa.service;

import com.mpetrischev.data.jpa.domain.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
    List<Session> findByCell(String cellId);
}