package team.groupfour.risk.gameConfiguration;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

/**
 * This class is used to create a window for users for configuring the game Risk
 * @author Jingyu Lu
 *
 */
public class ConfigureGame {

	// create a window
	JFrame myFrame=new JFrame();
			
	/**
	 * This method is used to configure the game risk
	 */
	public void gameconfiguration(){
		
		//uneditable instruction-text
		JLabel playerLabel=new JLabel("Players in this game: ");
		JLabel continentLabel=new JLabel("How many continents in total?");
		JLabel countryLabel=new JLabel("How many countries in total?");
		
		//editable text
		JTextField playerTextField=new JTextField("Input number of players only!");
		JTextField continentTextField=new JTextField("Input number of continents only!");
		JTextField countryTextField=new JTextField("Input number of countires only!");
		
		//other settings used in code
		//border used in JTextField
		MatteBorder myBorder = new MatteBorder(0, 0, 1, 0, new Color(0, 0, 255));  
		
		//frame configuration
		//layout
		myFrame.setLayout(null);
		//position and size
		myFrame.setBounds(300, 60, 300, 600);
		//title
		myFrame.setTitle("Risk Configuration");
		//cannot adjust window
		myFrame.setResizable(false);
		//visible
		myFrame.setVisible(true);
		//close window
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//text configuration
		playerLabel.setBounds(30, 30, 160, 30);
		playerTextField.setBounds(30, 61, 200, 20);
		playerTextField.setBorder(myBorder);
		
		continentLabel.setBounds(30, 100, 200, 30);
		continentTextField.setBounds(30, 131, 200, 20);
		continentTextField.setBorder(myBorder);
		
		countryLabel.setBounds(30, 170, 200, 30);
		countryTextField.setBounds(30, 201, 200, 20);
		countryTextField.setBorder(myBorder);
		//add text field on frame
		myFrame.add(playerLabel);
		myFrame.add(playerTextField);
		
		myFrame.add(continentLabel);
		myFrame.add(continentTextField);
		
		myFrame.add(countryLabel);
		myFrame.add(countryTextField);
		
	}
	
	/**
	 * This method is for users to start a new risk
	 */
	public void startGame(){
		
		//create button
		JButton startGameBtn=new JButton("Start Risk");
		
		//button configuration
		startGameBtn.setBounds(100, 500, 100, 50);
		startGameBtn.setVisible(true);
		//add button on frame
		myFrame.add(startGameBtn);
		
		startGameBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("loading... ...");
			}
		});
	}
}
