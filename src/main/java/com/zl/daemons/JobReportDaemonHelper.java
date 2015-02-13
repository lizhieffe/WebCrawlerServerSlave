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
import utils.SimpleLogger;
import Job.WebCrawlingJob;
import abstracts.AJob;

public class JobReportDaemonHelper {
	
	private static String URI =  "/addmasterjob";
	
	JobReportDaemonHelper() {
	}
	
	public void report(final AJob job) {		
		SimpleLogger.info("[Reporter] Reporting to MASTER about job: URL=" 
				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
				+ ((WebCrawlingJob)job).getDepth());
		
		AsyncRestTemplate rest = new AsyncRestTemplate();
		ListenableFuture<ResponseEntity<String>> future = rest.exchange(constructRequestUrl(),
				HttpMethod.POST, constructRequestHttpEntity(job), String.class);
		SimpleLogger.info("[Reporter] Report job to MASTER about job: URL=" 
				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
				+ ((WebCrawlingJob)job).getDepth());
		future.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
			@Override
			public void onSuccess(ResponseEntity<String> result) {
				SimpleLogger.info("[Reporter] Report finished (" + result.getBody() + ") to MASTER about job: URL=" 
						+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
						+ ((WebCrawlingJob)job).getDepth());
			}
			@Override
			public void onFailure(Throwable e) {
				e.printStackTrace();
			}
		});
	}
	
	static String constructRequestUrl() {
		return "http://" + AppProperties.getInstance().get("master.ip")+ ":" 
				+ AppProperties.getInstance().get("master.port") + URI;
	}
	
	static HttpEntity<String> constructRequestHttpEntity(AJob job) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		JSONObject item = new JSONObject();
		item.put("type", "webcrawling");
		item.put("url", ((WebCrawlingJob)job).getUrl().toString());
		item.put("depth", Integer.valueOf(((WebCrawlingJob)job).getDepth()));
		return new HttpEntity<String>(item.toString(), header);
	}
}
