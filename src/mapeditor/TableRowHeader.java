package mapeditor;
import javax.swing.*;  
import java.awt.*;  
import javax.swing.table.*;  
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
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);  
		this.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);  
		this.setDefaultRenderer(Object.class,new RowHeaderRenderer(refTable,this,columnTitle));  
		this.setPreferredScrollableViewportSize (new Dimension(columnWidth,0));  
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
		if ( isSelect(row) ){       
			setForeground(Color.white);  
			setBackground(Color.lightGray);  
		}  
        else{
        	setForeground(header.getForeground());     
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
