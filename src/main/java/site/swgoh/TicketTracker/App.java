package site.swgoh.TicketTracker;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import site.swgoh.TicketTracker.helper.DBWriteHelper;
import site.swgoh.TicketTracker.helper.FileHelper;
import site.swgoh.TicketTracker.helper.ImageHelper;
import site.swgoh.TicketTracker.helper.OCRHelper;
import site.swgoh.TicketTracker.lists.DailyTrackingList;


public class App 
{
	//Set up the Logger and ResourceBundle
	private static Logger logger = Logger.getLogger(App.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	//Get some of the constants for the base directory info
	private static final String rootDir = bundle.getString("rootdir");
	
    public static void main( String[] args )
    {
    	//Make the Directory for daily screenshots / csv
    	String dailyDirString = FileHelper.mkDailyDir(rootDir);
    	logger.info(dailyDirString);
    	//String dailyDirString = rootDir + "\\2017-05-30";
    	
    	String uploadToAmazon = bundle.getString("activites.aws.writetobucket");
    	if (uploadToAmazon.toLowerCase().equals("true")){
    		//FileHelper.moveToAmazon(rootDir, "*.png");
    	}
    	
        //Move the screenshot files from the root directory to the created daily folder
        //FileHelper.moveFiles(rootDir, dailyDirString, "*.png");
        FileHelper.moveFiles(rootDir, "*.png");
        
        //Create the Cropped Images
        String croppedDirString = ImageHelper.cropImages(dailyDirString);
        
        //Process those Cropped Images through the OCR Software
        DailyTrackingList trackList = new DailyTrackingList();
        OCRHelper.processImageFiles(croppedDirString, trackList);
        logger.info(trackList.toString());
        
        //Write the data to a csv file
        trackList.writeList(dailyDirString + "output.csv");
        
        //Check to see if the results should be written to DB
        String writeDB = bundle.getString("activites.db.write");
        if (writeDB.equals("true")){
        	logger.info("Writing results to DB.");
        	DBWriteHelper.writeToDB(trackList);
        } else {
        	logger.info("Not writing to DB");
        }
        
        //Finished
        logger.debug("Program finished executing.");
    }
}
