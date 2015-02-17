package com.zl.daemons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.zl.abstracts.AJob;
import com.zl.interfaces.IReportJobService;

@Component
public class ReportJobDaemonHelper {
	
	@Autowired
	private ApplicationContext appContext;
	
	public void reportJob(AJob job) {
		/**
		 * getBean() method together with "prototype" scope bean make sure each service is a new bean
		 */
		IReportJobService service = appContext.getBean(IReportJobService.class);
		service.reportJob(job);
	}
}
