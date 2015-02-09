package services;

import interfaces.IThreadPoolService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.SimpleLogger;

public class ThreadPoolService implements IThreadPoolService {
	private static ThreadPoolService instance;
	private ExecutorService es;
	
	private ThreadPoolService() {
		es = Executors.newFixedThreadPool(50);
	}
	
	public static ThreadPoolService getInstance() {
		if (instance == null)
			instance = new ThreadPoolService();
		return instance;
	}
	
	public void submit(Runnable task) {
		this.es.submit(task);
	}
	
	public void start() {
		es = Executors.newFixedThreadPool(50);
		SimpleLogger.logServiceStartSucceed(this.getClass().getName());
	}
	
	public void stop() {
		es.shutdownNow();
		if (es.isShutdown())
			SimpleLogger.logServiceStopSucceed(this.getClass().getName());
		else
			SimpleLogger.logServiceStopFail(this.getClass().getName());
	}
}
