package com.temps.rest.model;

import java.util.Collection;

//@XmlRootElement(name = "Celsiuss")
//@XmlAccessorType(XmlAccessType.FIELD)
public class Temps {

	// @XmlElementWrapper(name = "Celsiuss")
	// @XmlElement(name = "Celsius")
	private Collection<Temp> Celsiuss = null;

	public Collection<Temp> getTemps() {
		return Celsiuss;
	}

	public void setTemps(Collection<Temp> Celsiuss) {
		this.Celsiuss = Celsiuss;
	}

}
