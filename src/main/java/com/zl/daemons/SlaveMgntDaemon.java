package com.zl.daemons;

import org.springframework.stereotype.Component;

import interfaces.IDaemon;
import interfaces.IThreadPoolDaemon;
import utils.SimpleLogger;
import utils.TimeUtil;

import com.zl.interfaces.ISlaveMgntMonitor;

@Component
public class SlaveMgntDaemon implements IDaemon, ISlaveMgntMonitor {

	private boolean started = false;
	private boolean added = false;
	private static final int interval = 60; // delay between each add slave request
	private int last = 0;
	
	private static SlaveMgntDaemon instance;
	private SlaveMgntDaemonAddSlaveHelper addSlaveHelper;
	private SlaveMgntDaemonRemoveSlaveHelper removeSlaveHelper;

	public SlaveMgntDaemon() {
		addSlaveHelper = new SlaveMgntDaemonAddSlaveHelper();
		removeSlaveHelper = new SlaveMgntDaemonRemoveSlaveHelper();
	}
	
	public static SlaveMgntDaemon getInstance() {
		if (instance == null)
			instance = new SlaveMgntDaemon();
		return instance;
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
					addSlaveHelper.addSlave();
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
	public void onAddSlaveSuccess() {
		
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
