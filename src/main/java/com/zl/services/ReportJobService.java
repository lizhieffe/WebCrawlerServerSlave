package com.zl.services;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import utils.AppProperties;
import Job.WebCrawlingJob;
import abstracts.AJob;
import abstracts.AService;

@Service
public class ReportJobService extends AService {

	private AJob job;
	
	@Async
	public void reportJob(final AJob job) {
		this.job = job;
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

	}

	@Override
	public void onFailure(ResponseEntity<String> response) {

	}
	
}
