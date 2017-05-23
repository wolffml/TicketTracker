package site.swgoh.TicketTraker.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

public class ProcessingPanel extends CalcPanel {

	private static final long serialVersionUID = 2930096542884969203L;
	private static Logger logger = Logger.getLogger(ProcessingPanel.class);
	
	//Labels
	private JLabel panelLabel1;
	
	//Calculate Button
	private JButton runImageProcessingButton;
	private JButton runImageOCRButton;
	private JButton runDBUpdateButton;
	
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
		addComponent( panelLabel1, gridRow, 0, 5, 1 );
		gridRow++;
		
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
		addComponent(runImageProcessingButton, gridRow, 1, 1, 2);
		addComponent(runImageOCRButton, gridRow, 3, 1, 2);
		gridRow++;
		
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
				//Need to call the Shape dependent implemenation of the calculate function
				logger.debug("Calculate Button Pressed");
				//String answer = shape.calculateVolume();
				//answerTextArea.append(answer); 

			} else if (event.getSource() == runImageOCRButton) {
				// Clear things
				logger.debug("Clear Button Pressed");
				//answerTextArea.setText(getTextAreaInitialText());
				//shape.clear();
				
			} else if (event.getSource() == runDBUpdateButton){
				//Save things
				logger.debug("Save Button Pressed");
			}
				
		} // actionPerformed
	}
}
