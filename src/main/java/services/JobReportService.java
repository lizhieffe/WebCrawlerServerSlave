package services;

import interfaces.IJobToReportMonitor;
import interfaces.IService;
import interfaces.IThreadPoolService;
import jobManager.JobManager;
import jobReporter.JobReporter;
import utils.SimpleLogger;
import Job.WebCrawlingJob;
import abstracts.AJob;

public class JobReportService implements IService, IJobToReportMonitor {
	
	private static JobReportService instance;
	
	private boolean started;
	
	private JobReportService() {

	}
	
	synchronized public static JobReportService getInstance() {
		if (instance == null)
			instance = new JobReportService();
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
			while (started) {
				AJob jobToReport = null;
				while ((jobToReport = JobManager.getInstance().popJobToReport()) == null)
					wait();
				JobReporter.getInstance().report((WebCrawlingJob)jobToReport);
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
