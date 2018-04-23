package com.temps.rest.model;

import java.math.BigDecimal;
import java.util.Date;

public class Celsius implements Temp {

	private char tempScale;
	private Integer id;
	private BigDecimal celsius;
	private Date create_date;
	private Date update_date;

	
	public Celsius() {
		super();
	}

	@Override
	public Celsius objCelsius() {
		return this;
	}
	
	public char tempScale() {
		return tempScale;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getCelsius() {
		return celsius;
	}

	public void setCelsius(BigDecimal celsius) {
		this.celsius = celsius;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	private Celsius(Build build) {
		this.tempScale = build.tempScale;
		this.id = build.id;
		this.celsius = build.celsius;
		this.create_date = build.create_date;
		this.update_date = build.update_date;
	}

	public static class Build {

		private char tempScale;
		private int id;
		private BigDecimal celsius;
		private Date create_date;
		private Date update_date;

		public Build(char tempScale, int id, BigDecimal celsius, Date create_date, Date update_date) {
			this.tempScale = tempScale;
			this.id = id;
			this.celsius = celsius;
			this.create_date = create_date;
			this.update_date = update_date;
		}

		public Build(int id) {
			this.id = id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setCelsius(BigDecimal celsius) {
			this.celsius = celsius;
		}

		public void setCreate_date(Date create_date) {
			this.create_date = create_date;
		}

		public void setUpdate_date(Date update_date) {
			this.update_date = update_date;
		}

		public Celsius build() {
			return new Celsius(this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

}
