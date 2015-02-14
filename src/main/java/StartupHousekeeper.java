import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.zl.daemons.SlaveMgntDaemon;
import com.zl.daemons.JobExecuteDaemon;
import com.zl.daemons.JobReportDaemon;

import daemons.ThreadPoolDaemon;
import utils.SimpleLogger;

@Component
public class StartupHousekeeper implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		SimpleLogger.info("Application has started");
        startServices();
	}
	
	private void startServices() {
    	SimpleLogger.info("Starting services:");
        ThreadPoolDaemon.getInstance().start();
    	JobExecuteDaemon.getInstance().start(ThreadPoolDaemon.getInstance());
    	JobReportDaemon.getInstance().start(ThreadPoolDaemon.getInstance());
//        SimpleLogger.info("port = " + ApplicationProperties.getInstance().getIp());
    	SlaveMgntDaemon.getInstance().start(ThreadPoolDaemon.getInstance());
    	
    }
}