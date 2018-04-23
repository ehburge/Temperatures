package com.temps.rest.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.temps.rest.model.Celsius;
import com.temps.rest.model.Fahrenheit;
import com.temps.rest.model.Temp;

@Component("tempsDASetup")
public class TempsDASetup {

	public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private Map<Integer, Temp> temps = new HashMap<>();;
	public Collection<Temp> values() {
		return temps.values();
	}

	public boolean containsKey(Object key) {
		return temps.containsKey(key);
	}

	public Temp get(Object key) {
		return temps.get(key);
	}

	public Temp create(Integer key, Temp value) {
		return temps.put(key, value);
	}

	public Temp put(Integer key, Temp value) {
		return temps.put(key, value);
	}

	public Temp remove(Object key) {
		return temps.remove(key);
	}

	private AtomicInteger atomicInteger;

	public TempsDASetup() {
		super();	

		atomicInteger = new AtomicInteger(500);

		try {
			temps.put(101, new Celsius.Build('C', 101, new BigDecimal("12.3"), df.parse("2018-02-25T14:37:45"),
					df.parse("2018-02-25T14:37:45")).build());

			temps.put(102, new Fahrenheit(new Celsius.Build('F', 102, new BigDecimal("20.3"),
					df.parse("2018-02-25T14:38:25"), df.parse("2018-02-25T14:38:25")).build()));

			temps.put(103, new Celsius.Build('C', 103, new BigDecimal("17.8"), df.parse("2018-02-26T119:09:45"),
					df.parse("2018-02-26T119:09:45")).build());
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
		}
	}

//	public Map<Integer, Temp> getTemps() {
//		return temps;
//	}

	public AtomicInteger getAtomicInteger() {
		return atomicInteger;
	}
	
	public Date getCreateDate() {

		return new Date(1524325034683L);
	}

}
