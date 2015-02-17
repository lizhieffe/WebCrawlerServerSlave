package com.zl.daemons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zl.interfaces.IDaemon;
import com.zl.interfaces.ISlaveMgntMonitor;
import com.zl.interfaces.IThreadPoolDaemon;
import com.zl.utils.SimpleLogger;

@Component
public class SlaveMgntDaemon implements IDaemon, ISlaveMgntMonitor {

	@Autowired
	public SlaveMgntDaemonHelper helper;
	
	private boolean started = false;
//	private boolean added = false;
	private static final int interval = 30 * 1000; // delay between each add slave request
//	private int last = 0;

	public SlaveMgntDaemon() {
	}
	
	@Override
	synchronized public boolean isStarted() {
		return this.started;
	}
	
	@Override
	public void start(IThreadPoolDaemon threadPoolDaemon) {
		Runnable task = new Runnable() {
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
		else {
			started = true;
		}
		
		final String serviceName = this.getClass().getName();
		synchronized (this) {
			SimpleLogger.logServiceStartSucceed(serviceName);
			
			try {
				while (started) {
//					added = true;
					helper.addSlave();
					Thread.sleep(interval);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void stop() {

	}
	
	@Override
	synchronized public void onAddSlaveSuccess() {
		
	}

	@Override
	synchronized public void onAddSlaveFailure() {
//		added = false;
	}

	@Override
	synchronized public void onRemoveSlaveSuccess() {
//		added = false;
	}

	@Override
	synchronized public void onRemoveSlaveFailure() {
		
	}
}
