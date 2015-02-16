package com.zl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.zl.utils.SimpleLogger;

import com.zl.daemons.CrawlWebDaemon;
import com.zl.daemons.ReportJobDaemon;
import com.zl.daemons.ParseWebContentDaemon;
import com.zl.daemons.SlaveMgntDaemon;
import com.zl.daemons.ThreadPoolDaemon;

@Component
@Configuration
public class StartupHousekeeper implements ApplicationListener<ContextRefreshedEvent> {

	
	@Autowired
	public ThreadPoolDaemon threadPoolDaemon;
	
	@Autowired
	public CrawlWebDaemon crawlWebDaemon;
	
	@Autowired
	public ReportJobDaemon jobReportDaemon;
	
	@Autowired
	public SlaveMgntDaemon slaveMgntDaemon;
	
	@Autowired
	public ParseWebContentDaemon parseWebContentDaemon;
	
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		SimpleLogger.info("Application has started");
        startServices();
	}
	
	private void startServices() {
    	SimpleLogger.info("Starting services:");
    	crawlWebDaemon.start(threadPoolDaemon);
    	jobReportDaemon.start(threadPoolDaemon);
    	slaveMgntDaemon.start(threadPoolDaemon);
    	parseWebContentDaemon.start(threadPoolDaemon);
    }
}