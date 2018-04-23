package com.temps.rest.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.temps.rest.model.Celsius;
import com.temps.rest.model.Fahrenheit;
import com.temps.rest.model.Temp;
import com.temps.rest.model.TempScale;

@Component("tempsDAOProto")
public class TempsDAOProto implements TempsDAO {

	@Autowired
	TempsDASetup tempsDASetup;

	TempsDAOProto() {
	}

	public Iterator<Temp> temps() {
		return tempsDASetup.values().iterator();
	}

	/**
	 * This creates a large numer of temperature objects (100)
	 * then invokes the temps iterator.
	 * 
	 * This proves that an iterator can be used for high volume responses.
	 * 
	 * This is a scalable approach because it does not load collection into memory.
	 * Instead, the iterator is used, which can be abstracted to iterate a ResultSet.
	 */
	@Override
	public Iterator<Temp> tempsVolume() {

		Random rnd = new Random();
		Character tempC;
		int temp;

		for (int i = 0; i < 100; i++) {
			tempC = (rnd.nextBoolean()) ? 'F' : 'C';

			int key = tempsDASetup.getAtomicInteger().incrementAndGet();
			temp = rnd.nextInt(40 - -10 + 1) + -10;

			Celsius cel = new Celsius.Build(tempC, key, new BigDecimal(temp), new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis())).build();
			if (tempC.equals('F')) {
				tempsDASetup.put(key, new Fahrenheit(cel));
			} else {
				tempsDASetup.put(key, cel);
			}
		}

		return temps();

	}

	@Override
	public TempScale get(Integer id) {
		TempScale ts = new TempScale();

		Temp t = tempsDASetup.get(id);

		ts.set_Celsius(t.objCelsius());

		if (t instanceof Fahrenheit) {
			ts.set_tempScale('F');
		} else {
			ts.set_tempScale('C');
		}
		return ts;
	}

	@Override
	public Temp create(Temp temp) {

		temp.setId(tempsDASetup.getAtomicInteger().incrementAndGet());
		tempsDASetup.put(temp.getId(), temp);
		return temp;
	}

	@Override
	public TempScale delete(Integer id) {

		TempScale ts = null;
		if (tempsDASetup.containsKey(id)) {
			Temp temp = tempsDASetup.remove(id);
			ts = new TempScale();
			ts.set_Celsius(temp.objCelsius());

			if (temp instanceof Fahrenheit) {
				ts.set_tempScale('F');
			} else {
				ts.set_tempScale('C');
			}
		}
		return ts;
	}

	@Override
	public TempScale update(Integer id, Celsius pTemp) {

		if (tempsDASetup.containsKey(id)) {
			Temp t = tempsDASetup.get(id);
			t.setCelsius(pTemp.getCelsius());
			t.setUpdate_date(new Date(System.currentTimeMillis()));
			TempScale ts = new TempScale();
			if (t instanceof Fahrenheit) {
				ts.set_tempScale('F');
				ts.set_Celsius(t.objCelsius());
			} else {
				ts.set_tempScale('C');
				ts.set_Celsius(t.objCelsius());
			}
			return ts;
		}
		return null;
	}

}
