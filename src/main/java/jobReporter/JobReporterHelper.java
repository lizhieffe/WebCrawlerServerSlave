package jobReporter;
import utils.ConfigUtil;
import abstracts.AJob;
import Job.WebCrawlingJob;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JobReporterHelper {
	
	private static String URI =  "/addmasterjob";
	
	static String constructRequestUrl() {
		return ConfigUtil.getMasterIp() + ":" + ConfigUtil.getMasterPort() + URI;
	}
	
	static ObjectNode constructRequestJson(AJob job) {
//		ObjectNode json = Json.newObject();
//		json.put("type", "webcrawling");
//		json.put("url", ((WebCrawlingJob)job).getUrl().toString());
//		json.put("depth", String.valueOf(((WebCrawlingJob)job).getDepth()));
//		return json;
		
		return null;
	}
}
