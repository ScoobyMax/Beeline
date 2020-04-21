package com.mpetrischev.data.jpa;

import com.mpetrischev.DetailServiceApplication;
import com.mpetrischev.controller.impl.DetailServiceControllerImpl;
import com.mpetrischev.dto.Profile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DetailServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("scratch")
// Separate profile for web tests to avoid clashing databases
public class SampleDataRestApplicationTests {

	@Autowired
	private WebApplicationContext context;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	@Resource
	private DetailServiceControllerImpl detailServiceController;

	private MockMvc mvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Profile profile = new Profile("");
		profile.setName("someName");
		profile.setEmail("someEmail");
		when(restTemplate.getForObject(anyString(), eq(Profile.class), anyMap())).thenReturn(profile);

		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void testGetData() throws Exception {
		this.mvc.perform(get("/data/11111")).andExpect(status().isOk())
				.andExpect(content().string(containsString("\"total\":9")))
				.andExpect(content().string(containsString("1234567893")));
	}
}
