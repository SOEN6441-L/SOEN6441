package basicclasses;

import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * Class to extend JTable, add two new methods
 * @see JTable
 */
public class MyTable extends JTable{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of class.
	 * @param table the data modal of table
	 */
	public MyTable(DefaultTableModel table){
		super(table);
	}

	/**
	 * Method to adjust the table's column size to preferred width.
	 * @return max width of all columns
	 */
	public int fitTableColumns() {
		JTableHeader header = getTableHeader();
		int rowCount = getRowCount();

		Enumeration<?> columns = getColumnModel().getColumns();
		int maxWidth = 0;
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
			int width = (int) header.getDefaultRenderer()
					.getTableCellRendererComponent(this, column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferredWidth = (int) getCellRenderer(row, col)
						.getTableCellRendererComponent(this, getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferredWidth);
				if (width>maxWidth) maxWidth = width;
			}
			header.setResizingColumn(column);
			column.setWidth(Math.max(width,50) + getIntercellSpacing().width + 10);
		}
		return maxWidth;
	}

	/**
	 * Method to adjust the table's column size as the parameter setWidth.
	 * @param setWidth the column size want to adjust
	 * @return max width of all columns
	 */
	public int fitTableColumns(int setWidth) {
		JTableHeader header = getTableHeader();
		int rowCount = getRowCount();

		Enumeration<?> columns = getColumnModel().getColumns();
		int maxWidth = 0;
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
			int width = (int) header.getDefaultRenderer()
					.getTableCellRendererComponent(this, column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferredWidth = (int) getCellRenderer(row, col)
						.getTableCellRendererComponent(this, getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferredWidth);
				if (width>maxWidth) maxWidth = width;
			}
			header.setResizingColumn(column);
			column.setWidth(setWidth + getIntercellSpacing().width);
		}
		return maxWidth;
	}
}
