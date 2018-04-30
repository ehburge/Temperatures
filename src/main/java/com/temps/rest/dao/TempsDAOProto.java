package com.temps.rest.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.temps.rest.model.CelsiusAll;

@Component("tempsDAOProto")
public class TempsDAOProto implements TempsDAO {

	TempsDASetup tempsDASetup;

	TempsDAOProto() {
		tempsDASetup = new TempsDASetup();
		tempsDASetup.dataInit();
	}

	@Override
	public Iterator<CelsiusAll> tempsAll() {
		
		return tempsDASetup.temps();
	}

	public void tempsVolume() {

		Random rnd = new Random();
		Character tempC;
		int temp;

		for (int i = 0; i < 100; i++) {
			tempC = (rnd.nextBoolean()) ? 'F' : 'C';
			temp = rnd.nextInt(40 - -10 + 1) + -10;

			CelsiusAll cel =
					new CelsiusAll.BuildPost(tempC, new BigDecimal(temp)).build();

			tempsDASetup.create(cel);

		}

	}

	@Override
	public Collection<CelsiusAll> tempsRange(Date fromDate, Date toDate) {
		
		List<CelsiusAll> lst = new ArrayList<CelsiusAll>();
		CelsiusAll ca =
				new CelsiusAll.BuildPost('C', new BigDecimal("16.3")).build();
		ca.setId(1);
		lst.add(ca);
		CelsiusAll ca2 =
				new CelsiusAll.BuildPost('F', new BigDecimal("14.7")).build();
		ca2.setId(2);
		lst.add(ca2);

		return lst;
	}

	@Override
	public CelsiusAll get(Integer id) {
		return tempsDASetup.get(id);
	}

	@Override
	public CelsiusAll create(CelsiusAll celPost) {

		return tempsDASetup.create(celPost);
	}

	@Override
	public CelsiusAll delete(Integer id) {
		return tempsDASetup.delete(id);
	}

	@Override
	public CelsiusAll update(Integer id, CelsiusAll celsius) {
		return tempsDASetup.update(id, celsius);
	}

}
