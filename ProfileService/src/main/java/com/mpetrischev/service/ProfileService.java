package com.mpetrischev.service;

import com.mpetrischev.model.ProfileInfo;
import com.mpetrischev.model.ProfileResponce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@EnableAutoConfiguration
@RestController
@RequestMapping("/api/profile")
public class ProfileService {
	private static final Logger log = LoggerFactory.getLogger(ProfileService.class);

	public static void main(String[] args) {
		SpringApplication.run(ProfileService.class);
	}

	@GetMapping(value = {"/{ctn}"})
	public Map<String, String> getProfile(@PathVariable("ctn") String ctn) {
		Map<String, String> params = new HashMap<>();
		params.put("ctn", ctn);
		RestTemplate restTemplate = new RestTemplate();

		String url = "https://randomuser.me/api/?phone={ctn}&inc=name,email";
		ProfileResponce result = restTemplate.getForObject(url, ProfileResponce.class, params);
		log.info(result.getInfo().toString());
		assert result != null;
		ProfileInfo info = result.getResults().get(0);
		params.put("Email", info.getEmail());
		params.put("Name", info.getName().getFirst() + " " + info.getName().getLast());
		return params;
	}
}
