package phases;

import javax.swing.table.AbstractTableModel;

/**
 * Function class, which used to fill the table in window.
 * <p> This class extends from AbstractTableModel</p>
 * 
 * @author Jingyu Lu
 * @see AbstractTableModel
 *
 */
public class FillTableContent extends AbstractTableModel {

			//store all data used in table in array
			//title
			String[] column_names={"Country", "Number of Armies"};
			
			//get current players
			
			
			//content in rows
			Object[][] table_content={
					{"China","oo"},
					{"UUUUU","4"},
					{"EEEEE","3"},
					{"JJJJJ","2"},
					{"KKKKK","1"},
					{"FFFFF","1"},
					{"YYYYY","o"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"},
					{"China","oo"}
			};
			
			/**
			 * Rewrite methods getColumnCount(), so that to be invoked by JTable
			 * 
			 * @param null
			 * @return The number of columns
			 * @see javax.swing.table.AbstractTableModel #getColumnCount()
			 */
			@Override
			public int getColumnCount() {
				return column_names.length;
			}

			/**
			 * Rewrite methods getRowCount(), so that to be invoked by JTable
			 * 
			 * @param null
			 * @return The number of rows
			 * @see javax.swing.table.AbstractTableModel #getRowCount()
			 */
			@Override
			public int getRowCount() {
				return table_content.length;
			}
			
			/**
			 * Rewrite methods getColumnClass(), so that to be invoked by JTable
			 * 
			 * @param columnIndex
			 * @return The number of type of elements in table
			 * @see javax.swing.table.AbstractTableModel #getColumnClass(int)
			 */
			@Override
			public Class getColumnClass(int columnIndex) {
				return getValueAt(0, columnIndex).getClass();
			}

			/**
			 * Rewrite methods getColumnName(), so that to be invoked by JTable
			 * 
			 * @param column
			 * @return The name of column
			 * @see javax.swing.table.AbstractTableModel #getColumnName(int)
			 */
			@Override
			public String getColumnName(int column) {
				return column_names[column];
			}

			/**
			 * Rewrite methods getValueAt(), so that to be invoked by JTable
			 * 
			 * @param row
			 * @param column
			 * @return Data in the related row and column
			 * @see javax.swing.table.AbstractTableModel #getValueAt(int, int)
			 */
			@Override
			public Object getValueAt(int row, int column) {
				// get data in row+column
				return table_content[row][column];
			}


}
