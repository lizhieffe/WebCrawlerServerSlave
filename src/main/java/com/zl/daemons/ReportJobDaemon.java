package com.zl.daemons;

import com.zl.interfaces.IDaemon;
import com.zl.interfaces.IThreadPoolDaemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zl.utils.SimpleLogger;
import com.zl.jobs.WebCrawlingJob;
import com.zl.abstracts.AJob;

import com.zl.interfaces.IJobToReportMonitor;
import com.zl.managers.JobManager;

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
		int i = 0;
		try {
			SimpleLogger.logServiceStartSucceed(serviceName);
			AJob jobToReport = null;
			while (started) {
				++i;
				if (i > 0 && i % 41 == 0)
					SimpleLogger.info(i + " reached");
				while ((jobToReport = jobManager.popJobToReport()) == null)
					wait();
				helper.reportJob((WebCrawlingJob)jobToReport);
				SimpleLogger.info("[" + i +"] Slave is reporting job to master  [" + ((WebCrawlingJob)jobToReport).getUrl());
			}
			SimpleLogger.logServiceStopSucceed(serviceName);
		} catch (InterruptedException e) {
			SimpleLogger.error(e.toString());
			SimpleLogger.logServiceStartFail(serviceName);
		}
	}
	
	@Override
	synchronized public void stop() {
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
