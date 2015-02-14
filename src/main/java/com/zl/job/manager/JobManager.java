package com.zl.job.manager;

import java.util.ArrayList;
import java.util.List;

import com.zl.daemons.JobExecuteDaemon;
import com.zl.daemons.JobReportDaemon;

import abstracts.AJob;
import interfaces.IJobManager;

public class JobManager implements IJobManager {

	private List<AJob> jobsToExecute = new ArrayList<AJob>();
	private List<AJob> jobsInExecuting = new ArrayList<AJob>();
	private List<AJob> jobsToReport = new ArrayList<AJob>();
	
	private static JobManager instance = null;
	
	private JobManager() {
		
	}
	
	synchronized public static JobManager getInstance() {
		if (instance == null)
			instance = new JobManager();
		return instance;
	}
	
	@Override
	public boolean addJob(AJob job) {
		synchronized (this) {
			/**
			 * TODO: add repeated job check
			 */
			jobsToExecute.add(job);
		}
		JobExecuteDaemon.getInstance().onJobToExecuteAdded();
		return true;
	}

	@Override
	synchronized public boolean moveJobToWaitingStatus(AJob job) {
		if (!jobsInExecuting.contains(job))
			return false;
		jobsInExecuting.remove(job);
		jobsToExecute.add(job);
		JobExecuteDaemon.getInstance().onJobToExecuteAdded();
		return true;
	}

	@Override
	synchronized public boolean moveJobToRunningStatus(AJob job) {
		if (!jobsToExecute.contains(job))
			return false;
		jobsToExecute.remove(job);
		jobsInExecuting.add(job);
		return true;
	}

	synchronized public int getWaitingJobsCount() {
		return this.jobsToExecute.size();
	}
	
	synchronized public AJob popWaitingJob() {
		if (this.jobsToExecute.size() == 0)
			return null;
		return this.jobsToExecute.remove(0);
	}
	
	public boolean addJobToReport(AJob job) {
		synchronized(this) {
			if (jobsToReport.contains(job))
				return false;
			this.jobsToReport.add(job);
		}
		JobReportDaemon.getInstance().onJobToReportAdded();
		return true;
	}
	
	synchronized public AJob popJobToReport() {
		if (this.jobsToReport.size() == 0)
			return null;
		else
			return this.jobsToReport.remove(0);
	}
}
