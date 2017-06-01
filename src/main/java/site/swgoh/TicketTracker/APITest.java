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
	private static final String rootDir = bundle.getString("rootdir");
	
	public static void main(String[] args) {
		
		logger.info("Starting APITest main");
		
		//FileHelper.getDateFromFile("Screenshot_2017-08-29-16-45-45776.png");
		//FileHelper.moveFiles( bundle.getString("rootdir"), "*.png");
		
		//DBWriteHelper.update30K("5/17/2017","5/18/2017");
		
		//String rootdir = bundle.getString("rootdir");
		//FileHelper.waitForFiles(rootdir);
		
		//String bucket_name = "mlwbackupbucket1";
		//AWSS3Helper.listObjects(bucket_name);
		
		//String filePath ="C:/Google Drive/SWGOH/MEmu Download/Screenshot/2017-06-25/Screenshot_2017-06-25-16-45-28619.png";
		//String s3Path = "2017-06-25/Screenshot_2017-06-25-16-45-28619.png";
		
		//AWSS3Helper.copyFile("swgoh-lambdainput", s3Path, filePath);
		
		FileHelper.moveToAmazon(rootDir, "*.png");
		FileHelper.moveFiles(rootDir, "*.png");
		
    	logger.info("Execution Complete");
	}
}
