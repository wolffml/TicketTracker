package site.swgoh.TicketTraker.gui.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import site.swgoh.TicketTracker.helper.FileHelper;

public class ProcessingActions {
	
	private static String dailyDirString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	
	
	public ProcessingActions() {
	}
	/*
	public static void processFiles(){
		//Make the Directory for daily screenshots / csv
    	dailyDirString = FileHelper.mkDailyDir(rootDir);
    	
    	String uploadToAmazon = bundle.getString("activites.aws.writetobucket");
    	if (uploadToAmazon.toLowerCase().equals("true")){
    		FileHelper.moveToAmazon(rootDir, FileHelper.getDateFormatString(), "*.png");
    	}
    	
        //Move the screenshot files from the root directory to the created daily folder
        FileHelper.moveFiles(rootDir, dailyDirString, "*.png");
	}
	*/

}
