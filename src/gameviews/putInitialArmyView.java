package gameviews;


import java.awt.Color;

import java.awt.Dimension;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import gamemodels.ObservableNodes;
import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;
import mapmodels.CountryModel;
import mapmodels.RiskMapModel;


/**
 * Class to define the GUI for players to place one by one their initial given armies on 
 * their own countries.
 * 
 * <p> Player place armies in round-robin fashion.</p>
 * 
 * @see JDialog
 */
public class putInitialArmyView extends JDialog{

	private static final long serialVersionUID = 1L;
	//components in this window
	JLabel playerLabel;
	JLabel countryLabel;
	JLabel InitialArmy;
	JLabel turnLabel;
	JTree treeCountry;
	JLabel promptLabel;
	JScrollPane scrollPaneForCountry;
	JButton cancelBtn;
	JButton byComputerBtn;
	JButton enterBtn;
	private int width= 420,height = 560;
	
	private RiskMapModel gameMap;
	private PlayerModel[] players;
	private int curTurn,curPlayer;
	private int totalInitialArmies;
	private int totalPlayers;
	private int totalTurn;
	private int[] leftArmies;
	private ObservableNodes localCountries;	
	
	public int state=0; //0-Cancel, 1-confirm
	
	/**
	 * This is the Constructor for configuring this GUI.
	 * @param myGame Object of class RiskGame
	 */
	public putInitialArmyView(RiskGameModel myGame){
		this.gameMap = myGame.getGameMap();
		this.players = myGame.getPlayers();
		
		leftArmies = new int[players.length];
		localCountries = myGame.getLocalCountries();
		setTitle("Startup Phase - Initial Armies");

		setSize(width,height);
		int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		setLocation((screenWidth-width)/2, (screenHeight-height)/2);  		  
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);//set hide window, when close the window
		setResizable(false);  
		setLayout(null); 
		setModal(true);
		setVisible(false);
		
		Dimension size;
		totalInitialArmies=0;
		totalPlayers = 0;
		//Loop to initial armies belong to each player,and armies in country
		for(int i=0;i<players.length;i++){
			if (players[i].getState()){ 
				totalInitialArmies+=myGame.getInitialArmies();
				totalPlayers++;
				leftArmies[i] = myGame.getInitialArmies();
			}	
		}
		totalTurn = totalInitialArmies/totalPlayers;
		if (totalInitialArmies%totalPlayers!=0) totalTurn++;
		curTurn = 1;
		curPlayer = -1;
				
		turnLabel = new JLabel("TURN "+curTurn+" of "+totalTurn+": ");
		add(turnLabel);  
		turnLabel.setFont(new java.awt.Font("dialog",1,24));
		turnLabel.setForeground(Color.BLACK);
		size = turnLabel.getPreferredSize();
		turnLabel.setBounds(15,15,size.width+15,size.height);
		
		findNextPlayer();
		
		playerLabel =  new JLabel(players[curPlayer].getName());
		add(playerLabel);  
		playerLabel.setFont(new java.awt.Font("dialog",1,18));
		playerLabel.setForeground(players[curPlayer].getMyColor());
		playerLabel.setBounds(turnLabel.getBounds().x+size.width+15,15,size.width,size.height); 	
		
		InitialArmy = new JLabel("Available initial given armies: "+leftArmies[curPlayer]);
		add(InitialArmy);  
		InitialArmy.setFont(new java.awt.Font("dialog",1,18));
		InitialArmy.setForeground(Color.BLACK);
		size = InitialArmy.getPreferredSize();
		InitialArmy.setBounds(20,60,size.width,size.height); 	
		
		countryLabel = new JLabel("Territories ("+players[curPlayer].getCountries().size()+"):");  
		add(countryLabel);  
		countryLabel.setFont(new java.awt.Font("dialog",1,15));
		size = countryLabel.getPreferredSize();
		countryLabel.setBounds(15,100,size.width,size.height);   
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
		for (int i=0;i<localCountries.getNodes()[curPlayer].length;i++) { 
			CountryModel loopCountry = gameMap.findCountry(localCountries.getNodes()[curPlayer][i].getName());
			myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getShowName()
					+" (In "+loopCountry.getBelongTo().getShowName()+", "+localCountries.getNodes()[curPlayer][i].getNumber()+" armies)"));
		}
		treeCountry= new JTree(myTreeRoot);
		treeCountry.addMouseListener( new  MouseAdapter(){
			public void mousePressed(MouseEvent e){
				int selRow = treeCountry.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = treeCountry.getPathForLocation(e.getX(), e.getY());
				if (selRow!=-1 && (e.getClickCount()==2)){
					treeCountry.setSelectionPath(selPath);
					if (selPath!=null) {
						if (selPath.getParentPath()==null){ //root node
						}
						else if (selPath.getParentPath().getParentPath()==null){//countries
							totalInitialArmies--;
							leftArmies[curPlayer]--;
							localCountries.increaseValue(curPlayer,selRow-1);
							findNextPlayer();
							reloadGUI(true);
						}
					}
				}
			}
		});
				
		treeCountry.setCellRenderer(new CountryNodeRenderer(players[curPlayer].getMyColor()));

		scrollPaneForCountry= new JScrollPane(treeCountry,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPaneForCountry);  
		scrollPaneForCountry.setBounds(15,125,385,330);	

		
		promptLabel = new JLabel("Double click one country to place one army each turn.");  
		add(promptLabel);  
		promptLabel.setFont(new java.awt.Font("dialog",1,13));
		promptLabel.setForeground(Color.RED);
		size = promptLabel.getPreferredSize();
		promptLabel.setBounds(15,458,size.width,size.height);
		
		ButtonHandler buttonHandler = new ButtonHandler();
		cancelBtn = new JButton("Cancel");
		cancelBtn.setMnemonic('c');
		cancelBtn.setDisplayedMnemonicIndex(0);		
		add(cancelBtn);  	     
		size = cancelBtn.getPreferredSize();		
		cancelBtn.setBounds(scrollPaneForCountry.getBounds().x+scrollPaneForCountry.getSize().width-size.width-1,484,size.width,size.height);
		cancelBtn.addActionListener(buttonHandler);
		
		byComputerBtn = new JButton("Continue by Computer");	
		add(byComputerBtn);  	     
		size = byComputerBtn.getPreferredSize();		
		byComputerBtn.setBounds(cancelBtn.getBounds().x-size.width-10,484,size.width,size.height);
		byComputerBtn.addActionListener(buttonHandler);
		
		
		enterBtn = new JButton("Confirm");	
		add(enterBtn);  	     
		size = enterBtn.getPreferredSize();		
		enterBtn.setBounds(cancelBtn.getBounds().x-size.width-10,78,size.width,size.height);
		enterBtn.setVisible(false);
		enterBtn.addActionListener(buttonHandler);
	}	

	/**
	 * Method to find the next valid player
	 * @return succeed or not
	 */
	public Boolean findNextPlayer(){
		if (totalInitialArmies==0) return false;
		do {
			int tempPlayer=(curPlayer+1)%players.length;
			if (tempPlayer<curPlayer) curTurn++;
			curPlayer = tempPlayer;			
		}while (!players[curPlayer].getState()||leftArmies[curPlayer]==0);
		return true;
	}
		
	/**
	 * This is a method that can refresh the state of all components in current GUI
	 * @param allowClick decide if the continent tree response to user's click 
	 */
	public void reloadGUI(Boolean allowClick){
		if (totalInitialArmies==0){
			turnLabel.setText("All turns done!");
			Dimension size = turnLabel.getPreferredSize();
			turnLabel.setBounds(15,15,size.width,size.height);
			playerLabel.setVisible(false);
			InitialArmy.setVisible(false); 		
			countryLabel.setVisible(false);  
			scrollPaneForCountry.getViewport().removeAll();
			scrollPaneForCountry.setVisible(false);
			promptLabel.setVisible(false);
			enterBtn.setVisible(true);
			cancelBtn.setBounds(cancelBtn.getBounds().x,78,cancelBtn.getSize().width,cancelBtn.getSize().height);
			setSize(width,160);
			return;
		}		
		turnLabel.setText("TURN "+curTurn+" of "+totalTurn+": ");
		
		playerLabel.setText(players[curPlayer].getName());
		playerLabel.setForeground(players[curPlayer].getMyColor());
		
		InitialArmy.setText("Available initial given armies: "+leftArmies[curPlayer]); 	
		
		countryLabel.setText("Territories ("+players[curPlayer].getCountries().size()+"):");  
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
		for (int i=0;i<localCountries.getNodes()[curPlayer].length;i++) { 
			CountryModel loopCountry = gameMap.findCountry(localCountries.getNodes()[curPlayer][i].getName());
			myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getShowName()
					+" (In "+loopCountry.getBelongTo().getShowName()+", "+localCountries.getNodes()[curPlayer][i].getNumber()+" armies)"));
		}
		treeCountry = null;
		treeCountry= new JTree(myTreeRoot);
		if (allowClick){
			treeCountry.addMouseListener( new  MouseAdapter(){
				public void mousePressed(MouseEvent e){
					int selRow = treeCountry.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = treeCountry.getPathForLocation(e.getX(), e.getY());
					if (selRow>0 && (e.getClickCount()==2)){
						treeCountry.setSelectionPath(selPath);
						if (selPath!=null) {
							if (selPath.getParentPath()==null){ //root node
							}
							else if (selPath.getParentPath().getParentPath()==null){//countries
								totalInitialArmies--;
								leftArmies[curPlayer]--;
								localCountries.increaseValue(curPlayer,selRow-1);
								findNextPlayer();
								reloadGUI(true);
							}
						}
					}
				}	
			});
		};	
		treeCountry.setCellRenderer(new CountryNodeRenderer(players[curPlayer].getMyColor()));
		scrollPaneForCountry.getViewport().removeAll();
		scrollPaneForCountry.getViewport().add(treeCountry);		
	}

	/**
	 * Method to define action performed according to different users' action.
	 * @see ActionListener
	 */
	private class ButtonHandler implements ActionListener {
		/**
		 * action actionPerformed
		 * @param e event of button
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			String buttonName = e.getActionCommand();
			switch (buttonName){
			case "Cancel":
				state = 0;
				setVisible(false);
				break;
			case "Confirm":
				confirmInput();
				break;
			case "Continue by Computer":
				byComputer();
				break;				
			}
		}
	}	
	
	/**
	 * confirm the operation
	 */
	public void confirmInput() { 
		state = 1;
		setVisible(false);
	}
	
	/**
	 * Method to define a swing worker to do put armies job in background.
	 */
	private void byComputer() { 
		SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
			/**
			 * Method to define background job
			 */
			@Override
			protected Void doInBackground() {
				do {
					int children = localCountries.getNodes()[curPlayer].length;
					int randomCountryIndex = (int)(Math.random()*children);
					totalInitialArmies--;
					leftArmies[curPlayer]--;
					localCountries.increaseValue(curPlayer,randomCountryIndex);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					publish(1);
				}while (findNextPlayer());
				return null;
			}

			/**
			 * Method to handle process
			 * @param chunks the info
			 */
			@Override
			protected void process(List<Integer> chunks) {
				reloadGUI(false);
			}
			/**
			 * Method to handle done
			 */
			@Override
			protected void done() {
				cancelBtn.setVisible(true);
			}
		};
		byComputerBtn.setVisible(false);
		cancelBtn.setVisible(false);
		reloadGUI(false);
		worker.execute();
	}	
	
	/**
	 * Method to define a swing worker to do put armies job in background.
	 */
	public void byComputerManul() { 
		byComputerBtn.setVisible(false);
		cancelBtn.setVisible(false);
		reloadGUI(false);
		do {
			int children = localCountries.getNodes()[curPlayer].length;
			int randomCountryIndex = (int)(Math.random()*children);
			totalInitialArmies--;
			leftArmies[curPlayer]--;
			localCountries.increaseValue(curPlayer,randomCountryIndex);
		}while (findNextPlayer());
	}
	
}
 