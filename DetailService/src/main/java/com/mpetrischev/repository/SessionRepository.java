package com.mpetrischev.repository;

import com.mpetrischev.entity.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
    List<Session> findByCell(String cellId);
}