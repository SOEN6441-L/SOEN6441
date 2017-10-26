package mapeditor;

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

import mapelements.Continent;
import mapelements.Country;
import mapelements.ErrorMsg;
import mapelements.RiskMap;

/**
 * This is a class, which used to create a window for designers to edit maps for games
 * @see JFrame
 */
public class MapEditor extends JFrame {
	private JButton newMapBtn;
	private JButton loadFromFileBtn;
	private JButton newContinentBtn;
	private JButton newCountryBtn;
	private JButton saveToFileBtn;
	private JButton completeBtn;
	private JButton clearBtn;
	private JButton setWidthBtn;
	private JButton adjustWidthBtn;	
	private JTextField colWidthEdit;
    
	private JLabel labelContinent;
	private JLabel labelCountry;
	
	private JPopupMenu continentMenu, countryMenu, mapMenu;
	private JMenuItem mDeleteContinent, mRenameContinent, mChangeContinentControl;
	private JMenuItem mDeleteCountry, mRenameCountry, mMoveCountry;
	private JMenuItem mExpandAll, mCollapseAll;	

	private JTree treeContinent;
	private JScrollPane scrollPaneForContinent;
	private JTable adjacencyMatrix;
	private JScrollPane scrollPaneForMatrix;
	
	private RiskMap myMap;
	
	private String selContinentName, selCountryName, lastUsedContinent = "";
	private String matrixDisplayMode = "preferred"; //preferred, same
	private int matrixColumnWidth = 25, continentExpandMode = 1;

	/**
	 * The constructor of class MapEditor to generate mapEditor's GUI.
	 */
	private MapEditor(){  
		
		myMap = new RiskMap("Untitled");
		//general configurations
		setTitle("Map Editor - Untitled by "+myMap.getAuthor());  
		setSize(1280,700);
		int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		setLocation((screenWidth-1280)/2, (screenHeight-700)/2-30); 
		setResizable(false);
		setLayout(null); 
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK); //handle windows events	
		setDefaultCloseOperation(3); //set exit program when close the window   
		
        //Label and TreeView for continents and countries
		labelContinent = new javax.swing.JLabel("Continents (0):");  
		add(labelContinent);  
		labelContinent.setFont(new java.awt.Font("dialog",1,15));
		Dimension size = labelContinent.getPreferredSize();
		labelContinent.setBounds(15,8,size.width+200,size.height);   
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");
		treeContinent= new JTree(myTreeRoot);
		treeContinent.setCellRenderer(new CategoryNodeRenderer());

		scrollPaneForContinent= new JScrollPane(treeContinent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPaneForContinent);
		scrollPaneForContinent.setBounds(15,35,300,580);	

	    continentMenu = new JPopupMenu();	
	    mDeleteContinent = new JMenuItem("Delete ",KeyEvent.VK_D);
	    mDeleteContinent.addActionListener(new buttonHandler());
	    mRenameContinent = new JMenuItem("Rename ",KeyEvent.VK_R);
	    mRenameContinent.addActionListener(new buttonHandler());	
	    mChangeContinentControl = new JMenuItem("Control Number",KeyEvent.VK_C);
	    mChangeContinentControl.addActionListener(new buttonHandler());
	    
	    continentMenu.add(mDeleteContinent);
	    continentMenu.addSeparator();
	    continentMenu.add(mRenameContinent);
	    continentMenu.add(mChangeContinentControl);
	    
	    countryMenu = new JPopupMenu();	
	    mDeleteCountry = new JMenuItem("Delete",KeyEvent.VK_D);
	    mDeleteCountry.addActionListener(new buttonHandler());
	    mRenameCountry = new JMenuItem("Rename",KeyEvent.VK_R);
	    mRenameCountry.addActionListener(new buttonHandler());	    
	    mMoveCountry = new JMenuItem("Move to another continent",KeyEvent.VK_M);
	    mMoveCountry.addActionListener(new buttonHandler());	    

	    countryMenu.add(mDeleteCountry);
	    countryMenu.addSeparator();
	    countryMenu.add(mRenameCountry);
	    countryMenu.add(mMoveCountry);
	    
	    mapMenu = new JPopupMenu();
	    mExpandAll = new JMenuItem("Exapnd All",KeyEvent.VK_E);
	    mExpandAll.addActionListener(new buttonHandler());	    
	    mCollapseAll = new JMenuItem("Collapse All",KeyEvent.VK_C);
	    mCollapseAll.addActionListener(new buttonHandler());
	    
	    mapMenu.add(mExpandAll);
	    mapMenu.addSeparator();
	    mapMenu.add(mCollapseAll);
	    		
		//Labels and Matrix for countries and connections
		labelCountry = new javax.swing.JLabel("Countries (0) and Adjacency Matrix:");  
		add(labelCountry);  
		labelCountry.setFont(new java.awt.Font("dialog",1,15));
		size = labelCountry.getPreferredSize();
		labelCountry.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),8,size.width+200,size.height);         

		JLabel labelPrompt = new JLabel("Double click cells to add/remove connections.");
		add(labelPrompt);  
		labelPrompt.setFont(new java.awt.Font("dialog",1,13));
		labelPrompt.setForeground(Color.RED);
		size = labelPrompt.getPreferredSize();
		labelPrompt.setBounds(labelCountry.getBounds().x,630,size.width,size.height);        
		
		scrollPaneForMatrix= new JScrollPane(null);
		add(scrollPaneForMatrix);
		scrollPaneForMatrix.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),35,946,580);
        
        //9 Buttons
		saveToFileBtn = new javax.swing.JButton("Save To File");
		saveToFileBtn.setMnemonic('s');
		saveToFileBtn.setDisplayedMnemonicIndex(0);
		add(saveToFileBtn);  	     
		size = saveToFileBtn.getPreferredSize();
		size.width+=20;
		size.height+=2;
		saveToFileBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getWidth()-size.width,625,size.width-1,size.height);
		saveToFileBtn.addActionListener(new buttonHandler());

		newCountryBtn = new javax.swing.JButton("New Country");
		newCountryBtn.setMnemonic('c');
		newCountryBtn.setDisplayedMnemonicIndex(4);
		add(newCountryBtn);  	     
		newCountryBtn.setBounds(saveToFileBtn.getBounds().x-size.width-10,625,size.width,size.height);
		newCountryBtn.addActionListener(new buttonHandler());
		
		newContinentBtn = new javax.swing.JButton("New Continent");
		newContinentBtn.setMnemonic('n');
		newContinentBtn.setDisplayedMnemonicIndex(0);
		add(newContinentBtn);  	     
		newContinentBtn.setBounds(newCountryBtn.getBounds().x-size.width-10,625,size.width,size.height);	        
		newContinentBtn.addActionListener(new buttonHandler());		
		
		newMapBtn = new javax.swing.JButton("New Map");
		newMapBtn.setMnemonic('m');
		newMapBtn.setDisplayedMnemonicIndex(4);
		add(newMapBtn);  	     
		newMapBtn.setBounds(15,625,size.width,size.height);
		newMapBtn.addActionListener(new buttonHandler());
        
		loadFromFileBtn = new javax.swing.JButton("Load Exiting Map");
		loadFromFileBtn.setMnemonic('l');
		loadFromFileBtn.setDisplayedMnemonicIndex(0);
		add(loadFromFileBtn);  	     
		loadFromFileBtn.setBounds(newMapBtn.getBounds().x+size.width+10,625,size.width+10,size.height);  
		loadFromFileBtn.addActionListener(new buttonHandler());
		
		completeBtn = new javax.swing.JButton("Complete");
		completeBtn.setMnemonic('e');
		completeBtn.setDisplayedMnemonicIndex(5);
		add(completeBtn);  	     
		size = completeBtn.getPreferredSize();
		size.height-=4;
		completeBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getWidth()-size.width-1,7,size.width,size.height);
		completeBtn.setEnabled(false);
		completeBtn.addActionListener(new buttonHandler());
		
		clearBtn = new javax.swing.JButton("Clear");
		clearBtn.setMnemonic('r');
		clearBtn.setDisplayedMnemonicIndex(4);
		add(clearBtn);  	     
		clearBtn.setBounds(completeBtn.getBounds().x-size.width-10,7,size.width,size.height);
		clearBtn.setEnabled(false);
		clearBtn.addActionListener(new buttonHandler());
		
		adjustWidthBtn = new javax.swing.JButton("Full width");
		adjustWidthBtn.setMnemonic('f');
		adjustWidthBtn.setDisplayedMnemonicIndex(0);
		add(adjustWidthBtn);  	     
		adjustWidthBtn.setBounds(clearBtn.getBounds().x-size.width-30,7,size.width,size.height);
		adjustWidthBtn.setEnabled(false);
		adjustWidthBtn.addActionListener(new buttonHandler());	
		
		setWidthBtn = new javax.swing.JButton("Set");
		add(setWidthBtn);  	     
		setWidthBtn.setBounds(adjustWidthBtn.getBounds().x-size.width+25,7,size.width-30,size.height);
		setWidthBtn.setEnabled(false);
		setWidthBtn.addActionListener(new buttonHandler());
		
		colWidthEdit = new javax.swing.JTextField();
		colWidthEdit.setText(String.valueOf(matrixColumnWidth));
		add(colWidthEdit);  	     
		colWidthEdit.setBounds(setWidthBtn.getBounds().x-size.width+19,7,size.width-20,size.height);
		colWidthEdit.setEnabled(false);		
		      
		setVisible(true);          
	}

	/**
	 * Method to handle windows event, add a prompt if there is an unsaved map upon exiting the program.
	 * @param e event that invoke this method
	 */
	@Override
	protected void processWindowEvent(WindowEvent e){
		if (e.getID() == WindowEvent.WINDOW_CLOSING){
			if (myMap.isModified()){
				if (JOptionPane.showConfirmDialog(null,
						"This will discard any modification since last save, do you want to proceed?",
						"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
					return;	
			}
		}	
		super.processWindowEvent(e);
	}

	/**
	 * Method to expand or collapse all tree structure.
	 * @param tree the tree object
	 * @param parent the parent node
	 * @param mode 1 - expand, other number - collapse
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
	 * Method to refresh the tree view of continents and countries upon modifications.
	 */
	private void reloadContinents(){	
		//configuration
		labelContinent.setText("Continents ("+String.valueOf(myMap.getContinents().size())+"):");  
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - "+myMap.getRiskMapName()+" ");
		for (Continent loopContinent : myMap.getContinents()) { 
			ArrayList<Country> loopCountriesList = loopContinent.getCountryList();
			DefaultMutableTreeNode loopContinentNode =  new DefaultMutableTreeNode(loopContinent.getName()+" ("+loopContinent.getControlNum()+") ("+loopCountriesList.size()+" countries) "); 
			for (Country loopCountry:loopCountriesList){
				loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.getName()+" "));
			}
			myTreeRoot.add(loopContinentNode);
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
						else if (selPath.getParentPath().getParentPath()==null){//continents
							selContinentName = selPath.getLastPathComponent().toString().trim();
							selContinentName = selContinentName.substring(0,selContinentName.indexOf(" ("));
							continentMenu.show(e.getComponent(), e.getX()+5, e.getY()+5);
						}
						else{//countries
							selCountryName = selPath.getLastPathComponent().toString().trim();
							countryMenu.show(e.getComponent(), e.getX()+5, e.getY()+5);
						}
					}
				}
				//popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}); 
		treeContinent.setCellRenderer(new CategoryNodeRenderer());
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
	 * Method to adjust the table's column size to preferred width.
	 * @param myTable table want to adjust
	 * @return max width of all columns
	 */
	public int fitTableColumns(JTable myTable) {
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
				int preferredWidth = (int) myTable.getCellRenderer(row, col)
						.getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferredWidth);
				if (width>maxWidth) maxWidth = width;
			}
			header.setResizingColumn(column);
			column.setWidth(Math.max(width,50) + myTable.getIntercellSpacing().width + 10);
		}
		return maxWidth;
	}

	/**
	 * Method to adjust the table's column size as the parameter setWidth.
	 * @param myTable table want to adjust
	 * @param setWidth the column size want to adjust
	 * @return max width of all columns
	 */
	public int fitTableColumns(JTable myTable, int setWidth) {
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
				int preferredWidth = (int) myTable.getCellRenderer(row, col)
						.getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferredWidth);
				if (width>maxWidth) maxWidth = width;
			}
			header.setResizingColumn(column);
			column.setWidth(setWidth + myTable.getIntercellSpacing().width);
		}
		return maxWidth;
	}

	/**
	 * Method to refresh the adjacency matrix view of the connection between countries.
	 */
	private void reloadMatrix(){	
		labelCountry.setText("Countries ("+String.valueOf(myMap.getCountryNum())+") and Adjacency Matrix:"); 
		DefaultTableModel matrixModel = new DefaultTableModel(myMap.getCountryNum(),myMap.getCountryNum());
		
		Object[][] dataVector = new String[myMap.getCountryNum()][myMap.getCountryNum()];
		Object[] identifiers = new String[myMap.getCountryNum()];
		int i =0;
		for (Continent loopContinent : myMap.getContinents()) { 
			ArrayList<Country> loopCountriesList = loopContinent.getCountryList();
			for (Country loopCountry:loopCountriesList){
				identifiers[i++] = loopCountry.getName();
			}
		}
		for (i=0;i<myMap.getCountryNum();i++){
			ArrayList<Country> adjacentCountryList = myMap.getAdjacencyList().get(myMap.findCountry((String)identifiers[i]));
			for (int j=0;j<myMap.getCountryNum();j++){
				if (adjacentCountryList.contains(myMap.findCountry((String)identifiers[j]))){
					dataVector[i][j] = "X";
				}
				else{
					dataVector[i][j] = "";
				}
			}
		}
		matrixModel.setDataVector(dataVector, identifiers);
		
		adjacencyMatrix = new JTable(matrixModel){
			public boolean isCellEditable(int row, int column){
				return false;
			}//table not allow to be modified
		};
		//adjacencyMatrix.setColumnSelectionAllowed(true);
		adjacencyMatrix.setCellSelectionEnabled(true);
		//adjacencyMatrix.setRowSelectionAllowed(false);
		adjacencyMatrix.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {					
					if (e.getClickCount() == 2) {
						ErrorMsg errorMsg;
						int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
						int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint());
						if (row!=col){
							String cellValue = (String) (matrixModel.getValueAt(row, col));
							if (cellValue.isEmpty()) {
								if ((errorMsg = myMap.addConnection((String)identifiers[row],(String)identifiers[col])).isResult()){
									matrixModel.setValueAt("X", row, col);
									matrixModel.setValueAt("X", col, row);									
								}
								else JOptionPane.showMessageDialog(null, errorMsg.getMsg());
							}	
							else {
								if ((errorMsg=myMap.removeConnection((String)identifiers[row],(String)identifiers[col])).isResult()){
									matrixModel.setValueAt("", row, col);
									matrixModel.setValueAt("", col, row);
								}
								else JOptionPane.showMessageDialog(null, errorMsg.getMsg());
							}
						}	
					}
				}
		});
		
		adjacencyMatrix.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		adjacencyMatrix.getTableHeader().setReorderingAllowed(false);
		if (myMap.getCountryNum()>0) adjacencyMatrix.setRowHeight(adjacencyMatrix.getTableHeader().getPreferredSize().height);
		int continentNum = myMap.getContinents().size();
		int[] areaContinents = new int[continentNum+1];
		areaContinents[0] = 0;
		for (i=0;i<continentNum;i++){
			areaContinents[i+1] = areaContinents[i]+myMap.getContinents().get(i).getCountryList().size();
		}
		adjacencyMatrix.setDefaultRenderer(Object.class,new MatrixRenderer(areaContinents));
		
		adjacencyMatrix.getTableHeader().setFont(new java.awt.Font("dialog",1,13));
		adjacencyMatrix.getTableHeader().setForeground(Color.BLUE);
		//adjacencyMatrix.getTableHeader().setDefaultRenderer(new AdjacencyMatrixHeaderRenderer(adjacencyMatrix));
		int maxWidth;
		if (matrixDisplayMode.equals("preferred"))
			maxWidth = fitTableColumns(adjacencyMatrix);
		else maxWidth = fitTableColumns(adjacencyMatrix,matrixColumnWidth); 
		
		scrollPaneForMatrix.getViewport().removeAll();
		scrollPaneForMatrix.getViewport().add(adjacencyMatrix);
		scrollPaneForMatrix.setRowHeaderView(new RowHeaderTable(adjacencyMatrix,Math.max(50,maxWidth+10),identifiers));
		
		completeBtn.setEnabled(myMap.getCountryNum()>1);
		clearBtn.setEnabled(myMap.getCountryNum()>1);
		adjustWidthBtn.setEnabled(myMap.getCountryNum()>0);
		setWidthBtn.setEnabled(myMap.getCountryNum()>0);
		colWidthEdit.setEnabled(myMap.getCountryNum()>0);
	}
	
	/**
	 * Method to implement response to newContinentBtn, provide GUI to input new continent's name, 
	 * then call mapelements.RiskMap#addContinent to really add a continent (controlNum initialize to 0).
	 * @see mapelements.RiskMap#addContinent(String, int)
	 */
	public void newContinent(){
		boolean retry = true;
		while (retry){
			String inputWord=JOptionPane.showInputDialog(null,"Input the continent name: ");
			if (inputWord!=null){
				if ((inputWord = inputWord.trim()).isEmpty()){
					JOptionPane.showMessageDialog(null,"Continnet name can't be empty");
				}
				else {
					ErrorMsg errorMsg;
					if ((errorMsg = myMap.addContinent(inputWord,0)).isResult()) {
						reloadContinents();
						retry = false;
					}
					else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
				}
			}
			else retry = false;
		}	
	}

	/**
	 * Method to implement response to newCountryBtn, provide GUI to input new country's name 
	 * and choose continent belongs to, then call mapelements.RiskMap#addCountry to really add a country.
	 * @see mapelements.RiskMap#addCountry(String, String, int, int)
	 */
	public void newCountry(){
		if (myMap.getContinents().size()==0)
			JOptionPane.showMessageDialog(null,"Country must belong to a continent, create at least one continent.");
		else{
			//Configure the ConfirmDialog
			JTextField countryInput = new JTextField();
			String continents[]= new String[myMap.getContinents().size()];
			int loopcount = 0, defaultIndex = 0;
			for (Continent loopContinent : myMap.getContinents()) {
				continents[loopcount++]=loopContinent.getName();
				if (loopContinent.getName().equals(lastUsedContinent)) defaultIndex = loopcount-1; 
			}
			JComboBox<Object> continentInput = new JComboBox<Object>(continents);
			Object[] message = {
					"Input country Name:", countryInput,
					"Choose Continent Name:", continentInput
			};  
			continentInput.setSelectedIndex(defaultIndex);
			boolean retry = true;
			while (retry){
				int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if (countryInput.getText()==null||countryInput.getText().trim().isEmpty()){
						JOptionPane.showMessageDialog(null,"Country name can't be empty");
					}
					else if (continentInput.getSelectedIndex()==-1){
						JOptionPane.showMessageDialog(null,"Country must belong to a continent, choose one exiting continent or create a new one.");
					}
					else {
						ErrorMsg errorMsg;
						lastUsedContinent = (String)continentInput.getItemAt(continentInput.getSelectedIndex());
						if ((errorMsg = myMap.addCountry(countryInput.getText().trim(), lastUsedContinent, 0, 0)).isResult()){
							reloadContinents();
							reloadMatrix();
							retry = false;
						}
						else JOptionPane.showMessageDialog(null, errorMsg.getMsg());

					}
				}
				else retry = false;
			}	
		}	
	}

	/**
	 * Method to implement response to saveToFileBtn, check data validity, provide GUI to input file's name,  
	 * then call mapelements.RiskMap#saveToFile to really save map to the file.
	 * @see mapelements.RiskMap#saveToFile(String)
	 * @see mapelements.RiskMap#checkErrors()
	 * @see mapelements.RiskMap#checkWarnings()
	 */
	public void saveToFile(){
		ErrorMsg errorMsg;
		if ((errorMsg = myMap.checkErrors()).isResult()){
			BasicInfoView basicInfo = new BasicInfoView(myMap,myMap.checkWarnings(),0); //mode = 0 save file
			basicInfo.setVisible(true);
			int state = basicInfo.getState();
			basicInfo.dispose();
			if (state==0) return;
			
			String outputFileName;
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("./src/map"));
			chooser.setDialogTitle("Save map file");
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
			if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}	
			outputFileName = chooser.getSelectedFile().getAbsolutePath().trim();
			if (outputFileName.isEmpty()){
				JOptionPane.showMessageDialog(null,"Map file name invalid");
			}
			else{
				if (outputFileName.indexOf(".map")==-1){
					outputFileName+=".map";
				}
				if ((errorMsg = myMap.saveToFile(outputFileName)).isResult()){
					JOptionPane.showMessageDialog(null,"Successfully save the file.");
					setTitle("Map Editor - "+myMap.getRiskMapName()+" by "+myMap.getAuthor());
					reloadContinents();
				}
				else JOptionPane.showMessageDialog(null,  errorMsg.getMsg());
			}
		}
		else JOptionPane.showMessageDialog(null,  errorMsg.getMsg());
	}

	/**
	 * Method to implement response to newMapBtn, create a new map object to edit.
	 */
	public void newMap(){
		if (myMap.isModified()){
			if (JOptionPane.showConfirmDialog(null,
					"This will discard any modification since last save, do you want to proceed?",
					"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
				return;	
		}
		myMap = null;
		myMap = new RiskMap("Untitled");
		
		setTitle("Map Editor - Untitled by "+myMap.getAuthor());   
		
		labelContinent.setText("Continents (0):");
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");
		JTree treeContinent= new JTree(myTreeRoot);
		treeContinent.setCellRenderer(new CategoryNodeRenderer());
		scrollPaneForContinent.getViewport().removeAll();
		scrollPaneForContinent.getViewport().add(treeContinent);
		
		reloadMatrix();			
	}

	/**
	 * Method to implement response to loadFromFileBtn, provide GUI to input file's name, call 
	 * mapelements.RiskMap#loadMapFile to load data into data structure, 
	 * call checkErrors,checkWarnings to validate data, 
	 * then refresh the main GUI.
	 * @see mapelements.RiskMap#loadMapFile(String)
	 * @see mapelements.RiskMap#checkErrors()
	 * @see mapelements.RiskMap#checkWarnings()
	 */
	private void loadFromFile(){
		ErrorMsg errorMsg;
		if (myMap.isModified()){
			if (JOptionPane.showConfirmDialog(null,
					"This will discard any modification since last save, do you want to proceed?",
					"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
				return;	
		}
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
		inputFileName = chooser.getSelectedFile().getAbsolutePath().trim();//
		if (inputFileName.isEmpty()){
			JOptionPane.showMessageDialog(null,"Map file name invalid");
		}
		else{
			RiskMap existingMap = new RiskMap();
			if ((errorMsg = existingMap.loadMapFile(inputFileName)).isResult()){//succeed load file data into our data structure
				if ((errorMsg = existingMap.checkErrors()).isResult()
						||JOptionPane.showConfirmDialog(null,errorMsg.getMsg()
								+"Do you still want to open the map and correct these errors?",
								"Confirm", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){//succeed check the errors
					int checkWarning = existingMap.checkWarnings();
					if (checkWarning>0) {
						BasicInfoView basicInfo = new BasicInfoView(existingMap,checkWarning,1); //mode = 1 load file
						basicInfo.setVisible(true);
						int state = basicInfo.getState();
						basicInfo.dispose();
						if (state==0) return;
					}
					myMap = null;
					myMap = existingMap;	
					myMap.setModified(false);
    		
					setTitle("Map Editor - "+myMap.getRiskMapName()+" by "+myMap.getAuthor());   
    		
					reloadContinents();		
					reloadMatrix();
				}
				else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
			}
			else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
		}
	}

	/**
	 * Method to define action performed according to different users' action.
	 */
	private class buttonHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			String buttonName = e.getActionCommand();
			switch (buttonName){
			case "New Map":
				newMap();
				break;				
			case "Load Exiting Map":
				loadFromFile();
				break;
			case "New Continent":
				newContinent();
				break;
			case "New Country":
				newCountry();
				break;
			case "Save To File":
				saveToFile();
				break;
			case "Delete ":
				deleteContinent();
				break;
			case "Rename ":
				renameContinent();
				break;
			case "Control Number":
				changeContinentControl();
				break;
			case "Delete":
				deleteCountry();
				break;
			case "Rename":
				renameCountry();
				break;
			case "Move to another continent":
				moveCountry();
				break;
			case "Complete":
				myMap.addCompletedConnection();
				reloadMatrix();	
				break;
			case "Clear":
				myMap.removeAllConnection();
				reloadMatrix();
				break;				
			case "Full width":
				matrixDisplayMode = "preferred";
				fitTableColumns(adjacencyMatrix);
				break;
			case "Set":
				Pattern pattern = Pattern.compile("[0-9]*");  
				String width = colWidthEdit.getText().trim();
				if (pattern.matcher(width).matches())
					matrixColumnWidth = Integer.parseInt(width);			
				else colWidthEdit.setText(String.valueOf(matrixColumnWidth));
				matrixDisplayMode = "same";
				fitTableColumns(adjacencyMatrix,matrixColumnWidth);	
				break;
			case "Expand All":
				if (treeContinent!=null){
					TreeNode root = (TreeNode) treeContinent.getModel().getRoot();
					continentExpandMode = 1;
					expandAll(treeContinent, new TreePath(root),1);
				}				
				break;
			case "Collapse All":
				if (treeContinent!=null){
					TreeNode root = (TreeNode) treeContinent.getModel().getRoot();
					continentExpandMode=2;
					if (root.getChildCount() >= 0) {
						for (Enumeration eu = root.children(); eu.hasMoreElements();) {
							TreeNode n = (TreeNode) eu.nextElement();
							TreePath path = new TreePath(root).pathByAddingChild(n);
							expandAll(treeContinent, path, 2);
						}
					}
				}				
				break;				
			}
		}
	}	
	
	/**
	 * Method to implement response to mDeleteContinentMenu, call 
	 * mapelements.RiskMap#deleteContinent to really delete a continent.
	 * @see mapelements.RiskMap#deleteContinent(String)
	 */
	private void deleteContinent() {
		ErrorMsg errorMsg;
		if ((errorMsg=myMap.deleteContinent(selContinentName)).isResult()){
			reloadContinents();
		}
		else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
	}

	/**
	 * Method to implement response to mDeleteCountryMenu, call 
	 * mapelements.RiskMap#deleteCountry to really delete a country.
	 * @see mapelements.RiskMap#deleteCountry(String)
	 */
	private void deleteCountry() {
		myMap.deleteCountry(selCountryName);
		reloadContinents();
		reloadMatrix();
	}

	/**
	 * Method to implement response to mRenameContinentMenu, provide GUI to input new name for a continent, then call
	 * mapelements.RiskMap#renameContinent to really rename the continent.
	 * @see mapelements.RiskMap#renameContinent(String, String)
	 */
	private void renameContinent() {
		boolean retry=true;
		while (retry){			
			String inputWord=JOptionPane.showInputDialog(null,"Input the new name for continent <"+selContinentName+">:",selContinentName);
			if (inputWord!=null){
				if (inputWord.trim().isEmpty()){
					JOptionPane.showMessageDialog(null,"Continnet name can't be empty");
				}
				else if (!inputWord.trim().equals(selContinentName)) {
					ErrorMsg errorMsg;
					if ((errorMsg=myMap.renameContinent(selContinentName,inputWord.trim())).isResult()){
						reloadContinents();
						retry = false;
					}
					else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
				}
			}
			else retry = false;
		}
	}	

	/**
	 * Method to implement response to mChangeContinentControl menu, provide GUI to input new control number for a continent, 
	 * then call mapelements.RiskMap#changeControlNum to really change a continent's control number.
	 * @see mapelements.RiskMap#changeControlNum(String, int)
	 */
	private void changeContinentControl() {
		int oldControlNum = myMap.findContinent(selContinentName).getControlNum();
		boolean retry = true;
		while(retry){
			String inputWord=JOptionPane.showInputDialog(null,"Input the new control number for continent <"+selContinentName+">:",oldControlNum);
			if (inputWord!=null){
				inputWord = inputWord.trim();
				if (inputWord.isEmpty()){
					JOptionPane.showMessageDialog(null,"Control number can't be empty");
				}
				else{
					Pattern pattern = Pattern.compile("[0-9]*");  
					if (pattern.matcher(inputWord).matches()){ 
						ErrorMsg errorMsg;
						if ((errorMsg = myMap.changeControlNum(selContinentName, Integer.parseInt(inputWord))).isResult()){
							reloadContinents();
							retry = false;
						}
						else JOptionPane.showMessageDialog(null, errorMsg.getMsg());								
					}
					else JOptionPane.showMessageDialog(null,"Control number must be integer");
				}
			}
			else retry = false;
		}	
	}

	/**
	 * Method to implement response to mRenameCountry menu, provide GUI to input new name for a country, 
	 * then call mapelements.RiskMap#renameCountry to really rename the country.
	 * @see mapelements.RiskMap#renameCountry(String, String)
	 */
	private void renameCountry() {
		boolean retry=true;
		while (retry){
			String inputWord=JOptionPane.showInputDialog(null,"Input the new name for country <"+selCountryName+">:",selCountryName);
			if (inputWord!=null){
				if (inputWord.trim().isEmpty()){
					JOptionPane.showMessageDialog(null,"Country name can't be empty");
				}
				else if (!inputWord.trim().equals(selCountryName)) {	
					ErrorMsg errorMsg;
					if ((errorMsg=myMap.renameCountry(selCountryName,inputWord.trim())).isResult()){
						reloadContinents();
						reloadMatrix();
						retry = false;
					}
					else JOptionPane.showMessageDialog(null, errorMsg.getMsg());
				}
			}
			else retry = false;
		}	
	}

	/**
	 * Method to implement response to mMoveCountry menu, provide GUI to choose a new continent for a country to move in, 
	 * then call mapelements.RiskMap#moveCountry to really move the country.
	 * @see mapelements.RiskMap#moveCountry(String, String)
	 */
	private void moveCountry() {
		if (myMap.getContinents().size()<=1){
			JOptionPane.showMessageDialog(null,"No other continents.");
			return;
		}				
		Continent oldContinent = myMap.findCountry(selCountryName).getBelongTo();
		String continents[]= new String[myMap.getContinents().size()-1];
		int loopcount = 0;
		for (Continent loopContinent : myMap.getContinents()) {
			if (!loopContinent.getName().equals(oldContinent.getName())){
				continents[loopcount++]=loopContinent.getName();
			}	
		}
		JComboBox<Object> continentInput = new JComboBox<Object>(continents);
		Object[] message = {
			"Move country <"+selCountryName+"> from <"+oldContinent.getName()+"> to:  ", continentInput
		};    	
		boolean retry = true;
		while (retry){
			int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				ErrorMsg errorMsg;
				if ((errorMsg=myMap.moveCountry((String)continentInput.getItemAt(continentInput.getSelectedIndex()), selCountryName)).isResult()){
					reloadContinents();
					reloadMatrix();
					retry = false;
				}	
				else JOptionPane.showMessageDialog(null, errorMsg.getMsg());
			}
			else retry = false;
		}	
	}

	/**
	 * Main method to startup the map editor.
	 * @param args command line parameters
	 */
	public static void main(String[] args) {  
		new MapEditor(); 
	}  
}
    


