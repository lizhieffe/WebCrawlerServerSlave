package controllers;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import resources.RJob;

@RestController
public class CJob {

	private static final Logger logger = Logger.getLogger(CJob.class);

	@RequestMapping(value = "/addslavejob", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RJob addSlaveJob(@RequestBody RJob job) {
		logger.debug("Received POST request:" + job.getUrl());
		return job;
	}
}
