package controllers;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class CJob {

	private static final Logger logger = Logger.getLogger(CJob.class);

	@RequestMapping(value = "/addslavejob", method = RequestMethod.POST)
	public String addSlaveJob(@RequestParam("json") String json) {
		logger.debug("Received POST request:" + json);
		return null;
	}
}
