package site.swgoh.TicketTracker;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import site.swgoh.TicketTraker.gui.MainFrame;

public class MainGUI {

	private static Logger logger = Logger.getLogger(MainGUI.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
    public static void main( String[] args ) {
    	
    	logger.info("Run Dir: " + MainGUI.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    	MainFrame frame = MainFrame.build();
    }
	
	
	
}
