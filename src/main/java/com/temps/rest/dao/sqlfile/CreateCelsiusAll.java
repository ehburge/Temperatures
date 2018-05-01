package com.temps.rest.dao.sqlfile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.temps.rest.model.CelsiusAll;

public class CreateCelsiusAll implements CreateObjFromInsertLine<CelsiusAll> {

	private final Map<String, LoadCelsiusAll> loadMap = new HashMap<>();
	final Pattern timestampPattern = Pattern.compile("([\'.])(\\\\?.)*?\\1");
	final Pattern removeApostsPattern =  Pattern.compile("/'+/g");
	private final String[] cols =
			{ "id", "tempscale", "celsius", "create_date", "update_date" };

	public CreateCelsiusAll() {
		super();
		loadMap.put("id", new Id());
		loadMap.put("tempscale", new TempScale());
		loadMap.put("celsius", new TempCelsius());
		loadMap.put("create_date", new CreateDate());
		loadMap.put("update_date", new UpdateDate());

	}

	@Override
	public CelsiusAll makeObj(JSONObject jsonObject) {
		CelsiusAll cAll = new CelsiusAll();

		try {
			for (String colName : cols) {
				LoadCelsiusAll loadCelsiusAll = loadMap.get(colName);
				loadCelsiusAll.setCelsiusAll(cAll);

				String colVal = (String) jsonObject.get(colName);

				loadCelsiusAll.setValue(colVal);
			}
			return cAll;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	String pullOutDate(String fromInsLine) {

		String[] sa = fromInsLine.split("\\'");
		String[] dot = sa[1].split("\\.");
		return dot[0];
	}

	interface LoadCelsiusAll {

		void setCelsiusAll(CelsiusAll ca);

		void setValue(String val);
	}

	class Id implements LoadCelsiusAll {

		CelsiusAll ca;

		@Override
		public void setCelsiusAll(CelsiusAll ca) {
			this.ca = ca;
		}

		@Override
		public void setValue(String val) {
			ca.setId(new Integer(val));
		}
	}

	class TempScale implements LoadCelsiusAll {

		CelsiusAll ca;

		@Override
		public void setCelsiusAll(CelsiusAll ca) {
			this.ca = ca;
		}

		@Override
		public void setValue(String val) {
			ca.setTempScale(val.charAt(0));
		}
	}

	class TempCelsius implements LoadCelsiusAll {

		CelsiusAll ca;

		@Override
		public void setCelsiusAll(CelsiusAll ca) {
			this.ca = ca;
		}

		@Override
		public void setValue(String val) {
			ca.setCelsius(new BigDecimal(val));
		}
	}

	class CreateDate implements LoadCelsiusAll {

		CelsiusAll ca;

		@Override
		public void setCelsiusAll(CelsiusAll ca) {
			this.ca = ca;
		}

		@Override
		public void setValue(String val) {
			
			String crtDate = pullOutDate(val);
			ca.setCreate_date(crtDate);
		}
	}

	class UpdateDate implements LoadCelsiusAll {

		CelsiusAll ca;

		@Override
		public void setCelsiusAll(CelsiusAll ca) {
			this.ca = ca;
		}

		@Override
		public void setValue(String val) {
			
			String updDate = pullOutDate(val);
			ca.setUpdate_date(updDate);
		}
	}
}
