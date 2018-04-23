/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.temps.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.temps.rest.model.Celsius;
import com.temps.rest.model.TempScale;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TempsRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getTempsStream() throws Exception {

		ResultActions ra = this.mockMvc.perform(get("/tempsStream")).andDo(print()).andExpect(status().isOk());
		// .andExpect(jsonPath("$.content").value("Hello, World!"));
		String tempsStr = ra.andReturn().getResponse().getContentAsString();

		Collection<String> allTemps = TempsRestController.jsonAsObject(tempsStr, Collection.class);

		System.out.println("getTempsStream size: " + allTemps.size());

		System.out.println("***Begin temps listing");
		for (Object temp : allTemps) {
			System.out.println(temp);
		}
		System.out.println("***End temps listing");
	}

	@Test
	public void getIdParam() throws Exception {
		//
		ResultActions ra = this.mockMvc.perform(get("/temps/102")).andDo(print()).andExpect(status().isOk());
		// .andExpect(jsonPath("$.content").value("Hello, Spring
		// Community!")).andReturn();

		String resTemp = ra.andReturn().getResponse().getContentAsString();
		System.out.println("result str :" + resTemp);

		TempScale ts = TempsRestController.jsonAsObject(resTemp, TempScale.class);

		assertEquals(new Character('F'), ts.get_tempScale());
		assertEquals(new Integer(102), ts.get_Celsius().getId());
	}

	@Test
	public void postTemp() throws Exception {

		Celsius celsius = new Celsius.Build('C', -1, new BigDecimal("15.6"), new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis())).build();

		MvcResult mvcr = mockMvc
				.perform(MockMvcRequestBuilders.post("/temps").content(TempsRestController.asJsonString(celsius))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		String resTemp = mvcr.getResponse().getContentAsString();

		Celsius resTempObj = TempsRestController.jsonAsObject(resTemp, Celsius.class);

		// assertTrue(resTempObj.getId().equals(celsius.getId()));
		assertTrue(resTempObj.getCelsius().equals(celsius.getCelsius()));
		assertTrue(resTempObj.getUpdate_date().equals(celsius.getUpdate_date()));
		assertTrue(resTempObj.getCreate_date().equals(celsius.getCreate_date()));

		// MediaType depends on ContentType in GreetingController

	}

	@Test
	public void putTemp() throws Exception {

		Celsius celsius = new Celsius.Build('C', -1, new BigDecimal("11.8"), new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis())).build();

		// **********************
		// Create a new temp
		// **********************

		MvcResult postmvcr = mockMvc
				.perform(MockMvcRequestBuilders.post("/temps").content(TempsRestController.asJsonString(celsius))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		String resTemp = postmvcr.getResponse().getContentAsString();

		Celsius resTempObj = TempsRestController.jsonAsObject(resTemp, Celsius.class);

		ResultActions ra = this.mockMvc.perform(get("/temps/501")).andDo(print()).andExpect(status().isOk());
		// .andExpect(jsonPath("$.content").value("Hello, Spring Community!"));

		MvcResult mvcr = ra.andReturn();
		String jsonResult = mvcr.getResponse().getContentAsString();

		TempScale tempScale = TempsRestController.jsonAsObject(jsonResult, TempScale.class);

		// **********************
		// Update the Temperature with PUT
		// **********************

		Celsius getcelsius = tempScale.get_Celsius();
		getcelsius.setCelsius(new BigDecimal("1.4"));

		MvcResult putMvcResult = mockMvc
				.perform(MockMvcRequestBuilders.put("/temps/501").content(TempsRestController.asJsonString(getcelsius))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String putresTemp = putMvcResult.getResponse().getContentAsString();

		TempScale tsPutResult = TempsRestController.jsonAsObject(putresTemp, TempScale.class);
		Celsius putResult = tsPutResult.get_Celsius();

		// **********************
		// Verify that the returned temp from PUT matches
		// the updated temp before the PUT
		// **********************
		
		assertTrue(putResult.getCelsius().equals(getcelsius.getCelsius()));
		assertTrue(putResult.getUpdate_date().getTime() != getcelsius.getCreate_date().getTime());
		assertTrue(putResult.getCreate_date().getTime() == getcelsius.getCreate_date().getTime());

		// **********************
		// Do a GET for good measure
		// **********************

		ResultActions getRA = this.mockMvc.perform(get("/temps/501")).andDo(print()).andExpect(status().isOk());
		// .andExpect(jsonPath("$.content").value("Hello, Spring
		// Community!")).andReturn();

		String getRAtemp = getRA.andReturn().getResponse().getContentAsString();
		System.out.println("result str :" + getRAtemp);

		TempScale getTS = TempsRestController.jsonAsObject(getRAtemp, TempScale.class);

		assertEquals(tsPutResult.get_tempScale(), getTS.get_tempScale());
		assertEquals(tsPutResult.get_Celsius().getId(), getTS.get_Celsius().getId());
		assertEquals(tsPutResult.get_Celsius().getCelsius(), getTS.get_Celsius().getCelsius());
		assertTrue(tsPutResult.get_Celsius().getUpdate_date().getTime() == getTS.get_Celsius().getUpdate_date().getTime());

	}
}
