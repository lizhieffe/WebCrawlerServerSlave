package jobExecutor;

import java.net.URL;
import java.util.List;

import utils.SimpleLogger;
import jobManager.JobManager;
import Job.WebCrawlingJob;
import Job.WebCrawlingJobFactory;

public class JobExecutor {
	private static JobExecutor instance;
	private JobManager jobManager;
	
	private JobExecutor() {
		this.jobManager = JobManager.getInstance();
	}
	
	synchronized public static JobExecutor getInstance() {
		if (instance == null)
			instance = new JobExecutor();
		return instance;
	}
	
	public void execute(final WebCrawlingJob job) {
		if (job == null)
			return;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				SimpleLogger.info("[Execution] Executing job: " + job.getUrl().toString());
				List<URL> result = JobExecutorHelper.getContainedURL(job.getUrl());
				SimpleLogger.info("[Execution] Execution done. URL found: " + result.size());
				for (URL url : result) {
//					Logger.info("Fould URL: " + url);
					if (job.getDepth() > 1) {
						WebCrawlingJob newJob = WebCrawlingJobFactory.create(url.toString(), job.getDepth() - 1);
						jobManager.addJobToReport(newJob);
					}
				}
			}
		};
		new Thread(runnable).start();
	}
}
