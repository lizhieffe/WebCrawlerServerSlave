package com.zl;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.zl.daemons.CrawlWebDaemon;
import com.zl.daemons.JobReportDaemon;
import com.zl.daemons.JobReportDaemonHelper;
import com.zl.daemons.ParseWebContentDaemon;
import com.zl.daemons.SlaveMgntDaemon;
import com.zl.daemons.ThreadPoolDaemon;
import com.zl.interfaces.IBeanConfiguration;

@Component
@Configuration
public class DaemonConfiguration implements IBeanConfiguration {
	
	public ThreadPoolDaemon createThreadPoolDaemon() {
		return new ThreadPoolDaemon();
	}
	
	public CrawlWebDaemon createCrawlWebDaemon() {
		return new CrawlWebDaemon();
	}
	
	public JobReportDaemon createJobReportDaemon() {
		return new JobReportDaemon();
	}
	
	public SlaveMgntDaemon createSlaveMgntDaemon() {
		return new SlaveMgntDaemon();
	}
	
	public ParseWebContentDaemon createParseWebContentDaemon() {
		return new ParseWebContentDaemon();
	}
	
	public JobReportDaemonHelper createJobReportDaemonHelper() {
		return new JobReportDaemonHelper();
	}
}
