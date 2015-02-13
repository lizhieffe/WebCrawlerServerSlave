package com.zl.util;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import utils.StringUtil;

public class ResponseUtil {
	public static boolean succeed(ResponseEntity<String> response) {
		if (response == null)
			return false;
		if (StringUtil.strToJson(response.getBody()) == null)
			return false;
		try {
			int code = StringUtil.strToJson(response.getBody()).getJSONObject("error").getInt("code");
			return code == 0;
		} catch(JSONException e) {
			return false;
		}
	}
}
