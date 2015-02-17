package com.zl.managers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zl.abstracts.AJob;
import com.zl.daemons.CrawlWebDaemon;
import com.zl.daemons.ReportJobDaemon;
import com.zl.interfaces.IJobManager;

@Component
public class JobManager implements IJobManager {

	@Autowired
	public ReportJobDaemon jobReportDaemon;
	
	@Autowired
	public CrawlWebDaemon crawlWebDaemon;
	
	private CopyOnWriteArrayList<AJob> jobsToExecute = new CopyOnWriteArrayList<AJob>();
	private CopyOnWriteArrayList<AJob> jobsInExecuting = new CopyOnWriteArrayList<AJob>();
	private CopyOnWriteArrayList<AJob> jobsToReport = new CopyOnWriteArrayList<AJob>();
		
	public JobManager() {
		
	}
	
	@Override
	public boolean addJob(AJob job) {
			/**
			 * TODO: add repeated job check
			 */
		jobsToExecute.add(job);
		crawlWebDaemon.onJobToExecuteAdded();
		return true;
	}

	@Override
	public boolean moveJobToWaitingStatus(AJob job) {
		try {
			jobsInExecuting.remove(job);
			jobsToExecute.add(job);
			crawlWebDaemon.onJobToExecuteAdded();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	@Override
	public boolean moveJobToRunningStatus(AJob job) {
		try {
			if (!jobsToExecute.contains(job))
				return false;
			jobsToExecute.remove(job);
			jobsInExecuting.add(job);
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	@Override
	public boolean removeJobFromRunningStatus(AJob job) {
		return jobsInExecuting.remove(job);
	}
	
	public int getWaitingJobsCount() {
		return this.jobsToExecute.size();
	}
	
	public AJob popWaitingJob() {
		try {
			return this.jobsToExecute.remove(0);
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public boolean addJobToReport(AJob job) {
		jobsToReport.add(job);
		jobReportDaemon.onJobToReportAdded();
		return true;
	}
	
	public boolean addAllJobsToReport(List<AJob> jobs) {
		this.jobsToReport.addAll(jobs);
		jobReportDaemon.onJobToReportAdded();
		return true;
	}
	
	synchronized public AJob popJobToReport() {
		try {
			return this.jobsToReport.remove(0);
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}


}
