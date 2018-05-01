package com.temps.rest.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.temps.rest.model.CelsiusAll.BuildPost;

public class Celsius implements Temp {

	/**
	 * Date format is yyyy-MM-dd'T'HH:mm:ss
	 */
	public final static SimpleDateFormat DateFormatter =
			new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

	private CelsiusEntity celsiusEntity;

	public Celsius() {
		super();

		this.celsiusEntity = new CelsiusEntity();
	}

	public Celsius(CelsiusEntity celsiusEntity) {
		super();
		this.celsiusEntity = celsiusEntity;
	}

	@Override
	public Celsius objCelsius() {
		return new Celsius(this.objCelsiusEntity());
	}

	public CelsiusEntity objCelsiusEntity() {
		return this.celsiusEntity;
	}

	public Character tempScale() {
		String[] ts;
		try {
			ts = celsiusEntity.getCelsius_tempscale().split("_");
			return new Character(ts[1].charAt(0));
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public void setTempScale(Character tempScale) {
		celsiusEntity.setCelsius_tempscale(
				CelsiusEntity.manageTempScaleString(
						celsiusEntity.getCelsius_tempscale(), null, tempScale
				)
		);
	}

	public Integer getId() {
		return celsiusEntity.getId();
	}

	public void setId(Integer id) {
		// This set is used by json
		// celsiusEntity cannot have a set method
		celsiusEntity.id = id;
	}

	public BigDecimal getCelsius() {
		String[] ts;
		try {
			ts = celsiusEntity.getCelsius_tempscale().split("_");
			return new BigDecimal(ts[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public void setCelsius(BigDecimal celsius) {
		celsiusEntity.setCelsius_tempscale(
				CelsiusEntity.manageTempScaleString(
						celsiusEntity.getCelsius_tempscale(), celsius, null)
				);
	}

	public String getCreate_date() {

		return DateFormatter.format(celsiusEntity.getCreate_date());
	}

	public void setCreate_date(String create_date) {
		try {
			this.celsiusEntity
					.setCreate_date(Celsius.DateFormatter.parse(create_date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getUpdate_date() {
		return DateFormatter.format(celsiusEntity.getUpdate_date());
	}

	public void setUpdate_date(String updDateStr) {
		try {
			this.celsiusEntity
					.setUpdate_date(Celsius.DateFormatter.parse(updDateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Celsius(Build build) {
		this.celsiusEntity = new CelsiusEntity();
		this.celsiusEntity.setCelsius_tempscale(
				CelsiusEntity.manageTempScaleString(
						celsiusEntity.getCelsius_tempscale(), build.celsius,
						build.tempScale)
				);
		try {
			this.celsiusEntity.setCreate_date(
					Celsius.DateFormatter.parse(build.create_date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			this.celsiusEntity.setUpdate_date(
					Celsius.DateFormatter.parse(build.update_date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	Celsius(BuildPost buildPost) {
		this.celsiusEntity = new CelsiusEntity();
		this.celsiusEntity.setCelsius_tempscale(
				CelsiusEntity.manageTempScaleString(
						celsiusEntity.getCelsius_tempscale(), buildPost.celsius,
						buildPost.tempScale)
				);
		try {
			this.celsiusEntity.setCreate_date(
					Celsius.DateFormatter.parse(buildPost.create_date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			this.celsiusEntity.setUpdate_date(
					Celsius.DateFormatter.parse(buildPost.update_date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static class Build {

		private char tempScale;
		private BigDecimal celsius;
		private String create_date;
		private String update_date;

		public Build(char tempScale, BigDecimal celsius) {
			this.tempScale = tempScale;
			this.celsius = celsius;
			Date bldDate = new Date(System.currentTimeMillis());
			this.create_date = Celsius.DateFormatter.format(bldDate);
			this.update_date = Celsius.DateFormatter.format(bldDate);
		}

		public void setCelsius(BigDecimal celsius) {
			this.celsius = celsius;
		}

		public void setTempScale(Character tempScale) {
			this.tempScale = tempScale;
		}

		// public void setCreate_date(Date create_date) {
		// this.create_date = create_date;
		// }
		//
		// public void setUpdate_date(Date update_date) {
		// this.update_date = update_date;
		// }

		public Celsius build() {
			return new Celsius(this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
				prime * result + ((celsiusEntity.getId() == null) ? 0
						: celsiusEntity.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Temp other = (Temp) obj;
		if (celsiusEntity.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!celsiusEntity.getId().equals(other.getId()))
			return false;
		return true;
	}

}
