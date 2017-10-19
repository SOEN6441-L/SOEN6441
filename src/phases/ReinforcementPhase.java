package phases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import gameelements.Player;
import gameelements.ReinforceArmy;
import mapelements.Country;


/**
 * This class is the implementation of reinforcement phase in the Risk.
 * <p> The ReinforcementPhase class will allow players add armies to their<br>
 * to their country, based on the countries under control.</p>
 * 
 * @author Jingyu Lu
 * @version alpha 0.1
 * @see javax.swing.JFrame
 *
 */
public class ReinforcementPhase extends JFrame{
	
	FillTableContent my_table_content=new FillTableContent();
	//components in this window
	JLabel playerLabel=new JLabel();
	JLabel currentPlayer=new JLabel();
	JLabel armyLabel=new JLabel();
	JLabel currentReinforcements=new JLabel();
	JButton btn_new_armies=new JButton("Add Reinforcement");
	
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
		
		armyLabel.setText("Countries under Control :");
		armyLabel.setBounds(10, 60, 200, 50);
		currentReinforcements.setText("oo");
		currentReinforcements.setBounds(251, 60, 100, 50);
		
		//button
		btn_new_armies.setBounds(150, 200, 150, 40);
		/**
		 * This is a listener to invoke a new GUI to set armies.
		 * @author Jingyu Lu
		 */
		btn_new_armies.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// new window for changing armies
				int [] cards = {0,2,0};
		        ArrayList <Country> country = new ArrayList<Country>(3);
				Player shirley = new Player("lalala", cards, country, 0, 0);
				ReinforceArmy new_reinforcement=new ReinforceArmy(shirley);
				
			}
		});
		
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
		myPanel.add(btn_new_armies);
		rfpContainer.add(myPanel, new GridLayout(1, 1, 3, 3));
		rfpContainer.add(myScrollPane, BorderLayout.CENTER);
		
		/**
		 * Adds the specified window listener to receive window events from this window.
		 * 
		 * @see java.awt.Window.addWindowListener(WindowListener arg0) #removeWindowListener(java.awt.event.WindowListener)
		 * @see java.awt.Window.addWindowListener(WindowListener arg0) #getWindowListeners()
		 */
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

	/**
	 * Print information in table
	 */
	private void printTable(){
		int num_of_row=my_table_content.getRowCount();
		int num_of_column=my_table_content.getColumnCount();
			
		for(int i=0;i<num_of_row;i++){
			System.out.print("row"+i+":");
			for(int j=0;j<num_of_column;j++){
				System.out.print(" "+my_table_content.table_content[i][j]);
			}
			System.out.println();
		}
		System.out.println("-------------------------");
	}
	
	/**
	 * Attention!!! This function is only for testing and debugging during coding. Do delete later
	 * @param args
	 */
	public static void main(String[] args) {
		
		new ReinforcementPhase();

	}

}