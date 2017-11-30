package gameviews;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import basicclasses.MyPopupMenu;
import basicclasses.MyTable;
import basicclasses.MyTree;
import basicclasses.RowHeaderTable;
import gamecontrollers.RiskGameController;
import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;
import mapmodels.RiskMapModel;

/**
 * The class is used to create a window for player to edit maps of games
 *@see Observer
 *@see JFrame
 */
public class RiskGameView extends JFrame implements Observer{
	private static final long serialVersionUID = 1L;
	//button in window
	private JButton functionsBtn;
	private JButton previousBtn;
	private JButton setWidthBtn;
	private JButton adjustWidthBtn;	
	private JButton saveBtn;
	private JButton loadBtn;
	public JButton stopBtn;
	private JTextField colWidthEdit;
    
	//label show info
	private JLabel labelContinent;
	private JLabel labelCountry;
	private JLabel labelPlayer;
	
	private MyPopupMenu mapMenu;
	private JMenuItem mExpandAll, mCollapseAll;	
	
	private MyTree treeContinent;
	private JScrollPane scrollPaneForContinent;
	private MyTable adjacencyMatrix;
	private JScrollPane scrollPaneForMatrix;
	private MyTree treePlayer;	
	private JScrollPane scrollPaneForPlayers;
	
	private RiskGameModel myGame;
	
	private String matrixDisplayMode = "preferred"; //Preferred, same
	private int matrixColumnWidth = 25;
	private int continentExpandMode = 1, playerExpandMode = 2;//1-expand 2-collapse

	/**
	 * Constructor of RiskGameView class.
	 */
	public RiskGameView(){		
		//configuration
		setTitle("Risk Game by Invincible Team Four");
		setSize(1280,705);
		int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		setLocation((screenWidth-1280)/2, (screenHeight-705)/2-30);  
		//set exit program when close the window  
		setDefaultCloseOperation(3);  
		//not capable adjust windows size  
		setResizable(false);  
		setLayout(null);  
		
        //Tree for continents and countries
		labelContinent = new javax.swing.JLabel("Continents (0):");  
		add(labelContinent);  
		labelContinent.setFont(new java.awt.Font("dialog",1,15));
		Dimension size = labelContinent.getPreferredSize();
		labelContinent.setBounds(15,8,size.width+200,size.height);   
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");
		treeContinent= new MyTree(myTreeRoot);

		scrollPaneForContinent= new JScrollPane(treeContinent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPaneForContinent);  
		scrollPaneForContinent.setBounds(15,35,400,320);	

		ButtonHandler buttonHandler = new ButtonHandler();
		
	    mapMenu = new MyPopupMenu();
	    mExpandAll = new JMenuItem("Expand All",KeyEvent.VK_E);
	    mExpandAll.addActionListener(buttonHandler);	    
	    mCollapseAll = new JMenuItem("Collapse All",KeyEvent.VK_C);
	    mCollapseAll.addActionListener(buttonHandler);
	    
	    mapMenu.add(mExpandAll);
	    mapMenu.addSeparator();
	    mapMenu.add(mCollapseAll);		    
	    
        //Tree for Players
		labelPlayer = new javax.swing.JLabel("Players (Total 0):");  
		add(labelPlayer);  
		labelPlayer.setFont(new java.awt.Font("dialog",1,15));
		size = labelPlayer.getPreferredSize();
		labelPlayer.setBounds(15,362,size.width+200,size.height);   
        
		myTreeRoot = new DefaultMutableTreeNode("Players ");
		treePlayer = new MyTree(myTreeRoot);

		scrollPaneForPlayers= new JScrollPane(treePlayer,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPaneForPlayers);  
		scrollPaneForPlayers.setBounds(15,389,400,226);	
	    
		//Matrix for countries and connections
		labelCountry = new javax.swing.JLabel("Countries (0) and Adjacency Matrix:");  
		add(labelCountry);  
		labelCountry.setFont(new java.awt.Font("dialog",1,15));
		size = labelCountry.getPreferredSize();
		labelCountry.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),8,size.width+200,size.height);         

		scrollPaneForMatrix= new JScrollPane(null);
		add(scrollPaneForMatrix);
		scrollPaneForMatrix.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),35,846,580);
        
        
		functionsBtn = new javax.swing.JButton("Step 1 - Load A Risk Map");
		functionsBtn.setFont(new java.awt.Font("dialog",1,17));		
		add(functionsBtn);  	
		size = functionsBtn.getPreferredSize();		
		functionsBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getSize().width-size.width-11,625,size.width+10,size.height);  
	
		previousBtn = new javax.swing.JButton("Tournament");
		previousBtn.setFont(new java.awt.Font("dialog",1,17));		
		previousBtn.setVisible(true);
		add(previousBtn);	
		previousBtn.setBounds(functionsBtn.getBounds().x-size.width+30,625,size.width-40,size.height); 
		
		stopBtn = new javax.swing.JButton("Stop");
		stopBtn.setFont(new java.awt.Font("dialog",1,17));		
		stopBtn.setVisible(false);
		add(stopBtn);	
		stopBtn.setBounds(previousBtn.getBounds().x-size.width+30,625,size.width-40,size.height);	
		
		adjustWidthBtn = new javax.swing.JButton("Full width");
		adjustWidthBtn.setMnemonic('f');
		adjustWidthBtn.setDisplayedMnemonicIndex(0);
		add(adjustWidthBtn);  	     
		size = adjustWidthBtn.getPreferredSize();		
		adjustWidthBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getSize().width-size.width-1,7,size.width,size.height-5);
		adjustWidthBtn.setEnabled(false);
		adjustWidthBtn.addActionListener(buttonHandler);	
		
		setWidthBtn = new javax.swing.JButton("Set");
		add(setWidthBtn);  	     
		setWidthBtn.setBounds(adjustWidthBtn.getBounds().x-size.width+25,7,size.width-30,size.height-5);
		setWidthBtn.setEnabled(false);
		setWidthBtn.addActionListener(buttonHandler);
		
		colWidthEdit = new javax.swing.JTextField();
		colWidthEdit.setText(String.valueOf(matrixColumnWidth));
		add(colWidthEdit);  	     
		colWidthEdit.setBounds(setWidthBtn.getBounds().x-size.width+19,7,size.width-20,size.height-5);
		colWidthEdit.setEnabled(false);		

		saveBtn = new javax.swing.JButton("Save Game");
		saveBtn.setFont(new java.awt.Font("dialog",1,17));		
		saveBtn.setVisible(true);
		add(saveBtn);	
		size = saveBtn.getPreferredSize();
		saveBtn.setBounds(scrollPaneForPlayers.getBounds().x,625,size.width,size.height);	
		
		loadBtn = new javax.swing.JButton("Load Game");
		loadBtn.setFont(new java.awt.Font("dialog",1,17));		
		loadBtn.setVisible(true);
		add(loadBtn);	
		size = loadBtn.getPreferredSize();
		loadBtn.setBounds(saveBtn.getBounds().x+ saveBtn.getSize().width+15,625,size.width,size.height);

		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        
		setVisible(false);          
	}
	
	/**
	 * Method to handle windows event, add a prompt upon exiting the program.
	 * @param e event that invoke this method
	 */	
	@Override
	protected void processWindowEvent(WindowEvent e){
		if (e.getID() == WindowEvent.WINDOW_CLOSING){
			if (JOptionPane.showConfirmDialog(null,
					"Do you really want quit the game?",
					"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
				return;	
		}	
		super.processWindowEvent(e);
	}

	/**
	 * Method to add the related RiskGame object.
	 * @param myGame the new RiskGame model
	 */
	public void addModel(RiskGameModel myGame){
		this.myGame = myGame;
		//treeContinent.setCellRenderer(new ContinentNodeRenderer(myGame.getGameMap()));
		//treePlayer.setCellRenderer(new PlayerNodeRenderer(myGame.getPlayers()));
	}	
	
	/**
	 * Method to add controller to relative components.
	 * @param controller the new controller
	 */
	public void addController(RiskGameController controller){
		functionsBtn.addActionListener(controller);
		previousBtn.addActionListener(controller);
		saveBtn.addActionListener(controller);
		loadBtn.addActionListener(controller);
		stopBtn.addActionListener(controller);
	}	
	
	/**
	 * Class to define local action Listener.
	 * @see ActionListener
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
			case "Full width":
				matrixDisplayMode = "preferred";
				adjacencyMatrix.fitTableColumns();
				break;
			case "Set":
				Pattern pattern = Pattern.compile("[0-9]*");  
				String width = colWidthEdit.getText().trim();
				if (pattern.matcher(width).matches())
					matrixColumnWidth = Integer.parseInt(width);			
				else colWidthEdit.setText(String.valueOf(matrixColumnWidth));
				matrixDisplayMode = "same";
				adjacencyMatrix.fitTableColumns(matrixColumnWidth);	
				break;
			case "Expand All":
				menuName = ((MyPopupMenu)(((JMenuItem)(e.getSource())).getParent())).getOwner();
				if (menuName.equals("treeContinent")){
					if (treeContinent!=null){
						continentExpandMode = 1;
						TreeNode root = (TreeNode) treeContinent.getModel().getRoot();
						treeContinent.expandAll(new TreePath(root),1);
					}
				}
				else if (menuName.equals("treePlayer")){
					if (treePlayer!=null){
						playerExpandMode = 1;
						TreeNode root = (TreeNode)treePlayer.getModel().getRoot();
						treePlayer.expandAll(new TreePath(root),1);
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
				else if (menuName.equals("treePlayer")){	
					if (treePlayer!=null){
						TreeNode root = (TreeNode) treePlayer.getModel().getRoot();
						playerExpandMode=2;
						if (root.getChildCount() >= 0) {
							for (Enumeration<?> eu = root.children(); eu.hasMoreElements();) {
								TreeNode n = (TreeNode) eu.nextElement();
								TreePath path = new TreePath(root).pathByAddingChild(n);
								treePlayer.expandAll(path, 2);
							}
						}
					}					
				}
				break;
			}	
		}
	}	
	
	/**
	 * The method is to reload continents tree
	 */
	private void reloadContinents(){	
		//configuration
		DefaultMutableTreeNode myTreeRoot;
		RiskMapModel myMap = myGame.getGameMap();
		if (myMap==null){
			labelContinent.setText("Continents (0):");
			myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");		
		}
		else{
			labelContinent.setText("Continents ("+String.valueOf(myMap.getContinents().size())+"):");
			myTreeRoot = new DefaultMutableTreeNode("Map - "+myMap.getRiskMapName()+" ");

			for (ContinentModel loopContinent : myMap.getContinents()) { 
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
		treeContinent.setCellRenderer(new ContinentNodeRenderer(myMap));
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
	 * The method is to reload players tree
	 */
	private void reloadPlayers(){	
		//configuration
		DefaultMutableTreeNode myTreeRoot;
		PlayerModel [] players = myGame.getPlayers();
		if (players == null){
			labelPlayer.setText("Players (Total 0):");
			myTreeRoot = new DefaultMutableTreeNode("Players ");		
		}
		else{
			labelPlayer.setText("Players (Total "+String.valueOf(players.length)+", and "+String.valueOf(myGame.getValidPlayers())+" still in game):");
			myTreeRoot = new DefaultMutableTreeNode("Players ");

			for (int i=0;i<players.length;i++) { 
				DefaultMutableTreeNode loopPlayerNode =  new DefaultMutableTreeNode(players[i].getName()+" ("+players[i].getCountries().size()
						+" Countries, "+players[i].getTotalArmies()+" armies, "+players[i].getCardsString(0)+" cards) "); 
				for (CountryModel loopCountry:players[i].getCountries()){
					loopPlayerNode.add(new DefaultMutableTreeNode(loopCountry.getShowName()+" (In "+loopCountry.getBelongTo().getShowName()+", "+loopCountry.getArmyNumber()+" armies) "));						
				}
				myTreeRoot.add(loopPlayerNode);
			}
		}	
		treePlayer= new MyTree(myTreeRoot); 
		if  (players!=null){
			treePlayer.addMouseListener( new  MouseAdapter(){
				public void mousePressed(MouseEvent e){
					int selRow = treePlayer.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = treePlayer.getPathForLocation(e.getX(), e.getY());
					if (selRow==0 && (e.getButton() == 3)){
						treePlayer.setSelectionPath(selPath);
						if (selPath!=null) {
							if (selPath.getParentPath()==null){ //root node
								mapMenu.setOwner("treePlayer");
								mapMenu.show(e.getComponent(), e.getX()+5, e.getY()+5);
							}
						}
					}
				}
			});
		}
		
		treePlayer.setCellRenderer(new PlayerNodeRenderer(players));
		scrollPaneForPlayers.getViewport().removeAll();
		scrollPaneForPlayers.getViewport().add(treePlayer);
		TreeNode root = (TreeNode) treePlayer.getModel().getRoot();
		if (playerExpandMode==1) treePlayer.expandAll(new TreePath(root),1);
		else {
			if (root.getChildCount() >= 0) {
				for (Enumeration<?> e = root.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = new TreePath(root).pathByAddingChild(n);
					treePlayer.expandAll(path, 2);
				}
			}
		}
	}

	/**
	 * To reload the adjacency matrix after changes.
	 */
	private void reloadMatrix(){
		DefaultTableModel matrixModel;
		Object[][] newDataVector;
		Object[] newIdentifiers = null;
		RiskMapModel myMap = myGame.getGameMap();
		int countryNum = 0;
		if (myMap==null){
			labelCountry.setText("Countries (0) and Adjacency Matrix:");
			matrixModel = new DefaultTableModel(0,0);
			adjacencyMatrix = new MyTable(matrixModel){
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column){
					return false;
				}//table not allow to be modified
			};
		}
		else{
			countryNum = myMap.getCountryNum();
			labelCountry.setText("Countries ("+String.valueOf(countryNum)+") and Adjacency Matrix:"); 
			matrixModel = new DefaultTableModel(countryNum, countryNum);
		
			newDataVector = new String[countryNum][countryNum];
			newIdentifiers = new String[countryNum];
			int i =0;
			if (myGame.getGameStage()>=30){
				for (PlayerModel loopPlayer : myGame.getPlayers()) { 
					ArrayList<CountryModel> loopCountriesList = loopPlayer.getCountries();
					for (CountryModel loopCountry:loopCountriesList){
						newIdentifiers[i++] = loopCountry.getShowName();
					}
				}
			}
			else{
				for (ContinentModel loopContinent : myMap.getContinents()) { 
					ArrayList<CountryModel> loopCountriesList = loopContinent.getCountryList();
					for (CountryModel loopCountry:loopCountriesList){
						newIdentifiers[i++] = loopCountry.getShowName();
					}
				}
			}
			for (i=0;i<countryNum;i++){
				ArrayList<CountryModel> adjacentCountryList = myMap.getAdjacencyList().get(myMap.findCountry((String)newIdentifiers[i]));
				for (int j=0;j<countryNum;j++){
					if (adjacentCountryList.contains(myMap.findCountry((String)newIdentifiers[j]))){
						if (myMap.getAdjacencyList().get(myMap.findCountry((String)newIdentifiers[j]))
								.contains(myMap.findCountry((String)newIdentifiers[i]))){
							newDataVector[i][j] = "X";
						}
						else newDataVector[i][j] = "Y";
					}
					else{
						newDataVector[i][j] = "";
					}
				}
				if (myGame.getGameStage()>=30) 
					newDataVector[i][i] = String.valueOf(myMap.findCountry((String)newIdentifiers[i]).getArmyNumber());
			}
			matrixModel.setDataVector(newDataVector, newIdentifiers);
			adjacencyMatrix = new MyTable(matrixModel){
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column){
					return false;
				}//table not allow to be modified
			};
			if (countryNum>0) adjacencyMatrix.setRowHeight(adjacencyMatrix.getTableHeader().getPreferredSize().height);
			
			int[] area = null;
			if (myGame.getGameStage()>=30){
				int playerNum = myGame.getPlayers().length;
				area = new int[playerNum+1];
				area[0] = 0;
				for (i=0;i<playerNum;i++){
					area[i+1] = area[i]+myGame.getPlayers()[i].getCountries().size();
				}
			}	
			else {
				int continentNum = myMap.getContinents().size();
				area = new int[continentNum+1];
				area[0] = 0;
				for (i=0;i<continentNum;i++){
					area[i+1] = area[i]+myMap.getContinents().get(i).getCountryList().size();
				}				
			}
			adjacencyMatrix.setDefaultRenderer(Object.class,new MatrixRenderer(area,myMap));
		}
		//adjacencyMatrix.setColumnSelectionAllowed(true);
		adjacencyMatrix.setCellSelectionEnabled(true);
		//adjacencyMatrix.setRowSelectionAllowed(false);
	
		adjacencyMatrix.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		adjacencyMatrix.getTableHeader().setReorderingAllowed(false);
		
		adjacencyMatrix.getTableHeader().setFont(new java.awt.Font("dialog",1,13));
		adjacencyMatrix.getTableHeader().setForeground(Color.BLUE);
		//adjacencyMatrix.getTableHeader().setDefaultRenderer(new AdjacencyMatrixHeaderRenderer(adjacencyMatrix));		
		int maxWidth;
		if (matrixDisplayMode.equals("preferred"))
			maxWidth = adjacencyMatrix.fitTableColumns();
		else maxWidth = adjacencyMatrix.fitTableColumns(matrixColumnWidth); 		
		scrollPaneForMatrix.getViewport().removeAll();
		scrollPaneForMatrix.getViewport().add(adjacencyMatrix);
		scrollPaneForMatrix.setRowHeaderView(new RowHeaderTable(adjacencyMatrix,Math.max(70,maxWidth+10),newIdentifiers));
		
		adjustWidthBtn.setEnabled((myMap==null)?false:countryNum>0);
		setWidthBtn.setEnabled((myMap==null)?false:countryNum>0);
		colWidthEdit.setEnabled((myMap==null)?false:countryNum>0);
	}
	
	/**
	 * Method to be called by Observable's notifyObservers method.
	 * @param arg0 the observable object
	 * @param arg1 an argument passed by the notifyObservers method.
	 */	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		int type = (int) arg1;
		if (type == 1){
			switch (((RiskGameModel)arg0).getGameStage()){
			case 0://initial status
				reloadContinents();
				reloadMatrix();
				reloadPlayers();
				saveBtn.setEnabled(true);
				loadBtn.setEnabled(true);
				previousBtn.setText("Tournament");
				previousBtn.setVisible(true);
				previousBtn.setEnabled(true);
				functionsBtn.setText("Step 1 - Load A Risk Map");
				functionsBtn.setEnabled(true);
				break;
			case 10://already load map
				reloadContinents();
				reloadPlayers();
				reloadMatrix();
				saveBtn.setEnabled(true);
				loadBtn.setEnabled(true);
				previousBtn.setText("<< Previous");
				previousBtn.setVisible(true);
				previousBtn.setEnabled(true);
				functionsBtn.setText("Step 2 - Create Players");
				functionsBtn.setEnabled(true);
				break;	
			case 20://already create players
				reloadContinents();
				reloadPlayers();
				reloadMatrix();
				saveBtn.setEnabled(true);
				loadBtn.setEnabled(true);
				previousBtn.setText("<< Previous");
				previousBtn.setVisible(true);
				previousBtn.setEnabled(true);
				functionsBtn.setText("Step 3 - Assign Countries");
				functionsBtn.setEnabled(true);
				break;	
			case 21://assign countries
				saveBtn.setEnabled(false);
				loadBtn.setEnabled(false);
				previousBtn.setText("<< Previous");
				previousBtn.setVisible(true);
				previousBtn.setEnabled(false);
				functionsBtn.setText("Step 3 - Assign Countries");
				functionsBtn.setEnabled(false);
				break;
			case 30://already assign countries
				reloadContinents();
				reloadPlayers();
				reloadMatrix();
				saveBtn.setEnabled(true);
				loadBtn.setEnabled(true);
				previousBtn.setText("<< Previous");
				previousBtn.setVisible(true);
				previousBtn.setEnabled(true);
				functionsBtn.setText("Step 4 - Put initial Armies");
				functionsBtn.setEnabled(true);
				break;				
			case 40://already put initial armies
				reloadContinents();
				reloadPlayers();
				reloadMatrix();
				saveBtn.setEnabled(true);
				loadBtn.setEnabled(true);
				previousBtn.setText("Run to end");
				previousBtn.setVisible(true);
				previousBtn.setEnabled(true);
				functionsBtn.setText("Great ! Start Game");
				functionsBtn.setEnabled(true);
				break;
			case 50://game started
				saveBtn.setEnabled(false);
				loadBtn.setEnabled(false);
				previousBtn.setText("Run to end");
				previousBtn.setVisible(true);
				previousBtn.setEnabled(false);
				functionsBtn.setText("Great ! Start Game");
				functionsBtn.setEnabled(false);
				break;	
			case 51://in game - after reinforcement
				reloadContinents();
				reloadPlayers();
				reloadMatrix();
				break;					
			case 52://in game - after attack
				reloadContinents();
				reloadPlayers();
				reloadMatrix();
				break;					
			case 53://in game - after fortification
				reloadContinents();
				reloadPlayers();
				reloadMatrix();
				saveBtn.setEnabled(true);
				loadBtn.setEnabled(true);
				previousBtn.setText("Run to end");
				previousBtn.setVisible(true);
				previousBtn.setEnabled(true);
				functionsBtn.setText("Next Player");
				functionsBtn.setEnabled(true);
				break;	
			case 54://game over
				saveBtn.setEnabled(true);
				loadBtn.setEnabled(true);
				previousBtn.setVisible(false);
				functionsBtn.setText("New Game");
				functionsBtn.setEnabled(true);
				break;		
			}
		}	
	}  
}

