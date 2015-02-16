package com.zl.job.manager;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zl.daemons.ParseWebContentDaemon;
import com.zl.entities.WebContentEntity;

@Component
public class WebContentManager {
	
	@Autowired
	public ParseWebContentDaemon parseWebContentDaemon;
	
	private static WebContentManager instance;
	private CopyOnWriteArrayList<WebContentEntity> contentsToProceed = new CopyOnWriteArrayList<WebContentEntity>();
	
	public WebContentManager() {
	}
	
	synchronized public static WebContentManager getInstance() {
		if (instance == null)
			instance = new WebContentManager();
		return instance;
	}
	
	public void addContent(WebContentEntity content) {
		this.contentsToProceed.add(content);
		parseWebContentDaemon.onWebContentAdded();
	}
	
	public WebContentEntity popContent() {
		try {
			return this.contentsToProceed.remove(0);
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public int getContentsNum() {
		return this.contentsToProceed.size();
	}
}
