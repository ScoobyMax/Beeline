package com.mpetrischev.repository;

import com.mpetrischev.DetailServiceApplication;
import com.mpetrischev.entity.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DetailServiceApplication.class)
public class SessionRepositoryIntegrationTests {

	@Autowired
	SessionRepository repository;

	@Test
	public void testFindByCell() {
		List<Session> sessions = this.repository.findByCell("11111");
		assertEquals(9,sessions.size());
	}
}
