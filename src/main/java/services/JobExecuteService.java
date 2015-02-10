package services;

import jobExecutor.JobExecutor;
import jobManager.JobManager;
import utils.SimpleLogger;
import abstracts.AJob;
import interfaces.IJobToExecuteMonitor;
import interfaces.IService;
import interfaces.IThreadPoolService;
import Job.WebCrawlingJob;

public class JobExecuteService implements IService, IJobToExecuteMonitor {
	
	private static JobExecuteService instance;
	private boolean started;
	
	private JobExecuteService() {
	}
	
	synchronized public static JobExecuteService getInstance() {
		if (instance == null)
			instance = new JobExecuteService();
		return instance;
	}
	
	@Override
	public void start(IThreadPoolService threadPoolService) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				start();
			}
		};
		threadPoolService.submit(task);
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
