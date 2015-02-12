package com.zl.daemons;

import com.zl.interfaces.IJobToExecuteMonitor;
import com.zl.job.executor.JobExecutor;
import com.zl.job.manager.JobManager;

import utils.SimpleLogger;
import abstracts.AJob;
import interfaces.IDaemon;
import interfaces.IThreadPoolDaemon;
import Job.WebCrawlingJob;

public class JobExecuteDaemon implements IDaemon, IJobToExecuteMonitor {
	
	private static JobExecuteDaemon instance;
	private boolean started;
	
	private JobExecuteDaemon() {
	}
	
	@Override
	synchronized public boolean isStarted() {
		return this.started;
	}
	
	synchronized public static JobExecuteDaemon getInstance() {
		if (instance == null)
			instance = new JobExecuteDaemon();
		return instance;
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
			AJob waitingJob = null;
			while (started) {
				while ((waitingJob = JobManager.getInstance().popWaitingJob()) == null)
					wait();
				JobExecutor.getInstance().execute((WebCrawlingJob)waitingJob);
			}
			SimpleLogger.logServiceStopSucceed(serviceName);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
	synchronized public void onJobToExecuteAdded() {
		notifyAll();
	}
}
