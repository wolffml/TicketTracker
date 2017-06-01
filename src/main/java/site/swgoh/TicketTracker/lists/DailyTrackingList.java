package site.swgoh.TicketTracker.lists;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import site.swgoh.TicketTracker.beans.MemberData;

public class DailyTrackingList extends ArrayList<MemberData> {
	
	private static final long serialVersionUID = 5793923322338285631L;
	private static Logger logger = Logger.getLogger(OCRRecordList.class);
	
	public void writeList(String filePath) {
    	
    	PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter(filePath));
			logger.debug("Writing file OCR results: " + filePath);
	    	for (MemberData data: this){
	    		pw.println(data);
	    	}
	    	pw.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			//e.printStackTrace();
		}
    	
    }

	public void addFileContents(OCRRecordList fileResultsList) {
		//Here we have the individual File's contents, need to create MemberData elements and add them to the arraylist
		int[] names = new int[5];
		int[] scores = new int[5];
		for (int i=0; i<5; i++) {
			names[i] = fileResultsList.findElement(i+1,"85");
			scores[i] = fileResultsList.findElement(i+1, "Tickets Produced:");
		}
		
		//TODO: fix this in cases where there are not all 5 members present on the screenshot
		for (int i=0; i<5; i++){
			MemberData md;
			if (names[i] == -1 || scores[i] == -1){
				md = new MemberData("","",fileResultsList.getPath());
			} else {
				//Need to make sure that that we don't have an index out of bounds situation
				if ( fileResultsList.size() > (scores[i] + 1 ) ){
					if ( names[i] == 0 ) {
						md = new MemberData("UNKNOWN",
								fileResultsList.get(scores[i]+1).getStringData(), fileResultsList.getPath());
					}else{
						md = new MemberData(fileResultsList.get(names[i]-1).getStringData(),
								fileResultsList.get(scores[i]+1).getStringData(), fileResultsList.getPath());
					}
					
				} else {
					md = new MemberData(fileResultsList.get(names[i]-1).getStringData(),
							"0", fileResultsList.getPath());
				}
				
			}
			this.add(md);
		}
		
	}
	@Override
	public String toString(){
		String strReturn = "";
		for (MemberData md: this){
			strReturn += md.toString() + "\n";
		}
		
		return strReturn;
	}

}
