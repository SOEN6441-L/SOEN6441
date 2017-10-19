package mapeditor;
import javax.swing.*;  
import java.awt.*;  
import javax.swing.table.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.event.*;

class RowHeaderTable extends JTable{  
	private JTable refTable;  
	/** 
	 * Add RowHeader to JTable, 
	 * @param refTable The JTable that needs to add rowHeader 
	 * @param columnWideth width of rowHeader 
	 */  
	public RowHeaderTable(JTable refTable,int columnWidth,Object[] columnTitle){  
		super(new RowHeaderTableModel(refTable.getRowCount()));  
		this.refTable=refTable;  
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
		this.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);  
		this.setDefaultRenderer(Object.class,new RowHeaderRenderer(refTable,this,columnTitle));  
		this.setPreferredScrollableViewportSize (new Dimension(columnWidth,0)); 
		this.setRowHeight(refTable.getRowHeight());
	}  
}  

class RowHeaderRenderer extends JLabel implements TableCellRenderer,ListSelectionListener{  
	JTable reftable; 
	JTable tableShow; 
	Object [] columnTitle;
	public RowHeaderRenderer(JTable reftable,JTable tableShow, Object[] columns){  
		this.reftable = reftable;  
		this.tableShow=tableShow;  
		this.columnTitle = columns;
		 
		ListSelectionModel listModel=reftable.getSelectionModel();  
		listModel.addListSelectionListener(this);   
	}  

	public Component getTableCellRendererComponent(JTable table,Object obj,
		boolean isSelected,boolean hasFocus,int row, int col){  
		((RowHeaderTableModel)table.getModel()).setRowCount(reftable.getRowCount());  
		JTableHeader header = reftable.getTableHeader();  
		this.setOpaque(true);  
		setBorder(UIManager.getBorder("TableHeader.cellBorder")); 
		setHorizontalAlignment(CENTER);  
		setBackground(header.getBackground());    
		setForeground(header.getForeground());
		if ( isSelect(row) ){       
			setForeground(Color.BLACK);  
			setBackground(new Color(153,217,234));  
		} 
		setFont(header.getFont());  
		if (row>=0) setText(String.valueOf(columnTitle[row]));
		return this;  
	}  
    
	public void valueChanged(ListSelectionEvent e){  
		this.tableShow.repaint();  
	}  
    
	private boolean isSelect(int row){  
		int[] sel = reftable.getSelectedRows();  
		for ( int i=0; i<sel.length; i++ )  
			if (sel[i] == row )   
				return true;  
		return false;  
	}  
}  
  
class RowHeaderTableModel extends AbstractTableModel{  
	private int rowCount; 
	public RowHeaderTableModel(int rowCount){  
		this.rowCount=rowCount;  
	}  
	public void setRowCount(int rowCount){  
		this.rowCount=rowCount;  
	}  
	public int getRowCount(){  
		return rowCount;  
	}  
	public int getColumnCount(){  
		return 1;  
	}  
	public Object getValueAt(int row, int column){  
		return row;  
	}  
}    

class MatrixRenderer implements TableCellRenderer{     
	private int[] areaContinents;
	
	public MatrixRenderer(int[] area){
		this.areaContinents = area;
	}

	public static final DefaultTableCellRenderer DEFAULT_RENDERER =new DefaultTableCellRenderer();     
	
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

class  CategoryNodeRenderer  extends  DefaultTreeCellRenderer{
	ImageIcon rootIcon = new ImageIcon("src/images/map.png");
	ImageIcon continentIcon = new ImageIcon("src/images/continent.png");
	ImageIcon countryIcon = new ImageIcon("src/images/country.png");
  
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