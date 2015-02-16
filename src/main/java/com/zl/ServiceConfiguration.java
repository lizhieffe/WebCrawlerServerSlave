package com.zl;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.zl.interfaces.IBeanConfiguration;
import com.zl.interfaces.ICrawlWebContentService;
import com.zl.interfaces.IReportJobService;
import com.zl.services.CrawlWebContentService;
import com.zl.services.ReportJobService;

@Component
@Configuration
public class ServiceConfiguration implements IBeanConfiguration {
	
	public IReportJobService createIReportJobService() {
    	return new ReportJobService();
    }
    
    public ICrawlWebContentService createICrawlWebContentService() {
    	return new CrawlWebContentService();
    }
}
