package com.zl.daemons;

import com.zl.interfaces.IDaemon;
import com.zl.interfaces.IThreadPoolDaemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.zl.utils.SimpleLogger;
import com.zl.utils.TimeUtil;
import com.zl.interfaces.ISlaveMgntMonitor;

@Component
public class SlaveMgntDaemon implements IDaemon, ISlaveMgntMonitor {

	@Autowired
	public SlaveMgntDaemonHelper helper;
	
	private boolean started = false;
	private boolean added = false;
	private static final int interval = 5; // delay between each add slave request
	private int last = 0;

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
					while (added) {
						wait();
					}
					int currTime = TimeUtil.getUnixTime();
					int eclapse;
					if ((eclapse = currTime - last) < interval) {
						SimpleLogger.info("[/addslave] next request will be sent in " + (interval - eclapse) + " sec");
						Thread.sleep((interval - eclapse) * 1000);
					}
					last = TimeUtil.getUnixTime();
					added = true;
					helper.addSlave();
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
		added = false;
	}

	@Override
	synchronized public void onRemoveSlaveSuccess() {
		added = false;
	}

	@Override
	synchronized public void onRemoveSlaveFailure() {
		
	}
}
