package com.temps.rest.dao.sqlfile;

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.temps.rest.model.CelsiusAll;

public class CreateCelsiusAllTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	final String regex =
			"\\bINSERT\\s+INTO\\s+\\S+\\s*\\(([^)]+)\\)\\s*VALUES\\s*\\(([^)]+)\\)";
	// final String regex = "^(INSERT INTO)(?:[^;']|(?:'[^']+'))+;\\s*$";
	final Pattern p =
			Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL
		);
	
	@Test
	public void insertPattermTest() throws Exception {
		String line = "INSERT INTO celsius_entity (id,celsius_tempscale,create_date,update_date) VALUES (14,'17.4_F',{ts '2018-04-29 14:54:18.0'},{ts '2018-04-29 14:54:18.0'});";

		Matcher m = p.matcher(line);
		while (m.find()) {
			String key = m.group(1);
			String body = m.group(2);
			
			System.out.println("key=" + key + " " + "body=" + body);
		}
	}
	
	@Test
	public void createDateString() throws Exception {
		CreateCelsiusAll ccAll = new CreateCelsiusAll();
		CreateCelsiusAll.CreateDate crtDate = ccAll.new CreateDate();
		CelsiusAll ca = new CelsiusAll();
		
		crtDate.setCelsiusAll(ca);
		crtDate.setValue("{ts '2018-04-28 14:01:18.0'}");
		
		assertEquals("2018-04-28 14:01:18", ca.getCreate_date());
	}
	
	@Test
	public void createDateStringNoDotZero() throws Exception {
		CreateCelsiusAll ccAll = new CreateCelsiusAll();
		CreateCelsiusAll.CreateDate crtDate = ccAll.new CreateDate();
		CelsiusAll ca = new CelsiusAll();
		
		crtDate.setCelsiusAll(ca);
		crtDate.setValue("{ts '2018-04-28 14:01:18'}");
		
		assertEquals("2018-04-28 14:01:18", ca.getCreate_date());
	}

//	@Test
//	public void dateStringtest() throws Exception {
//
//		CreateCelsiusAll ccAll = new CreateCelsiusAll();
//
//		String val = "{ts '2018-04-28 14:01:18.0'}";
//
//		String outStr = ccAll.pullOutDate(val);
//		assertEquals("2018-04-28 14:01:18.0", outStr);
//	}

}
