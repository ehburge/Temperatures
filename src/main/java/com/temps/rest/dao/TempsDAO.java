package com.temps.rest.dao;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.temps.rest.model.CelsiusAll;

public interface TempsDAO {
	
	Collection<CelsiusAll> tempsRange(Date fromDate, Date toDate);

	Iterator<CelsiusAll> tempsAll();

	CelsiusAll get(Integer id);

	/**
	 * tempScale is 'C' or 'F'
	 * @param tempScale
	 * @param temp
	 * @return
	 */
	CelsiusAll create(CelsiusAll celsius);

	CelsiusAll delete(Integer id);

	CelsiusAll update(Integer id, CelsiusAll temp);

}