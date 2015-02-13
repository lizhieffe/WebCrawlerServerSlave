package com.zl.daemons;

import java.util.concurrent.ExecutionException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import utils.AppProperties;
import utils.SimpleLogger;
import utils.ConfigUtil;
import com.zl.util.ResponseUtil;

public class AddSlaveDaemonHelper {
	
	private static String URI =  "/addslave";

	public void add() {
		SimpleLogger.info("[AddSlave] Adding self to master as slave");
		AsyncRestTemplate rest = new AsyncRestTemplate();
		ListenableFuture<ResponseEntity<String>> future = rest.exchange(constructRequestUrl(),
				HttpMethod.POST, constructRequestHttpEntity(), String.class);
		try {
			ResponseEntity<String> response = future.get();
			if (!ResponseUtil.succeed(response)) {
				SimpleLogger.info("[AddSlave] Add self to master as slave fails");
				AddSlaveDaemon.getInstance().onAddSlaveFail();
			}
			else
				SimpleLogger.info("[AddSlave] Add self to master as slave succeeds");
		} catch (InterruptedException e) {
//			e.printStackTrace();
			AddSlaveDaemon.getInstance().onAddSlaveFail();
		} catch (ExecutionException e) {
//			e.printStackTrace();
			AddSlaveDaemon.getInstance().onAddSlaveFail();
		}
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
