package com.temps.rest.model;

public class TempScale {

	private Celsius _Celsius;
	
	private Character _tempScale;

	public TempScale() {
		super();
	}

	public Celsius get_Celsius() {
		return _Celsius;
	}

	public void set_Celsius(Celsius celsius) {
		this._Celsius = celsius;
	}

	public Character get_tempScale() {
		return _tempScale;
	}

	public void set_tempScale(Character tempScale) {
		this._tempScale = tempScale;
	}
	
}
