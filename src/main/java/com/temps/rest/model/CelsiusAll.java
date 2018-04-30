package com.temps.rest.model;

import java.math.BigDecimal;
import java.util.Date;

public class CelsiusAll extends Celsius {

	public CelsiusAll() {
		super();
	}

	public CelsiusAll(CelsiusEntity celsiusEntity) {
		super(celsiusEntity);
	}

	private CelsiusAll(BuildPost build) {
		super();
		String temp_scale =
				CelsiusEntity.manageTempScaleString(
						objCelsiusEntity().getCelsius_tempscale(),
						build.celsius, build.tempScale
				);
		String[] cs = temp_scale.split("_");
		this.setCelsius(new BigDecimal(cs[0]));
		this.setTempScale(new Character(cs[1].charAt(0)));
		this.setCreate_date(build.create_date);
		this.setUpdate_date(build.update_date);
		if (build.id != null) {
			this.setId(build.id);
		}
	}

	public static class BuildPost {

		Integer id;
		char tempScale;
		BigDecimal celsius;
		String create_date;
		String update_date;

		public BuildPost(char tempScale, BigDecimal celsius) {
			this.tempScale = tempScale;
			this.celsius = celsius;
			Date dt = new Date(System.currentTimeMillis());
			this.create_date = Celsius.DateFormatter.format(dt);
			this.update_date = Celsius.DateFormatter.format(dt);
		}
		
		public BuildPost setCreateDate(String strCrtDt) {
			this.create_date = strCrtDt;
			return this;
		}
		
		public BuildPost setUpdateDate(String strUpdDt) {
			this.update_date = strUpdDt;
			return this;
		}
		
		public BuildPost setId(int id) {
			this.id = id;
			return this;
		}

		public CelsiusAll build() {
			return new CelsiusAll(this);
		}
	}

	public Character getTempScale() {

		return tempScale();
	}

}
