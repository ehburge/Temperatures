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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletOutputStream;

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
import com.temps.rest.model.CelsiusAll;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TempsRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getTempsRange() throws Exception {
	
		ResultActions ra = this.mockMvc.perform(get("/temps/from/2017-04-28 12:11:13/to/2019-04-28 12:11:13")).andDo(print()).andExpect(status().isOk());
	
		String tempsStr = ra.andReturn().getResponse().getContentAsString();
		
		Collection<String> allTemps = TempsRestController.jsonAsObject(tempsStr, Collection.class);
	
		//System.out.println("getTemps size: " + allTemps.size());
	
		System.out.println("***Begin temps listing");
		for (Object temp : allTemps) {
			System.out.println(temp);
		}
		System.out.println("***End temps listing");
	}
	
	@Test
	public void getIdParam() throws Exception {
		//
		ResultActions ra = this.mockMvc.perform(get("/temps/7")).andDo(print()).andExpect(status().isOk());
		// .andExpect(jsonPath("$.content").value("Hello, Spring
		// Community!")).andReturn();

		String resTemp = ra.andReturn().getResponse().getContentAsString();
		System.out.println("result str :" + resTemp);

		CelsiusAll celsius = TempsRestController.jsonAsObject(resTemp, CelsiusAll.class);

		assertEquals(new Character('F'), celsius.tempScale());
		assertEquals(new Integer(7), celsius.getId());
	}

	@Test
	public void postTemp() throws Exception {

		CelsiusAll celsius = new CelsiusAll.BuildPost('F', new BigDecimal("17.4")).build();

		MvcResult mvcr = mockMvc
				.perform(MockMvcRequestBuilders.post("/temps").content(TempsRestController.asJsonString(celsius))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		String resTemp = mvcr.getResponse().getContentAsString();

		CelsiusAll resTempObj = TempsRestController.jsonAsObject(resTemp, CelsiusAll.class);

		assertTrue(resTempObj.getCelsius().equals(celsius.getCelsius()));
		assertTrue(resTempObj.getTempScale().equals(celsius.getTempScale()));
		assertTrue(resTempObj.getUpdate_date().equals(celsius.getUpdate_date()));
		assertTrue(resTempObj.getCreate_date().equals(celsius.getCreate_date()));

		// MediaType depends on ContentType in GreetingController

	}

	@Test
	public void putTemp() throws Exception {

		CelsiusAll celsiusPost = new CelsiusAll.BuildPost('F', new BigDecimal("19.4")).build();

		// **********************
		// Create a new temp
		// **********************

		MvcResult postmvcr = mockMvc
				.perform(MockMvcRequestBuilders.post("/temps").content(TempsRestController.asJsonString(celsiusPost))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		String postTemp = postmvcr.getResponse().getContentAsString();
		Celsius postObj = TempsRestController.jsonAsObject(postTemp, CelsiusAll.class);

		//*******************
		// Get the new temp
		//*******************
		ResultActions ra = this.mockMvc.perform(get("/temps/" + postObj.getId())).andDo(print())
				.andExpect(status().isOk());
		
		MvcResult mvcr = ra.andReturn();
		String postGetResult = mvcr.getResponse().getContentAsString();
		CelsiusAll postGetObj = TempsRestController.jsonAsObject(postGetResult, CelsiusAll.class);

		//*******************
		// Make sure that what is returned from the database (get) 
		// matches what was posted.
		//*******************
		assertTrue(postObj.getCelsius().equals(postGetObj.getCelsius()));
		assertTrue(postObj.tempScale().equals(postGetObj.tempScale()));
		assertTrue(postObj.getCreate_date().equals(postGetObj.getCreate_date()));
		assertTrue(postObj.getUpdate_date().equals(postGetObj.getUpdate_date()));

		// **********************
		// Update the Temperature with PUT
		// **********************
		postGetObj.setCelsius(new BigDecimal("1.4"));

		MvcResult putMvcResult = mockMvc
				.perform(MockMvcRequestBuilders.put("/temps/" + postGetObj.getId())
						.content(TempsRestController.asJsonString(postGetObj)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String putresTemp = putMvcResult.getResponse().getContentAsString();
		Celsius celPutResult = TempsRestController.jsonAsObject(putresTemp, Celsius.class);

		assertTrue(celPutResult.getCelsius().equals(postGetObj.getCelsius()));
		assertTrue(celPutResult.tempScale().equals(postGetObj.tempScale()));
		assertTrue(celPutResult.getUpdate_date().equals(postGetObj.getUpdate_date()));
		assertTrue(celPutResult.getCreate_date().equals(postGetObj.getCreate_date()));

		// **********************
		// Do a GET to make sure update ws applied to database
		// **********************

		ResultActions getRA = this.mockMvc.perform(get("/temps/" + postGetObj.getId())).andDo(print())
				.andExpect(status().isOk());
		// .andExpect(jsonPath("$.content").value("Hello, Spring
		// Community!")).andReturn();

		String getRAtemp = getRA.andReturn().getResponse().getContentAsString();
		System.out.println("result str :" + getRAtemp);

		Celsius getCelsius = TempsRestController.jsonAsObject(getRAtemp, Celsius.class);

		assertTrue(celPutResult.getCelsius().equals(getCelsius.getCelsius()));
		assertEquals(celPutResult.tempScale(), getCelsius.tempScale());
		assertEquals(celPutResult.getId(), getCelsius.getId());
		assertTrue(celPutResult.getUpdate_date().equals(getCelsius.getUpdate_date()));

	}
}
