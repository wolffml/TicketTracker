package site.swgoh.TicketTraker.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import site.swgoh.TicketTracker.helper.FileHelper;

public class ProcessingPanel extends CalcPanel {

	private static final long serialVersionUID = 2930096542884969203L;
	private static Logger logger = Logger.getLogger(ProcessingPanel.class);
	
	private static final ResourceBundle bundleApp = ResourceBundle.getBundle("app");
	private static final String rootDir = bundleApp.getString("rootdir");
	
	//Labels
	private JLabel panelLabel1;
	
	//File picker
	final JFileChooser fc = new JFileChooser();
	
	//Calculate Button
	private JButton runImageProcessingButton;
	private JButton runImageOCRButton;
	private JButton runDBUpdateButton;
	
	private JLabel targetDirLabel;
	private JTextField targetDirTextField;
	
	//JTextArea
	private JTextArea infoTextArea;
	private JPanel infoPanel;
	
	public ProcessingPanel(String title, String toolTip, String name) {
		super(title, toolTip, name);
		initializePanel();
	}

	public void initializePanel(){
		
		logger.info("Initializing the panel: " + name);
		int gridRow = 0;
		
		//Top Label
		panelLabel1 = new JLabel(getResourseString("label"), SwingConstants.CENTER);
		panelLabel1.setFont(new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(),Font.PLAIN, 28));
		panelLabel1.setLabelFor(this);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.weighty = GridBagConstraints.FIRST_LINE_START;
		addComponent( panelLabel1, gridRow, 0, 5, 1 );
		gridRow++;
		
		targetDirLabel = new JLabel("Target Directory");
		targetDirTextField = new JTextField("Select from button.", 20);
		targetDirLabel.setLabelFor(targetDirTextField);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weighty = GridBagConstraints.FIRST_LINE_START;
		addComponent(targetDirLabel, gridRow, 0,2,1);
		constraints.anchor = GridBagConstraints.EAST;
		addComponent(targetDirTextField, gridRow, 2,3,1);
		gridRow = gridRow + 2;
		
		
		//Now for the Buttons - Calculate, Clear and Save
		logger.debug("Creating the Button Handler");
		ButtonHandler buttonHandler = new ButtonHandler();
		logger.debug("Creating the calculate button");
		runImageProcessingButton = new JButton(getResourseString("runimageprocess.button"));
		runImageProcessingButton.setMnemonic(getResourseString("runimageprocess.button.mnemonic").charAt(0));
		runImageProcessingButton.addActionListener(buttonHandler);
		runImageOCRButton = new JButton(getResourseString("runImageOCR.button"));
		runImageOCRButton.setMnemonic(getResourseString("runImageOCR.button.mnemonic").charAt(0));
		runImageOCRButton.addActionListener(buttonHandler);
		runDBUpdateButton = new JButton(getResourseString("runDBUpdate.button"));
		runDBUpdateButton.setMnemonic(getResourseString("runDBUpdate.button.mnemonic").charAt(0));
		runDBUpdateButton.addActionListener(buttonHandler);
		addComponent(runImageProcessingButton, gridRow, 0, 1, 2);
		addComponent(runImageOCRButton, gridRow, 2, 1, 2);
		addComponent(runDBUpdateButton, gridRow, 4, 1, 2);
		gridRow=gridRow + 3;;
		
		//Answer Panel
		infoPanel = new JPanel();
		infoTextArea = new JTextArea("", 10, 30);
		infoTextArea.setEditable(false);
		infoPanel.add(new JScrollPane(infoTextArea));
		addComponent(infoPanel, gridRow, 0, 5, 5);
	}
	
	
	
	private class ButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			//See which button was pressed
			if (event.getSource() == runImageProcessingButton) {
				logger.info("Process Image Files Button Pressed");
				//String dailyDirString = FileHelper.mkDailyDir(rootDir);
				ProcessingPanel.this.infoTextArea.setText("Uploading files to Amazon S3.\n");
		    	
		    	String uploadToAmazon = bundleApp.getString("activites.aws.writetobucket");
		    	if (uploadToAmazon.toLowerCase().equals("true")){
		    		FileHelper.moveToAmazon(rootDir, "*.png");
		    	}
		    	ProcessingPanel.this.infoTextArea.append("Completed uploading files to Amazon S3.\n");
		    	ProcessingPanel.this.infoTextArea.append("Moving files to daily folders.\n");
		        //Move the screenshot files from the root directory to the created daily folder
		        //FileHelper.moveFiles(rootDir, dailyDirString, "*.png");
		        FileHelper.moveFiles(rootDir, "*.png");
		        ProcessingPanel.this.infoTextArea.append("Completed moving files to daily folders.");
				

			} else if (event.getSource() == runImageOCRButton) {
				
				logger.debug("Run Image OCR Button Button Pressed");
				
				//answerTextArea.setText(getTextAreaInitialText());
				//shape.clear();
				
			} else if (event.getSource() == runDBUpdateButton){
				logger.debug("RunDBUpdateButton");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setCurrentDirectory(new File(bundleApp.getString("rootdir")));
				int returnVal = fc.showOpenDialog(ProcessingPanel.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	File file = fc.getSelectedFile();
		        	ProcessingPanel.this.infoTextArea.setText("Uploading data from dir: " + file.getAbsolutePath() + "\n");
		        	
		        }
			}
				
		} // actionPerformed
	}
}
