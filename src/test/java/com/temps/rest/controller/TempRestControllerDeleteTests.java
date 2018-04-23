package com.temps.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TempRestControllerDeleteTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void deleteTempNotFound() throws Exception {
		
		this.mockMvc.perform(delete("/temps/9999999")).andDo(print()).andExpect(status().isNotFound() );
		
	}

	@Test
	public void deleteTemp() throws Exception {
		
		ResultActions ra = this.mockMvc.perform(delete("/temps/103")).andDo(print()).andExpect(status().isOk() );
		
	}
}
