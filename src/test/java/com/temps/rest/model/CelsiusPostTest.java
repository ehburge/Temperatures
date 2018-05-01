package com.temps.rest.model;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CelsiusPostTest {

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

	@Test
	public void CelsiusPostCreate_test() throws Exception {

		String crtDate = "2017-08-23 17:57:57";
		String updDate = "2017-09-14 10:32:12";

		CelsiusAll cp = new CelsiusAll();

		cp.setCelsius(new BigDecimal("13.2"));
		cp.setTempScale('C');
		cp.setCreate_date("2017-08-23 17:57:57");
		cp.setUpdate_date("2017-09-14 10:32:12");

		assertTrue(new Character('C').equals(cp.getTempScale()));
		assertTrue(new BigDecimal("13.2").equals(cp.getCelsius()));
		assertTrue(crtDate.equals(cp.getCreate_date()));
		assertTrue(updDate.equals(cp.getUpdate_date()));

		CelsiusEntity ce = cp.objCelsiusEntity();
		assertTrue(ce.getCelsius_tempscale().equals("13.2_C"));

		assertTrue(new BigDecimal("13.2").equals(cp.getCelsius()));
		assertTrue(new Character('C').equals(cp.getTempScale()));

	}

}
