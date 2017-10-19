package gamecontroller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import gameelements.Player;
import gameelements.RiskGame;
import mapeditor.BasicInfoView;
import mapelements.Continent;
import mapelements.Country;
import mapelements.RiskMap;

/**
 * The class is used to create a window for player to edit maps of games
 *
 */
public class RiskGameMain extends JFrame {

	//button in window
	private JButton loadFromFileBtn;
	private JButton createPlayersBtn;
	private JButton previousBtn;
	private JButton startupBtn;
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
	
	private JPopupMenu mapMenu;
	private JMenuItem mExpandAll, mCollapseAll;	
	
	private JPopupMenu playerMenu;
	private JMenuItem mExpandAllPlayer, mCollapseAllPlayer;		

	private JTree treeContinent;
	private JScrollPane scrollPaneForContinent;
	private JTable adjacencyMatrix;
	private JScrollPane scrollPaneForMatrix;
	private JTree treePlayer;	
	private JScrollPane scrollPaneForPlayers;
	
	private RiskGame myGame;
	//private RiskMap gameMap;
    //private Player[] players;
	
	private String matrixDisplayMode = "preferred"; //Preferred, same
	private int matrixColumnWidth = 25;
	private int continentExpandMode = 1, playerExpandMode = 2;//1-expand 2-collapse

	/**
	 * It is the Constructor of RiskGameMain class.
	 */
	private RiskGameMain(){		
		//configuration
		myGame = new RiskGame();
		
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
		treeContinent= new JTree(myTreeRoot);
		treeContinent.setCellRenderer(new ContinentNodeRenderer(myGame.getGameMap()));

		scrollPaneForContinent= new JScrollPane(treeContinent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPaneForContinent);  
		scrollPaneForContinent.setBounds(15,35,400,320);	

	    mapMenu = new JPopupMenu();
	    mExpandAll = new JMenuItem("Exapnd All  ",KeyEvent.VK_E);
	    mExpandAll.addActionListener(new mExpandAllHandler("continent"));	    
	    mCollapseAll = new JMenuItem("Collapse All  ",KeyEvent.VK_C);
	    mCollapseAll.addActionListener(new mCollapseAllHandler("continent"));
	    
	    mapMenu.add(mExpandAll);
	    mapMenu.addSeparator();
	    mapMenu.add(mCollapseAll);		    
	    
        //Tree for Players
		labelPlayer = new javax.swing.JLabel("Players (0):");  
		add(labelPlayer);  
		labelPlayer.setFont(new java.awt.Font("dialog",1,15));
		size = labelPlayer.getPreferredSize();
		labelPlayer.setBounds(15,362,size.width+200,size.height);   
        
		myTreeRoot = new DefaultMutableTreeNode("Players ");
		treePlayer = new JTree(myTreeRoot);
		treePlayer.setCellRenderer(new PlayerNodeRenderer(myGame.getPlayers()));

		scrollPaneForPlayers= new JScrollPane(treePlayer,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPaneForPlayers);  
		scrollPaneForPlayers.setBounds(15,389,400,226);	
    
	    playerMenu = new JPopupMenu();
	    mExpandAllPlayer = new JMenuItem("Exapnd All  ",KeyEvent.VK_E);
	    mExpandAllPlayer.addActionListener(new mExpandAllHandler("player"));	    
	    mCollapseAllPlayer = new JMenuItem("Collapse All  ",KeyEvent.VK_C);
	    mCollapseAllPlayer.addActionListener(new mCollapseAllHandler("player"));
	    
	    playerMenu.add(mExpandAllPlayer);
	    playerMenu.addSeparator();
	    playerMenu.add(mCollapseAllPlayer);
	    
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
		loadFromFileBtn.addActionListener(new loadFromFileHandler());

		createPlayersBtn = new javax.swing.JButton("Step 2 - Create Players");
		createPlayersBtn.setMnemonic('c');
		createPlayersBtn.setDisplayedMnemonicIndex(9);
		createPlayersBtn.setFont(new java.awt.Font("dialog",1,17));		
		createPlayersBtn.setVisible(false);
		createPlayersBtn.setEnabled(false);
		add(createPlayersBtn);	
		createPlayersBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  
		createPlayersBtn.addActionListener(new createPlayersHandler());
		
		startupBtn = new javax.swing.JButton("Step 3 - Statup Phase");
		startupBtn.setMnemonic('s');
		startupBtn.setDisplayedMnemonicIndex(9);
		startupBtn.setFont(new java.awt.Font("dialog",1,17));		
		startupBtn.setVisible(false);
		startupBtn.setEnabled(false);
		add(startupBtn);	
		startupBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  
		startupBtn.addActionListener(new startupHandler());	
		
		startGameBtn = new javax.swing.JButton("Great ! Start Game");
		startGameBtn.setMnemonic('g');
		startGameBtn.setDisplayedMnemonicIndex(14);
		startGameBtn.setFont(new java.awt.Font("dialog",1,17));		
		startGameBtn.setVisible(false);
		startGameBtn.setEnabled(false);
		add(startGameBtn);	
		startGameBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  
		startGameBtn.addActionListener(new startGameHandler());		
		
		nextPlayerBtn = new javax.swing.JButton("Next Player");
		nextPlayerBtn.setMnemonic('n');
		nextPlayerBtn.setDisplayedMnemonicIndex(0);
		nextPlayerBtn.setFont(new java.awt.Font("dialog",1,17));		
		nextPlayerBtn.setVisible(false);
		nextPlayerBtn.setEnabled(false);
		add(nextPlayerBtn);	
		nextPlayerBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  
		nextPlayerBtn.addActionListener(new nextPlayerHandler());			

		newGameBtn = new javax.swing.JButton("New Game");
		newGameBtn.setFont(new java.awt.Font("dialog",1,17));		
		newGameBtn.setVisible(false);
		newGameBtn.setEnabled(false);
		add(newGameBtn);	
		newGameBtn.setBounds(loadFromFileBtn.getBounds().x,625,size.width+10,size.height);  
		//newGameBtn.addActionListener(new nextPlayerHandler());
		
		previousBtn = new javax.swing.JButton("<< Previous");
		previousBtn.setMnemonic('p');
		previousBtn.setDisplayedMnemonicIndex(3);
		previousBtn.setFont(new java.awt.Font("dialog",1,17));		
		previousBtn.setVisible(false);
		previousBtn.setEnabled(false);
		add(previousBtn);	
		previousBtn.setBounds(loadFromFileBtn.getBounds().x-size.width+30,625,size.width-40,size.height); 
		previousBtn.addActionListener(new previousHandler());
		
		adjustWidthBtn = new javax.swing.JButton("Full width");
		adjustWidthBtn.setMnemonic('f');
		adjustWidthBtn.setDisplayedMnemonicIndex(0);
		add(adjustWidthBtn);  	     
		size = adjustWidthBtn.getPreferredSize();		
		adjustWidthBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getSize().width-size.width-1,7,size.width,size.height-5);
		adjustWidthBtn.setEnabled(false);
		adjustWidthBtn.addActionListener(new adjustWidthHandler());	
		
		setWidthBtn = new javax.swing.JButton("Set");
		add(setWidthBtn);  	     
		setWidthBtn.setBounds(adjustWidthBtn.getBounds().x-size.width+25,7,size.width-30,size.height-5);
		setWidthBtn.setEnabled(false);
		setWidthBtn.addActionListener(new setWidthHandler());
		
		colWidthEdit = new javax.swing.JTextField();
		colWidthEdit.setText(String.valueOf(matrixColumnWidth));
		add(colWidthEdit);  	     
		colWidthEdit.setBounds(setWidthBtn.getBounds().x-size.width+19,7,size.width-20,size.height-5);
		colWidthEdit.setEnabled(false);		
		
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        
		setVisible(true);          
	}
	
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
	 * It is used to expand the tree.
	 * @param tree the tree structure
	 * @param parent the node to expand or collapse its children
	 * @param mode expand or collapse according current status
	 */
	private void expandAll(JTree tree, TreePath parent, int mode) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, mode);
			}
		}
		if (mode==1) tree.expandPath(parent);
		else tree.collapsePath(parent);
	}

	/**
	 * The method is to reload continents
	 */
	private void reloadContinents(){	
		//configuration
		DefaultMutableTreeNode myTreeRoot;
		if (myGame.getGameMap()==null){
			labelContinent.setText("Continents (0):");
			myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");		
		}
		else{
			labelContinent.setText("Continents ("+String.valueOf(myGame.getGameMap().continents.size())+"):");
			myTreeRoot = new DefaultMutableTreeNode("Map - "+myGame.getGameMap().riskMapName+" ");

			for (Continent loopContinent : myGame.getGameMap().continents) { 
				ArrayList<Country> loopCountriesList = myGame.getGameMap().countries.get(loopContinent.continentID);
				DefaultMutableTreeNode loopContinentNode =  new DefaultMutableTreeNode(loopContinent.continentName+" ("+loopContinent.controlNum+") ("+loopCountriesList.size()+" countries) "); 
				for (Country loopCountry:loopCountriesList){
					if (loopCountry.belongToPlayer!=null)
						loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.countryName+" ("+loopCountry.belongToPlayer.getName()+", "+loopCountry.armyNumber+" armies) "));						
					else loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.countryName+" ("+loopCountry.armyNumber+" armies) "));
				}
				myTreeRoot.add(loopContinentNode);
			}
		}	
		treeContinent= new JTree(myTreeRoot); 
		treeContinent.addMouseListener( new  MouseAdapter(){
			public void mousePressed(MouseEvent e){
				int selRow = treeContinent.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = treeContinent.getPathForLocation(e.getX(), e.getY());
				if (selRow!=-1 && (e.getButton() == 3)){
					treeContinent.setSelectionPath(selPath);
					if (selPath!=null) {
						if (selPath.getParentPath()==null){ //root node
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
		if (continentExpandMode==1) expandAll(treeContinent, new TreePath(root),1);
		else {
			if (root.getChildCount() >= 0) {
				for (Enumeration e = root.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = new TreePath(root).pathByAddingChild(n);
					expandAll(treeContinent, path, 2);
				}
			}
		}
	}

	/**
	 * The method is to reload players
	 */
	private void reloadPlayers(){	
		//configuration
		DefaultMutableTreeNode myTreeRoot;
		if (myGame.getPlayers()==null){
			labelPlayer.setText("Players (0):");
			myTreeRoot = new DefaultMutableTreeNode("Players ");		
		}
		else{
			labelPlayer.setText("Players ("+String.valueOf(myGame.getPlayers().length)+"):");
			myTreeRoot = new DefaultMutableTreeNode("Players ");

			for (int i=0;i<myGame.getPlayers().length;i++) { 
				DefaultMutableTreeNode loopPlayerNode =  new DefaultMutableTreeNode(myGame.getPlayers()[i].getName()+" ("+myGame.getPlayers()[i].getCountries().size()
						+" Countries, "+myGame.getPlayers()[i].getTotalArmies()+" armies, "+myGame.getPlayers()[i].getCardsString()+" cards) "); 
				for (Country loopCountry:myGame.getPlayers()[i].getCountries()){
					loopPlayerNode.add(new DefaultMutableTreeNode(loopCountry.countryName+" (In "+loopCountry.belongToContinent.continentName+", "+loopCountry.armyNumber+" armies) "));						
				}
				myTreeRoot.add(loopPlayerNode);
			}
		}	
		treePlayer= new JTree(myTreeRoot); 
		if  (myGame.getPlayers()!=null){
			treePlayer.addMouseListener( new  MouseAdapter(){
				public void mousePressed(MouseEvent e){
					int selRow = treePlayer.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = treePlayer.getPathForLocation(e.getX(), e.getY());
					if (selRow!=-1 && (e.getButton() == 3)){
						treePlayer.setSelectionPath(selPath);
						if (selPath!=null) {
							if (selPath.getParentPath()==null){ //root node
								playerMenu.show(e.getComponent(), e.getX()+5, e.getY()+5);
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
		if (playerExpandMode==1) expandAll(treePlayer, new TreePath(root),1);
		else {
			if (root.getChildCount() >= 0) {
				for (Enumeration e = root.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = new TreePath(root).pathByAddingChild(n);
					expandAll(treePlayer, path, 2);
				}
			}
		}
	}

	/**
	 * To resize the table columns to fit the window.
	 * @param myTable the table that needs resizing
	 * @return maxWidth maximum width of table columns
	 */
	public int FitTableColumns(JTable myTable) {
		JTableHeader header = myTable.getTableHeader();
		int rowCount = myTable.getRowCount();

		Enumeration<?> columns = myTable.getColumnModel().getColumns();
		int maxWidth = 0;
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
			int width = (int) header.getDefaultRenderer()
					.getTableCellRendererComponent(myTable, column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) myTable.getCellRenderer(row, col)
						.getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
				if (width>maxWidth) maxWidth = width;
			}
			header.setResizingColumn(column);
			column.setWidth(Math.max(width,50) + myTable.getIntercellSpacing().width + 10);
		}
		return maxWidth;
	}

	/**
	 * To resize the table columns to fit the window according to the given width
	 * @param myTable the table that needs resizing
	 * @param setWidth the given width to set the table columns
	 * @return maxWidth maximum width of table columns
	 */
	public int FitTableColumns(JTable myTable, int setWidth) {
		JTableHeader header = myTable.getTableHeader();
		int rowCount = myTable.getRowCount();

		Enumeration columns = myTable.getColumnModel().getColumns();
		int maxWidth = 0;
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
			int width = (int) header.getDefaultRenderer()
					.getTableCellRendererComponent(myTable, column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) myTable.getCellRenderer(row, col)
						.getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
				if (width>maxWidth) maxWidth = width;
			}
			header.setResizingColumn(column);
			column.setWidth(setWidth + myTable.getIntercellSpacing().width);
		}
		return maxWidth;
	}

	/**
	 * To reload the matrix after changes.
	 */
	private void reloadMatrix(){
		DefaultTableModel matrixModel;
		Object[][] newDataVector;
		Object[] newIdentifiers = null;
		if (myGame.getGameMap()==null){
			labelCountry.setText("Countries (0) and Adjacency Matrix:");
			matrixModel = new DefaultTableModel(0,0);
			adjacencyMatrix = new JTable(matrixModel){
				public boolean isCellEditable(int row, int column){
					return false;
				}//table not allow to be modified
			};
		}
		else{
			labelCountry.setText("Countries ("+String.valueOf(myGame.getGameMap().countryNum)+") and Adjacency Matrix:"); 
			matrixModel = new DefaultTableModel(myGame.getGameMap().countryNum,myGame.getGameMap().countryNum);
		
			newDataVector = new String[myGame.getGameMap().countryNum][myGame.getGameMap().countryNum];
			newIdentifiers = new String[myGame.getGameMap().countryNum];
			int i =0;
			for (Continent loopContinent : myGame.getGameMap().continents) { 
				ArrayList<Country> loopCountriesList = myGame.getGameMap().countries.get(loopContinent.continentID);
				for (Country loopCountry:loopCountriesList){
					newIdentifiers[i++] = loopCountry.countryName;
				}
			}
			for (i=0;i<myGame.getGameMap().countryNum;i++){
				ArrayList<Integer> adjacentCountryList = myGame.getGameMap().adjacencyList.get(myGame.getGameMap().findCountry((String)newIdentifiers[i]).countryID);
				for (int j=0;j<myGame.getGameMap().countryNum;j++){
					if (adjacentCountryList.contains(myGame.getGameMap().findCountry((String)newIdentifiers[j]).countryID)){
						newDataVector[i][j] = "X";
					}
					else{
						newDataVector[i][j] = "";
					}
				}
			}
			matrixModel.setDataVector(newDataVector, newIdentifiers);
			adjacencyMatrix = new JTable(matrixModel){
				public boolean isCellEditable(int row, int column){
					return false;
				}//table not allow to be modified
			};
			if (myGame.getGameMap().countryNum>0) adjacencyMatrix.setRowHeight(adjacencyMatrix.getTableHeader().getPreferredSize().height);
			int continentNum = myGame.getGameMap().continents.size();
			int[] areaContinents = new int[continentNum+1];
			areaContinents[0] = 0;
			for (i=0;i<continentNum;i++){
				areaContinents[i+1] = areaContinents[i]+myGame.getGameMap().countries.get(myGame.getGameMap().continents.get(i).continentID).size();
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
			maxWidth = FitTableColumns(adjacencyMatrix);
		else maxWidth = FitTableColumns(adjacencyMatrix,matrixColumnWidth); 		
		scrollPaneForMatrix.getViewport().removeAll();
		scrollPaneForMatrix.getViewport().add(adjacencyMatrix);
		scrollPaneForMatrix.setRowHeaderView(new RowHeaderTable(adjacencyMatrix,Math.max(70,maxWidth+10),newIdentifiers));
		
		adjustWidthBtn.setEnabled((myGame.getGameMap()==null)?false:myGame.getGameMap().countryNum>0);
		setWidthBtn.setEnabled((myGame.getGameMap()==null)?false:myGame.getGameMap().countryNum>0);
		colWidthEdit.setEnabled((myGame.getGameMap()==null)?false:myGame.getGameMap().countryNum>0);
	}

	/**
	 * Handler used to load files.
	 */
	private class loadFromFileHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(null, "load from file" );
			String inputFileName;
			JFileChooser chooser;
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("./src/map"));
			chooser.setDialogTitle("Choose map file");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new FileFilter(){
				@Override
				public boolean accept(File f){
					if(f.getName().endsWith(".map")||f.isDirectory())
						return true;
					else return false;
				}
				public String getDescription(){
					return "Map files(*.map)";
				}
			});
			if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}			
			inputFileName = chooser.getSelectedFile().getAbsolutePath();
			if (inputFileName.trim().isEmpty()){
				JOptionPane.showMessageDialog(null,"Map file name invalid");
			}
			else{
				if (myGame.loadMapFile(inputFileName.trim())){
					reloadContinents();		
					reloadMatrix();	
					loadFromFileBtn.setVisible(false);
					loadFromFileBtn.setEnabled(false);
					previousBtn.setVisible(true);
					previousBtn.setEnabled(true);
					createPlayersBtn.setVisible(true);
					createPlayersBtn.setEnabled(true);
				}
			}
		}
	}

	/**
	 * Handler used to handling previous actions
	 */
	private class previousHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			if (myGame.getGameStage()==1){
				if (myGame.getGameMap()!=null){
					myGame.clearGameMap();
				}	
				myGame.setGameStage(0);
				previousBtn.setVisible(false);
				previousBtn.setEnabled(false);
				createPlayersBtn.setVisible(false);
				createPlayersBtn.setEnabled(false);
				loadFromFileBtn.setVisible(true);
				loadFromFileBtn.setEnabled(true);
				reloadContinents();
				reloadMatrix();
			}
			else if (myGame.getGameStage()==2){
				if (myGame.getPlayers()!=null){
					myGame.clearPlayers();
				}	
				myGame.setGameStage(1);
				createPlayersBtn.setVisible(true);
				createPlayersBtn.setEnabled(true);
				startupBtn.setVisible(false);
				startupBtn.setEnabled(false);
				reloadContinents();
				reloadPlayers();
			}
			else if (myGame.getGameStage()==3){
				myGame.resetPlayersInfo();	
				myGame.setGameStage(2);
				startGameBtn.setVisible(false);
				startGameBtn.setEnabled(false);
				startupBtn.setVisible(true);
				startupBtn.setEnabled(true);
				reloadContinents();
				reloadPlayers();				
			}
		}
	}

	/**
	 * Handler used to create players.
	 */
	private class createPlayersHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			boolean retry = true;
			while(retry){
				String inputWord=JOptionPane.showInputDialog(null,"How many players do you want? \r\n(at least 2 players)");
				if (inputWord!=null){
					inputWord = inputWord.trim();
					if (inputWord.isEmpty()){
						JOptionPane.showMessageDialog(null,"Players number can't be empty.");
					}
					else{
						Pattern pattern = Pattern.compile("[0-9]*");  
						if (pattern.matcher(inputWord).matches()&&Integer.parseInt(inputWord)>=2){ 
							myGame.createPlayers(inputWord);
							reloadContinents();
							reloadPlayers();
							startupBtn.setVisible(true);
							startupBtn.setEnabled(true);
							createPlayersBtn.setVisible(false);
							createPlayersBtn.setEnabled(false);
							retry = false;
						}
						else JOptionPane.showMessageDialog(null,"Players number must be integer and >=2");
					}
				}
				else retry = false;
			}
		}
	}

	/**
	 * Handler used to start a phase
	 */
	private class startupHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			if (myGame.startupPhase()){
				startGameBtn.setVisible(true);
				startGameBtn.setEnabled(true);
				startupBtn.setVisible(false);
				startupBtn.setEnabled(false);
				reloadContinents();
				reloadPlayers();
			}
		}	
	}

	/**
	 * Handler used to start the game
	 */
	private class startGameHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			int result = myGame.startGame();
			if (result != 2){
				previousBtn.setVisible(false);
				previousBtn.setEnabled(false);
				startGameBtn.setVisible(false);
				startGameBtn.setEnabled(false);
				
				if (result == 1){ //somebody wins the game
					newGameBtn.setVisible(true);
					newGameBtn.setEnabled(true);
				}
				else{
					reloadContinents();
					reloadPlayers();
					nextPlayerBtn.setVisible(true);
					nextPlayerBtn.setEnabled(true);
				}
			}	
		}	
	}

	/**
	 * Handler for handling next player
	 */
	private class nextPlayerHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			if (myGame.playerTurn()==1){
				nextPlayerBtn.setVisible(false);
				nextPlayerBtn.setEnabled(false);
				newGameBtn.setVisible(true);
				newGameBtn.setEnabled(true);
			}
			else{
				reloadContinents();
				reloadPlayers();
			}
		}	
	}

	/**
	 * Handler used to adjust width
	 */
	private class adjustWidthHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			matrixDisplayMode = "preferred";
			FitTableColumns(adjacencyMatrix);
		}
	}

	/**
	 * Handler used to set width
	 */
	private class setWidthHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			Pattern pattern = Pattern.compile("[0-9]*");  
			String width = colWidthEdit.getText().trim();
			if (pattern.matcher(width).matches())
				matrixColumnWidth = Integer.parseInt(width);			
			else colWidthEdit.setText(String.valueOf(matrixColumnWidth));
			matrixDisplayMode = "same";
			FitTableColumns(adjacencyMatrix,matrixColumnWidth);	
		}
	}

	/**
	 * Handler for expanding action
	 */
	class mExpandAllHandler implements ActionListener {
		private String treeName;
		public mExpandAllHandler(String tree){
			treeName = tree;
		}
		public void actionPerformed(ActionEvent e) {
			JTree myTree;
			if (treeName.equals("continent")){
				myTree = treeContinent;
				if (myTree!=null) continentExpandMode = 1;
			}
			else{
				myTree = treePlayer;
				if (myTree!=null) playerExpandMode = 1;				
			}
			if (myTree!=null){
				TreeNode root = (TreeNode) myTree.getModel().getRoot();
				expandAll(myTree, new TreePath(root),1);
			}	
		}
	}

	/**
	 * Handler for collapsing action
	 */
	class mCollapseAllHandler implements ActionListener {
		private String treeName;
		public mCollapseAllHandler(String tree){
			treeName = tree;
		}		
		public void actionPerformed(ActionEvent e) {
			JTree myTree;
			if (treeName.equals("continent")){
				myTree = treeContinent;
				if (myTree!=null) continentExpandMode = 2;
			}
			else{
				myTree = treePlayer;
				if (myTree!=null) playerExpandMode = 2;				
			}			
			if (myTree!=null){
				TreeNode root = (TreeNode) myTree.getModel().getRoot();
					if (root.getChildCount() >= 0) {
					for (Enumeration eu = root.children(); eu.hasMoreElements();) {
						TreeNode n = (TreeNode) eu.nextElement();
						TreePath path = new TreePath(root).pathByAddingChild(n);
						expandAll(myTree, path, 2);
					}
				}
			}				
		}
	}		
	
	public static void main(String[] args) {  
		RiskGameMain riskGame = new RiskGameMain(); 
	}  
}

