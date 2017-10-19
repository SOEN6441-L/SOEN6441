package gamecontroller;


import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import gameelements.Player;
import gameelements.RiskGame;
import mapelements.Country;
import mapelements.NodeRecord;
import mapelements.RiskMap;


/**
 * StartupPhase is the GUI for players to place one by one their initial given armies on 
 * their own countries.
 * 
 * <p> Player place armies in round-robin fashion.</p>
 * 
 * @see JDialog
 */
public class StartupPhaseView extends JDialog{

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
	
	private RiskMap gameMap;
	private Player[] players;
	private int curTurn,curPlayer;
	private int totalInitialArmies;
	private int totalPlayers;
	private int totalTurn;
	private int[] leftArmies;
	private NodeRecord[][] localCountries;	
	
	public int state=0; //0-Cancel, 1-confirm
	
	/**
	 * This is the Constructor for configuring this GUI.
	 * @param myGame Object of class RiskGame
	 */
	public StartupPhaseView(RiskGame myGame){
		this.gameMap = myGame.getGameMap();
		this.players = myGame.getPlayers();
		
		leftArmies = new int[players.length];
		localCountries = new NodeRecord[players.length][];
		
		setTitle("Startup Phase");

		setSize(width,height);
		int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		setLocation((screenWidth-width)/2, (screenHeight-height)/2);  
		//set exit program when close the window  
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		//not capable adjust windows size  
		setResizable(false);  
		setLayout(null); 
		setModal(true);
		setVisible(false);
		
		Dimension size;
		totalInitialArmies=0;
		totalPlayers = 0;
		//Loop to initial armies belong to each player,and armies in country
		for(int i=0;i<players.length;i++){
			if (players[i].getState()==1){ 
				totalInitialArmies+=players[i].getInitialArmies();
				totalPlayers++;
				leftArmies[i] = players[i].getInitialArmies();
				localCountries[i] = new NodeRecord[players[i].getCountries().size()];
				int j = 0;
				for (Country loopCountry:players[i].getCountries()){
					localCountries[i][j++] = new NodeRecord(loopCountry.countryID, loopCountry.armyNumber);
				}
			}	
		}
		totalTurn = totalInitialArmies/totalPlayers;
		if (totalInitialArmies%totalPlayers!=0) totalTurn++;
		curTurn = 1;
		curPlayer = 0;
				
		turnLabel = new JLabel("TURN "+curTurn+" of "+totalTurn+": ");
		add(turnLabel);  
		turnLabel.setFont(new java.awt.Font("dialog",1,24));
		turnLabel.setForeground(Color.BLACK);
		size = turnLabel.getPreferredSize();
		turnLabel.setBounds(15,15,size.width,size.height); 
		
		if (totalInitialArmies>0){
			while (players[curPlayer].getState()!=1||leftArmies[curPlayer]==0){
				int tempPlayer=(curPlayer+1)%players.length;
				if (tempPlayer<curPlayer) curTurn++;
				curPlayer = tempPlayer;			
			}
		}
		
		playerLabel =  new JLabel(players[curPlayer].getName());
		add(playerLabel);  
		playerLabel.setFont(new java.awt.Font("dialog",1,18));
		playerLabel.setForeground(players[curPlayer].getMyColor());
		playerLabel.setBounds(turnLabel.getBounds().x+size.width+20,15,size.width,size.height); 	
		
		InitialArmy = new JLabel("Initial given armies: "+leftArmies[curPlayer]);
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
		for (int i=0;i<localCountries[curPlayer].length;i++) { 
			Country loopCountry = gameMap.findCountryByID(localCountries[curPlayer][i].ID);
			myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.countryName
					+" (In "+loopCountry.belongToContinent.continentName+", "+localCountries[curPlayer][i].Number+" armies)"));
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
							localCountries[curPlayer][selRow-1].Number++;
							int tempPlayer=(curPlayer+1)%players.length;
							if (tempPlayer<curPlayer) curTurn++;
							curPlayer = tempPlayer;
							reloadGUI();
						}
					}
				}
				//popupMenu.show(e.getComponent(), e.getX(), e.getY());
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
		
		cancelBtn = new JButton("Cancel");
		cancelBtn.setMnemonic('c');
		cancelBtn.setDisplayedMnemonicIndex(0);		
		add(cancelBtn);  	     
		size = cancelBtn.getPreferredSize();		
		cancelBtn.setBounds(scrollPaneForCountry.getBounds().x+scrollPaneForCountry.getSize().width-size.width-1,484,size.width,size.height);
		cancelBtn.addActionListener(new cancelBtnHandler());
		
		/*byComputerBtn = new JButton("Continue by Computer");	
		add(byComputerBtn);  	     
		size = byComputerBtn.getPreferredSize();		
		byComputerBtn.setBounds(cancelBtn.getBounds().x-size.width-10,484,size.width,size.height);
		byComputerBtn.addActionListener(new byComputerHandler());
		*/
		
		enterBtn = new JButton("Confirm");	
		add(enterBtn);  	     
		size = enterBtn.getPreferredSize();		
		enterBtn.setBounds(cancelBtn.getBounds().x-size.width-10,78,size.width,size.height);
		enterBtn.setVisible(false);
		enterBtn.addActionListener(new enterBtnHandler());
	}	
	
	/**
	 * This is a method that can refresh the state of all components in current GUI
	 */
	public void reloadGUI(){

		if (totalInitialArmies>0){
			while (players[curPlayer].getState()!=1||leftArmies[curPlayer]==0){
				int tempPlayer=(curPlayer+1)%players.length;
				if (tempPlayer<curPlayer) curTurn++;
				curPlayer = tempPlayer;			
			}
		}
		else {
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
		
		InitialArmy.setText("Initial given armies: "+leftArmies[curPlayer]); 	
		
		countryLabel.setText("Territories ("+players[curPlayer].getCountries().size()+"):");  
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
		for (int i=0;i<localCountries[curPlayer].length;i++) { 
			Country loopCountry = gameMap.findCountryByID(localCountries[curPlayer][i].ID);
			myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.countryName
					+" (In "+loopCountry.belongToContinent.continentName+", "+localCountries[curPlayer][i].Number+" armies)"));
		}
		treeCountry = null;
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
							localCountries[curPlayer][selRow-1].Number++;
							int tempPlayer=(curPlayer+1)%players.length;
							if (tempPlayer<curPlayer) curTurn++;
							curPlayer = tempPlayer;
							reloadGUI();
						}
					}
				}
				//popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});	
		treeCountry.setCellRenderer(new CountryNodeRenderer(players[curPlayer].getMyColor()));
		scrollPaneForCountry.getViewport().removeAll();
		scrollPaneForCountry.getViewport().add(treeCountry);		
	}

	/**
	 * This is a cancel button implements the class ActionListener.
	 * <p> create an invisible cancel button until the end of current phase</p>
	 */
	private class cancelBtnHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			state = 0;
			setVisible(false);
		}
	}	
	
	/**
	 * This is a enter button implements the class ActionListener.
	 * <p> Create an invisible enter button until the end of current phase</p>
	 */
	private class enterBtnHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<players.length;i++){
				if (players[i].getState()==1){ 
					players[i].setInitialArmies(0);
					if (localCountries[i]!=null){
						for (int j=0;j<localCountries[i].length;j++){
							gameMap.findCountryByID(localCountries[i][j].ID).armyNumber = localCountries[i][j].Number;
						}
					}
				}
			}
			state = 1;
			setVisible(false);
		}
	}	
}
 