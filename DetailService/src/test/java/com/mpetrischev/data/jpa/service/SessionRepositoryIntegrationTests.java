package com.mpetrischev.data.jpa.service;

import com.mpetrischev.Detailservice;
import com.mpetrischev.data.jpa.domain.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Detailservice.class)
public class SessionRepositoryIntegrationTests {

	@Autowired
	SessionRepository repository;

	@Test
	public void testFindByCell() {
		List<Session> sessions = this.repository.findByCell("1234567894");
		assertEquals(9,sessions.size());
	}
}
