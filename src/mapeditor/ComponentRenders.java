package mapeditor;

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

/**
 * Render of the adjacency matrix in mapEditor main GUI.
 */
class MatrixRenderer implements TableCellRenderer{ 
	/*store the upper and lower bounds of each continents*/
	private int[] areaContinents;
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();     
	
	/**
	 * Constructor of class
	 * @param area upper and lower bounds of each continents 
	 */
	public MatrixRenderer(int[] area){
		this.areaContinents = area;
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
		Color foreground, background;
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
 * Render of the tree view of continents and countries in mapEditor main GUI.
 */
class  CategoryNodeRenderer  extends  DefaultTreeCellRenderer{
	ImageIcon rootIcon = new ImageIcon("src/images/map.png");
	ImageIcon continentIcon = new ImageIcon("src/images/continent.png");
	ImageIcon countryIcon = new ImageIcon("src/images/country.png");
  
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
		}      
		return this;   
   }
}	