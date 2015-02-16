package com.zl;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.zl.interfaces.IBeanConfiguration;
import com.zl.job.managers.JobManager;
import com.zl.job.managers.WebContentManager;

@Component
@Configuration
public class ManagerConfigration implements IBeanConfiguration {
	
	 public JobManager createJobManager() {
	    	return new JobManager();
	    }
	    
	 public WebContentManager createWebContentManager() {
		 return new WebContentManager();
	 }
}
