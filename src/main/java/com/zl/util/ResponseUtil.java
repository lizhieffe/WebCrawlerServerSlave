package com.zl.util;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import utils.StringUtil;

public class ResponseUtil {
	public static boolean succeed(ResponseEntity<String> response) {
		if (response == null)
			return false;
		JSONObject body = null;
		if ((body = StringUtil.strToJson(response.getBody())) == null)
			return false;
		if (body.getJSONObject("error") == null)
			return false;
		String code = null;
		if ((code = body.getJSONObject("error").getString("code")) == null)
			return false;
		return code.equals("0");
	}
}
