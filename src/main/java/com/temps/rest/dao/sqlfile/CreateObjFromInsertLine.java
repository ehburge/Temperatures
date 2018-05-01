package com.temps.rest.dao.sqlfile;

import org.json.JSONObject;

public interface CreateObjFromInsertLine<T> {
	
	public T makeObj(JSONObject jsonObject);

}
