<<<<<<< HEAD
package mapeditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This is a class, which used to creat a window for player to edit maps for games
 * 
 *
 */
public class MapEditor extends JFrame {

	//button in window
	private JButton newMapBtn;
	private JButton loadFromFileBtn;
	private JButton newContinentBtn;
	private JButton newCountryBtn;
	private JButton saveToFileBtn;
    
	//label show info
	private JLabel labelContinent;
	private JLabel labelCountry;
    
	private JTree treeContinent;
	private JScrollPane scrollPaneForContinent;
	
	private JTable countryMatrix;
	private JScrollPane scrollPaneForMatrix;
    
	private void initGUI(){  
		
		//configuration
        setTitle("Map Editor - Untitled");  
        setSize(1024,770);
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        setLocation((screenWidth-1024)/2, (screenHeight-770)/2);  
        //set exit program when close the window  
        setDefaultCloseOperation(3);  
        //not capable adjust windows size  
        setResizable(false);  
        setLayout(null);  
        
        labelContinent = new javax.swing.JLabel("Continents: total = 0");  
        add(labelContinent);  
        labelContinent.setFont(new java.awt.Font("dialog",1,16));
        Dimension size = labelContinent.getPreferredSize();
        labelContinent.setBounds(15,6,size.width+100,size.height);   
        
        DefaultMutableTreeNode myTree = new DefaultMutableTreeNode("Map - Untitled");
        myTree.add(new DefaultMutableTreeNode("Isolated Countries (0 countrie)"));
        treeContinent= new JTree(myTree);

        scrollPaneForContinent= new JScrollPane(treeContinent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForContinent);  
        scrollPaneForContinent.setBounds(15,30,300,660);	

        labelCountry = new javax.swing.JLabel("Countries(0) and connections(0):");  
        add(labelCountry);  
        labelCountry.setFont(new java.awt.Font("dialog",1,16));
        size = labelCountry.getPreferredSize();
        labelCountry.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),6,size.width+100,size.height);         
        
        scrollPaneForMatrix= new JScrollPane(null,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForMatrix);  
        scrollPaneForMatrix.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),30,690,660);
        
        //Buttons
        newContinentBtn = new javax.swing.JButton("New Continent");
        add(newContinentBtn);  	     
        size = newContinentBtn.getPreferredSize();
        size.width+=5;
        size.height+=2;
        newContinentBtn.setBounds(620,700,size.width,size.height);	        
        newContinentBtn.addActionListener(new newContinentHandler());
        
        newCountryBtn = new javax.swing.JButton("New Country");
        add(newCountryBtn);  	     
        //size = newCountry.getPreferredSize();
        newCountryBtn.setBounds(newContinentBtn.getBounds().x+size.width+10,700,size.width,size.height);
        newCountryBtn.addActionListener(new newCountryHandler());
        
        saveToFileBtn = new javax.swing.JButton("Save To File");
        add(saveToFileBtn);  	     
        //size = newCountry.getPreferredSize();
        saveToFileBtn.setBounds(newCountryBtn.getBounds().x+size.width+10,700,size.width,size.height);
        saveToFileBtn.addActionListener(new saveToFileHandler());
        
        newMapBtn = new javax.swing.JButton("New Map");
        add(newMapBtn);  	     
        //size = newCountry.getPreferredSize();
        newMapBtn.setBounds(15,700,size.width,size.height);
        newMapBtn.addActionListener(new newMapHandler());
        
        loadFromFileBtn = new javax.swing.JButton("Load Exiting Map");
        add(loadFromFileBtn);  	     
        //size = newCountry.getPreferredSize();
        loadFromFileBtn.setBounds(newMapBtn.getBounds().x+size.width+10,700,size.width+10,size.height);  
        loadFromFileBtn.addActionListener(new loadFromFileHandler());
        
        setVisible(true);          
    }
	
    private class newContinentHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "new continent" );
        }
    }
    
    private class newCountryHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "new country" );
        }
    }   
    
    private class saveToFileHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "save to file" );
        }
    }      
    
    private class newMapHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "new map" );
        }
    }
    
    private class loadFromFileHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "load from file" );
        }
    }    
    
    public static void main(String[] args) {  
    	MapEditor myMapEditor = new MapEditor();  
    	myMapEditor.initGUI();  
    }      
}
=======
package mapeditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This is a class, which used to creat a window for player to edit maps for games
 * 
 *
 */
public class MapEditor extends JFrame {

	//button in window
	private JButton newMapBtn;
	private JButton loadFromFileBtn;
	private JButton newContinentBtn;
	private JButton newCountryBtn;
	private JButton saveToFileBtn;
    
	//label show info
	private JLabel labelContinent;
	private JLabel labelCountry;
    
	private JTree treeContinent;
	private JScrollPane scrollPaneForContinent;
	
	private JTable countryMatrix;
	private JScrollPane scrollPaneForMatrix;
    
	private void initGUI(){  
		
		//configuration
        setTitle("Map Editor - Untitled");  
        setSize(1024,770);
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        setLocation((screenWidth-1024)/2, (screenHeight-770)/2);  
        //set exit program when close the window  
        setDefaultCloseOperation(3);  
        //not capable adjust windows size  
        setResizable(false);  
        setLayout(null);  
        
        labelContinent = new javax.swing.JLabel("Continents: total = 0");  
        add(labelContinent);  
        labelContinent.setFont(new java.awt.Font("dialog",1,16));
        Dimension size = labelContinent.getPreferredSize();
        labelContinent.setBounds(15,6,size.width+100,size.height);   
        
        DefaultMutableTreeNode myTree = new DefaultMutableTreeNode("Map - Untitled");
        myTree.add(new DefaultMutableTreeNode("Isolated Countries (0 countrie)"));
        treeContinent= new JTree(myTree);

        scrollPaneForContinent= new JScrollPane(treeContinent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForContinent);  
        scrollPaneForContinent.setBounds(15,30,300,660);	

        labelCountry = new javax.swing.JLabel("Countries(0) and connections(0):");  
        add(labelCountry);  
        labelCountry.setFont(new java.awt.Font("dialog",1,16));
        size = labelCountry.getPreferredSize();
        labelCountry.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),6,size.width+100,size.height);         
        
        scrollPaneForMatrix= new JScrollPane(null,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForMatrix);  
        scrollPaneForMatrix.setBounds(scrollPaneForContinent.getBounds().x+(int)(scrollPaneForContinent.getBounds().getWidth()),30,690,660);
        
        //Buttons
        newContinentBtn = new javax.swing.JButton("New Continent");
        add(newContinentBtn);  	     
        size = newContinentBtn.getPreferredSize();
        size.width+=5;
        size.height+=2;
        newContinentBtn.setBounds(620,700,size.width,size.height);	        
        newContinentBtn.addActionListener(new newContinentHandler());
        
        newCountryBtn = new javax.swing.JButton("New Country");
        add(newCountryBtn);  	     
        //size = newCountry.getPreferredSize();
        newCountryBtn.setBounds(newContinentBtn.getBounds().x+size.width+10,700,size.width,size.height);
        newCountryBtn.addActionListener(new newCountryHandler());
        
        saveToFileBtn = new javax.swing.JButton("Save To File");
        add(saveToFileBtn);  	     
        //size = newCountry.getPreferredSize();
        saveToFileBtn.setBounds(newCountryBtn.getBounds().x+size.width+10,700,size.width,size.height);
        saveToFileBtn.addActionListener(new saveToFileHandler());
        
        newMapBtn = new javax.swing.JButton("New Map");
        add(newMapBtn);  	     
        //size = newCountry.getPreferredSize();
        newMapBtn.setBounds(15,700,size.width,size.height);
        newMapBtn.addActionListener(new newMapHandler());
        
        loadFromFileBtn = new javax.swing.JButton("Load Exiting Map");
        add(loadFromFileBtn);  	     
        //size = newCountry.getPreferredSize();
        loadFromFileBtn.setBounds(newMapBtn.getBounds().x+size.width+10,700,size.width+10,size.height);  
        loadFromFileBtn.addActionListener(new loadFromFileHandler());
        
        setVisible(true);          
    }
	
    private class newContinentHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "new continent" );
        }
    }
    
    private class newCountryHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "new country" );
        }
    }   
    
    private class saveToFileHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "save to file" );
        }
    }      
    
    private class newMapHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "new map" );
        }
    }
    
    private class loadFromFileHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        {
        	JOptionPane.showMessageDialog(null, "load from file" );
        }
    }    
    
    public static void main(String[] args) {  
    	MapEditor myMapEditor = new MapEditor();  
    	myMapEditor.initGUI();  
    }      
}
>>>>>>> origin/Jingyu_Lu
