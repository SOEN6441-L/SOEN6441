package gamecontroller;

import java.awt.Color;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;

import gameelements.RiskGame;

/**
 * StartupPhase is the GUI for players to place one by one their initial given armies on 
 * their own countries.
 * 
 * <p> Player place armies in round-robin fashion.</p>
 * 
 * @see JFrame
 */
public class PhaseView extends JFrame implements Observer{
	private static final long serialVersionUID = 1L;
	//components in this window
	JLabel phaseNameLabel;
	JLabel gameStageLabel1;
	JLabel gameStageLabel2;
	JLabel gameStageLabel3;
	JLabel gameStageLabel4;
	JLabel turnLabel;
	JLabel playerLabel;
    private int width= 420,height = 560;
    private RiskGame myGame;

	/**
	 * This is the Constructor for configuring this GUI.
	 * @param myGame Object of class RiskGame
	 */
	public PhaseView(RiskGame myGame){
		this.myGame = myGame;
		setTitle("Risk Game - Phase View");
		setSize(width,height);
		setLocation(5, 5);  
		//set exit program when close the window  
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//not capable adjust windows size  
		setResizable(false);  
		setLayout(null); 

		Dimension size;
		phaseNameLabel = new JLabel("");
		add(phaseNameLabel);  
		phaseNameLabel.setFont(new java.awt.Font("dialog",1,24));
		phaseNameLabel.setForeground(Color.BLACK);
		size = phaseNameLabel.getPreferredSize();
		phaseNameLabel.setBounds(15,15,size.width,size.height); 
		
		gameStageLabel1 = new JLabel("");
		add(gameStageLabel1);  
		gameStageLabel1.setFont(new java.awt.Font("dialog",1,18));
		gameStageLabel1.setForeground(Color.RED);
		size = gameStageLabel1.getPreferredSize();
		gameStageLabel1.setBounds(25,50,size.width,size.height); 
		
		gameStageLabel2 = new JLabel("");
		add(gameStageLabel2);  
		gameStageLabel2.setFont(new java.awt.Font("dialog",1,18));
		gameStageLabel2.setForeground(Color.RED);
		size = gameStageLabel2.getPreferredSize();
		gameStageLabel2.setBounds(25,80,size.width,size.height); 
		gameStageLabel2.setVisible(false);

		gameStageLabel3 = new JLabel("");
		add(gameStageLabel3);  
		gameStageLabel3.setFont(new java.awt.Font("dialog",1,18));
		gameStageLabel3.setForeground(Color.RED);
		size = gameStageLabel3.getPreferredSize();
		gameStageLabel3.setBounds(25,110,size.width,size.height); 
		gameStageLabel3.setVisible(false);

		gameStageLabel4 = new JLabel("Startup phase finished");
		add(gameStageLabel4);  
		gameStageLabel4.setFont(new java.awt.Font("dialog",1,18));
		gameStageLabel4.setForeground(Color.RED);
		size = gameStageLabel4.getPreferredSize();
		gameStageLabel4.setBounds(25,140,size.width,size.height); 
		gameStageLabel4.setVisible(false);
		
		setVisible(true);
	}
	
	public void update(Observable obs, Object x) {
		Dimension size;
		int type = (Integer) x;
		if (type == 0){//only text information, just show it.
			phaseNameLabel.setText(((RiskGame)obs).getPhaseString());
			size = phaseNameLabel.getPreferredSize();
			phaseNameLabel.setBounds(15,15,size.width,size.height); 
		}
		else if (type == 1){//only text information, just show it.
			switch (((RiskGame)obs).getGameStage()){
				case 0:
					gameStageLabel1.setForeground(Color.RED);
					gameStageLabel1.setText("Step 1 - Load a risk map");
					gameStageLabel2.setVisible(false);
					gameStageLabel3.setVisible(false);
					gameStageLabel4.setVisible(false);
					break;
				case 1:
					gameStageLabel1.setText("Step 1 - Loading risk map ...");
					break;
				case 2:
					gameStageLabel1.setText("Step 1 - Loading risk map ... failed");
					break;	
				case 3:
					gameStageLabel1.setText("Step 1 - Loading risk map ... canceled");
					break;						
				case 10:	
					gameStageLabel1.setForeground(Color.BLACK);
					gameStageLabel1.setText("Step 1 - Loading risk map ... succeed");
					gameStageLabel2.setForeground(Color.RED);
					gameStageLabel2.setVisible(true);
					gameStageLabel2.setText("Step 2 - Create players");
					gameStageLabel3.setVisible(false);
					gameStageLabel4.setVisible(false);
					break;	
				case 11:
					gameStageLabel2.setText("Step 2 - Creating players ...");
					break;
				case 12:
					gameStageLabel2.setText("Step 2 - Creating players ... canceled");
					break;
				case 20:
					gameStageLabel2.setForeground(Color.BLACK);
					gameStageLabel2.setText("Step 2 - Creating players ... succeed");
					gameStageLabel3.setVisible(true);
					gameStageLabel3.setForeground(Color.RED);
					gameStageLabel3.setText("Step 3 - Place intial armies");
					gameStageLabel4.setVisible(false);
					break;	
				case 21:
					gameStageLabel3.setText("Step 3 - Placing intial armies ...");
					break;	
				case 22:
					gameStageLabel3.setText("Step 3 - Placing intial armies ... canceled");
					break;
				case 30:
					gameStageLabel3.setForeground(Color.BLACK);
					gameStageLabel3.setText("Step 3 - Placing intial armies ... succeed");
					gameStageLabel3.setVisible(true);
					gameStageLabel4.setVisible(true);
					break;				
				case 40:
					gameStageLabel1.setVisible(false);
					gameStageLabel2.setVisible(false);
					gameStageLabel3.setVisible(false);
					gameStageLabel4.setVisible(false);
					break;					
			}
			size = gameStageLabel1.getPreferredSize();
			gameStageLabel1.setBounds(25,50,size.width,size.height); 
			size = gameStageLabel2.getPreferredSize();
			gameStageLabel2.setBounds(25,80,size.width,size.height);
			size = gameStageLabel3.getPreferredSize();
			gameStageLabel3.setBounds(25,110,size.width,size.height); 
		}
	}
}
 