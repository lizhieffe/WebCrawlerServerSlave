package com.zl.job.reporter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import utils.ConfigUtil;
import abstracts.AJob;
import Job.WebCrawlingJob;

public class JobReporterHelper {
	
	private static String URI =  "/addmasterjob";
	
	static String constructRequestUrl() {
		return ConfigUtil.getMasterIp() + ":" + ConfigUtil.getMasterPort() + URI;
	}
	
	static MultiValueMap<String, Object> constructRequestJson(AJob job) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("type", "webcrawling");
		map.add("url", ((WebCrawlingJob)job).getUrl().toString());
		map.add("depth", Integer.valueOf(((WebCrawlingJob)job).getDepth()));
		return map;
	}
	
	static HttpEntity<MultiValueMap<String, Object>> constructRequestHttpEntity(AJob job) {
		return new HttpEntity<MultiValueMap<String, Object>>(constructRequestJson(job), new HttpHeaders());
	}
}
