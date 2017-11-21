package phases;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * This class is the implementation phase of fortification phase in the Risk.
 * <p> The fortification is a mechanism that allow players move army from one<br> 
 * of their country to another.</p>
 *
 * @author Jingyu Lu
 * @version alpha 0.1
 * @see javax.swing.JFrame
 *
 */
public class FortificationPhase extends JFrame{

	//format of table 
	MyTableContent my_table_content=new MyTableContent();
	//components in this window
	JLabel currentPlayer=new JLabel();

	//table
	JTable myFromTable=new JTable(my_table_content);
	JTable myToTable=new JTable(my_table_content);
	
	//button
	JButton myFfpButton=new JButton("Enter");
	
	//pane
	JScrollPane myFromScrollPane=new JScrollPane(myFromTable);
	JScrollPane myToScrollPane=new JScrollPane(myToTable);
	
	JPanel headerPanel=new JPanel();
	JPanel footerPanel=new JPanel();
	
	public FortificationPhase(){
		
		//get base frame
		Container rfpContainer=getContentPane();
		
		//header area
		headerPanel.setLayout(null);
		headerPanel.add(currentPlayer);
		headerPanel.setPreferredSize(new Dimension(800, 60));
		//label
		currentPlayer.setText("PlayerWinner");
		currentPlayer.setBounds(5, 5, 100, 60);
		
		//footer
		footerPanel.setLayout(null);
		footerPanel.add(myFfpButton);
		footerPanel.setPreferredSize(new Dimension(800, 60));
		//button
		myFfpButton.setBounds(600, 10, 100, 30);
		
		//table
		myFromTable.setPreferredScrollableViewportSize(new Dimension(350, 300));
		myToTable.setPreferredScrollableViewportSize(new Dimension(350, 300));
		//scrollpane
		myFromScrollPane.setPreferredSize(new Dimension(300,250));
		myToScrollPane.setPreferredSize(new Dimension(300,250));
		
		JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myFromScrollPane, myToScrollPane);
		splitPane.setPreferredSize(new Dimension(700, 600));
		//add components to container
		rfpContainer.add(headerPanel, BorderLayout.NORTH);
		rfpContainer.add(splitPane, BorderLayout.CENTER);
		rfpContainer.add(footerPanel, BorderLayout.SOUTH);
		
		/**
		 * Adds the specified window listener to receive window events from this window.
		 * @see java.awt.Window.addWindowListener(WindowListener arg0) #removeWindowListener(java.awt.event.WindowListener)
		 * @see java.awt.Window.addWindowListener(WindowListener arg0) #getWindowListeners()
		 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pack();
		setSize(800, 700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Inner class, which used to fill the table in window.
	 * <p> This class extends from AbstractTableModel</p>
	 * 
	 * @author Jingyu Lu
	 * @see AbstractTableModel
	 *
	 */
	class MyTableContent extends AbstractTableModel{

		//store all data used in table in array
		//title
		final String[] column_names={"Country", "Number of Armies"};
		
		//content in rows
		final Object[][] table_content={
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

		/**
		 * Print information in table
		 */
		private void printTable(){
			int num_of_row=getRowCount();
			int num_of_column=getColumnCount();
			
			for(int i=0;i<num_of_row;i++){
				System.out.print("row"+i+":");
				for(int j=0;j<num_of_column;j++){
					System.out.print(" "+table_content[i][j]);
				}
				System.out.println();
			}
			System.out.println("-------------------------");
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new FortificationPhase();
	}

}
