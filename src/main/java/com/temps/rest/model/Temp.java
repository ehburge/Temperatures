package com.temps.rest.model;

import java.math.BigDecimal;
import java.util.Date;

public interface Temp {

	Integer getId();

	void setId(Integer id);

	void setCelsius(BigDecimal bigDecimal);

	void setUpdate_date(Date dt);

	Celsius objCelsius();
}
