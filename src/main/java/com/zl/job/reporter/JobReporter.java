package com.zl.job.reporter;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import utils.SimpleLogger;
import Job.WebCrawlingJob;
import abstracts.AJob;

public class JobReporter {
	
	private static JobReporter instance;
				
	private JobReporter() {
	}
	
	synchronized public static JobReporter getInstance() {
		if (instance == null)
			instance = new JobReporter();
		return instance;
	}
	
	public void report(final AJob job) {		
		/**
		 * TODO: change to AsyncRestTemplate
		 * http://javattitude.com/2014/04/20/using-spring-4-asyncresttemplate/
		 */
		SimpleLogger.info("[Reporter] Reporting to MASTER about job: URL=" 
				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
				+ ((WebCrawlingJob)job).getDepth());
		
		AsyncRestTemplate rest = new AsyncRestTemplate();
		ListenableFuture<ResponseEntity<String>> future = rest.exchange(JobReporterHelper.constructRequestUrl(),
				HttpMethod.POST, JobReporterHelper.constructRequestHttpEntity(job), String.class);
		SimpleLogger.info("[Reporter] Report job to MASTER about job: URL=" 
				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
				+ ((WebCrawlingJob)job).getDepth());
		try {
			ResponseEntity<String> response = future.get();
			SimpleLogger.info("[Reporter] Report finished (" + response.getBody() + ") to MASTER about job: URL=" 
				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
				+ ((WebCrawlingJob)job).getDepth());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
