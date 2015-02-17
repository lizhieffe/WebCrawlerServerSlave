package com.zl.daemons;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.zl.interfaces.ICrawlWebContentService;
import com.zl.jobs.WebCrawlingJob;

@Component
public class CrawlWebDaemonHelper {
	
	/**
	 * getBean() method together with "prototype" scope bean make sure each service is a new bean
	 */
	
	@Autowired
	private ApplicationContext appContext;
	
	public void crawlWeb(WebCrawlingJob job) {
		URL url = job.getUrl();
		int depth = job.getDepth();
		appContext.getBean(ICrawlWebContentService.class).crawlWebContent(url, depth);
	}
}
