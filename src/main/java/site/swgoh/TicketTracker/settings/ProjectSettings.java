package site.swgoh.TicketTracker.settings;

import java.util.ResourceBundle;

public class ProjectSettings {

	private static ProjectSettings ps;
	
	private ResourceBundle bundle;
	
	private ProjectSettings() {
		
	}

	public static ProjectSettings getInstance(){
		if (ps == null){
			ps = new ProjectSettings();
		}
		
		return ps;
	}
}
