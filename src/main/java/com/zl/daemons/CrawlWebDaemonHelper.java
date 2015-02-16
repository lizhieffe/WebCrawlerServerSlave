package com.zl.daemons;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zl.jobs.WebCrawlingJob;

import com.zl.interfaces.ICrawlWebContentService;

@Component
public class CrawlWebDaemonHelper {
	
	@Autowired
	public ICrawlWebContentService crawlWebContentService;
	
	public CrawlWebDaemonHelper() {
	}
	
	public void crawlWeb(WebCrawlingJob job) {
		URL url = job.getUrl();
		int depth = job.getDepth();
		crawlWebContentService.crawlWebContent(url, depth);
	}
}
