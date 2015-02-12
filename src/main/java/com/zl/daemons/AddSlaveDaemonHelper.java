package com.zl.daemons;

import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import utils.AppProperties;
import utils.ConfigUtil;
import utils.SimpleLogger;

import com.zl.util.ResponseUtil;
public class AddSlaveDaemonHelper {
	
	private static String URI =  "/addslave";

	public static void add() {
		SimpleLogger.info("[AddSlave] Add self to master as slave");
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
	
	private static String constructRequestUrl() {
		return "http://" + AppProperties.getInstance().get("master.ip") + ":"
				+ AppProperties.getInstance().get("master.port") + URI;
	}
	
	private static MultiValueMap<String, Object> constructRequestJson() {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("ip", ConfigUtil.getLocalIp());
		map.add("port", AppProperties.getInstance().get("server.port"));
		return map;
	}
	
	private static HttpEntity<MultiValueMap<String, Object>> constructRequestHttpEntity() {
		return new HttpEntity<MultiValueMap<String, Object>>(constructRequestJson(), new HttpHeaders());
	}
}
