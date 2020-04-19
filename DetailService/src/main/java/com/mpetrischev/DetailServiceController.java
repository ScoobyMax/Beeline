package com.mpetrischev;

import com.mpetrischev.data.jpa.domain.Abonent;
import com.mpetrischev.data.jpa.domain.Session;
import com.mpetrischev.data.jpa.service.AbonentRepository;
import com.mpetrischev.data.jpa.service.SessionRepository;
import com.mpetrischev.model.ResponseAbonents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/data")
public class DetailServiceController {

    private final SessionRepository sessionRepository;
    private final AbonentRepository abonentRepository;
    private static final Logger log = LoggerFactory.getLogger(DetailServiceController.class);
    ExecutorService executor = Executors.newFixedThreadPool(10);
    ExecutorService executor2 = Executors.newFixedThreadPool(10);
    RestTemplate restTemplate;

    @Value("${url.profileservice}")
    private String profileServiceUrl;

    @Autowired
    public DetailServiceController(SessionRepository sessionRepository, AbonentRepository abonentRepository, RestTemplate restTemplate) {
        this.sessionRepository = sessionRepository;
        this.abonentRepository = abonentRepository;
        if (restTemplate == null) {
            this.restTemplate = new RestTemplate();
        } else {
            this.restTemplate = restTemplate;
        }
    }

    @GetMapping(value = {"/{cellid}"})
    public ResponseAbonents getAbonents(@PathVariable("cellid") String cellid) {
        List<Session> sessions = sessionRepository.findByCell(cellid);
        ResponseAbonents responseAbonents = new ResponseAbonents(sessions.size());
        List<Future<Abonent>> list = new ArrayList<>(sessions.size());
        sessions.forEach( session -> {
            AbonentCallTask job = new AbonentCallTask(session.getCtn());
            Future<Abonent> future = executor.submit(job);
            list.add(future);
        });

        list.forEach( fut ->
        {
            try {
                responseAbonents.getResults().add(fut.get(2, TimeUnit.SECONDS));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                responseAbonents.getResults().add(null);
            }
        });

        return responseAbonents;
    }


    private class AbonentCallTask implements Callable<Abonent> {
        private final Abonent abonent;
        public AbonentCallTask(String ctn) {
            this.abonent = abonentRepository.getByCtn(ctn);
        }
        public Abonent call() {
            ProfileCallTask job = new ProfileCallTask(abonent);
            Future<Abonent> future = executor2.submit(job);
            try {
                return future.get(1, TimeUnit.SECONDS);
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                log.warn(String.format("Timeout to get profile for: %s", abonent.getCtn()));
            }

            return abonent;

        }
    }


    private class ProfileCallTask implements Callable<Abonent> {
        private final Abonent abonent;
        public ProfileCallTask(Abonent abonent) {
            this.abonent = abonent;
        }
        public Abonent call() {
            Map<String, String> params = new HashMap<>();
            params.put("ctn", abonent.getCtn());

            params = restTemplate.getForObject(profileServiceUrl, Map.class, params);
            if (params != null) {
                abonent.setName(params.get("Email"));
                abonent.setEmail(params.get("Name"));
            }
            return abonent;
        }
    }
}
