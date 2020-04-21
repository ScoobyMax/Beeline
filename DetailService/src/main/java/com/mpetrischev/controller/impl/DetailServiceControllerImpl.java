package com.mpetrischev.controller.impl;

import com.mpetrischev.controller.DetailServiceController;
import com.mpetrischev.dto.Profile;
import com.mpetrischev.dto.ResponseAbonents;
import com.mpetrischev.entity.Abonent;
import com.mpetrischev.entity.Session;
import com.mpetrischev.repository.AbonentRepository;
import com.mpetrischev.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController()
public class DetailServiceControllerImpl implements DetailServiceController {

    private final SessionRepository sessionRepository;
    private final AbonentRepository abonentRepository;
    private static final Logger log = LoggerFactory.getLogger(DetailServiceControllerImpl.class);
    ExecutorService executorService = Executors.newFixedThreadPool(300);
    RestTemplate restTemplate;

    @Value("${connection.timeout}")
    private int timeout;


    @Value("${url.profileservice}")
    private String profileServiceUrl;

    @Autowired
    public DetailServiceControllerImpl(SessionRepository sessionRepository, AbonentRepository abonentRepository, RestTemplate restTemplate) {
        this.sessionRepository = sessionRepository;
        this.abonentRepository = abonentRepository;
        if (restTemplate == null) {
            this.restTemplate = new RestTemplate();
        } else {
            this.restTemplate = restTemplate;
        }
    }

    @Override
    @GetMapping(value = {"/{cellid}"})
    public ResponseAbonents getAbonents(@PathVariable("cellid") String cellid) {
        long start = System.currentTimeMillis();
        log.debug("Start");
        List<Session> sessions = sessionRepository.findByCell(cellid);
        ResponseAbonents responseAbonents = new ResponseAbonents(sessions.size());
        List<Abonent> list = sessions.parallelStream().map(session -> {
            CompletableFuture<Abonent> uuid = CompletableFuture
                    .supplyAsync(() -> abonentRepository.getByCtn(session.getCtn()), executorService);
            CompletableFuture<Profile> profile = CompletableFuture
                    .supplyAsync(() -> getProfile(session), executorService);
            return createAbonent(session, uuid, profile);
        }).collect(Collectors.toList());

        responseAbonents.setResults(list);
        log.debug("Done " + (System.currentTimeMillis() - start));
        return responseAbonents;
    }

    private Abonent createAbonent(Session session, CompletableFuture<Abonent> uuid, CompletableFuture<Profile> profile) {
        Abonent abonent = new Abonent(session.getCtn());
        try {
            abonent.setId(uuid.get(timeout, TimeUnit.MILLISECONDS).getId());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(String.format("Timeout to get UUID for: %s", session.getCtn()));
        }
        try {
            Profile prof = profile.get(timeout, TimeUnit.MILLISECONDS);
            if (prof != null) {
                abonent.setName(prof.getName());
                abonent.setEmail(prof.getEmail());
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(String.format("Timeout to get Profile for: %s", session.getCtn()));
        }
        return abonent;
    }

    private Profile getProfile(Session session) {
        Map<String, String> params = new HashMap<>();
        params.put("ctn", session.getCtn());
        return restTemplate.getForObject(profileServiceUrl, Profile.class, params);
    }


}
