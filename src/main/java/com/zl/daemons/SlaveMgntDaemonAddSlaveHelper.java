package com.zl.daemons;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import utils.AppProperties;
import utils.ConfigUtil;
import utils.SimpleLogger;

import com.zl.util.ResponseUtil;

public class SlaveMgntDaemonAddSlaveHelper {
	
	private static String URI =  "/addslave";

	public void add() {
		SimpleLogger.info("[AddSlave] Adding self to master as slave");
		AsyncRestTemplate rest = new AsyncRestTemplate();
		ListenableFuture<ResponseEntity<String>> future = rest.exchange(constructRequestUrl(),
				HttpMethod.POST, constructRequestHttpEntity(), String.class);
		future.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
			@Override
			public void onSuccess(ResponseEntity<String> result) {
				if (!ResponseUtil.succeed(result)) {
					SimpleLogger.info("[AddSlave] Add self to master as slave fails");
					SlaveMgntDaemon.getInstance().onAddSlaveFail();
				}
				else
					SimpleLogger.info("[AddSlave] Add self to master as slave succeeds");
			}
			@Override
			public void onFailure(Throwable ex) {
				SlaveMgntDaemon.getInstance().onAddSlaveFail();
			}
		});
	}
	
	private String constructRequestUrl() {
		String url = "http://" + AppProperties.getInstance().get("master.ip") + ":"
				+ AppProperties.getInstance().get("master.port") + URI;
		return url;
	}
	
	private HttpEntity<String> constructRequestHttpEntity() {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		JSONObject item = new JSONObject();
		item.put("ip", ConfigUtil.getLocalIp());
		item.put("port", Integer.parseInt(AppProperties.getInstance().get("server.port")));
		return new HttpEntity<String>(item.toString(), header);
	}
}
