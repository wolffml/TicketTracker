package site.swgoh.TicketTraker.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;




public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -432312579468274361L;
	private static Logger logger = Logger.getLogger(MainFrame.class);
	public static final ResourceBundle bundle = ResourceBundle.getBundle("site.swgoh.TicketTraker.gui.settings");
	
	private JTabbedPane tabbedPane;
	private CalcPanel[] panels = { 	new ProcessingPanel("Processing","Image Processing","processing"),
									new CalcPanel("DB Actions", "Customer data entry", "db"),
									new CalcPanel("Settings","Program Settings","settings")
								};
	
	
	public MainFrame(){
		super(bundle.getString("window.title"));
		logger.debug("Setting window title.");
		
		//Create a JTabbedPane
		logger.debug("Creating Tabs");
		tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(new EmptyBorder(10,10,0,10));
		//Add tabs to the pane
		//Tab 1 - Pools
		logger.info("Creating Panel and Adding");
		
		for (CalcPanel panel : panels){
			logger.info("Adding panel titled: " + panel.getTitle());
			tabbedPane.addTab(panel.getTitle(), null, panel, panel.getToolTip());
		}
		
		//Add a footer panel for the exit button
		JPanel panelFooter = new JPanel();
		panelFooter.setLayout(new FlowLayout());
		
		JButton exitButton = new JButton("Exit");
		exitButton.setSize(getMinimumSize());
		exitButton.setMnemonic('x');
		ExitHandler exitHandler = new ExitHandler();
		exitButton.addActionListener(exitHandler);
		
		panelFooter.add(exitButton);
		
		add(tabbedPane, BorderLayout.CENTER);
		add(panelFooter, BorderLayout.SOUTH);
	}
	
	private class ExitHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			logger.info("Exiting program.");
			int height = MainFrame.this.getHeight();
			int width = MainFrame.this.getWidth();
			logger.info("Height: " + height + "\tWidth: " + width);
			MainFrame.this.dispose();
			System.exit(0);
		}
	}
	
	
	
	public static MainFrame build(){
		logger.info("Creating the main frame.");
		MainFrame frame = new MainFrame();
		logger.debug("Setting default close action.");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		logger.debug("Setting Image Icon");
		frame.setIconImage(new ImageIcon( bundle.getString("window.icon")).getImage());
		
		
		try{
			//Determine Height and Width of the JFrame
			int height = Integer.parseInt(bundle.getString("window.height"));
			int width = Integer.parseInt(bundle.getString("window.width"));
			logger.debug("Height: " + height + "\tWidth: " + width);
			frame.setSize(width, height);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
			frame.setVisible(true);
			
		} catch (NumberFormatException e){
			e.printStackTrace();
			logger.error(e.toString());
		}
		
		return frame;
	}
	

}
