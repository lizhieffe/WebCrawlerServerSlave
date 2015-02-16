package com.zl.daemons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import abstracts.AJob;

import com.zl.interfaces.IReportJobService;

@Component
public class ReportJobDaemonHelper {
	
	@Autowired
	public IReportJobService reportJobService;
	
	public void reportJob(AJob job) {
		reportJobService.reportJob(job);
	}
}
