package basicclasses;
import javax.swing.*;  
import java.awt.*;  
import javax.swing.table.*;
import javax.swing.event.*;

/**
 * This class used to show the row header of a JTable object.
 * 
 * @see JTable
 */
public class RowHeaderTable extends JTable{  
	private static final long serialVersionUID = 1L; 
	/** 
	 * Constructor of the class. 
	 * @param refTable The JTable that needs to add rowHeader 
	 * @param columnWidth width of rowHeader
	 * @param columnTitle titles of each row
	 */  
	public RowHeaderTable(JTable refTable,int columnWidth,Object[] columnTitle){  
		super(new RowHeaderTableModel(refTable.getRowCount()));  
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
		this.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);  
		this.setDefaultRenderer(Object.class,new RowHeaderRenderer(refTable,this,columnTitle));  
		this.setPreferredScrollableViewportSize (new Dimension(columnWidth,0)); 
		this.setRowHeight(refTable.getRowHeight());
	}  
}  

/**
 * This is a render class for the row headers.
 * @see JLabel
 * @see TableCellRenderer
 * @see ListSelectionListener
 */
class RowHeaderRenderer extends JLabel implements TableCellRenderer,ListSelectionListener{  
	private static final long serialVersionUID = 1L;
	private JTable reftable; 
	private JTable tableShow; 
	private Object [] columnTitle;
	
	/**
	 * Constructor for the class.
	 * 
	 * @param reftable the JTable object related to this row header	
	 * @param tableShow row header table
	 * @param columns titles for columns
	 */
	public RowHeaderRenderer(JTable reftable,JTable tableShow, Object[] columns){  
		this.reftable = reftable;  
		this.tableShow = tableShow;  
		this.columnTitle = columns;
		 
		ListSelectionModel listModel=reftable.getSelectionModel();  
		listModel.addListSelectionListener(this);   
	}  

	/**
	 * Method to get cells in row header table and render every cell.
	 * @param table	JTable object
	 * @param obj value of cell
	 * @param isSelected selection status of cell
	 * @param hasFocus focus status of cell
	 * @param row row of cell
	 * @param col column of cell
	 * @return the component used for drawing the cell. 
	 */
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

	/**
	 * Method to repaint headers receiving value change event.
	 * @param e	event
	 */
	public void valueChanged(ListSelectionEvent e){  
		this.tableShow.repaint();  
	}  
    
	/**
	 * Method to if the row header cell has been selected.
	 * @param row row of header
	 * @return the status of the cell
	 */
	private boolean isSelect(int row){  
		int[] sel = reftable.getSelectedRows();  
		for ( int i=0; i<sel.length; i++ )  
			if (sel[i] == row )   
				return true;  
		return false;  
	}  
}  
  
/**
 * This is header class used to fill the head of table.
 * @see AbstractTableModel
 */
class RowHeaderTableModel extends AbstractTableModel{  
	private static final long serialVersionUID = 1L;
	private int rowCount; 
	
	/**
	 * Constructor of class.
	 * @param rowCount how many rows need to create
	 */
	public RowHeaderTableModel(int rowCount){  
		this.rowCount=rowCount;  
	}  
	
	/**
	 * Method to set rows number.
	 * @param rowCount how many rows
	 */
	public void setRowCount(int rowCount){  
		this.rowCount=rowCount;  
	}  

	/**
	 * Method to get rows number.
	 * @return row number
	 */
	public int getRowCount(){  
		return rowCount;  
	} 
	
	/**
	 * Method to get columns number, always be 1.
	 * @return column number
	 */	
	public int getColumnCount(){  
		return 1;  
	}  
	
	/**
	 * Method to get value at row and column
	 * @param row the row of table
	 * @param column the column at the table
	 * @return value at row and column
	 */		
	public Object getValueAt(int row, int column){  
		return row;  
	}  
}    
