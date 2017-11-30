package gameviewsremote;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import gamemodels.PlayerModel;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;
import mapmodels.RiskMapModel;

/**
 * Render the matrix in table showing the relationship of countries.
 */
class MatrixRenderer implements TableCellRenderer{     
	private int[] areaContinents;
	private RiskMapModel myMap;
	
	public static final DefaultTableCellRenderer DEFAULT_RENDERER =new DefaultTableCellRenderer(); 
	
	/**
	 * Constructor of class
	 * @param area upper and lower bounds of each continents 
	 * @param myMap the map object
	 */
	public MatrixRenderer(int[] area, RiskMapModel myMap){
		this.areaContinents = area;
		this.myMap = myMap;
	}


	/**
	 * Method to get cells in matrix and render every cell, highlight the areas for each continent, 
	 * use different colors to display connections in matrix (upper_right light-gray, lower_left red). 
	 * @param table	JTable object
	 * @param value	value of cell
	 * @param isSelected selection status of cell
	 * @param hasFocus focus status of cell
	 * @param row row of cell
	 * @param column column of cell
	 * @return the component used for drawing the cell. 
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column){ 
		
		DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.CENTER);
		Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
		renderer.setFont(new java.awt.Font("dialog",Font.BOLD,12));
		renderer.setForeground(Color.RED);
		Color background;
		table.setOpaque(true);
		renderer.setPreferredSize(renderer.getPreferredSize());
		if (row == column) {     
			//foreground = Color.YELLOW;     
			background = new Color(210,210,210); 
			PlayerModel player = (myMap.findCountry(table.getColumnName(column)).getOwner());
			if (player==null) background = new Color(210,210,210);
			else background = player.getMyColor();
			//renderer.setForeground(foreground);     
			renderer.setBackground(background);   
		} 
		else{
			renderer.setBackground(Color.WHITE); 
			for (int i=0;i<areaContinents.length-1;i++){
				if (row<areaContinents[i+1] && row>=areaContinents[i] && column<areaContinents[i+1] && column>=areaContinents[i]){
					renderer.setBackground(new Color(240,250,250));
					break;
				}
			}
			if (table.getValueAt(row, column).equals("X")){
				if (row > column){
					renderer.setForeground(Color.RED); 
				}
				else renderer.setForeground(Color.LIGHT_GRAY);
			}
			else renderer.setForeground(Color.BLUE);
		}
		return renderer;
	}     
}

/**
 * Render each continent node
 */
class  ContinentNodeRenderer  extends  DefaultTreeCellRenderer{
	private static final long serialVersionUID = 1L;
	ImageIcon rootIcon = new ImageIcon("src/images/map.png");
	ImageIcon continentIcon = new ImageIcon("src/images/continent.png");
	ImageIcon countryIcon = new ImageIcon("src/images/country.png");
	RiskMapModel myMap;
	
	/**
	 * Constructor of class
	 * @param map map object 
	 */	
	public ContinentNodeRenderer(RiskMapModel map){
		myMap = map;
	}
	
	/**
	 * Method to get items in tree and render every item, show different icons.
	 * @param tree JTree object
	 * @param value	value of item
	 * @param sel selection status of item
	 * @param expanded expanded status of item 
	 * @param leaf if item is a leaf node
	 * @param row row of item
	 * @param hasFocus focus status of item
	 * @return the component used for drawing the cell. 
	 */
	public  Component getTreeCellRendererComponent(JTree tree, Object value, boolean  sel, boolean  expanded, 
			boolean  leaf, int  row, boolean  hasFocus){   
		super .getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); 	
		int index = value.toString().indexOf('(');
		if (tree.getPathForRow(row)==null) {		
		}
		else if (index == -1){
			setIcon(rootIcon);
		}
		else if (value.toString().indexOf(") (") >= 0){
			setIcon(continentIcon);
			if (myMap!=null){
				ContinentModel curContinent = myMap.findContinent(value.toString().substring(0,value.toString().indexOf("(")-1));
				if (curContinent.getOwner()!=null)
					setForeground(curContinent.getOwner().getMyColor());
			}
		}
		else{
			setIcon(countryIcon);
			if (myMap!=null){
				CountryModel curCountry = myMap.findCountry(value.toString().substring(0,value.toString().indexOf("(")-1));
				if (curCountry.getOwner()!=null)
					setForeground(curCountry.getOwner().getMyColor());
			}	
		}      
		return this;   
   }
 } 

/**
 * Render the players' node
 */
class  PlayerNodeRenderer  extends  DefaultTreeCellRenderer{
	private static final long serialVersionUID = 1L;
	ImageIcon rootIcon = new ImageIcon("src/images/players.png");
	ImageIcon playerIcon = new ImageIcon("src/images/player.png");
	ImageIcon countryIcon = new ImageIcon("src/images/country.png");
	PlayerModel[] myPlayer;

	/**
	 * Constructor of class
	 * @param player players array
	 */
	public PlayerNodeRenderer(PlayerModel[] player){
		myPlayer = player;
	}
  
	/**
	 * Method to get items in tree and render every item, show different icons.
	 * @param tree JTree object
	 * @param value	value of item
	 * @param sel selection status of item
	 * @param expanded expanded status of item 
	 * @param leaf if item is a leaf node
	 * @param row row of item
	 * @param hasFocus focus status of item
	 * @return the component used for drawing the cell. 
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean  sel, boolean  expanded, 
			boolean  leaf, int  row, boolean  hasFocus){   
		super .getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); 		
		if (tree.getPathForRow(row)==null) {		
		}
		else if (tree.getPathForRow(row).getParentPath()==null){
			setIcon(rootIcon);
		}
		else if (tree.getPathForRow(row).getParentPath().getParentPath()==null){
			setIcon(playerIcon);
			if (myPlayer!=null){
				String curPlayer = value.toString().substring(0,value.toString().indexOf("(")-1);
				for (int i=0;i<myPlayer.length;i++){
					if (myPlayer[i].getName().equals(curPlayer)){
						setForeground(myPlayer[i].getMyColor());
						break;
					}						
				}
			}	
		} 
		else{
			setIcon(countryIcon);
			//setForeground(tree.getPathForRow(row).getParentPath());
		}
		return this;   
   }
 } 

/**
 * Render the node of countries
 */
class  CountryNodeRenderer  extends  DefaultTreeCellRenderer{
	private static final long serialVersionUID = 1L;
	ImageIcon rootIcon = new ImageIcon("src/images/players.png");
	ImageIcon countryIcon = new ImageIcon("src/images/country.png");
	Color myColor;
	
	/**
	 * Constructor of class
	 * @param color default color for the country 
	 */
	public CountryNodeRenderer(Color color){
		this.myColor = color;
	}
	
	/**
	 * Method to get items in tree and render every item, show different icons.
	 * @param tree JTree object
	 * @param value	value of item
	 * @param sel selection status of item
	 * @param expanded expanded status of item 
	 * @param leaf if item is a leaf node
	 * @param row row of item
	 * @param hasFocus focus status of item
	 * @return the component used for drawing the cell. 
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean  sel, boolean  expanded, 
			boolean  leaf, int  row, boolean  hasFocus){   
		super .getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); 		
		if (tree.getPathForRow(row)==null) {		
		}
		else if (tree.getPathForRow(row).getParentPath()==null){
			setIcon(rootIcon);
			setForeground(Color.BLACK);
		}
		else if (tree.getPathForRow(row).getParentPath().getParentPath()==null){
			setIcon(countryIcon);	
			setForeground(myColor);
		}
		return this;   
   }
} 