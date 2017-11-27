package gameviewsremote;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import basicclasses.MyPopupMenu;
import basicclasses.MyTree;
import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;

/**
 * PhaseView is the GUI for monitors to see the progress of a game
 * 
 * @see JFrame
 */
public class PhaseViewRemote extends JFrame{
	private static final long serialVersionUID = 1L;
	//components in this window
	private JLabel phaseNameLabel;
	private JLabel gameStageLabel1;
	private JLabel gameStageLabel2;
	private JLabel gameStageLabel3;
	private JLabel gameStageLabel4;
	private JLabel gameStageLabel5;
	
	private JLabel turnLabel;
	private JLabel playerLabel;
	private JLabel playerPhaseLabel;
	
	private JLabel labelContinent;
	private JLabel[] labelPlayers;	
	private MyPopupMenu mapMenu;
	private JMenuItem mExpandAll, mCollapseAll;	
	private MyTree treeContinent;
	private MyTree[] treePlayers;
	private JScrollPane scrollPaneForContinent;
	private JScrollPane[] scrollPaneForPlayers;
	private int continentExpandMode = 1;//1-expand 2-collapse
	private int[] playerExpandMode = {2,2,2,2,2,2};//1-expand 2-collapse
	private int width= 380,height = 560;
    
	private RiskGameModel myGame;
	
	/**
	 * Constructor for PhaseView class.
	 */
	public PhaseViewRemote(){
		setTitle("Risk Game - Phase View");
		setSize(width,height);
		//setLocation(screenWidth+5, 5);  
		setLocation(5, 5); 
		//set exit program when close the window  
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		turnLabel = new JLabel("");
		add(turnLabel);  
		turnLabel.setFont(new java.awt.Font("dialog",1,32));
		turnLabel.setForeground(Color.BLUE);
		turnLabel.setVisible(false);

		playerLabel = new JLabel("");
		add(playerLabel);  
		playerLabel.setFont(new java.awt.Font("dialog",1,22)); 	
		playerLabel.setVisible(false);
		
		playerPhaseLabel = new JLabel("");
		add(playerPhaseLabel);  
		playerPhaseLabel.setFont(new java.awt.Font("dialog",1,18));
		playerPhaseLabel.setVisible(false);	
		
		gameStageLabel1 = new JLabel("");
		add(gameStageLabel1);  
		gameStageLabel1.setFont(new java.awt.Font("dialog",1,15));
		gameStageLabel1.setForeground(Color.RED);
		
		gameStageLabel2 = new JLabel("");
		add(gameStageLabel2);  
		gameStageLabel2.setFont(new java.awt.Font("dialog",1,15));
		gameStageLabel2.setForeground(Color.RED);
		gameStageLabel2.setVisible(false);

		gameStageLabel3 = new JLabel("");
		add(gameStageLabel3);  
		gameStageLabel3.setFont(new java.awt.Font("dialog",1,15));
		gameStageLabel3.setForeground(Color.RED);
		gameStageLabel3.setVisible(false);

		gameStageLabel4 = new JLabel("");
		add(gameStageLabel4);  
		gameStageLabel4.setFont(new java.awt.Font("dialog",1,15));
		gameStageLabel4.setForeground(Color.RED);
		gameStageLabel4.setVisible(false);
		
		gameStageLabel5 = new JLabel("Startup phase finished");
		add(gameStageLabel5);  
		gameStageLabel5.setFont(new java.awt.Font("dialog",1,15));
		gameStageLabel5.setForeground(Color.RED);
		size = gameStageLabel5.getPreferredSize();
		gameStageLabel5.setBounds(25,170,size.width,size.height); 
		gameStageLabel5.setVisible(false);		
		
        //Tree for continents and countries
		labelContinent = new javax.swing.JLabel("Continents (0):");  
		add(labelContinent);  
		labelContinent.setFont(new java.awt.Font("dialog",1,15));
		size = labelContinent.getPreferredSize();
		labelContinent.setBounds(15,230,size.width+200,size.height);   
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");
		treeContinent= new MyTree(myTreeRoot);

		scrollPaneForContinent= new JScrollPane(treeContinent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPaneForContinent);  
		scrollPaneForContinent.setBounds(15,255,345,260);	

		ButtonHandler buttonHandler = new ButtonHandler();
		
	    mapMenu = new MyPopupMenu();
	    mExpandAll = new JMenuItem("Expand All",KeyEvent.VK_E);
	    mExpandAll.addActionListener(buttonHandler);	    
	    mCollapseAll = new JMenuItem("Collapse All",KeyEvent.VK_C);
	    mCollapseAll.addActionListener(buttonHandler);
	    
	    mapMenu.add(mExpandAll);
	    mapMenu.addSeparator();
	    mapMenu.add(mCollapseAll);
		
		setVisible(false);
	}

	/**
	 * The method is to reload continents tree
	 */
	private void reloadContinents(){	
		//configuration
		DefaultMutableTreeNode myTreeRoot;
		if (myGame.getGameMap()==null){
			labelContinent.setText("Continents (0):");
			myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");		
		}
		else{
			labelContinent.setText("Continents ("+String.valueOf(myGame.getGameMap().getContinents().size())+"):");
			myTreeRoot = new DefaultMutableTreeNode("Map - "+myGame.getGameMap().getRiskMapName()+" ");

			for (ContinentModel loopContinent : myGame.getGameMap().getContinents()) { 
				ArrayList<CountryModel> loopCountriesList = loopContinent.getCountryList();
				DefaultMutableTreeNode loopContinentNode =  new DefaultMutableTreeNode(loopContinent.getShowName()+" ("+loopContinent.getControlNum()+") ("+loopCountriesList.size()+" countries) "); 
				for (CountryModel loopCountry:loopCountriesList){
					if (loopCountry.getOwner()!=null)
						loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.getShowName()+" ("+loopCountry.getOwner().getName()+", "+loopCountry.getArmyNumber()+" armies) "));						
					else loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.getShowName()+" ("+loopCountry.getArmyNumber()+" armies) "));
				}
				myTreeRoot.add(loopContinentNode);
			}
		}	
		treeContinent= new MyTree(myTreeRoot); 
		treeContinent.addMouseListener( new  MouseAdapter(){
			public void mousePressed(MouseEvent e){
				int selRow = treeContinent.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = treeContinent.getPathForLocation(e.getX(), e.getY());
				if (selRow==0 && (e.getButton() == 3)){
					treeContinent.setSelectionPath(selPath);
					if (selPath!=null) {
						if (selPath.getParentPath()==null){ //root node
							mapMenu.setOwner("treeContinent");
							mapMenu.show(e.getComponent(), e.getX()+5, e.getY()+5);
						}
					}
				}
			}
		}); 		
		treeContinent.setCellRenderer(new ContinentNodeRenderer(myGame.getGameMap()));
		scrollPaneForContinent.getViewport().removeAll();
		scrollPaneForContinent.getViewport().add(treeContinent);
		TreeNode root = (TreeNode) treeContinent.getModel().getRoot();
		if (continentExpandMode==1) treeContinent.expandAll(new TreePath(root),1);
		else {
			if (root.getChildCount() >= 0) {
				for (Enumeration<?> e = root.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = new TreePath(root).pathByAddingChild(n);
					treeContinent.expandAll(path, 2);
				}
			}
		}
	}
	
	/**
	 * The method is to reload all player trees
	 */
	private void reloadPlayers(){
		for (int i=0;i<treePlayers.length;i++)
			reloadPlayers(i,myGame.getPlayers()[i]);	
	}
	
	/**
	 * The method is to reload one player tree
	 * @param index the index of player
	 */
	private void reloadPlayers(int index, PlayerModel player){
		//configuration
		DefaultMutableTreeNode myTreeRoot;
		labelPlayers[index].setText("Player"+(index+1)+" ("+
				player.getTotalArmies()+" armies, "+
				player.getCardsString(0)+" cards)");
		myTreeRoot = new DefaultMutableTreeNode("Countries ("+player.getCountries().size()+")");		
		for (CountryModel loopCountry:player.getCountries()){
			DefaultMutableTreeNode loopPlayerNode =  new DefaultMutableTreeNode(loopCountry.getShowName()+
					" (In "+loopCountry.getBelongTo().getShowName()+", "+loopCountry.getArmyNumber()+" armies) "); 
			myTreeRoot.add(loopPlayerNode);
		}
		treePlayers[index]= new MyTree(myTreeRoot); 
		treePlayers[index].addMouseListener( new  MouseAdapter(){
			public void mousePressed(MouseEvent e){
				int selRow = treePlayers[index].getRowForLocation(e.getX(), e.getY());
				TreePath selPath = treePlayers[index].getPathForLocation(e.getX(), e.getY());
				if (selRow == 0 && (e.getButton() == 3)){
					treePlayers[index].setSelectionPath(selPath);
					if (selPath!=null) {
						if (selPath.getParentPath()==null){ //root node
							mapMenu.setOwner(String.valueOf(index));
							mapMenu.show(e.getComponent(), e.getX()+5, e.getY()+5);
						}
					}	
				}
			}	
		});
		treePlayers[index].setCellRenderer(new CountryNodeRenderer(Color.BLACK));
		scrollPaneForPlayers[index].getViewport().removeAll();
		scrollPaneForPlayers[index].getViewport().add(treePlayers[index]);
		TreeNode root = (TreeNode) treePlayers[index].getModel().getRoot();
		if (playerExpandMode[index]==1) treePlayers[index].expandAll(new TreePath(root),1);
		else {
			if (root.getChildCount() >= 0) {
				for (Enumeration<?> e = root.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = new TreePath(root).pathByAddingChild(n);
					treePlayers[index].expandAll(path, 2);
				}
			}
		}
	}
	
	/**
	 * Class to define local action Listener.
	 */
	private class ButtonHandler implements ActionListener { 
		/**
		 * Method to define action performed according to different users' action.
		 * @param e the action event of user.
		 */	
		public void actionPerformed(ActionEvent e) {
			String buttonName = e.getActionCommand();
			String menuName;
			switch (buttonName){
			case "Expand All":
				menuName = ((MyPopupMenu)(((JMenuItem)(e.getSource())).getParent())).getOwner();
				if (menuName.equals("treeContinent")){
					if (treeContinent!=null){
						continentExpandMode = 1;
						TreeNode root = (TreeNode) treeContinent.getModel().getRoot();
						treeContinent.expandAll(new TreePath(root),1);
					}
				}
				else {
					if (treePlayers[Integer.parseInt(menuName)]!=null){
						playerExpandMode[Integer.parseInt(menuName)] = 1;
						TreeNode root = (TreeNode)treePlayers[Integer.parseInt(menuName)].getModel().getRoot();
						treePlayers[Integer.parseInt(menuName)].expandAll(new TreePath(root),1);
					}
				}
				break;
			case "Collapse All":
				menuName = ((MyPopupMenu)(((JMenuItem)(e.getSource())).getParent())).getOwner();
				if (menuName.equals("treeContinent")){
					if (treeContinent!=null){
						TreeNode root = (TreeNode) treeContinent.getModel().getRoot();
						continentExpandMode=2;
						if (root.getChildCount() >= 0) {
							for (Enumeration<?> eu = root.children(); eu.hasMoreElements();) {
								TreeNode n = (TreeNode) eu.nextElement();
								TreePath path = new TreePath(root).pathByAddingChild(n);
								treeContinent.expandAll(path, 2);
							}
						}
					}
				}	
				else {	
					if (treePlayers[Integer.parseInt(menuName)]!=null){
						TreeNode root = (TreeNode) treePlayers[Integer.parseInt(menuName)].getModel().getRoot();
						playerExpandMode[Integer.parseInt(menuName)]=2;
						if (root.getChildCount() >= 0) {
							for (Enumeration<?> eu = root.children(); eu.hasMoreElements();) {
								TreeNode n = (TreeNode) eu.nextElement();
								TreePath path = new TreePath(root).pathByAddingChild(n);
								treePlayers[Integer.parseInt(menuName)].expandAll(path, 2);
							}
						}
					}					
				}
				break;
			}	
		}
	}		
	
	/**
	 * Method to be called by Observable's notifyObservers method.
	 * @param obs the observable object
	 * @param x an argument passed by the notifyObservers method.
	 * @throws RemoteException remote error
	 */	
	public void update(String info, Color color, int type) throws RemoteException{
		Dimension size;
		if (type == 0){//update for top level phase information. startup and game started
			phaseNameLabel.setText(info);
			size = phaseNameLabel.getPreferredSize();
			phaseNameLabel.setBounds(15,15,size.width,size.height); 
			gameStageLabel1.setVisible(false);
			gameStageLabel2.setVisible(false);
			gameStageLabel3.setVisible(false);
			gameStageLabel4.setVisible(false);
			gameStageLabel5.setVisible(false);
			turnLabel.setVisible(false);
			playerLabel.setVisible(false);
			playerPhaseLabel.setVisible(false);;
		}
		else if (type == 1){
			int stage = Integer.parseInt(info);
			if (stage>=230){ //special for assigning countries process
				gameStageLabel3.setText("Step 3 - Start with Player"+String.valueOf(stage-230+1)+" ...");
				size = gameStageLabel3.getPreferredSize();
				gameStageLabel3.setBounds(25,110,size.width,size.height); 
				return;
			}
			switch (stage){
				case 1://ready to load a map
					gameStageLabel1.setText("Step 1 - Loading risk map ...");
					break;
				case 2://load map failed
					gameStageLabel1.setText("Step 1 - Loading risk map ... failed");
					break;	
				case 3://load map canceled by user
					gameStageLabel1.setText("Step 1 - Loading risk map ... canceled");
					break;						
				case 11://ready to create players
					gameStageLabel2.setText("Step 2 - Creating players ...");
					break;
				case 12://create players canceled by user
					gameStageLabel2.setText("Step 2 - Creating players ... canceled");
					break;	
				case 21://ready to assign countries randomly
					gameStageLabel3.setText("Step 3 - Assigning countries ...");
					break;
				case 31:
					gameStageLabel4.setText("Step 4 - Placing initial armies ...");
					break;	
				case 32:
					gameStageLabel4.setText("Step 4 - Placing initial armies ... canceled");
					break;				
			}
			size = gameStageLabel1.getPreferredSize();
			gameStageLabel1.setBounds(25,50,size.width,size.height); 
			size = gameStageLabel2.getPreferredSize();
			gameStageLabel2.setBounds(25,80,size.width,size.height);
			size = gameStageLabel3.getPreferredSize();
			gameStageLabel3.setBounds(25,110,size.width,size.height); 
			size = gameStageLabel4.getPreferredSize();
			gameStageLabel4.setBounds(25,140,size.width,size.height); 
		}
		else if (type == 2){//update turn info
			turnLabel.setText("Turn  "+info);
			turnLabel.setVisible(true);
			size = turnLabel.getPreferredSize();
			turnLabel.setBounds(350-size.width,10,size.width,size.height); 	
		}
		else if (type == 3){//update player info
			int curPlayer = Integer.parseInt(info);
			playerLabel.setText("Player"+(curPlayer+1)+":");
			playerLabel.setForeground(color);
			playerLabel.setVisible(true);
			size = playerLabel.getPreferredSize();
			playerLabel.setBounds(20,56,size.width,size.height); 
			setPlayersView(curPlayer);
		}
		else if (type == 4){//update player phase info
			playerPhaseLabel.setText(info);
			playerPhaseLabel.setVisible(true);
			size = playerPhaseLabel.getPreferredSize();
			playerPhaseLabel.setBounds(113,59,size.width,size.height); 	
		}
		else if (type == 7){//update reinforce cards info
			gameStageLabel4.setText(info);
			gameStageLabel4.setVisible(true);
			gameStageLabel4.setForeground(Color.RED);
			size = gameStageLabel4.getPreferredSize();
			gameStageLabel4.setBounds(25,labelContinent.getBounds().y-30,size.width,size.height);
		}	
		else if (type == 8){//update attack info
			gameStageLabel1.setText("Waiting for attack command ...");
			gameStageLabel1.setVisible(true);
			gameStageLabel1.setForeground(Color.RED);
			size = gameStageLabel1.getPreferredSize();
			gameStageLabel1.setBounds(25,90,size.width,size.height);
		}
		else if (type == 9){//update attack info
			gameStageLabel1.setText(info);
			gameStageLabel1.setVisible(true);
			gameStageLabel1.setForeground(Color.RED);
			size = gameStageLabel1.getPreferredSize();
			gameStageLabel1.setBounds(25,90,size.width,size.height);
		}
		else if (type == 10){//update attack info
			gameStageLabel2.setText(info);
			gameStageLabel2.setVisible(true);
			size = gameStageLabel2.getPreferredSize();
			gameStageLabel2.setBounds(25,120,size.width,size.height);
		}
	}

	/**
	 * Method to be called by Observable's notifyObservers method.
	 * @param obs the observable object
	 * @param x an argument passed by the notifyObservers method.
	 * @throws RemoteException remote error
	 */	
	public void update(Observable obs,int current, int type) throws RemoteException{
		Dimension size;
		if (type == 1){//update detail step information.
			this.myGame = (RiskGameModel)obs;
			int stage = myGame.getGameStage();
			if (stage>=1000){ //special for assigning countries process
				gameStageLabel3.setText("Step 3 - Assign "+myGame.getGameMap().findCountryByID(stage/1000).getShowName()+
						" to Player"+String.valueOf(stage%1000+1));
				size = gameStageLabel3.getPreferredSize();
				gameStageLabel3.setBounds(25,110,size.width,size.height); 
				reloadContinents();
				reloadPlayers(stage%1000,myGame.getPlayers()[stage%1000]);
				return;
			}
			switch (stage){
				case 0://initial status, no map, no players, when beginning or back from 10
					reloadContinents();
					gameStageLabel1.setVisible(true);
					gameStageLabel1.setForeground(Color.RED);					
					gameStageLabel1.setText("Step 1 - Load a risk map");
					gameStageLabel2.setVisible(false);
					gameStageLabel3.setVisible(false);
					gameStageLabel4.setVisible(false);
					break;
				case 10://load map successfully, from 1, or back from 20
					reloadContinents();
					if (labelPlayers!=null){
						for (int i=0;i<labelPlayers.length;i++){
							this.remove(labelPlayers[i]);
							this.remove(scrollPaneForPlayers[i]);							
						}
						labelPlayers = null;
						treePlayers = null;
						scrollPaneForPlayers = null;					
					}
					setSize(width,height);
					gameStageLabel1.setForeground(Color.BLACK);
					gameStageLabel1.setText("Step 1 - Loading risk map ... succeed");
					gameStageLabel2.setForeground(Color.RED);
					gameStageLabel2.setVisible(true);
					gameStageLabel2.setText("Step 2 - Create players");
					gameStageLabel3.setVisible(false);
					gameStageLabel4.setVisible(false);
					break;	
				case 20://create players successfully, from 11, or back from 30
					int newWidth = width+(int)Math.ceil((double)(myGame.getPlayers().length)/2)*360;
					setSize(newWidth,height);//resize view
					int playerNum = myGame.getPlayers().length;
					//begin to create individual trees for each players
					if (labelPlayers==null){
						labelPlayers = new JLabel[playerNum];
						treePlayers = new MyTree[playerNum];
						scrollPaneForPlayers = new JScrollPane[playerNum];
						DefaultMutableTreeNode myTreeRoot;
						for (int i=0;i<playerNum;i++){
							labelPlayers[i] = new JLabel("Player"+(i+1)+" (0 armies, 0 cards)");
							add(labelPlayers[i]);  
							labelPlayers[i].setFont(new java.awt.Font("dialog",1,15));
							labelPlayers[i].setForeground(myGame.getPlayers()[i].getMyColor());
							size = labelPlayers[i].getPreferredSize();
							labelPlayers[i].setBounds(width+(i/2)*360-10,12+(i%2)*252,size.width+100,size.height); 	
							myTreeRoot = new DefaultMutableTreeNode("Countries(0) ");
							treePlayers[i] = new MyTree(myTreeRoot);
							treePlayers[i].setCellRenderer(new CountryNodeRenderer(Color.BLACK));
							scrollPaneForPlayers[i] = new JScrollPane(treePlayers[i],JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
							add(scrollPaneForPlayers[i]);
							scrollPaneForPlayers[i].setBounds(width+(i/2)*360-10,36+(i%2)*252,350,226);	
						}
					}
					else reloadPlayers();
					reloadContinents();
					gameStageLabel2.setForeground(Color.BLACK);
					gameStageLabel2.setText("Step 2 - Creating "+myGame.getPlayers().length+" players ... succeed");
					gameStageLabel3.setVisible(true);
					gameStageLabel3.setForeground(Color.RED);
					gameStageLabel3.setText("Step 3 - Assign countries");
					gameStageLabel4.setVisible(false);
					break;	

				case 30://ready to place initial armies
					reloadContinents();
					reloadPlayers();
					gameStageLabel3.setForeground(Color.BLACK);
					gameStageLabel3.setText("Step 3 - Assigning countries ... succeed");
					gameStageLabel4.setText("Step 4 - Place initial armies");
					gameStageLabel4.setForeground(Color.RED);
					gameStageLabel4.setVisible(true);
					gameStageLabel5.setVisible(false);
					break;					
				case 40://startup phase finished
					reloadContinents();
					reloadPlayers();
					gameStageLabel4.setForeground(Color.BLACK);
					gameStageLabel4.setText("Step 4 - Placing initial armies ... succeed");
					gameStageLabel5.setVisible(true);
					break;
				case 50://game started, can not go back to previous steps now
					reloadContinents();
					reloadPlayers();
					break;	
				case 51://reinforcement finished
					reloadContinents();
					reloadPlayers();
					setPlayersView(myGame.getCurPlayer());
					break;
				case 52://attack finished
					reloadContinents();
					reloadPlayers();
					setPlayersView(myGame.getCurPlayer());
					break;
				case 53://fortification finished
					reloadContinents();
					reloadPlayers();
					setPlayersView(myGame.getCurPlayer());
					break;
			}
			if (stage<50){ //adjust size for display information correctly
				size = gameStageLabel1.getPreferredSize();
				gameStageLabel1.setBounds(25,50,size.width,size.height); 
				size = gameStageLabel2.getPreferredSize();
				gameStageLabel2.setBounds(25,80,size.width,size.height);
				size = gameStageLabel3.getPreferredSize();
				gameStageLabel3.setBounds(25,110,size.width,size.height); 
				size = gameStageLabel4.getPreferredSize();
				gameStageLabel4.setBounds(25,140,size.width,size.height); 
			}
			else {//clear all information for last phase
				gameStageLabel1.setVisible(false);
				gameStageLabel2.setVisible(false);
				gameStageLabel3.setVisible(false);
				gameStageLabel4.setVisible(false);
				gameStageLabel5.setVisible(false);				
			}
		}
		else if (type == 5){//update reinforcement Status
			gameStageLabel1.setText("Calculating reinforcement armies ... succeed");
			gameStageLabel1.setVisible(true);
			gameStageLabel1.setForeground(Color.BLACK);
			size = gameStageLabel1.getPreferredSize();
			gameStageLabel1.setBounds(25,90,size.width,size.height);
			
			gameStageLabel2.setVisible(false);
			
			gameStageLabel3.setText(((PlayerModel)obs).getReinforcementStr());
			gameStageLabel3.setFont(new java.awt.Font("dialog",1,11));
			gameStageLabel3.setForeground(Color.blue);
			gameStageLabel3.setVisible(true);
			size = gameStageLabel3.getPreferredSize();
			gameStageLabel3.setBounds(40,110,size.width,size.height);
			reloadPlayers(current,(PlayerModel)obs);
			setPlayersView(current);
		}
		else if (type == 6){//update exchange cards info
			gameStageLabel2.setText((((PlayerModel)obs).getExchangeStatus()));
			if (gameStageLabel2.getText().equals("Exchange Cards ... finished."))
				gameStageLabel2.setForeground(Color.BLACK);
			else gameStageLabel2.setForeground(Color.RED);
			gameStageLabel2.setVisible(true);
			size = gameStageLabel2.getPreferredSize();
			gameStageLabel2.setBounds(25,115,size.width,size.height); 
			
			gameStageLabel3.setText((((PlayerModel)obs).getReinforcementStr()));
			size = gameStageLabel3.getPreferredSize();
			gameStageLabel3.setBounds(40,135,size.width,size.height);	
			reloadPlayers(current,(PlayerModel)obs);
			setPlayersView(current);
		}
		
	}

	/**
	 * Method to highlight the current player's tree
	 * @param curPlayer current player
	 */
	private void setPlayersView(int curPlayer) {
		// TODO Auto-generated method stub
		for (int i=0;i<this.treePlayers.length;i++){
			treePlayers[i].setEnabled(i==curPlayer);
			labelPlayers[i].setEnabled(i==curPlayer);
		}
	}
	
	public void setLabel(String info){
		gameStageLabel4.setText(String.valueOf(info));
		Dimension size = gameStageLabel4.getPreferredSize();
		gameStageLabel4.setBounds(gameStageLabel4.getBounds().x,gameStageLabel4.getBounds().y,size.width,size.height);
	}	
}
