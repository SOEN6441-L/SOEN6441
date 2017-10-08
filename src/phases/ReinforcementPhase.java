package phases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


/**
 * Reinforcement is seen as an object, and all methods in this phases are seen as its actions.
 * In this class, it contains all necessary actions in reinforcement phase.
 * 
 * @author Jingyu Lu
 *
 */
public class ReinforcementPhase extends JFrame{
	
	
	MyTableContent my_table_content=new MyTableContent();
	//components in this window
	JLabel playerLabel=new JLabel();
	JLabel currentPlayer=new JLabel();
	JLabel armyLabel=new JLabel();
	JLabel currentReinforcements=new JLabel();
	
	JTable myTable=new JTable(my_table_content);
	
	//pane
	JPanel myPanel=new JPanel();
	JScrollPane myScrollPane=new JScrollPane(myTable);
	
	public ReinforcementPhase(){
		
		//get base frame
		Container rfpContainer=getContentPane();
		rfpContainer.setLayout(new GridLayout(2, 1, 3, 3));
		//label
		playerLabel.setText("Current Player :");
		playerLabel.setBounds(10, 10, 200, 50);
		currentPlayer.setText("PlayerWinner");
		currentPlayer.setBounds(251, 10, 100, 50);
		
		armyLabel.setText("Army under Control :");
		armyLabel.setBounds(10, 60, 200, 50);
		currentReinforcements.setText("oo");
		currentReinforcements.setBounds(251, 60, 100, 50);
		//table
		myTable.setPreferredScrollableViewportSize(new Dimension(500, 300));
//		myTable.setBackground(Color.red);
		//scrollpane
		myPanel.setLayout(null);
		myPanel.setPreferredSize(new Dimension(500, 200));
		myScrollPane.setPreferredSize(new Dimension(500,250));
		

		//add components to container
		myPanel.add(playerLabel);
		myPanel.add(currentPlayer);
		myPanel.add(armyLabel);
		myPanel.add(currentReinforcements);
		rfpContainer.add(myPanel, new GridLayout(1, 1, 3, 3));
//		rfpContainer.add(myScrollPane, new GridLayout(2,1,3,3));
		rfpContainer.add(myScrollPane, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pack();
		setSize(500, 700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
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
		new ReinforcementPhase();

	}

}
