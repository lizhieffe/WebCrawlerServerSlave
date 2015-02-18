package com.zl.tasks;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.zl.abstracts.AFutureTask;
import com.zl.abstracts.AFutureTaskCallback;
import com.zl.abstracts.AJob;
import com.zl.jobs.WebCrawlingJobFactory;
import com.zl.managers.JobManager;

public class CrawlWebContentTask extends AFutureTask <List<URL>> {

	private String content;
	private int depth;
	private JobManager jobManager;
	
	public CrawlWebContentTask() {
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
				Thread.currentThread().setPriority(10);
				return CrawlWebContentTaskHelper.getContainedURL(content);
			}
		};
	}

	private AFutureTaskCallback <List<URL>> getCallback() {
		return new AFutureTaskCallback<List<URL>>(id) {
			@Override
			public void onSuccess(List<URL> result) {
				super.onSuccess(result);
				if (depth >= 1) {
					List<AJob> jobs = new ArrayList<AJob>();
					for (URL url : result)
						jobs.add(WebCrawlingJobFactory.create(url.toString(), depth));
					jobManager.addAllJobsToReport(jobs);
				}
			}
			
			@Override
			public void onFailure(Throwable thrown) {
				super.onFailure(thrown);
			}
		};
	}
}
