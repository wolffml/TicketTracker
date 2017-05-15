package site.swgoh.TicketTracker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.pattern.LineSeparatorPatternConverter;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import site.swgoh.TicketTracker.beans.ResultRecord;

public class APITest {

	//Set up the Logger and ResourceBundle
	private static Logger logger = Logger.getLogger(App.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	
	public static void main(String[] args) {
		
		Path path = Paths.get("C:\\Google Drive\\SWGOH\\MEmu Download\\Screenshot\\2017-05-11\\tmp\\crop_Screenshot_2017-05-10-18-30-16867.png");
		
		logger.info("Path: " + FilenameUtils.getFullPath(path.toString()));
		logger.info("FileBaseName: " + FilenameUtils.getBaseName(path.toString()));
		logger.info("Extention: " + FilenameUtils.getExtension(path.toString()));
		String returnFile = FilenameUtils.getFullPath(path.toString()) + FilenameUtils.getBaseName(path.toString()) + ".txt";
		logger.info("Textfile name: " + returnFile);
		
		/*
		// Just testing some of the Vision rest code from Microsoft here
		String apiKey = bundle.getString("ocr.apikey");
		logger.debug("API Key: " + apiKey);
		String baseURI = bundle.getString("ocr.uri");
		logger.debug("Base URI: " + baseURI);
		VisionServiceRestClient c = new VisionServiceRestClient(apiKey, baseURI);
    	String strFile = "C:\\Google Drive\\SWGOH\\MEmu Download\\Screenshot\\2017-05-11\\tmp\\crop_Screenshot_2017-05-10-18-30-16867.png";
    	
    	Gson gson = new Gson();
        // Put the image into an input stream for detection.
        File file = new File(strFile);
    	try {
    		
    		
    	    //Bitmap mBitmap;
    	    //mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
    	    ByteArrayInputStream inputStream = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(file)));
    		
			OCR ocr = c.recognizeText(inputStream, "en", false);
			//OCR ocr = c.recognizeText("https://static1.squarespace.com/static/53e8d221e4b00c61990b52a4/t/54c69295e4b05c3509c1b6c5/1422299824465/image.jpg?format=750w", "en",false);
			
			String result = gson.toJson(ocr);
			logger.info(result);
			
			ArrayList<ResultRecord> resultLines = new ArrayList<ResultRecord>();
			
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
					ResultRecord rec = new ResultRecord(i, strLine);
					resultLines.add(rec);
				}
			}
			for (ResultRecord rec: resultLines){
				logger.debug(rec);
	    	}
		} catch (Exception e) {
			logger.error(e.toString());
			//e.printStackTrace();
		}
    	
    	*/
    	
    	logger.debug("Execution Complete");
	}

}
