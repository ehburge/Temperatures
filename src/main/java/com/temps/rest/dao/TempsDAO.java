package com.temps.rest.dao;

import java.util.Iterator;

import com.temps.rest.model.Celsius;
import com.temps.rest.model.Temp;
import com.temps.rest.model.TempScale;

public interface TempsDAO {

	Iterator<Temp> temps();

	Iterator<Temp> tempsVolume();

	TempScale get(Integer id);

	Temp create(Temp temp);

	TempScale delete(Integer id);

	TempScale update(Integer id, Celsius temp);

}