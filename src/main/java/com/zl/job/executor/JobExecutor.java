package com.zl.job.executor;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import utils.SimpleLogger;
import Job.WebCrawlingJob;
import Job.WebCrawlingJobFactory;
import abstracts.AFutureTaskCallback;

import com.zl.daemons.ThreadPoolDaemon;
import com.zl.job.manager.JobManager;
import com.zl.tasks.WebCrawlingTask;

public class JobExecutor {
	
	@Autowired
	public ThreadPoolDaemon threadPoolDaemon;
	
	private static JobExecutor instance;
	private JobManager jobManager;
	
	private JobExecutor() {
		this.jobManager = JobManager.getInstance();
	}
	
	synchronized public static JobExecutor getInstance() {
		if (instance == null)
			instance = new JobExecutor();
		return instance;
	}
	
	public void execute(final WebCrawlingJob job) {
		if (job == null)
			return;
		SimpleLogger.info("[Execution] Executing job: " + job.getUrl().toString());
		WebCrawlingTask task = new WebCrawlingTask(threadPoolDaemon.getExecutorService(), job.getUrl());
		task.startWithCallback(new AFutureTaskCallback<List<URL>>() {
			@Override
			public void onSuccess(List<URL> result) {
				super.onSuccess(result);
				for (URL url : result) {
					if (job.getDepth() > 1) {
						WebCrawlingJob newJob = WebCrawlingJobFactory.create(url.toString(), job.getDepth() - 1);
						jobManager.addJobToReport(newJob);
					}
				}
				SimpleLogger.info("[Execution] Execution done. URL found: " + result.size());
			}

			@Override
			public void onFailure(Throwable thrown) {
				super.onFailure(thrown);
				jobManager.moveJobToWaitingStatus(job);
			}
		});
	}
}
