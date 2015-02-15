package com.zl.daemons;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.zl.services.RemoveSlaveService;

@Configuration
@EnableAsync
@EnableAutoConfiguration
public class SlaveMgntDaemonRemoveSlaveHelper {
	
	public void remove() {
		new RemoveSlaveService().removeSlave();
	}
	
//	
//	private static String URI =  "/removeslave";
//
//	public void remove() {
//		SimpleLogger.info("[RemoveSlave] Removing self to master as slave");
//		AsyncRestTemplate rest = new AsyncRestTemplate();
//		ListenableFuture<ResponseEntity<String>> future = rest.exchange(constructRequestUrl(),
//				HttpMethod.POST, constructRequestHttpEntity(), String.class);
//		future.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
//			@Override
//			public void onSuccess(ResponseEntity<String> result) {
//				if (!ResponseUtil.isSuccess(result)) {
//					SimpleLogger.info("[RemoveSlave] Remove self to master as slave fails");
//					SlaveMgntDaemon.getInstance().onAddSlaveFailure();
//				}
//				else
//					SimpleLogger.info("[RemoveSlave] Remove self to master as slave succeeds");
//			}
//			@Override
//			public void onFailure(Throwable ex) {
//				SlaveMgntDaemon.getInstance().onAddSlaveFailure();
//			}
//		});
//	}
//	
//	private String constructRequestUrl() {
//		String url = "http://" + AppProperties.getInstance().get("master.ip") + ":"
//				+ AppProperties.getInstance().get("master.port") + URI;
//		return url;
//	}
//	
//	private HttpEntity<String> constructRequestHttpEntity() {
//		HttpHeaders header = new HttpHeaders();
//		header.setContentType(MediaType.APPLICATION_JSON);
//		JSONObject item = new JSONObject();
//		item.put("ip", ConfigUtil.getLocalIp());
//		item.put("port", Integer.parseInt(AppProperties.getInstance().get("server.port")));
//		return new HttpEntity<String>(item.toString(), header);
//	}
}
