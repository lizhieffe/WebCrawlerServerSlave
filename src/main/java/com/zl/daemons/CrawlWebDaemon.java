package com.zl.daemons;

import com.zl.interfaces.IDaemon;
import com.zl.interfaces.IThreadPoolDaemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zl.utils.SimpleLogger;
import com.zl.jobs.WebCrawlingJob;
import com.zl.abstracts.AJob;

import com.zl.interfaces.IJobToExecuteMonitor;
import com.zl.managers.JobManager;

@Component
public class CrawlWebDaemon implements IDaemon, IJobToExecuteMonitor {
	
	@Autowired
	public JobManager jobManager;
	
	@Autowired
	public CrawlWebDaemonHelper helper;
	
	private boolean started;
	
	public CrawlWebDaemon() {
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
			AJob waitingJob = null;
			while (started) {
				while ((waitingJob = jobManager.popWaitingJob()) == null)
					wait();
				helper.crawlWeb((WebCrawlingJob)waitingJob);
//				JobExecutor.getInstance().execute((WebCrawlingJob)waitingJob);
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
