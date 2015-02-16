package com.zl;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.zl.daemons.CrawlWebDaemon;
import com.zl.daemons.ParseWebContentDaemon;
import com.zl.daemons.ReportJobDaemon;
import com.zl.daemons.ReportJobDaemonHelper;
import com.zl.daemons.SlaveMgntDaemon;
import com.zl.daemons.SlaveMgntDaemonHelper;
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
	
	public ReportJobDaemon createJobReportDaemon() {
		return new ReportJobDaemon();
	}
	
	public ParseWebContentDaemon createParseWebContentDaemon() {
		return new ParseWebContentDaemon();
	}
	
	public ReportJobDaemonHelper createJobReportDaemonHelper() {
		return new ReportJobDaemonHelper();
	}
	
	public SlaveMgntDaemon createSlaveMgntDaemon() {
		return new SlaveMgntDaemon();
	}
	
	public SlaveMgntDaemonHelper createSlaveMgntDaemonHelper() {
		return new SlaveMgntDaemonHelper();
	}
}
