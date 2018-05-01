package com.temps.rest.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.temps.rest.dao.sqlfile.CreateCelsiusAll;
import com.temps.rest.dao.sqlfile.CreateObjFromInsertLine;
import com.temps.rest.model.CelsiusAll;

@Component("tempsDASetup")
public class TempsDASetup {

	private Map<Integer, CelsiusAll> temps = new HashMap<>();

	public final static AtomicInteger atomicInteger = new AtomicInteger(500);

	private CreateObjFromInsertLine<CelsiusAll> objFromInsert;

	public TempsDASetup() {
		super();
		objFromInsert = new CreateCelsiusAll();
	}

	public Iterator<CelsiusAll> temps() {

		return temps.values().iterator();
	}

	public CelsiusAll get(Integer id) {
		CelsiusAll ca = temps.get(id);
		return ca;
	}

	public CelsiusAll create(CelsiusAll ca) {
		int id = atomicInteger.incrementAndGet();
		ca.setId(id);

		temps.put(id, ca);
		return temps.get(id);
	}

	public CelsiusAll update(Integer id, CelsiusAll celsius) {
		if (temps.containsKey(id)) {
			temps.put(id, celsius);
			return temps.get(id);
		}
		return null;
	}

	public CelsiusAll delete(Integer id) {
		return temps.remove(id);
	}

	public void dataInit() {

		temps.clear();

		InputStream in =
				this.getClass().getClassLoader().getResourceAsStream("data.sql");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String line = null;
			while ((line = br.readLine()) != null) {

				CelsiusAll ca = objFromInsert.makeObj(parse(line));

				temps.put(ca.getId(), ca);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// public void addCalsiusAll(Map<String, String> colValsMap) {
	// final Pattern p = Pattern.compile("([\'.])(\\\\?.)*?\\1");
	// Matcher m;
	//
	// String[] tempScale = removeAposts(vals[1]).split("_");
	// String crtDate = null;
	// m = p.matcher(vals[2]);
	// if (m.find()) {
	// crtDate = m.group(0);
	// crtDate = removeAposts(crtDate);
	// }
	// String updDate = null;
	// m = p.matcher(vals[3]);
	// if (m.find()) {
	// updDate = m.group(0);
	// updDate = removeAposts(updDate);
	// }
	// CelsiusAll ca =
	// new CelsiusAll.BuildPost(tempScale[1].charAt(0), new
	// BigDecimal(tempScale[0])).setCreateDate(crtDate).setUpdateDate(updDate).setId(new
	// Integer(vals[0])).build();
	// temps.put(ca.getId(), ca);
	//
	// return;
	// }

	final String regex =
			"\\bINSERT\\s+INTO\\s+\\S+\\s*\\(([^)]+)\\)\\s*VALUES\\s*\\(([^)]+)\\)";
	// final String regex = "^(INSERT INTO)(?:[^;']|(?:'[^']+'))+;\\s*$";
	final Pattern p =
			Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL
	);

	JSONObject parse(String line) {
		if (line == null) {
		System.out.println("No String to parse for SQL statements!");
			return null;
		}

		Matcher m = p.matcher(line);
		while (m.find()) {
			String key = m.group(1);
			String body = m.group(2);
			String[] keys = key.split(",");
			String[] vals = body.split(",");
			JSONObject json = new JSONObject();
			try {
				for (int i = 0; i < keys.length; i++) {
					if (keys[i].equals("celsius_tempscale")) {
						String[] ctKey = keys[i].split("_");
						String[] ctVal = removeAposts(vals[i]).split("_");
						json.put(ctKey[0], ctVal[0]);
						json.put(ctKey[1], ctVal[1]);
					} else {
						json.put(keys[i], vals[i]);
					}
				}
				return json;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}return null;

	}
	

	String removeAposts(String apostStr) {
		String noaposts = apostStr.replaceAll("'", "");
			return noaposts;
	}

	public static void main(String[] args) {

		TempsDASetup daSetup = new TempsDASetup();
		daSetup.dataInit();

	}
}
