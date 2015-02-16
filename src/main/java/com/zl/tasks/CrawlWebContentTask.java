package com.zl.tasks;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import Job.WebCrawlingJob;
import Job.WebCrawlingJobFactory;
import abstracts.AFutureTask;
import abstracts.AFutureTaskCallback;

import com.zl.job.manager.JobManager;

public class CrawlWebContentTask extends AFutureTask <List<URL>> {

	private String content;
	private int depth;
	private JobManager jobManager;
	
	public CrawlWebContentTask(ExecutorService es) {
		super(es);
	}
	
	/**
	 * @param content: the String of web content
	 * @param depth: the depth of the original job
	 */
	public void parseWebContent(String content, int depth, JobManager jobManager) {
		this.content = content;
		this.depth = depth;
		this.jobManager = jobManager;
		this.initCallable();
		this.startWithCallback(getCallback());
	}
	
	private void initCallable() {
		this.callable = new Callable<List<URL>>() {
			@Override
			public List<URL> call() throws Exception {
				return CrawlWebContentTaskHelper.getContainedURL(content);
			}
		};
	}

	private AFutureTaskCallback <List<URL>> getCallback() {
		return new AFutureTaskCallback<List<URL>>() {
			@Override
			public void onSuccess(List<URL> result) {
				super.onSuccess(result);
				for (URL url : result) {
					if (depth > 1) {
						WebCrawlingJob newJob = WebCrawlingJobFactory.create(url.toString(), depth);
						jobManager.addJobToReport(newJob);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable thrown) {
				super.onFailure(thrown);
			}
		};
	}
}
