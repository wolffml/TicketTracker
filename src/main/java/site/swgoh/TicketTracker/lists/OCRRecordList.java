package site.swgoh.TicketTracker.lists;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import site.swgoh.TicketTracker.beans.ResultRecord;

public class OCRRecordList extends ArrayList<ResultRecord> {
	
	private static Logger logger = Logger.getLogger(OCRRecordList.class);
	private static final long serialVersionUID = 6323389706867385432L;
	private String path;
	private String json;
	
	
	
	public OCRRecordList(String path) {
		super();
		this.path = path;
		json = "";
	}

	public String getJson() {
		return json;
	}


	public void setJson(String json) {
		this.json = json;
	}



	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}

	public void writeList() throws IOException{
    	String txtFileName = FilenameUtils.getFullPath(path.toString()) + FilenameUtils.getBaseName(path.toString()) + ".txt";
    	PrintWriter pw = new PrintWriter(new FileWriter(txtFileName));
    	logger.debug("Writing file OCR results: " + txtFileName);
    	for (ResultRecord record: this){
    		pw.println(record);
    	}
    	pw.println(json);
    	pw.close();
    }

	public int findElement(int xth, String searchString) {
		int returnInt=-1;
		int matches = 0;
		for(int i=0; i<this.size(); i++){
			if ( this.get(i).getStringData().equals(searchString)){
				matches++;
				if (matches == xth){
					returnInt = i;
					break;
				}
			}
		}
		return returnInt;
		
		
	}
	
}
