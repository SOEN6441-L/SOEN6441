package gamecontroller;

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
	 * @see AbstractTableModel
	 */
	class MyTableContent extends AbstractTableModel{

		//store all data used in table in array
		//title
		String[] column_names={"Country", "Number of Armies"};
		
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
		};
		
		//overwrite methods so that to be invoked by JTable
		@Override
		public int getColumnCount() {
			// get num of columns
			return column_names.length;
		}

		@Override
		public int getRowCount() {
			// get num of rows
			return table_content.length;
		}
		
		@Override
		public Class getColumnClass(int columnIndex) {
			// type of each element in table
			return getValueAt(0, columnIndex).getClass();
		}

		@Override
		public String getColumnName(int column) {
			// get name of some columns 
			return column_names[column];
		}

		@Override
		public Object getValueAt(int row, int column) {
			// get data in row+column
			return table_content[row][column];
		}

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
