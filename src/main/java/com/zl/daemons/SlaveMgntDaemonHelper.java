package com.zl.daemons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.zl.interfaces.IAddSlaveService;
import com.zl.interfaces.IRemoveSlaveService;

@Component
public class SlaveMgntDaemonHelper {
	
	/**
	 * getBean() method together with "prototype" scope bean make sure each service is a new bean
	 */
	
	@Autowired
	private ApplicationContext appContext;
	
	public void addSlave() {
		appContext.getBean(IAddSlaveService.class).addSlave();
	}
	
	public void remove() {
		appContext.getBean(IRemoveSlaveService.class).removeSlave();
	}
}
