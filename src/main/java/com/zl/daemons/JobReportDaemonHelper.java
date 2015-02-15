package com.zl.daemons;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import abstracts.AJob;

import com.zl.services.ReportJobService;

@Configuration
@EnableAsync
@EnableAutoConfiguration
public class JobReportDaemonHelper {
	
	public void reportJob(AJob job) {
		new ReportJobService().reportJob(job);
	}
	
//	private static String URI =  "/addmasterjob";
//	
//	JobReportDaemonHelper() {
//	}
//	
//	public void report(final AJob job) {		
//		SimpleLogger.info("[Reporter] Reporting to MASTER about job: URL=" 
//				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
//				+ ((WebCrawlingJob)job).getDepth());
//		
//		AsyncRestTemplate rest = new AsyncRestTemplate();
//		ListenableFuture<ResponseEntity<String>> future = rest.exchange(constructRequestUrl(),
//				HttpMethod.POST, constructRequestHttpEntity(job), String.class);
//		SimpleLogger.info("[Reporter] Report job to MASTER about job: URL=" 
//				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
//				+ ((WebCrawlingJob)job).getDepth());
//		future.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
//			@Override
//			public void onSuccess(ResponseEntity<String> result) {
//				SimpleLogger.info("[Reporter] Report finished (" + result.getBody() + ") to MASTER about job: URL=" 
//						+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
//						+ ((WebCrawlingJob)job).getDepth());
//			}
//			@Override
//			public void onFailure(Throwable e) {
//				e.printStackTrace();
//			}
//		});
//	}
//	
//	static String constructRequestUrl() {
//		return "http://" + AppProperties.getInstance().get("master.ip")+ ":" 
//				+ AppProperties.getInstance().get("master.port") + URI;
//	}
//	
//	static HttpEntity<String> constructRequestHttpEntity(AJob job) {
//		HttpHeaders header = new HttpHeaders();
//		header.setContentType(MediaType.APPLICATION_JSON);
//		JSONObject item = new JSONObject();
//		item.put("type", "webcrawling");
//		item.put("url", ((WebCrawlingJob)job).getUrl().toString());
//		item.put("depth", Integer.valueOf(((WebCrawlingJob)job).getDepth()));
//		return new HttpEntity<String>(item.toString(), header);
//	}
}
