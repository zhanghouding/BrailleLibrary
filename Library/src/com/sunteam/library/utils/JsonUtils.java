package com.sunteam.library.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

	public static int getInt(String json, String key) {
		int value = 0;
		try {
			JSONObject jsonObj = new JSONObject(json);
			value = jsonObj.getInt(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String getString(String json, String key) {
		String value = "";
		try {
			JSONObject jsonObj = new JSONObject(json);
			value = jsonObj.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static JSONObject getJsonObject(String json, String key) {
		JSONObject value = null;
		try {
			JSONObject jsonObj = new JSONObject(json);
			value = jsonObj.getJSONObject(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String addQuotation(String s) {
		return ("\"" + s + "\"");
	}
}
