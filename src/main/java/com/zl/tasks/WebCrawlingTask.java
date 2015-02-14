package com.zl.tasks;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import abstracts.AFutureTask;

public class WebCrawlingTask extends AFutureTask <List<URL>> {

	private URL url;
	
	public WebCrawlingTask(ExecutorService es, final URL url) {
		super(es);
		this.url = url;
		this.initCallable();
	}
	
	private void initCallable() {
		this.callable = new Callable<List<URL>>() {
			@Override
			public List<URL> call() throws Exception {
				return WebCrawlingTaskHelper.getContainedURL(url);
			}
		};
	}
}
