package site.swgoh.TicketTracker;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import site.swgoh.TicketTracker.helper.AWSS3Helper;
import site.swgoh.TicketTracker.helper.DBWriteHelper;
import site.swgoh.TicketTracker.helper.FileHelper;

public class APITest {

	//Set up the Logger and ResourceBundle
	private static Logger logger = Logger.getLogger(App.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	public static void main(String[] args) {
		
		logger.info("Starting APITest main");
		
		DBWriteHelper.update30K("5/17/2017","5/18/2017");
		
		
		//String rootdir = bundle.getString("rootdir");
		//FileHelper.waitForFiles(rootdir);
		
		//String bucket_name = "mlwbackupbucket1";
		//AWSS3Helper.listObjects(bucket_name);
		
    	logger.info("Execution Complete");
	}
}
