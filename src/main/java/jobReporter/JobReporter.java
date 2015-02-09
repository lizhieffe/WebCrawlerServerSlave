package jobReporter;
import org.springframework.web.client.RestTemplate;

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
//		WSRequestHolder holder = WS.url(JobReporterHelper.constructRequestUrl());
//		ObjectNode json = JobReporterHelper.constructRequestJson(job);
//		holder.post(json).onRedeem(new F.Callback<WSResponse>() {
//			@Override
//			public void invoke(WSResponse response) throws Throwable {
//				LogUtil.info("[Reporter] Reporting job:" + ((WebCrawlingJob)job).getUrl());
//				JsonNode responseJson = response.asJson();
//				if (responseJson.get("error") != null && responseJson.get("error").get("code").equals("0")) {
//					LogUtil.info("[Reporter] job id [" + job.getId() + "] is reported to master " 
//							+ ConfigUtil.getMasterIp() + ":" + ConfigUtil.getMasterPort());
//				}
//				/*
//				 * if dispatch failed, move the job back to waiting status
//				 */
//				else {
////					jobManager.moveJobToWaitingStatus(job);
//				}
//			}
//		});
		
		/**
		 * TODO: change to AsyncRestTemplate
		 * http://javattitude.com/2014/04/20/using-spring-4-asyncresttemplate/
		 */
		SimpleLogger.info("[Reporter] Reporting to MASTER about job: URL=" 
				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
				+ ((WebCrawlingJob)job).getDepth());
		RestTemplate rest = new RestTemplate();
		String result = rest.postForObject(JobReporterHelper.constructRequestUrl()
				, JobReporterHelper.constructRequestJson(job), String.class);
		SimpleLogger.info("[Reporter] Report finished to MASTER about job: URL=" 
				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
				+ ((WebCrawlingJob)job).getDepth());
	}
}
