package com.mpetrischev.data.jpa.service;

import com.mpetrischev.Detailservice;
import com.mpetrischev.data.jpa.domain.Abonent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Detailservice.class)
public class AbonentRepositoryIntegrationTests {

	@Autowired
	AbonentRepository repository;

	@Test
	public void testGetByCtn() {

		Abonent abonent = this.repository.getByCtn("1234567894");
		assertNotNull(abonent);
	}
}
