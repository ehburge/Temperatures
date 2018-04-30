package com.temps.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.temps.rest.model.CelsiusAll;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TempRestControllerDeleteTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void deleteTempNotFound() throws Exception {
		
		ResultActions ra = this.mockMvc.perform(delete("/temps/9999999")).andDo(print()).andExpect(status().isNotFound() );
		MvcResult mvcRes = ra.andReturn();
		String resStr = mvcRes.getResponse().getContentAsString();
		assertTrue(resStr.length() == 0);
	}

	@Test
	public void deleteTemp() throws Exception {
		
		ResultActions raGet = this.mockMvc.perform(get("/temps/2")).andDo(print()).andExpect(status().isOk());
		// .andExpect(jsonPath("$.content").value("Hello, Spring
		// Community!")).andReturn();

		String resGet = raGet.andReturn().getResponse().getContentAsString();
		System.out.println("result str :" + resGet);

		CelsiusAll cGet = TempsRestController.jsonAsObject(resGet, CelsiusAll.class);
		
		ResultActions raDel = this.mockMvc.perform(delete("/temps/2")).andDo(print()).andExpect(status().isOk() );
		
		MvcResult mvcRes = raDel.andReturn();
		String resDel = mvcRes.getResponse().getContentAsString();

		CelsiusAll cDel = TempsRestController.jsonAsObject(resDel, CelsiusAll.class);
		
		assertEquals(cGet.getId(), cDel.getId());
		assertEquals(cGet.getTempScale(), cDel.getTempScale());
		assertEquals(cGet.getCelsius(), cDel.getCelsius());
		assertEquals(cGet.getCreate_date(), cDel.getCreate_date());
		assertEquals(cGet.getUpdate_date(), cDel.getUpdate_date());
		
	}
}
