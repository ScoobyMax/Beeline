package com.mpetrischev;

import com.mpetrischev.data.jpa.domain.Abonent;
import com.mpetrischev.data.jpa.domain.Session;
import com.mpetrischev.data.jpa.service.AbonentRepository;
import com.mpetrischev.data.jpa.service.SessionRepository;
import com.mpetrischev.model.ResponseAbonents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    private SessionRepository sessionRepository;
    private AbonentRepository abonentRepository;
    ExecutorService executor = Executors.newFixedThreadPool(10);
    RestTemplate restTemplate = new RestTemplate();

    @Value("${url.profileservice}")
    private String profileServiceUrl;

    @Autowired
    public DetailServiceController(SessionRepository sessionRepository, AbonentRepository abonentRepository) {
        this.sessionRepository = sessionRepository;
        this.abonentRepository = abonentRepository;
    }

    @RequestMapping(value = {"/{cellid}"}, method = RequestMethod.GET)
    public ResponseAbonents getAbonents(@PathVariable("cellid") String cellid) {
        List<Session> sessions = sessionRepository.findByCell(cellid);
        ResponseAbonents responseAbonents = new ResponseAbonents(sessions.size());
        List<Future<Abonent>> list = new ArrayList<>(sessions.size());
        sessions.forEach( session -> {
            ProfileCallTask job = new ProfileCallTask(session.getCtn());
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


    private class ProfileCallTask implements Callable<Abonent> {
        private Abonent abonent;
        public ProfileCallTask(String ctn) {
            this.abonent = abonentRepository.getByCtn(ctn);
        }
        public Abonent call() {
            Map<String, String> params = new HashMap<>();
            params.put("ctn", abonent.getCtn());

            params = restTemplate.getForObject(profileServiceUrl, Map.class, params);
            assert params != null;
            abonent.setName(params.get("Email"));
            abonent.setEmail(params.get("Name"));
            return abonent;
        }
    }
}
