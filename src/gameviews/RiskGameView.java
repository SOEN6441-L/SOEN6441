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
import gamemodels.RiskGameModel;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;

/**
 * The class is used to create a window for player to edit maps of games
 *
 */
public class RiskGameView extends JFrame implements Observer{
	private static final long serialVersionUID = 1L;
	//button in window
	private JButton loadFromFileBtn;
	private JButton createPlayersBtn;
	private JButton assignCountryBtn;
	private JButton previousBtn;
	private JButton putArmiesBtn;
	private JButton startGameBtn;
	private JButton nextPlayerBtn;
	private JButton newGameBtn;
	private JButton setWidthBtn;
	private JButton adjustWidthBtn;	
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
        
        
		loadFromFileBtn = new javax.swing.JButton("Step 1 - Load A Risk Map");
		loadFromFileBtn.setMnemonic('l');
		loadFromFileBtn.setDisplayedMnemonicIndex(9);
		loadFromFileBtn.setFont(new java.awt.Font("dialog",1,17));		
		add(loadFromFileBtn);  	
		size = loadFromFileBtn.getPreferredSize();		
		loadFromFileBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getSize().width-size.width-11,625,size.width+10,size.height);  

		createPlayersBtn = new javax.swing.JButton("Step 2 - Create Players");
		createPlayersBtn.setMnemonic('c');
		createPlayersBtn.setDisplayedMnemonicIndex(9);
		createPlayersBtn.setFont(new java.awt.Font("dialog",1,17));		
		createPlayersBtn.setVisible(false);
		add(createPlayersBtn);	
		createPlayersBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  

		assignCountryBtn = new javax.swing.JButton("Step 3 - Assign Countries");
		assignCountryBtn.setMnemonic('a');
		assignCountryBtn.setDisplayedMnemonicIndex(9);
		assignCountryBtn.setFont(new java.awt.Font("dialog",1,17));		
		assignCountryBtn.setVisible(false);
		add(assignCountryBtn);	
		assignCountryBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height); 
				
		putArmiesBtn = new javax.swing.JButton("Step 4 - Put initial Armies");
		putArmiesBtn.setMnemonic('i');
		putArmiesBtn.setDisplayedMnemonicIndex(9);
		putArmiesBtn.setFont(new java.awt.Font("dialog",1,17));		
		putArmiesBtn.setVisible(false);
		add(putArmiesBtn);	
		putArmiesBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  
		
		startGameBtn = new javax.swing.JButton("Great ! Start Game");
		startGameBtn.setMnemonic('g');
		startGameBtn.setDisplayedMnemonicIndex(14);
		startGameBtn.setFont(new java.awt.Font("dialog",1,17));		
		startGameBtn.setVisible(false);
		add(startGameBtn);	
		startGameBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  
		
		nextPlayerBtn = new javax.swing.JButton("Next Player");
		nextPlayerBtn.setMnemonic('n');
		nextPlayerBtn.setDisplayedMnemonicIndex(0);
		nextPlayerBtn.setFont(new java.awt.Font("dialog",1,17));		
		nextPlayerBtn.setVisible(false);
		add(nextPlayerBtn);	
		nextPlayerBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  

		newGameBtn = new javax.swing.JButton("New Game");
		newGameBtn.setFont(new java.awt.Font("dialog",1,17));		
		newGameBtn.setVisible(false);
		add(newGameBtn);	
		newGameBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  
		
		previousBtn = new javax.swing.JButton("<< Previous");
		previousBtn.setMnemonic('p');
		previousBtn.setDisplayedMnemonicIndex(3);
		previousBtn.setFont(new java.awt.Font("dialog",1,17));		
		previousBtn.setVisible(false);
		add(previousBtn);	
		previousBtn.setBounds(loadFromFileBtn.getBounds().x-size.width+30,625,size.width-40,size.height); 
		
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
		treeContinent.setCellRenderer(new ContinentNodeRenderer(myGame.getGameMap()));
		treePlayer.setCellRenderer(new PlayerNodeRenderer(myGame.getPlayers()));
	}	
	
	/**
	 * Method to add controller to relative components.
	 * @param controller the new controller
	 */
	public void addController(RiskGameController controller){
		loadFromFileBtn.addActionListener(controller);
		createPlayersBtn.addActionListener(controller);
		assignCountryBtn.addActionListener(controller);
		putArmiesBtn.addActionListener(controller);	
		startGameBtn.addActionListener(controller);		
		nextPlayerBtn.addActionListener(controller);			
		newGameBtn.addActionListener(controller);
		previousBtn.addActionListener(controller);
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
		if (myGame.getGameMap()==null){
			labelContinent.setText("Continents (0):");
			myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");		
		}
		else{
			labelContinent.setText("Continents ("+String.valueOf(myGame.getGameMap().getContinents().size())+"):");
			myTreeRoot = new DefaultMutableTreeNode("Map - "+myGame.getGameMap().getRiskMapName()+" ");

			for (ContinentModel loopContinent : myGame.getGameMap().getContinents()) { 
				ArrayList<CountryModel> loopCountriesList = loopContinent.getCountryList();
				DefaultMutableTreeNode loopContinentNode =  new DefaultMutableTreeNode(loopContinent.getName()+" ("+loopContinent.getControlNum()+") ("+loopCountriesList.size()+" countries) "); 
				for (CountryModel loopCountry:loopCountriesList){
					if (loopCountry.getOwner()!=null)
						loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.getName()+" ("+loopCountry.getOwner().getName()+", "+loopCountry.getArmyNumber()+" armies) "));						
					else loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.getName()+" ("+loopCountry.getArmyNumber()+" armies) "));
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
	 * The method is to reload players tree
	 */
	private void reloadPlayers(){	
		//configuration
		DefaultMutableTreeNode myTreeRoot;
		if (myGame.getPlayers()==null){
			labelPlayer.setText("Players (Total 0):");
			myTreeRoot = new DefaultMutableTreeNode("Players ");		
		}
		else{
			labelPlayer.setText("Players (Total "+String.valueOf(myGame.getPlayers().length)+", and "+String.valueOf(myGame.getValidPlayers())+" still in game):");
			myTreeRoot = new DefaultMutableTreeNode("Players ");

			for (int i=0;i<myGame.getPlayers().length;i++) { 
				DefaultMutableTreeNode loopPlayerNode =  new DefaultMutableTreeNode(myGame.getPlayers()[i].getName()+" ("+myGame.getPlayers()[i].getCountries().size()
						+" Countries, "+myGame.getPlayers()[i].getTotalArmies()+" armies, "+myGame.getPlayers()[i].getCardsString(0)+" cards) "); 
				for (CountryModel loopCountry:myGame.getPlayers()[i].getCountries()){
					loopPlayerNode.add(new DefaultMutableTreeNode(loopCountry.getName()+" (In "+loopCountry.getBelongTo().getName()+", "+loopCountry.getArmyNumber()+" armies) "));						
				}
				myTreeRoot.add(loopPlayerNode);
			}
		}	
		treePlayer= new MyTree(myTreeRoot); 
		if  (myGame.getPlayers()!=null){
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
		
		treePlayer.setCellRenderer(new PlayerNodeRenderer(myGame.getPlayers()));
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
		if (myGame.getGameMap()==null){
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
			labelCountry.setText("Countries ("+String.valueOf(myGame.getGameMap().getCountryNum())+") and Adjacency Matrix:"); 
			matrixModel = new DefaultTableModel(myGame.getGameMap().getCountryNum(),myGame.getGameMap().getCountryNum());
		
			newDataVector = new String[myGame.getGameMap().getCountryNum()][myGame.getGameMap().getCountryNum()];
			newIdentifiers = new String[myGame.getGameMap().getCountryNum()];
			int i =0;
			for (ContinentModel loopContinent : myGame.getGameMap().getContinents()) { 
				ArrayList<CountryModel> loopCountriesList = loopContinent.getCountryList();
				for (CountryModel loopCountry:loopCountriesList){
					newIdentifiers[i++] = loopCountry.getName();
				}
			}
			for (i=0;i<myGame.getGameMap().getCountryNum();i++){
				ArrayList<CountryModel> adjacentCountryList = myGame.getGameMap().getAdjacencyList().get(myGame.getGameMap().findCountry((String)newIdentifiers[i]));
				for (int j=0;j<myGame.getGameMap().getCountryNum();j++){
					if (adjacentCountryList.contains(myGame.getGameMap().findCountry((String)newIdentifiers[j]))){
						newDataVector[i][j] = "X";
					}
					else{
						newDataVector[i][j] = "";
					}
				}
			}
			matrixModel.setDataVector(newDataVector, newIdentifiers);
			adjacencyMatrix = new MyTable(matrixModel){
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column){
					return false;
				}//table not allow to be modified
			};
			if (myGame.getGameMap().getCountryNum()>0) adjacencyMatrix.setRowHeight(adjacencyMatrix.getTableHeader().getPreferredSize().height);
			int continentNum = myGame.getGameMap().getContinents().size();
			int[] areaContinents = new int[continentNum+1];
			areaContinents[0] = 0;
			for (i=0;i<continentNum;i++){
				areaContinents[i+1] = areaContinents[i]+myGame.getGameMap().getContinents().get(i).getCountryList().size();
			}
			adjacencyMatrix.setDefaultRenderer(Object.class,new MatrixRenderer(areaContinents));
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
		
		adjustWidthBtn.setEnabled((myGame.getGameMap()==null)?false:myGame.getGameMap().getCountryNum()>0);
		setWidthBtn.setEnabled((myGame.getGameMap()==null)?false:myGame.getGameMap().getCountryNum()>0);
		colWidthEdit.setEnabled((myGame.getGameMap()==null)?false:myGame.getGameMap().getCountryNum()>0);
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
				previousBtn.setVisible(false);
				loadFromFileBtn.setVisible(true);
				createPlayersBtn.setVisible(false);
				break;
			case 10://already load map
				reloadContinents();
				reloadPlayers();
				reloadMatrix();
				loadFromFileBtn.setVisible(false);
				previousBtn.setVisible(true);
				createPlayersBtn.setVisible(true);
				assignCountryBtn.setVisible(false);
				break;	
			case 20://already create players
				reloadContinents();
				reloadPlayers();
				createPlayersBtn.setVisible(false);
				previousBtn.setVisible(true);
				assignCountryBtn.setVisible(true);
				putArmiesBtn.setVisible(false);
				break;	
			case 21://assign countries
				previousBtn.setEnabled(false);
				assignCountryBtn.setEnabled(false);
				break;
			case 30://already assign countries
				reloadContinents();
				reloadPlayers();
				previousBtn.setEnabled(true);
				assignCountryBtn.setEnabled(true);
				assignCountryBtn.setVisible(false);
				previousBtn.setVisible(true);
				putArmiesBtn.setVisible(true);
				startGameBtn.setVisible(false);
				break;				
			case 40://already put initial armies
				reloadContinents();
				reloadPlayers();
				putArmiesBtn.setVisible(false);
				previousBtn.setVisible(true);
				startGameBtn.setVisible(true);
				break;
			case 50://game started
				reloadContinents();
				reloadPlayers();
				previousBtn.setVisible(false);
				startGameBtn.setVisible(false);
				nextPlayerBtn.setVisible(true);
				break;	
			case 51://in game - after reinforcement
				reloadContinents();
				reloadPlayers();
				break;					
			case 52://in game - after attack
				reloadContinents();
				reloadPlayers();
				break;					
			case 53://in game - after fortification
				reloadContinents();
				reloadPlayers();
				break;					
			}
		}	
	}  
}

