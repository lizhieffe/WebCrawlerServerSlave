package com.zl.daemons;

import interfaces.IDaemon;
import interfaces.IThreadPoolDaemon;
import utils.SimpleLogger;
import Job.WebCrawlingJob;
import abstracts.AJob;
import com.zl.interfaces.IJobToReportMonitor;
import com.zl.job.manager.JobManager;

public class JobReportDaemon implements IDaemon, IJobToReportMonitor {
	
	private static JobReportDaemon instance;
	private JobReportDaemonHelper helper;
	private boolean started;
	
	private JobReportDaemon() {
		this.helper = new JobReportDaemonHelper();
	}
	
	synchronized public static JobReportDaemon getInstance() {
		if (instance == null)
			instance = new JobReportDaemon();
		return instance;
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
				while ((jobToReport = JobManager.getInstance().popJobToReport()) == null)
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
