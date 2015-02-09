package services;

import jobExecutor.JobExecutor;
import jobManager.JobManager;
import utils.LogUtil;
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
			LogUtil.logServiceAlreadyStarted(this);
			return;
		}
		else
			started = true;
		
		final String serviceName = this.getClass().getName();
		try {
			LogUtil.logServiceStartSucceed(serviceName);
			while (started) {
				AJob waitingJob = null;
				while ((waitingJob = JobManager.getInstance().popWaitingJob()) == null)
					wait();
				JobExecutor.getInstance().execute((WebCrawlingJob)waitingJob);
			}
			LogUtil.logServiceStopSucceed(serviceName);
		} catch (InterruptedException e) {
			e.printStackTrace();
			LogUtil.logServiceStartFail(serviceName);
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
