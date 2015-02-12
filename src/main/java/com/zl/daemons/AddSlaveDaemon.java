package com.zl.daemons;

import com.zl.interfaces.IAddSlaveMonitor;

import interfaces.IDaemon;
import interfaces.IThreadPoolDaemon;
import utils.SimpleLogger;
import utils.TimeUtil;

public class AddSlaveDaemon implements IDaemon, IAddSlaveMonitor {

	private boolean started = false;
	private boolean added = false;
	private static final int interval = 60; // delay between each add slave request
	private int last = 0;
	
	private static AddSlaveDaemon instance;
	
	private AddSlaveDaemon() {
		
	}
	
	public static AddSlaveDaemon getInstance() {
		if (instance == null)
			instance = new AddSlaveDaemon();
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
		if (isStarted())
			return;
		else {
			SimpleLogger.logServiceAlreadyStarted(this);
			started = true;
		}
		
		synchronized (this) {
			try {
				while (started) {
					while (added) {
						wait();
					}
					int currTime = TimeUtil.getUnixTime();
					int eclapse;
					if ((eclapse = currTime - last) < interval)
						Thread.sleep((interval - eclapse) * 1000);
					last = TimeUtil.getUnixTime();
					added = true;
					AddSlaveDaemonHelper.add();
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
	public void onAddSlaveSucceed() {
		
	}

	@Override
	synchronized public void onAddSlaveFail() {
		added = false;
	}
}
