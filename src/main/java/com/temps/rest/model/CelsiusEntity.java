package com.temps.rest.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQuery(name = "findTempsRange", query = "SELECT t FROM CelsiusEntity t WHERE t.create_date >= :fromDt and t.create_date <= :toDt")
public class CelsiusEntity {
	
	public static String manageTempScaleString(String celsius_tempscale, BigDecimal bd, Character scale) {
		if (celsius_tempscale == null) {
			celsius_tempscale = " _ ";
		}
		String[] ts = celsius_tempscale.split("_");
		if (bd != null) {
			ts[0] = bd.toPlainString();
		}
		if (scale != null) {
			ts[1] = scale.toString();
		} else {
			scale = new Character(' ');
		}
		StringBuilder sb = new StringBuilder();
		sb.append(ts[0]).append("_").append(ts[1]);
		
		return sb.toString();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	private String celsius_tempscale;
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;

	public CelsiusEntity() {
		super();
	}

	public CelsiusEntity(Celsius celsius) throws java.text.ParseException {
		super();
		this.id = celsius.getId();
		this.celsius_tempscale = celsius.getCelsius().toPlainString() + "_" + celsius.tempScale();
		this.create_date = Celsius.DateFormatter.parse(celsius.getCreate_date());
		this.update_date = Celsius.DateFormatter.parse(celsius.getUpdate_date());
	}

	public Integer getId() {
		return id;
	}

	public String getCelsius_tempscale() {
		return celsius_tempscale;
	}

	public void setCelsius_tempscale(String celsius_scale) {
		this.celsius_tempscale = celsius_scale;
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

	public Celsius getCelsiusObj() {
		return new Celsius(this);
	}

}
