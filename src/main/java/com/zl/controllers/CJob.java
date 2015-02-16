package com.zl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zl.resources.RSimpleResponse;
import com.zl.resources.RWebCrawlingJob;
import com.zl.resources.SimpleResponseFactory;
import com.zl.utils.SimpleLogger;
import com.zl.jobs.JobHelper;
import com.zl.jobs.WebCrawlingJobFactory;
import com.zl.abstracts.AJob;

import com.zl.managers.JobManager;

@RestController
public class CJob {

	@Autowired
	public JobManager jobManager;
	
	@RequestMapping(value = "/addslavejob", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse addSlaveJob(@RequestBody RWebCrawlingJob reqJob) {
		
		SimpleLogger.info(this.getClass(), "[Request] URI=/addslavejob, URL=" + reqJob.getUrl() + ", Depth=" + reqJob.getDepth());

		String url = reqJob.getUrl();
		int depth = reqJob.getDepth();
		String type = reqJob.getType();
		
		if (type == null || url == null || !JobHelper.isValidTypeNameForWebCrawlingJob(type) 
				|| !JobHelper.isValidUrl(url) || !JobHelper.isValidDepth(String.valueOf(depth))) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		
		AJob job = WebCrawlingJobFactory.create(url, depth);
		boolean addJobSucceed = jobManager.addJob(job);
		if (!addJobSucceed) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot add job");
		}
		
		return SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();

	}
}
