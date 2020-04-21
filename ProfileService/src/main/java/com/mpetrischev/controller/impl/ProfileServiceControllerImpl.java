package com.mpetrischev.controller.impl;

import com.mpetrischev.controller.ProfileServiceController;
import com.mpetrischev.dto.Profile;
import com.mpetrischev.dto.ProfileInfo;
import com.mpetrischev.dto.ProfileResponce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileServiceControllerImpl implements ProfileServiceController {
	private static final Logger log = LoggerFactory.getLogger(ProfileServiceControllerImpl.class);

	private final RestTemplate restTemplate;
	@Value("${url.randomuser}")
	private String randomuserUrl;

	@Autowired
	public ProfileServiceControllerImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	@GetMapping(value = {"/{ctn}"})
	public Profile getProfile(@PathVariable("ctn") String ctn) {
		long start = System.currentTimeMillis();
		log.debug("Start");
		Map<String, String> params = new HashMap<>();
		params.put("ctn", ctn);
		ProfileResponce result = restTemplate.getForObject(randomuserUrl, ProfileResponce.class, params);
		assert result != null;
		log.debug(result.getInfo().toString());
		ProfileInfo info = result.getResults().get(0);
		Profile profile = new Profile(ctn);
		profile.setEmail(info.getEmail());
		profile.setName(info.getName().getFirst() + " " + info.getName().getLast());
		log.debug("Done " + (System.currentTimeMillis() - start));
		return profile;
	}
}
