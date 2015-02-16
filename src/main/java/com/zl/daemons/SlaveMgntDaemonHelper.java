package com.zl.daemons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zl.interfaces.IAddSlaveService;
import com.zl.interfaces.IRemoveSlaveService;

@Component
public class SlaveMgntDaemonHelper {
	
	@Autowired
	public IAddSlaveService addSlaveService;
	
	@Autowired
	public IRemoveSlaveService removeSlaveService;
	
	public void addSlave() {
		addSlaveService.addSlave();
	}
	
	public void remove() {
		removeSlaveService.removeSlave();
	}
}
