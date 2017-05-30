package site.swgoh.TicketTraker.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import org.apache.log4j.Logger;


//All of the Panels for the Tabbed Pane need to be subclasses of Abstract Panel
public class CalcPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -290872976116235228L;
	private static Logger logger = Logger.getLogger(CalcPanel.class);
	public static final ResourceBundle bundle = ResourceBundle.getBundle("site.swgoh.TicketTraker.gui.settings");
	
	public String title;
	public String toolTip;
	public String name;
	
	//Layouts
	protected GridBagLayout layout;
	protected GridBagConstraints constraints;
	
	public CalcPanel(String title, String toolTip, String name) {
		this.name = name;
		this.title = title;
		this.toolTip = toolTip;
		layout = new GridBagLayout();
		this.setLayout(layout);
		constraints = new GridBagConstraints();
		constraints.weightx=0;//0.00001;
		constraints.weighty=0;//0.00001;
		constraints.insets = new Insets(0, 0, 10, 10);
	}
	
	//Just a helper method to make GridBagLayout easier
	protected void addComponent(Component component, int row, int column, int width, int height) {
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		layout.setConstraints(component, constraints);
		add(component);
		constraints= new GridBagConstraints();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getToolTip() {
		return toolTip;
	}
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	protected String getResourseString(String key){
		String strReturn = "";
		try{
			strReturn = bundle.getString(name.toLowerCase() + "." + key);
		} catch(Exception e){
			e.printStackTrace();
			logger.error(e.toString());
		}
		return strReturn;
	}
}

