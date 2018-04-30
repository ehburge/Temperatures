package com.temps.rest.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.temps.rest.model.CelsiusAll;

@Component("tempsDASetup")
public class TempsDASetup {

	private Map<Integer, CelsiusAll> temps = new HashMap<>();

	public final static AtomicInteger atomicInteger = new AtomicInteger(500);

	public TempsDASetup() {
		super();
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

	private Map<String, String> sql = new HashMap<>();

	String line = null;

	public void dataInit() {

		temps.clear();

		InputStream in =
				this.getClass().getClassLoader().getResourceAsStream("data.sql");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			while ((line = br.readLine()) != null) {
				parse();
				addCalsiusAll();
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

	public void addCalsiusAll() {
		final Pattern p = Pattern.compile("([\'.])(\\\\?.)*?\\1");
		Matcher m;
		Set<Entry<String, String>> kvs = sql.entrySet();

		for (Entry<String, String> entry : kvs) {
			//String key = entry.getKey();
			String val = entry.getValue();
			//String[] keys = key.split(",");
			String[] vals = val.split(",");
			String[] tempScale = removeAposts(vals[1]).split("_");
			String crtDate = null;
			m = p.matcher(vals[2]);
			if (m.find()) {
				crtDate = m.group(0);
				crtDate = removeAposts(crtDate);
			}
			String updDate = null;
			m = p.matcher(vals[3]);
			if (m.find()) {
				updDate = m.group(0);
				updDate = removeAposts(updDate);
			}
			CelsiusAll ca =
					new CelsiusAll.BuildPost(tempScale[1].charAt(0), new BigDecimal(tempScale[0])).setCreateDate(crtDate).setUpdateDate(updDate).setId(new Integer(vals[0])).build();
			temps.put(ca.getId(), ca);
		}
		return;
	}

	private String removeAposts(String str) {
		StringBuilder sb = new StringBuilder(str);
		sb.deleteCharAt(0);
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	final String regex =
			"\\bINSERT\\s+INTO\\s+\\S+\\s*\\(([^)]+)\\)\\s*VALUES\\s*\\(([^)]+)\\)";
	// final String regex = "^(INSERT INTO)(?:[^;']|(?:'[^']+'))+;\\s*$";
	final Pattern p =
			Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);

	private void parse() {
		if (line == null) {
			System.out.println("No String to parse for SQL statements!");
			return;
		}

		Matcher m = p.matcher(line);
		while (m.find()) {
			String key = m.group(1);
			String body = m.group(2);
			System.out.println("Adding " + key + " to SQL map.");
			sql.put(key, body.trim());
		}
	}

	public static void main(String[] args) {

		TempsDASetup daSetup = new TempsDASetup();
		daSetup.dataInit();

	}
}
