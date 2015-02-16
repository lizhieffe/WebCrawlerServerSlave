package com.zl.daemons;

import com.zl.interfaces.IDaemon;
import com.zl.interfaces.IThreadPoolDaemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import utils.SimpleLogger;
import Job.WebCrawlingJob;
import abstracts.AJob;

import com.zl.interfaces.IJobToReportMonitor;
import com.zl.job.managers.JobManager;

@Component
public class ReportJobDaemon implements IDaemon, IJobToReportMonitor {
	
	@Autowired
	public ReportJobDaemonHelper helper;
	
	@Autowired
	public JobManager jobManager;

	private boolean started;
	
	public ReportJobDaemon() {
	}
	
	@Override
	synchronized public boolean isStarted() {
		return this.started;
	}
	
	@Override
	public void start(IThreadPoolDaemon threadPoolDaemon) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				start();
			}
		};
		threadPoolDaemon.submit(task);
	}
	
	synchronized private void start() {
		if (started) {
			SimpleLogger.logServiceAlreadyStarted(this);
			return;
		}
		else
			started = true;
		
		final String serviceName = this.getClass().getName();
		try {
			SimpleLogger.logServiceStartSucceed(serviceName);
			AJob jobToReport = null;
			while (started) {
				while ((jobToReport = jobManager.popJobToReport()) == null)
					wait();
				helper.reportJob((WebCrawlingJob)jobToReport);
			}
			SimpleLogger.logServiceStopSucceed(serviceName);
		} catch (InterruptedException e) {
			e.printStackTrace();
			SimpleLogger.logServiceStartFail(serviceName);
		}
	}
	
	@Override
	public void stop() {
		if (!this.started)
			return;
		else
			this.started = false;
	}
	
	@Override
	synchronized public void onJobToReportAdded() {
		notifyAll();
	}
}
