package com.zl.daemons;

import interfaces.IDaemon;
import interfaces.IThreadPoolDaemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import utils.SimpleLogger;

import com.zl.entities.WebContentEntity;
import com.zl.interfaces.IWebContentMonitor;
import com.zl.job.manager.JobManager;
import com.zl.job.manager.WebContentManager;
import com.zl.tasks.CrawlWebContentTask;

@Component
public class ParseWebContentDaemon implements IDaemon, IWebContentMonitor {

	@Autowired
	public JobManager jobManager;
	
	@Autowired
	public WebContentManager webContentManager;
	
	private boolean started;
	IThreadPoolDaemon threadPoolService;
	
	private static ParseWebContentDaemon instance;
	
	public ParseWebContentDaemon() {
		this.started = false;
	}
	
	synchronized public static ParseWebContentDaemon getInstance() {
		if (instance == null)
			instance = new ParseWebContentDaemon();
		return instance;
	}
	
	@Override
	synchronized public void start(IThreadPoolDaemon threadPoolDaemon) {
		this.threadPoolService = threadPoolDaemon;
		Runnable task = new Runnable() {
			@Override
			public void run() {
				start();
			}
		};
		threadPoolDaemon.submit(task);
	}

	synchronized private void start() {
		if (isStarted()) {
			SimpleLogger.logServiceAlreadyStarted(this);
			return;
		}
		else
			started = true;
		
		final String serviceName = this.getClass().getName();
		try {
			SimpleLogger.logServiceStartSucceed(serviceName);
			while (started) {
				WebContentEntity content = null;
				while ((content = webContentManager.popContent()) == null)
					wait();
				new CrawlWebContentTask(threadPoolService.getExecutorService())
						.parseWebContent(content.getStr(), content.getDepth(), this.jobManager);
			}
			SimpleLogger.logServiceStopSucceed(serviceName);
		}
		catch (Exception e) {
			e.printStackTrace();
			SimpleLogger.logServiceStartFail(serviceName);
		}
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	synchronized public boolean isStarted() {
		return started;
	}

	@Override
	synchronized public void onWebContentAdded() {
		notifyAll();
	}

}
