package com.zl.services;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.zl.utils.AppProperties;
import com.zl.utils.SimpleLogger;
import com.zl.jobs.WebCrawlingJob;
import com.zl.abstracts.AJob;
import com.zl.abstracts.AService;
import com.zl.interfaces.IReportJobService;

@Service
@Scope(value="prototype")
public class ReportJobService extends AService implements IReportJobService {

	private static int count = 0;
	private int i;
	private AJob job;
	
	public ReportJobService() {
		i = ++count;
	}
	
	@Async
	@Override
	public void reportJob(AJob job) {
		this.job = job;
		SimpleLogger.info("[" + i +"] Slave reported job to master [" + ((WebCrawlingJob)job).getUrl());
		this.start();
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	public String getUri() {
		return "/addmasterjob";
	}

	@Override
	public String constructRequestUrl() {
		return "http://" + AppProperties.getInstance().get("master.ip")+ ":" 
				+ AppProperties.getInstance().get("master.port") + getUri();
	}

	@Override
	public HttpEntity<String> constructRequestHttpEntity() {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		JSONObject item = new JSONObject();
		item.put("type", "webcrawling");
		item.put("url", ((WebCrawlingJob)job).getUrl().toString());
		item.put("depth", Integer.valueOf(((WebCrawlingJob)job).getDepth()));
		return new HttpEntity<String>(item.toString(), header);
	}

	@Override
	public void onSuccess(ResponseEntity<String> response) {
		SimpleLogger.info("[" + i +"] Success! Slave reported job to master [" + ((WebCrawlingJob)job).getUrl());
	}

	@Override
	public void onFailure(ResponseEntity<String> response) {
		SimpleLogger.info("[" + i +"] Success! Slave reported job to master [" + ((WebCrawlingJob)job).getUrl());
	}
	
}
