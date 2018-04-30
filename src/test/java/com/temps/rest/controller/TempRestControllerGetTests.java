package com.temps.rest.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.temps.rest.model.Temp;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class TempRestControllerGetTests {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * This needs to run by itself. If other tests are doing things to the
	 * datamodel, then this may fail.
	 * 
	 * @throws Exception
	 */
	@Test
	public void getTemps() throws Exception {

		ResultActions ra =
				this.mockMvc.perform(get("/temps")).andDo(print()).andExpect(status().isOk());

		MvcResult res = ra.andReturn();
		String resJson = res.getResponse().getContentAsString();
		Collection<Temp> coll =
				TempsRestController.jsonAsObject(resJson, Collection.class);
		System.out.println("***Begin temps listing");
		for (Object temp : coll) {
			System.out.println(temp);
		}
		System.out.println("***End temps listing");
		
		assertTrue(coll.size() == 30);
	}
}
