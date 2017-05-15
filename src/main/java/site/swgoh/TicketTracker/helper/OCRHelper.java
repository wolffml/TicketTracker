package site.swgoh.TicketTracker.helper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import site.swgoh.TicketTracker.beans.ResultRecord;
import site.swgoh.TicketTracker.lists.DailyTrackingList;
import site.swgoh.TicketTracker.lists.OCRRecordList;

public class OCRHelper {
	
	private static Logger logger = Logger.getLogger(OCRHelper.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	//String for the OCR Call
	private static final String baseURI = bundle.getString("ocr.uri");
	private static final String apiKey = bundle.getString("ocr.apikey");
	private static final String lang = bundle.getString("ocr.lang");
	
	
	public  static void processImageFiles(String sourceDir, DailyTrackingList trackList) {
	    	
	    	Path sourceDirPath = Paths.get(sourceDir);
	    	
	    	DirectoryStream<Path> directoryStream;
			try {
				directoryStream = Files.newDirectoryStream(sourceDirPath, "*.png");
				for (Path path : directoryStream) {
		    		OCRRecordList fileResultsList;
		            logger.debug("Performing OCR on File: " + path.toString());
		            fileResultsList = OCRHelper.getOCRData(path.toString());
		            boolean writeFile = Boolean.parseBoolean(bundle.getString("activities.ocr.saveresponses"));
		            if (writeFile){
		            	fileResultsList.writeList();
		            	//imageFileRecordWriter(fileResultsList, path);
		            }
		            //Now add the file's results into the tracking list
		            trackList.addFileContents(fileResultsList);
		        }
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	    	//ArrayList<String> stringList = new ArrayList<String>();
	    	
	    }
	
	
	public static OCRRecordList getOCRData(String strFile){
		VisionServiceRestClient c = new VisionServiceRestClient(apiKey, baseURI);
		Gson gson = new Gson();
	    // Put the image into an input stream for detection.
	    File file = new File(strFile);
	    OCRRecordList resultLines = new OCRRecordList(strFile);
		try {
		    ByteArrayInputStream inputStream = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(file)));
			
			OCR ocr = c.recognizeText(inputStream, lang, false);
			//OCR ocr = c.recognizeText("https://static1.squarespace.com/static/53e8d221e4b00c61990b52a4/t/54c69295e4b05c3509c1b6c5/1422299824465/image.jpg?format=750w", "en",false);
			
			String result = gson.toJson(ocr);
			logger.info(result);
			int i=0;
			for (Region region : ocr.regions) {
				//logger.debug("Bounds: " + region.boundingBox);
				//for (int i=0; i < region.lines.size(); i++) {
				for (Line line: region.lines) {
					i++;
				    String strLine = "";
					for (Word word: line.words) {
						strLine += word.text + " ";
						//logger.debug("Word: " + word.text);
					}
					ResultRecord rec = new ResultRecord(i, strLine.trim());
					resultLines.add(rec);
				}
			}
			for (ResultRecord rec: resultLines){
				logger.debug(rec);
	    	}
			resultLines.setJson(result);
		} catch (Exception e) {
			logger.error(e.toString());
			//e.printStackTrace();
		}		
		return resultLines;
	}
	
}
