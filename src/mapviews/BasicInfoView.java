package mapviews;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import mapmodels.RiskMapModel;

/**
 * This is a view class to show basic information of game.
 * <p> When player save their map in mapEditor, this is the basic information 
 * of new map</p>
 * @see JDialog
 */
public class BasicInfoView extends JDialog{
	private static final long serialVersionUID = 1L;
	//components
	private JButton proceedBtn;
	private JButton cancelBtn;

	private JTextField authorTF;
	private JTextField imageTF;

	private ButtonGroup warnRadio;;
	private JRadioButton warnYes;
	private JRadioButton warnNo;

	private ButtonGroup wrapRadio;
	private JRadioButton wrapYes;
	private JRadioButton wrapNo;

	private ButtonGroup scrollRadio;
	private JRadioButton scrollHor;
	private JRadioButton scrollVer;
	private JRadioButton scrollNone;
	
	private JLabel warnAuthor;
	private JLabel warnWarn;
	private JLabel warnWrap;
	private JLabel warnImage;
	private JLabel warnScroll;
	
	private RiskMapModel curMap;	
	private int state=0; //0-Cancel, 1-continue
	
	/**
	 * Constructor of class.
	 * <p> configuring GUI of the class</p>
	 * @param map	Object of RiskMap
	 * @param checkWarning return value of mapelements.RiskMap#checkWarnings(), to indicate warnings related to the 5 basic information
	 * @param mode	Indicator, affect size of the GUI
	 * @see mapmodels.RiskMapModel#checkWarnings()
	 */
	public BasicInfoView(RiskMapModel map, int checkWarning, int mode){///mode 0-upon save, 1-upon load
		//configuration
		setTitle("Basic information");
		this.curMap = map;
		int width;
		if (mode==1) width = 750;
		else width = 430;
		setSize(width,350);
		int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		setLocation((screenWidth-width)/2, (screenHeight-350)/2);  
		//set exit program when close the window  
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		//not capable adjust windows size  
		setResizable(false);  
		setLayout(null); 
		Dimension size;
		
		int curWarning;
		curWarning = checkWarning%2;
		if (curWarning == 1){
			warnAuthor = new JLabel("Warning: Parameter 'author' can't be empty, set to default value 'anonymous'");
			warnAuthor.setForeground(Color.RED);
		}	
		else {
			warnAuthor = new JLabel("Default value is 'anonymous'");
			warnAuthor.setForeground(Color.BLACK);
		}
		add(warnAuthor);  
		warnAuthor.setFont(new java.awt.Font("dialog",1,13));
		size = warnAuthor.getPreferredSize();
		warnAuthor.setBounds(230,25,size.width,size.height); 
		JLabel labelAuthor = new JLabel("Author: ");
		add(labelAuthor);  
		labelAuthor.setFont(new java.awt.Font("dialog",1,14));
		size = labelAuthor.getPreferredSize();
		labelAuthor.setBounds(15,25,size.width,size.height); 
		authorTF = new JTextField(curMap.getAuthor());
		authorTF.setFont(new java.awt.Font("dialog",1,13));
		add(authorTF);
		authorTF.setBounds(labelAuthor.getBounds().x+size.width+5,25,140,size.height);	
		
		checkWarning = checkWarning/2;
		curWarning = checkWarning%2;
		if (curWarning == 1){
			warnWarn = new JLabel("Warning: Parameter 'warn' is invalid, set to default value 'yes'");
			warnWarn.setForeground(Color.RED);
		}
		else{
			warnWarn = new JLabel("Default value is 'yes'");
			warnWarn.setForeground(Color.BLACK);
		}
		add(warnWarn);  
		warnWarn.setFont(new java.awt.Font("dialog",1,13));
		size = warnWarn.getPreferredSize();
		warnWarn.setBounds(230,62,size.width,size.height); 		
		JLabel labelWarn = new JLabel("Warn: ");
		add(labelWarn);  
		labelWarn.setFont(new java.awt.Font("dialog",1,14));
		size = labelWarn.getPreferredSize();
		labelWarn.setBounds(15,62,size.width,size.height); 
		warnYes = new JRadioButton("yes");
		warnYes.setSelected(curMap.getWarn().equals("yes"));
		warnNo = new JRadioButton("no");
		warnNo.setSelected(curMap.getWarn().equals("no"));
		warnRadio = new ButtonGroup();		
		warnRadio.add(warnYes);
		warnRadio.add(warnNo);
		add(warnYes);
		warnYes.setBounds(authorTF.getBounds().x-3, 65,50,15);
		add(warnNo);
		warnNo.setBounds(authorTF.getBounds().x+60, 65,40,15);

		checkWarning = checkWarning/2;
		curWarning = checkWarning%2;		
		if (curWarning == 1){
			warnImage = new JLabel("Warning: Parameter 'image' can't be empty, set to default value 'none'");
			warnImage.setForeground(Color.RED);
		}	
		else{
			warnImage = new JLabel("Default value is 'none'");
			warnImage.setForeground(Color.BLACK);			
		}
		add(warnImage);  
		warnImage.setFont(new java.awt.Font("dialog",1,13));
		size = warnImage.getPreferredSize();
		warnImage.setBounds(230,99,size.width,size.height);		
		JLabel labelImage = new JLabel("Image: ");
		add(labelImage);  
		labelImage.setFont(new java.awt.Font("dialog",1,14));
		size = labelImage.getPreferredSize();
		labelImage.setBounds(15,99,size.width,size.height); 
		imageTF = new JTextField(curMap.getImage());
		imageTF.setFont(new java.awt.Font("dialog",1,13));
		add(imageTF);
		imageTF.setBounds(authorTF.getBounds().x,99,140,size.height);

		checkWarning = checkWarning/2;
		curWarning = checkWarning%2;
		if (curWarning == 1){
			warnWrap = new JLabel("Warning: Parameter 'wrap' is invalid, set to default value 'no'");
			warnWrap.setForeground(Color.RED);
		}
		else{
			warnWrap = new JLabel("Default value is 'no'");
			warnWrap.setForeground(Color.BLACK);		
		}
		add(warnWrap);  
		warnWrap.setFont(new java.awt.Font("dialog",1,13));
		size = warnWrap.getPreferredSize();
		warnWrap.setBounds(230,136,size.width,size.height); 		
		
		JLabel labelWrap = new JLabel("Wrap: ");
		add(labelWrap);  
		labelWrap.setFont(new java.awt.Font("dialog",1,14));
		size = labelWrap.getPreferredSize();
		labelWrap.setBounds(15,136,size.width,size.height); 
		wrapYes = new JRadioButton("yes");
		wrapYes.setSelected(curMap.getWrap().equals("yes"));
		wrapNo = new JRadioButton("no");
		wrapNo.setSelected(curMap.getWrap().equals("no"));
		wrapRadio = new ButtonGroup();		
		wrapRadio.add(wrapYes);
		wrapRadio.add(wrapNo);
		add(wrapYes);
		wrapYes.setBounds(authorTF.getBounds().x-3,139,50,15);
		add(wrapNo);
		wrapNo.setBounds(authorTF.getBounds().x+60,139,40,15);	
		
		checkWarning = checkWarning/2;
		curWarning = checkWarning%2;
		if (curWarning == 1){
			warnScroll = new JLabel("Warning: Parameter 'scroll' is invalid, set to default value 'none'");
			warnScroll.setForeground(Color.RED);
		}
		else{
			warnScroll = new JLabel("Default value is 'none'");
			warnScroll.setForeground(Color.BLACK);			
		}
		add(warnScroll);  
		warnScroll.setFont(new java.awt.Font("dialog",1,13));
		size = warnScroll.getPreferredSize();
		warnScroll.setBounds(230,183,size.width,size.height); 		
		JLabel labelScroll = new JLabel("Scroll:");
		add(labelScroll);  
		labelScroll.setFont(new java.awt.Font("dialog",1,14));
		size = labelScroll.getPreferredSize();
		labelScroll.setBounds(15,183,size.width,size.height); 
		scrollHor = new JRadioButton("horizontal");
		scrollHor.setSelected(curMap.getScroll().equals("horizontal"));
		scrollVer = new JRadioButton("vertical");
		scrollVer.setSelected(curMap.getScroll().equals("vertical"));
		scrollNone = new JRadioButton("none");
		scrollNone.setSelected(curMap.getScroll().equals("none"));
		
		scrollRadio = new ButtonGroup();		
		scrollRadio.add(scrollHor);
		scrollRadio.add(scrollVer);
		scrollRadio.add(scrollNone);
		add(scrollHor);
		scrollHor.setBounds(authorTF.getBounds().x-3,176,90,15);
		add(scrollVer);
		scrollVer.setBounds(authorTF.getBounds().x-3,196,80,15);	
		add(scrollNone);
		scrollNone.setBounds(authorTF.getBounds().x+85,176,60,15);	
		
		proceedBtn = new JButton("Proceed");		
		proceedBtn.setMnemonic('p');
		proceedBtn.setDisplayedMnemonicIndex(0);
		add(proceedBtn);  	     
		size = proceedBtn.getPreferredSize();
		proceedBtn.setBounds(width/2-size.width-39,250,size.width+29,size.height+10);
		proceedBtn.addActionListener(new proceedHandler());
		
		cancelBtn = new JButton("Cancel");		
		cancelBtn.setMnemonic('c');
		cancelBtn.setDisplayedMnemonicIndex(0);
		add(cancelBtn);  	     
		cancelBtn.setBounds(width/2+10,250,size.width+29,size.height+10);
		cancelBtn.addActionListener(new cancelHandler());		
		setModal(true);
		//setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
	}

	/**
	 * Method to get dialog return value, 0 - user cancel, 1 - continue 
	 * @return dialog return value
	 */
	public int getState() {
		return state;
	}

	/**
	 * Method to respond user choosing proceed button, which will continues to next step.
	 */
	private class proceedHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			if (authorTF.getText()==null||authorTF.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null,"Parameter author can't be empty.");
				authorTF.setText("somebody");
				authorTF.requestFocus();
				return;
			}
			if (imageTF.getText()==null||imageTF.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null,"Parameter image can't be empty.");
				imageTF.setText("none");
				imageTF.requestFocus();
				return;
			}
			curMap.setAuthor(authorTF.getText().trim());
			curMap.setWarn((warnYes.isSelected())?"yes":"no");
			curMap.setWrap((wrapYes.isSelected())?"yes":"no");
			curMap.setImage(imageTF.getText().trim());
			curMap.setScroll((scrollHor.isSelected())?"horizontal":(scrollVer.isSelected())?"vertical":"none");
			state = 1;
			setVisible(false);
		}
	}
	
	/**
	 * Method to respond user cancel the process
	 */
	private class cancelHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			state = 0;
			setVisible(false);
		}
	}	
}
