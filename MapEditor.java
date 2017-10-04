package team.groupfour.risk.mapeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout.Constraints;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 * This class is used to create a new map for the Risk. 
 * At the same time, player can also view their creation 
 * at the right side of window.
 * 
 * @author Jingyu Lu
 *
 */
public class MapEditor extends JFrame{

	//new panels
	JPanel myLeftPanel=new JPanel();
	JPanel myMidTopPanel=new JPanel();
	JPanel myMidBottomPanel=new JPanel();
	JPanel myRightPanel=new JPanel();
	
//	JScrollPane midTopScrollPane=new JScrollPane();
//	JScrollPane midBottomScrollPane=new JScrollPane();
//	JScrollPane rightScrollPane=new JScrollPane();
	
	//components in panel
	//leftPanel
	//border used in JTextField
	MatteBorder myBorder = new MatteBorder(0, 0, 1, 0, new Color(0, 0, 255));
	//label
	JLabel labelCountry=new JLabel("Add Country: ");
	JLabel labelContinent=new JLabel("Add Continent: ");
	JLabel labelConnection=new JLabel("Add Connection: ");		
	//text
	JTextField txtCountry=new JTextField("country");
	JTextField txtContinent=new JTextField("continent");
	JTextField txtConnection=new JTextField("connection");
	//button
	JButton btnAddCountry=new JButton("Add Country");
	JButton btnAddContinent=new JButton("Add Continent");
	JButton btnAddConnection=new JButton("Add Connection");
	JButton btnSaveMap=new JButton("Save");
	JButton btnLoadMap=new JButton("Load");
	JButton btnExist=new JButton("Exist");
	//midTopPanel
	
	//midBottomPanel
	
	//rightPanel
//	//JTable
//	//table header
//	String[] right_table_header={"Country1", "Country2","Country3", "Country4", "Country5", "Country6"};
//	//create new table with content
//	JTable rightTable=new JTable(null, right_table_header);
	
	MapEditor(){
		//layout
		Container panelContainer=getContentPane();
		panelContainer.setLayout(new GridLayout(1, 3, 3, 3));
		
		//leftPanel
		//set and add components
		myLeftPanel.setLayout(null);					//clean exist layout in layout manager
		labelCountry.setBounds(30, 30, 160, 30);
		txtCountry.setBounds(30, 61, 200, 20);
		txtCountry.setBorder(myBorder);
		btnAddCountry.setBounds(250, 61, 130, 25);
				
		labelContinent.setBounds(30, 100, 200, 30);
		txtContinent.setBounds(30, 131, 200, 20);
		txtContinent.setBorder(myBorder);
		btnAddContinent.setBounds(250, 131, 130, 25);
				
		labelConnection.setBounds(30, 170, 200, 30);
		txtConnection.setBounds(30, 201, 200, 20);
		txtConnection.setBorder(myBorder);
		btnAddConnection.setBounds(250, 201, 130, 25);
		
		myLeftPanel.add(labelCountry);
		myLeftPanel.add(txtCountry);
		myLeftPanel.add(btnAddCountry);
		
		myLeftPanel.add(labelContinent);
		myLeftPanel.add(txtContinent);
		myLeftPanel.add(btnAddContinent);
		
		myLeftPanel.add(labelConnection);
		myLeftPanel.add(txtConnection);
		myLeftPanel.add(btnAddConnection);
		
		btnSaveMap.setBounds(30, 600, 100, 25);
		myLeftPanel.add(btnSaveMap);
		btnSaveMap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 存储编辑好的地图，未完成
				
			}
		});
		
		btnLoadMap.setBounds(130, 500, 100, 25);
		myLeftPanel.add(btnLoadMap);
		btnLoadMap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//create file loader
				JFileChooser file_loader=new JFileChooser();
				//set title 
				file_loader.setDialogTitle("Please Select A Map!");
				//file chooser
				int selection_validation=file_loader.showOpenDialog(null);
				//judge selected a map or not
				if(JFileChooser.APPROVE_OPTION==selection_validation){
					//file directory
					File file_directory=file_loader.getCurrentDirectory();
					File file_src=file_loader.getSelectedFile();
					System.out.println(file_directory);
					System.out.println(file_src);
				}
				
			}
		});
		
		btnExist.setBounds(230, 600, 100, 25);
		myLeftPanel.add(btnExist);
		btnExist.addActionListener(new ActionListener() {
			//close map editor
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		
		//midTopPanel
		myMidTopPanel.setBackground(Color.green);
		//midBottomPanel
		myMidBottomPanel.setBackground(Color.yellow);
		//rightPanel
		myRightPanel.setBackground(Color.red);
		
		JSplitPane midTempPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT, myMidTopPanel, myMidBottomPanel);
		
		panelContainer.add(myLeftPanel, new GridLayout(1, 1, 3, 3));
		panelContainer.add(midTempPane, new GridLayout(1, 2, 3, 3));
		panelContainer.add(myRightPanel, new GridLayout(1, 3, 3, 3));
		
		//general configuration
		pack();
		setSize(1200, 700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	
	public static void main(String[] args) {
		// test during coding only
		new MapEditor();
	}
}
