package gamecontroller;

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

import gameelements.Player;
import mapelements.Country;
import mapelements.RiskMap;

/**
 * Render the matrix in table showing the relationship of countries.
 */
class MatrixRenderer implements TableCellRenderer{     
	private int[] areaContinents;
	
	public MatrixRenderer(int[] area){
		this.areaContinents = area;
	}

	public static final DefaultTableCellRenderer DEFAULT_RENDERER =new DefaultTableCellRenderer();     
	
	/**
	 * This method is used to get unit in table and render every unit
	 * @param table	Object of JTable
	 * @param value	
	 * @param isSelected	Status of components
	 * @param hasFocus	Status of focus on table 
	 * @param row	Row of table
	 * @param column	Column of table
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
			if (row > column){
				renderer.setForeground(Color.RED); 
			}
			else renderer.setForeground(Color.LIGHT_GRAY);
		}
		return renderer;
	}     
}

/**
 * Render each continent node
 */
class  ContinentNodeRenderer  extends  DefaultTreeCellRenderer{
	ImageIcon rootIcon = new ImageIcon("src/images/map.png");
	ImageIcon continentIcon = new ImageIcon("src/images/continent.png");
	ImageIcon countryIcon = new ImageIcon("src/images/country.png");
	RiskMap myMap;
	public ContinentNodeRenderer(RiskMap map){
		myMap = map;
	}
	
	/**
	 * This method is used to get unit in table and render every unit
	 * @param tree Object of JTree
	 * @param value	
	 * @param isSelected	Status of components
	 * @param hasFocus	Status of focus on table 
	 * @param row	Row of table
	 * @param column	Column of table
	 */
	public  Component getTreeCellRendererComponent(JTree tree, Object value, boolean  sel, boolean  expanded, 
			boolean  leaf, int  row, boolean  hasFocus){   
		super .getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); 		
		if (tree.getPathForRow(row)==null) {		
		}
		else if (tree.getPathForRow(row).getParentPath()==null){
			setIcon(rootIcon);
		}
		else if (tree.getPathForRow(row).getParentPath().getParentPath()==null){
			setIcon(continentIcon);
		}
		else{
			setIcon(countryIcon);
			if (myMap!=null){
				Country curCountry = myMap.findCountry(value.toString().substring(0,value.toString().indexOf("(")-1));
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
	ImageIcon rootIcon = new ImageIcon("src/images/players.png");
	ImageIcon playerIcon = new ImageIcon("src/images/player.png");
	ImageIcon countryIcon = new ImageIcon("src/images/country.png");
	Player[] myPlayer;
	
	public PlayerNodeRenderer(Player[] player){
		myPlayer = player;
	}
  
	/**
	 * This method is used to get unit in table and render every unit
	 * @param tree	Object of JTree
	 * @param value	
	 * @param sel	Status of components
	 * @param expanded	Status of focus on table 
	 * @param leaf	Row of table
	 * @param row	Column of table
	 * @param hasFocus
	 * @return the component
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
	ImageIcon rootIcon = new ImageIcon("src/images/players.png");
	ImageIcon countryIcon = new ImageIcon("src/images/country.png");
	Color myColor;
	
	public CountryNodeRenderer(Color color){
		this.myColor = color;
	}
	
	/**
	 * This method is used to get unit in table and render every unit
	 * @param tree	Object of JTree
	 * @param value	
	 * @param isSelected	Status of components
	 * @param hasFocus	Status of focus on table 
	 * @param row	Row of table
	 * @param column	Column of table
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