package site.swgoh.TicketTracker;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import site.swgoh.TicketTracker.helper.DBWriteHelper;

public class DBActionsMain {

	//Set up the Logger and ResourceBundle
		private static Logger logger = Logger.getLogger(DBActionsMain.class);
		private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
		
		public static void main(String[] args) {
			
			logger.info("Starting DBActions main");
			
			//For Days with 30k, just need to run this with the dates to update the table
			//DBWriteHelper.update30K("5/17/2017","5/18/2017");
			
			//For other days, you need to specify a particular DataRun to copy the data to LifetimeTickets
			//TODO: write the code to take a particular run and put it in the lifetime
			DBWriteHelper.commitRun(8L);
			
			//Show who is short given a particular data run
			//DBWriteHelper.showShort(7L);
			
			logger.info("Exiting DBActions");
		}
		
		
	
}
