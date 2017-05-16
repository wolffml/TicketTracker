package site.swgoh.TicketTracker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import site.swgoh.TicketTracker.helper.DBWriteHelper;
import site.swgoh.TicketTracker.helper.FileHelper;
import site.swgoh.TicketTracker.helper.ImageHelper;
import site.swgoh.TicketTracker.helper.OCRHelper;
import site.swgoh.TicketTracker.lists.DailyTrackingList;

/**
 * Hello world!
 *
 */
public class App 
{
	//Set up the Logger and ResourceBundle
	private static Logger logger = Logger.getLogger(App.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	//Get some of the constants for the basie directory info
	private static final String rootDir = bundle.getString("rootdir");
	
	
	
	
    public static void main( String[] args )
    {
        //Get today's date as a string and create the folder for the daily screenshots
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(new Date());
        logger.debug("Today's Date: " + dateString);
       
        String dailyDirString = rootDir + dateString + "\\";
        //Make the new Daily Directory
        FileHelper.mkDir(dailyDirString);
        //Move the screenshot files from the root directory to the created daily folder
        FileHelper.moveFiles(rootDir, dailyDirString, "*.png");
        //Create the Cropped Images
        String croppedDirString = ImageHelper.cropImages(dailyDirString);
        //Process those Cropped Images through the OCR Software
        DailyTrackingList trackList = new DailyTrackingList();
        OCRHelper.processImageFiles(croppedDirString, trackList);
        logger.info(trackList.toString());
        
        try {
			trackList.writeList(dailyDirString + "output.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String writeDB = bundle.getString("activites.db.write");
        if (writeDB.equals("true")){
        	DBWriteHelper.writeToDB(trackList);
        }
        
        
        //Create the cropped image files
        logger.debug("Program finished executing.");
    }
   
}
