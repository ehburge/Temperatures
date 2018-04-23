package com.temps.rest.model;

import java.math.BigDecimal;
import java.util.Date;

public class Fahrenheit implements Temp {

	private Celsius _Celsius;

	public Fahrenheit(Celsius _Celsius) {
		super();
		this._Celsius = _Celsius;
	}

	public Celsius objCelsius() {
		return _Celsius;
	}

	public char tempScale() {
		return 'F';
	}

	public Integer getId() {
		return _Celsius.getId();
	}

	public BigDecimal getFahrenheit() {
		BigDecimal fahr = _Celsius.getCelsius().multiply(new BigDecimal("1.8")).add(new BigDecimal("32"));
		return fahr;
	}

	public void setCelsius(BigDecimal celsius) {
		_Celsius.setCelsius(celsius);
	}

	public Date getCreate_date() {
		return _Celsius.getCreate_date();
	}

	public Date getUpdate_date() {
		return _Celsius.getUpdate_date();
	}

	public void setUpdate_date(Date update_date) {
		_Celsius.setUpdate_date(update_date);
	}

	public int hashCode() {
		return _Celsius.hashCode();
	}

	public boolean equals(Object obj) {
		return _Celsius.equals(obj);
	}

	@Override
	public void setId(Integer id) {
		this._Celsius.setId(id);
	}
}
