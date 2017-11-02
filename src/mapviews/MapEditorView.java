package mapviews;

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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import mapcontrollers.MapEditorController;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;
import mapmodels.ErrorMsg;
import mapmodels.RiskMapModel;

/**
 * Class used to create a window for designers to edit maps for games
 * @see JFrame
 */
public class MapEditorView extends JFrame implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	private RiskMapModel myMapModel;
	
	private String selectedObj;
	private String matrixDisplayMode = "preferred"; //preferred, same
	private int matrixColumnWidth = 25, continentExpandMode = 1;

	/**
	 * The constructor of class MapEditor to generate mapEditor's GUI.
	 */
	public MapEditorView(){
		//general configurations
		//this.myMapModel = myMapModel;
		setTitle("Map Editor - by Invincible Team Four");  
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
		
		ButtonHandler buttonHandler = new ButtonHandler();
		
	    continentMenu = new JPopupMenu();	
	    mDeleteContinent = new JMenuItem("Delete ",KeyEvent.VK_D);
	    mRenameContinent = new JMenuItem("Rename ",KeyEvent.VK_R);
	    mChangeContinentControl = new JMenuItem("Control Number",KeyEvent.VK_C);
	    
	    continentMenu.add(mDeleteContinent);
	    continentMenu.addSeparator();
	    continentMenu.add(mRenameContinent);
	    continentMenu.add(mChangeContinentControl);
	    
	    countryMenu = new JPopupMenu();	
	    mDeleteCountry = new JMenuItem("Delete",KeyEvent.VK_D);
	    mRenameCountry = new JMenuItem("Rename",KeyEvent.VK_R);
	    mMoveCountry = new JMenuItem("Move to another continent",KeyEvent.VK_M);

	    countryMenu.add(mDeleteCountry);
	    countryMenu.addSeparator();
	    countryMenu.add(mRenameCountry);
	    countryMenu.add(mMoveCountry);
	    
	    mapMenu = new JPopupMenu();
	    mExpandAll = new JMenuItem("Expand All",KeyEvent.VK_E);
	    mExpandAll.addActionListener(buttonHandler);	    
	    mCollapseAll = new JMenuItem("Collapse All",KeyEvent.VK_C);
	    mCollapseAll.addActionListener(buttonHandler);
	    
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

		newCountryBtn = new javax.swing.JButton("New Country");
		newCountryBtn.setMnemonic('c');
		newCountryBtn.setDisplayedMnemonicIndex(4);
		add(newCountryBtn);  	     
		newCountryBtn.setBounds(saveToFileBtn.getBounds().x-size.width-10,625,size.width,size.height);
		
		newContinentBtn = new javax.swing.JButton("New Continent");
		newContinentBtn.setMnemonic('n');
		newContinentBtn.setDisplayedMnemonicIndex(0);
		add(newContinentBtn);  	     
		newContinentBtn.setBounds(newCountryBtn.getBounds().x-size.width-10,625,size.width,size.height);	        
		
		newMapBtn = new javax.swing.JButton("New Map");
		newMapBtn.setMnemonic('m');
		newMapBtn.setDisplayedMnemonicIndex(4);
		add(newMapBtn);  	     
		newMapBtn.setBounds(15,625,size.width,size.height);
        
		loadFromFileBtn = new javax.swing.JButton("Load Exiting Map");
		loadFromFileBtn.setMnemonic('l');
		loadFromFileBtn.setDisplayedMnemonicIndex(0);
		add(loadFromFileBtn);  	     
		loadFromFileBtn.setBounds(newMapBtn.getBounds().x+size.width+10,625,size.width+10,size.height);  
		
		completeBtn = new javax.swing.JButton("Complete");
		completeBtn.setMnemonic('e');
		completeBtn.setDisplayedMnemonicIndex(5);
		add(completeBtn);  	     
		size = completeBtn.getPreferredSize();
		size.height-=4;
		completeBtn.setBounds(scrollPaneForMatrix.getBounds().x+scrollPaneForMatrix.getWidth()-size.width-1,7,size.width,size.height);
		completeBtn.setEnabled(false);
		
		clearBtn = new javax.swing.JButton("Clear");
		clearBtn.setMnemonic('r');
		clearBtn.setDisplayedMnemonicIndex(4);
		add(clearBtn);  	     
		clearBtn.setBounds(completeBtn.getBounds().x-size.width-10,7,size.width,size.height);
		clearBtn.setEnabled(false);
		
		adjustWidthBtn = new javax.swing.JButton("Full width");
		adjustWidthBtn.setMnemonic('f');
		adjustWidthBtn.setDisplayedMnemonicIndex(0);
		add(adjustWidthBtn);  	     
		adjustWidthBtn.setBounds(clearBtn.getBounds().x-size.width-30,7,size.width,size.height);
		adjustWidthBtn.setEnabled(false);
		adjustWidthBtn.addActionListener(buttonHandler);	
		
		setWidthBtn = new javax.swing.JButton("Set");
		add(setWidthBtn);  	     
		setWidthBtn.setBounds(adjustWidthBtn.getBounds().x-size.width+25,7,size.width-30,size.height);
		setWidthBtn.setEnabled(false);
		setWidthBtn.addActionListener(buttonHandler);
		
		colWidthEdit = new javax.swing.JTextField();
		colWidthEdit.setText(String.valueOf(matrixColumnWidth));
		add(colWidthEdit);  	     
		colWidthEdit.setBounds(setWidthBtn.getBounds().x-size.width+19,7,size.width-20,size.height);
		colWidthEdit.setEnabled(false);		
		      
		//setVisible(true);          
	}

	/**
	 * Method to re-initialize the GUI.
	 */
	private void reInitializeGUI(){
		labelContinent.setText("Continents (0):");
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - Untitled ");
		JTree treeContinent= new JTree(myTreeRoot);
		treeContinent.setCellRenderer(new CategoryNodeRenderer());
		scrollPaneForContinent.getViewport().removeAll();
		scrollPaneForContinent.getViewport().add(treeContinent);	
		reloadMatrix();	
	}
	
	/**
	 * Method to handle windows event, add a prompt if there is an unsaved map upon exiting the program.
	 * @param e event that invoke this method
	 */
	@Override
	protected void processWindowEvent(WindowEvent e){
		if (e.getID() == WindowEvent.WINDOW_CLOSING){
			if (myMapModel.isModified()){
				if (JOptionPane.showConfirmDialog(null,
						"This will discard any modification since last save, do you want to proceed?",
						"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
					return;	
			}
		}	
		super.processWindowEvent(e);
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
			switch (buttonName){
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
					TreeNode rootNode = (TreeNode) treeContinent.getModel().getRoot();
					continentExpandMode=2;
					if (rootNode.getChildCount() >= 0) {
						for (Enumeration<?> eu = rootNode.children(); eu.hasMoreElements();) {
							TreeNode n = (TreeNode) eu.nextElement();
							TreePath path = new TreePath(rootNode).pathByAddingChild(n);
							expandAll(treeContinent, path, 2);
						}
					}
				}				
				break;				
			}
		}
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
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, mode);
			}
		}
		if (mode==1) tree.expandPath(parent);
		else tree.collapsePath(parent);
	}

	/**
	 * Method to get the user select object.
	 * @return object's name
	 */
	public String getSelectedObj() {
		return selectedObj;
	}
	
	/**
	 * Method to refresh the tree view of continents and countries upon modifications.
	 */
	private void reloadContinents(){	
		//configuration
		labelContinent.setText("Continents ("+String.valueOf(myMapModel.getContinents().size())+"):");  
        
		DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Map - "+myMapModel.getRiskMapName()+" ");
		for (ContinentModel loopContinent : myMapModel.getContinents()) { 
			ArrayList<CountryModel> loopCountriesList = loopContinent.getCountryList();
			DefaultMutableTreeNode loopContinentNode =  new DefaultMutableTreeNode(loopContinent.getName()+" ("+loopContinent.getControlNum()+") ("+loopCountriesList.size()+" countries) "); 
			for (CountryModel loopCountry:loopCountriesList){
				loopContinentNode.add(new DefaultMutableTreeNode(loopCountry.getName()+" "));
			}
			myTreeRoot.add(loopContinentNode);
		}	
		treeContinent = new JTree(myTreeRoot);
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
							selectedObj = selPath.getLastPathComponent().toString().trim();
							selectedObj = selectedObj.substring(0,selectedObj.indexOf(" ("));
							continentMenu.show(e.getComponent(), e.getX()+5, e.getY()+5);
						}
						else{//countries
							selectedObj = selPath.getLastPathComponent().toString().trim();
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
				for (Enumeration<?> e = root.children(); e.hasMoreElements();) {
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
	private int fitTableColumns(JTable myTable) {
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
	private int fitTableColumns(JTable myTable, int setWidth) {
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
			column.setWidth(setWidth + myTable.getIntercellSpacing().width);
		}
		return maxWidth;
	}

	/**
	 * Method to refresh the adjacency matrix view of the connection between countries.
	 */
	private void reloadMatrix(){	
		labelCountry.setText("Countries ("+String.valueOf(myMapModel.getCountryNum())+") and Adjacency Matrix:"); 
		DefaultTableModel matrixModel = new DefaultTableModel(myMapModel.getCountryNum(),myMapModel.getCountryNum());
		
		Object[][] dataVector = new String[myMapModel.getCountryNum()][myMapModel.getCountryNum()];
		Object[] identifiers = new String[myMapModel.getCountryNum()];
		int i =0;
		for (ContinentModel loopContinent : myMapModel.getContinents()) { 
			ArrayList<CountryModel> loopCountriesList = loopContinent.getCountryList();
			for (CountryModel loopCountry:loopCountriesList){
				identifiers[i++] = loopCountry.getName();
			}
		}
		for (i=0;i<myMapModel.getCountryNum();i++){
			ArrayList<CountryModel> adjacentCountryList =myMapModel.getAdjacencyList().get(myMapModel.findCountry((String)identifiers[i]));
			for (int j=0;j<myMapModel.getCountryNum();j++){
				if (adjacentCountryList.contains(myMapModel.findCountry((String)identifiers[j]))){
					dataVector[i][j] = "X";
				}
				else{
					dataVector[i][j] = "";
				}
			}
		}
		matrixModel.setDataVector(dataVector, identifiers);
		
		adjacencyMatrix = new JTable(matrixModel){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
								if ((errorMsg = myMapModel.addConnection((String)identifiers[row],(String)identifiers[col])).isResult()){
									matrixModel.setValueAt("X", row, col);
									matrixModel.setValueAt("X", col, row);									
								}
								else JOptionPane.showMessageDialog(null, errorMsg.getMsg());
							}	
							else {
								if ((errorMsg=myMapModel.removeConnection((String)identifiers[row],(String)identifiers[col])).isResult()){
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
		if (myMapModel.getCountryNum()>0) adjacencyMatrix.setRowHeight(adjacencyMatrix.getTableHeader().getPreferredSize().height);
		int continentNum = myMapModel.getContinents().size();
		int[] areaContinents = new int[continentNum+1];
		areaContinents[0] = 0;
		for (i=0;i<continentNum;i++){
			areaContinents[i+1] = areaContinents[i]+myMapModel.getContinents().get(i).getCountryList().size();
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
		
		completeBtn.setEnabled(myMapModel.getCountryNum()>1);
		clearBtn.setEnabled(myMapModel.getCountryNum()>1);
		adjustWidthBtn.setEnabled(myMapModel.getCountryNum()>0);
		setWidthBtn.setEnabled(myMapModel.getCountryNum()>0);
		colWidthEdit.setEnabled(myMapModel.getCountryNum()>0);
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
		switch (type){
		case 0:
			reloadContinents();
			break;
		case 1:
			reloadMatrix();
			break;	
		case 2:
			reloadContinents();
			reloadMatrix();
			break;
		case 4:	
			reInitializeGUI();
			break;	
		}
	}
	
	/**
	 * Method to change the related RiskMap object.
	 * @param newMap the new RiskMap object
	 */
	public void changeMapModel(RiskMapModel newMap){
		myMapModel.deleteObservers();
		myMapModel = null;
		myMapModel = newMap;	
		myMapModel.setModified(false);
		myMapModel.addObserver(this);
		myMapModel.generateNotify();
	}
	
	/**
	 * Method to change the related RiskMap object.
	 * @param myMap the new RiskMap model
	 */
	public void addModel(RiskMapModel myMap){
		myMapModel = myMap;
	}	
	
	/**
	 * Method to add controller to relative components.
	 * @param controller the new controller
	 */
	public void addController(MapEditorController controller){
	    mDeleteContinent.addActionListener(controller);
	    mRenameContinent.addActionListener(controller);	
	    mChangeContinentControl.addActionListener(controller);
	    mDeleteCountry.addActionListener(controller);
	    mRenameCountry.addActionListener(controller);	    
	    mMoveCountry.addActionListener(controller);	    
		saveToFileBtn.addActionListener(controller);
		newCountryBtn.addActionListener(controller);
		newContinentBtn.addActionListener(controller);		
		newMapBtn.addActionListener(controller);
		loadFromFileBtn.addActionListener(controller);
		completeBtn.addActionListener(controller);
		clearBtn.addActionListener(controller);
	}
}