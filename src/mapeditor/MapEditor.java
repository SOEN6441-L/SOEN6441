package mapeditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import mapelements.Continent;
import mapelements.Country;
import mapelements.RiskMap;

/**
 * This is a class, which used to create a window for player to edit maps for games
 * 
 *
 */
public class MapEditor extends JFrame {

	//button in window
	private JButton newMapBtn;
	private JButton loadFromFileBtn;
	private JButton newContinentBtn;
	private JButton newCountryBtn;
	private JButton saveToFileBtn;
	private JButton completeBtn;
	private JButton clearBtn;
    
	//label show info
	private JLabel labelContinent;
	private JLabel labelCountry;
	
	private JPopupMenu continentMenu, countryMenu;
	private JMenuItem mDeleteContinent, mRenameContinent, mChangeContinentControl;
	private JMenuItem mDeleteCountry, mRenameCountry, mMoveCountry;

	private JScrollPane scrollPaneForContinent;
	private JScrollPane scrollPaneForMatrix;
	
	private RiskMap newMap;
	
	private String selContinentName, selCountryName, lastUsedContinent="";
    
	private void initGUI(){  
		
		newMap = new RiskMap("Untitled");
		//configuration
		setTitle("Map Editor - Untitled by "+newMap.author);  
		setSize(1280,700);
		int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		setLocation((screenWidth-1280)/2, (screenHeight-700)/2);  
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
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - Untitled");
		JTree treeContinent= new JTree(myTreeRoot);
		treeContinent.setCellRenderer(new CategoryNodeRenderer());

		scrollPaneForContinent= new JScrollPane(treeContinent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPaneForContinent);  
		scrollPaneForContinent.setBounds(15,35,300,580);	

	    continentMenu = new JPopupMenu();	
	    mDeleteContinent = new JMenuItem("Delete     ",KeyEvent.VK_D);
	    mDeleteContinent.addActionListener(new mDeleteContinentHandler());
	    mRenameContinent = new JMenuItem("Rename     ",KeyEvent.VK_R);
	    mRenameContinent.addActionListener(new mRenameContinentHandler());	
	    mChangeContinentControl = new JMenuItem("Control Number     ",KeyEvent.VK_C);
	    mChangeContinentControl.addActionListener(new mChangeContinentControlHandler());
	    
	    continentMenu.add(mDeleteContinent);
	    continentMenu.addSeparator();
	    continentMenu.add(mRenameContinent);
	    continentMenu.add(mChangeContinentControl);
	    
	    countryMenu = new JPopupMenu();	
	    mDeleteCountry = new JMenuItem("Delete     ",KeyEvent.VK_D);
	    mDeleteCountry.addActionListener(new mDeleteCountryHandler());
	    mRenameCountry = new JMenuItem("Rename     ",KeyEvent.VK_R);
	    mRenameCountry.addActionListener(new mRenameCountryHandler());	    
	    mMoveCountry = new JMenuItem("Move to another continent",KeyEvent.VK_M);
	    mMoveCountry.addActionListener(new mMoveCountryHandler());	    

	    countryMenu.add(mDeleteCountry);
	    countryMenu.addSeparator();
	    countryMenu.add(mRenameCountry);
	    countryMenu.add(mMoveCountry);
	    		
		//Matrix for countries and connections
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
		
		/*DefaultTableModel matrixModel = new DefaultTableModel(0,0);
		Object[] newIdentifiers={};
		matrixModel.setColumnIdentifiers(newIdentifiers);
		JTable adjacencyMatrix = new JTable(matrixModel);
		adjacencyMatrix.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
		adjacencyMatrix.setDefaultRenderer(Object.class,new MatrixRenderer());         
		scrollPaneForMatrix= new JScrollPane(adjacencyMatrix);
		scrollPaneForMatrix.setRowHeaderView(new RowHeaderTable(adjacencyMatrix,90,newIdentifiers));*/
		scrollPaneForMatrix= new JScrollPane(null);
		add(scrollPaneForMatrix);
		scrollPaneForMatrix.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),35,946,580);
        
        //5 Buttons
		saveToFileBtn = new javax.swing.JButton("Save To File");
		saveToFileBtn.setMnemonic('s');
		saveToFileBtn.setDisplayedMnemonicIndex(0);
		add(saveToFileBtn);  	     
		size = saveToFileBtn.getPreferredSize();
		size.width+=20;
		size.height+=2;
		saveToFileBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getWidth()-size.width,625,size.width-1,size.height);
		saveToFileBtn.addActionListener(new saveToFileHandler());

		newCountryBtn = new javax.swing.JButton("New Country");
		newCountryBtn.setMnemonic('c');
		newCountryBtn.setDisplayedMnemonicIndex(4);
		add(newCountryBtn);  	     
		newCountryBtn.setBounds(saveToFileBtn.getBounds().x-size.width-10,625,size.width,size.height);
		newCountryBtn.addActionListener(new newCountryHandler());
		
		newContinentBtn = new javax.swing.JButton("New Continent");
		newContinentBtn.setMnemonic('n');
		newContinentBtn.setDisplayedMnemonicIndex(0);
		add(newContinentBtn);  	     
		newContinentBtn.setBounds(newCountryBtn.getBounds().x-size.width-10,625,size.width,size.height);	        
		newContinentBtn.addActionListener(new newContinentHandler());		
		
		newMapBtn = new javax.swing.JButton("New Map");
		newMapBtn.setMnemonic('m');
		newMapBtn.setDisplayedMnemonicIndex(4);
		add(newMapBtn);  	     
		newMapBtn.setBounds(15,625,size.width,size.height);
		newMapBtn.addActionListener(new newMapHandler());
        
		loadFromFileBtn = new javax.swing.JButton("Load Exiting Map");
		loadFromFileBtn.setMnemonic('l');
		loadFromFileBtn.setDisplayedMnemonicIndex(0);
		add(loadFromFileBtn);  	     
		loadFromFileBtn.setBounds(newMapBtn.getBounds().x+size.width+10,625,size.width+10,size.height);  
		loadFromFileBtn.addActionListener(new loadFromFileHandler());
		
		completeBtn = new javax.swing.JButton("Complete");
		completeBtn.setMnemonic('e');
		completeBtn.setDisplayedMnemonicIndex(5);
		add(completeBtn);  	     
		size = completeBtn.getPreferredSize();
		size.height-=4;
		completeBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getWidth()-size.width-1,7,size.width,size.height);
		completeBtn.setEnabled(false);
		completeBtn.addActionListener(new completeHandler());
		
		clearBtn = new javax.swing.JButton("Clear");
		clearBtn.setMnemonic('r');
		clearBtn.setDisplayedMnemonicIndex(4);
		add(clearBtn);  	     
		clearBtn.setBounds(completeBtn.getBounds().x-size.width-10,7,size.width,size.height);
		clearBtn.setEnabled(false);
		clearBtn.addActionListener(new clearHandler());
        
		setVisible(true);          
	}
	
	private void reloadNewMap(){  		
		newMap = null;
		newMap = new RiskMap("Untitled");
		
		setTitle("Map Editor - Untitled by "+newMap.author);    
		labelContinent.setText("Continents (0):");  
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - Untitled");
		JTree treeContinent= new JTree(myTreeRoot);
		treeContinent.setCellRenderer(new CategoryNodeRenderer());
		scrollPaneForContinent.getViewport().removeAll();
		scrollPaneForContinent.getViewport().add(treeContinent);
		
		reloadMatrix();		
	}
	
	private void expandAll(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path);
			}
		}
		tree.expandPath(parent);
		// tree.collapsePath(parent);
	}
	
	private void reloadContinents(){	
		//configuration
		labelContinent.setText("Continents ("+String.valueOf(newMap.continents.size())+"):");  
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - Untitled");
		for (Continent loopContinent : newMap.continents) { 
			ArrayList<Country> loopCountriesList = newMap.countries.get(loopContinent.continentID);
			DefaultMutableTreeNode loopContinentNode =  new DefaultMutableTreeNode(loopContinent.continentName+" ("+loopContinent.controlNum+") ("+loopCountriesList.size()+" countries)"); 
			for (Country loopCountry:loopCountriesList){
				loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.countryName));
			}
			myTreeRoot.add(loopContinentNode);
		}
		JTree treeContinent= new JTree(myTreeRoot);
		treeContinent.addMouseListener( new  MouseAdapter(){
			public void mousePressed(MouseEvent e){
				int selRow = treeContinent.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = treeContinent.getPathForLocation(e.getX(), e.getY());
				if (selRow!=-1 && (e.getButton() == 3)){
					treeContinent.setSelectionPath(selPath);
					if (selPath!=null) {
						if (selPath.getParentPath()==null){ //root node
						}
						else if (selPath.getParentPath().getParentPath()==null){//continents
							selContinentName = selPath.getLastPathComponent().toString();
							selContinentName = selContinentName.substring(0,selContinentName.indexOf(" ("));
							continentMenu.show(e.getComponent(), e.getX()+5, e.getY()+5);
						}
						else{//countries
							selCountryName = selPath.getLastPathComponent().toString();
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
		expandAll(treeContinent, new TreePath(root));
	}
	
	public int FitTableColumns(JTable myTable) {
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
			column.setWidth(Math.max(width,50) + myTable.getIntercellSpacing().width + 10);
		}
		return maxWidth;
	}

	private void reloadMatrix(){	

		labelCountry.setText("Countries ("+String.valueOf(newMap.countryNum)+") and Adjacency Matrix:"); 
		DefaultTableModel matrixModel = new DefaultTableModel(newMap.countryNum,newMap.countryNum);
		
		Object[][] newDataVector = new String[newMap.countryNum][newMap.countryNum];
		Object[] newIdentifiers = new String[newMap.countryNum];
		int i =0;
		for (Continent loopContinent : newMap.continents) { 
			ArrayList<Country> loopCountriesList = newMap.countries.get(loopContinent.continentID);
			for (Country loopCountry:loopCountriesList){
				newIdentifiers[i++] = loopCountry.countryName;
			}
		}
		for (i=0;i<newMap.countryNum;i++){
			ArrayList<Integer> adjacentCountryList = newMap.adjacencyList.get(newMap.findCountry((String)newIdentifiers[i]).countryID);
			for (int j=0;j<newMap.countryNum;j++){
				if (adjacentCountryList.contains(newMap.findCountry((String)newIdentifiers[j]).countryID)){
					newDataVector[i][j] = "X";
				}
				else{
					newDataVector[i][j] = "";
				}
			}
		}
		matrixModel.setDataVector(newDataVector, newIdentifiers);
		
		JTable adjacencyMatrix = new JTable(matrixModel){
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
						int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
						int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint());
						if (row!=col){
							String cellValue = (String) (matrixModel.getValueAt(row, col));
							if (cellValue.isEmpty()) {
								if (newMap.addConnection((String)newIdentifiers[row],(String)newIdentifiers[col])){
									matrixModel.setValueAt("X", row, col);
									matrixModel.setValueAt("X", col, row);									
								}
							}	
							else {
								if (newMap.removeConnection((String)newIdentifiers[row],(String)newIdentifiers[col])){
									matrixModel.setValueAt("", row, col);
									matrixModel.setValueAt("", col, row);
								}
							}
						}	
					}
				}
		});
		
		adjacencyMatrix.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		adjacencyMatrix.getTableHeader().setReorderingAllowed(false);
		if (newMap.countryNum>0) adjacencyMatrix.setRowHeight(adjacencyMatrix.getTableHeader().getPreferredSize().height);
		int continentNum = newMap.continents.size();
		int[] areaContinents = new int[continentNum+1];
		areaContinents[0] = 0;
		for (i=0;i<continentNum;i++){
			areaContinents[i+1] = areaContinents[i]+newMap.countries.get(newMap.continents.get(i).continentID).size();
		}
		adjacencyMatrix.setDefaultRenderer(Object.class,new MatrixRenderer(areaContinents));
		
		adjacencyMatrix.getTableHeader().setFont(new java.awt.Font("dialog",1,13));
		adjacencyMatrix.getTableHeader().setForeground(Color.BLUE);
		//adjacencyMatrix.getTableHeader().setDefaultRenderer(new AdjacencyMatrixHeaderRenderer(adjacencyMatrix));
		int maxWidth = FitTableColumns(adjacencyMatrix);
		
		scrollPaneForMatrix.getViewport().removeAll();
		scrollPaneForMatrix.getViewport().add(adjacencyMatrix);
		scrollPaneForMatrix.setRowHeaderView(new RowHeaderTable(adjacencyMatrix,Math.max(70,maxWidth+10),newIdentifiers));
		
		completeBtn.setEnabled(newMap.countryNum>1);
		clearBtn.setEnabled(newMap.countryNum>1);
	}
		
	private class newContinentHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(null, "new continent" );
			String inputWord=JOptionPane.showInputDialog(null,"Input the continent name: ");
			if (inputWord!=null){
				if (inputWord.trim().isEmpty()){
					JOptionPane.showMessageDialog(null,"Continnet name can't be empty");
				}
				else {
					if (newMap.addContinent(inputWord.trim())){;
						reloadContinents();
					}
				}
			}
		}
	}
	
	private class newCountryHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(null, "new country" );
			if (newMap.continents.size()==0)
				JOptionPane.showMessageDialog(null,"Country must belong to a continent, create at least one continent.");
			else{
				//Configure the ConfirmDialog
				JTextField countryInput = new JTextField();
				String continents[]= new String[newMap.continents.size()];
				int loopcount = 0, defaultIndex = 0;
				for (Continent loopContinent : newMap.continents) {
					continents[loopcount++]=loopContinent.continentName;
					if (loopContinent.continentName.equals(lastUsedContinent)) defaultIndex = loopcount-1; 
				}
				JComboBox continentInput = new JComboBox(continents);
				Object[] message = {
						"Input country Name:", countryInput,
						"Choose Continent Name:", continentInput
				};  
				continentInput.setSelectedIndex(defaultIndex);
				int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if (countryInput.getText()!=null){
						if (countryInput.getText().trim().isEmpty()){
							JOptionPane.showMessageDialog(null,"Country name can't be empty");
						}
						else if (continentInput.getSelectedIndex()==-1){
							JOptionPane.showMessageDialog(null,"Country must belong to a continent, choose one exiting continent or create a new one.");
						}
						else {
							lastUsedContinent = (String)continentInput.getItemAt(continentInput.getSelectedIndex());
							if (newMap.addCountry(countryInput.getText().trim(), lastUsedContinent)){
								reloadContinents();
								reloadMatrix();
							}
						}
					}	
				}
			}	
		}		
	}   
    
	private class saveToFileHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "save to file" );
		}
	}      
    
	private class newMapHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(null, "new map" );
			if (newMap.modified){
				if (JOptionPane.showConfirmDialog(null,
						"This will discard any modification since last save, do you want to proceed?",
						"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
					return;	
			}
			reloadNewMap();
		}
	}
    
	private class loadFromFileHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(null, "load from file" );
			if (newMap.modified){
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
            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            	return;
            }			
        	inputFileName = chooser.getSelectedFile().getAbsolutePath();
		}
	} 
	
	private class completeHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			newMap.addCompletedConnection();
			reloadMatrix();
		}
	}
	
	private class clearHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			newMap.removeAllConnection();
			reloadMatrix();
		}
	}
	
	class mDeleteContinentHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(null,selContinentName);
			if (newMap.deleteContinent(selContinentName)){
				reloadContinents();
			}
		}
	}
	
	class mDeleteCountryHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (newMap.deleteCountry(selCountryName)){
				reloadContinents();
				reloadMatrix();
			}	
		}
	}	
	
	private class mRenameContinentHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			String inputWord=JOptionPane.showInputDialog(null,"Input the new name for continent '"+selContinentName+"':",selContinentName);
			if (inputWord!=null){
				if (inputWord.trim().isEmpty()){
					JOptionPane.showMessageDialog(null,"Continnet name can't be empty");
				}
				else if (!inputWord.trim().equals(selContinentName)) {					
					if (newMap.renameContinent(selContinentName,inputWord.trim())){
						reloadContinents();
					}
				}
			}
		}
	}	
	
	private class mChangeContinentControlHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			int oldControlNum = newMap.findContinent(selContinentName).controlNum;
			String inputWord=JOptionPane.showInputDialog(null,"Input the new control number for continent '"+selContinentName+"':",oldControlNum);
			if (inputWord!=null){
				if (inputWord.trim().isEmpty()){
					JOptionPane.showMessageDialog(null,"Control number can't be empty");
				}
				else{
					Pattern pattern = Pattern.compile("[0-9]*");  
					if (pattern.matcher(inputWord.trim()).matches()){ 
						if (newMap.changeContinentControl(selContinentName, Integer.parseInt(inputWord.trim())))
						reloadContinents();
					}
					else JOptionPane.showMessageDialog(null,"Control number must be integer");
				}
			}
		}
	}	
	
	private class mRenameCountryHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			String inputWord=JOptionPane.showInputDialog(null,"Input the new name for country '"+selCountryName+"':",selCountryName);
			if (inputWord!=null){
				if (inputWord.trim().isEmpty()){
					JOptionPane.showMessageDialog(null,"Country name can't be empty");
				}
				else if (!inputWord.trim().equals(selCountryName)) {					
					if (newMap.renameCountry(selCountryName,inputWord.trim())){
						reloadContinents();
						reloadMatrix();
					}
				}
			}
		}
	}
	
	private class mMoveCountryHandler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			Continent oldContinent = newMap.findCountry(selCountryName).belongToContinent;
			String continents[]= new String[newMap.continents.size()-1];
			int loopcount = 0;
			for (Continent loopContinent : newMap.continents) {
				if (!loopContinent.continentName.equals(oldContinent.continentName)){
					continents[loopcount++]=loopContinent.continentName;
				}	
			}
			JComboBox continentInput = new JComboBox(continents);
			Object[] message = {
				"Move country '"+selCountryName+"' from '"+oldContinent.continentName+"' to:  ", continentInput
			};    	
			int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				if (continentInput.getSelectedIndex()==-1){
					JOptionPane.showMessageDialog(null,"Country must belong to a continent, choose one exiting continent or create a new one.");
				}
				else {
					if (newMap.moveContinentCountry((String)continentInput.getItemAt(continentInput.getSelectedIndex()), selCountryName)){
						reloadContinents();
						reloadMatrix();
					}	
				}
			}
		}
	}	
	
	public static void main(String[] args) {  
		MapEditor myMapEditor = new MapEditor();  
		myMapEditor.initGUI();  
	}  
}
    


