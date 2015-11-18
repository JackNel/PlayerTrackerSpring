package com.theironyard;

import com.theironyard.services.PlayerRepository;
import com.theironyard.services.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PlayerTrackerSpringApplication.class)
@WebAppConfiguration
public class PlayerTrackerSpringApplicationTests {
	@Autowired
	PlayerRepository players;
	@Autowired
	UserRepository users;
	@Autowired
	WebApplicationContext wap;

	MockMvc mockMvc;

	@Before
	public void before() {
		players.deleteAll();
		users.deleteAll();
		mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
	}

	@Test
	public void testLogin() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
					.param("username", "testUser")
					.param("password", "testPass")
		);
		assertTrue(users.count() == 1);
	}

	@Test
	public void testCreate() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/create")
					.param("name", "testName")
					.param("number", "1")
					.param("team", "testTeam")
					.param("position", "testPosition")
					.param("age", "1")
					.sessionAttr("username", "testUser")
		);
		assertTrue(players.count() == 1);
	}

	@Test
	public void testEditPlayer() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
						.param("username", "testUser")
						.param("password", "testPass")
		);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/create")
						.param("name", "testName")
						.param("number", "1")
						.param("team", "testTeam")
						.param("position", "testPosition")
						.param("age", "1")
						.sessionAttr("username", "testUser")
		);
		int x = players.findAll().iterator().next().id;
		mockMvc.perform(
				MockMvcRequestBuilders.post("/edit-player")
					.sessionAttr("username", "testUser")
					.param("id", x + "")
					.param("name", "testName")
					.param("number", "2")
					.param("team", "testTeam")
					.param("position", "testPosition")
					.param("age", "2")
		);
		assertTrue(players.findOneByName("testName").age == 2);
	}

	@Test
	public void testDelete() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
						.param("username", "testUser")
						.param("password", "testPass")
		);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/create")
						.param("name", "testName")
						.param("number", "1")
						.param("team", "testTeam")
						.param("position", "testPosition")
						.param("age", "1")
						.sessionAttr("username", "testUser")
		);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/delete")
					.sessionAttr("username", "testUser")
					.param("id", players.findAll().iterator().next().id+"")
		);
		assertTrue(players.count() == 0);
	}
}
