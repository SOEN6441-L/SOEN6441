package mapEditor;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class MapEditor extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	javax.swing.JButton newMapBtn;
    javax.swing.JButton loadFromFileBtn;
	javax.swing.JButton newContinentBtn;
    javax.swing.JButton newCountryBtn;
    javax.swing.JButton saveToFileBtn;
    javax.swing.JButton exitBtn;
    
    JTree treeContinent;
    
    
    public static void main(String[] args) {  
    	MapEditor myMapEditor = new MapEditor();  
    	myMapEditor.initGUI();  
    }  
    
	public void initGUI(){  
        setTitle("Map Editor - Untitled");  
        setSize(1024,770);
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        setLocation((screenWidth-1024)/2, (screenHeight-770)/2);  
        //set exit program when close the window  
        setDefaultCloseOperation(3);  
        //not capable adjust windows size  
        setResizable(false);  

        //java.awt.FlowLayout fl = new java.awt.FlowLayout();   
        setLayout(null);  
        
        
        javax.swing.JLabel jlaName1 = new javax.swing.JLabel("Continents:");  
        add(jlaName1);  
        jlaName1.setFont(new java.awt.Font("dialog",1,16));
        Dimension size = jlaName1.getPreferredSize();
        jlaName1.setBounds(15,6,size.width+40,size.height);   
        
        DefaultMutableTreeNode myMap = new DefaultMutableTreeNode("Map - Untitled");
        
        DefaultMutableTreeNode asia = new DefaultMutableTreeNode("Continent - Asia");
        
        DefaultMutableTreeNode western = new DefaultMutableTreeNode("Continent - Conference");

        DefaultMutableTreeNode pacific = new DefaultMutableTreeNode("Country - Pacific Division Teams");
        
        myMap.add(asia);
        myMap.add(western);
        western.add(pacific);
        
        treeContinent= new JTree(myMap);
        
        

        JScrollPane scrollPane1= new JScrollPane(treeContinent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane1);  
        scrollPane1.setBounds(15,30,300,660);	
        
        jlaName1.setText("Continents: totl");

        
        
        javax.swing.JLabel jlaName2 = new javax.swing.JLabel("Countries and connections:");  
        add(jlaName2);  
        jlaName2.setFont(new java.awt.Font("dialog",1,16));
        size = jlaName2.getPreferredSize();
        jlaName2.setBounds(scrollPane1.getBounds().x+(int)(scrollPane1.getBounds().getWidth()),6,size.width,size.height);         
        
        JScrollPane scrollPane2= new JScrollPane(null,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane2);  
        scrollPane2.setBounds(scrollPane1.getBounds().x+(int)(scrollPane1.getBounds().getWidth()),30,690,660);
        
        newContinentBtn = new javax.swing.JButton("New Continent");
        add(newContinentBtn);  	     
        size = newContinentBtn.getPreferredSize();
        size.width+=5;
        size.height+=2;
        newContinentBtn.setBounds(505,700,size.width,size.height);	        
        setVisible(true);  
        
        newCountryBtn = new javax.swing.JButton("New Country");
        add(newCountryBtn);  	     
        //size = newCountry.getPreferredSize();
        newCountryBtn.setBounds(newContinentBtn.getBounds().x+size.width+10,700,size.width,size.height);
        
        saveToFileBtn = new javax.swing.JButton("Save To File");
        add(saveToFileBtn);  	     
        //size = newCountry.getPreferredSize();
        saveToFileBtn.setBounds(newCountryBtn.getBounds().x+size.width+10,700,size.width,size.height);
        
        exitBtn = new javax.swing.JButton("Exit");
        add(exitBtn);  	     
        //size = newCountry.getPreferredSize();
        exitBtn.setBounds(saveToFileBtn.getBounds().x+size.width+10,700,size.width-15,size.height);        
        
        newMapBtn = new javax.swing.JButton("New Map");
        add(newMapBtn);  	     
        //size = newCountry.getPreferredSize();
        newMapBtn.setBounds(15,700,size.width,size.height);
        
        loadFromFileBtn = new javax.swing.JButton("Load Exiting Map");
        add(loadFromFileBtn);  	     
        //size = newCountry.getPreferredSize();
        loadFromFileBtn.setBounds(newMapBtn.getBounds().x+size.width+10,700,size.width+10,size.height);   
        
        setVisible(true);          
    }

}
