package gamecontroller;


import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import gameelements.Player;
import gameelements.RiskGame;
import mapelements.Continent;
import mapelements.Country;
import mapelements.NodeRecord;

/**
 * This class is the implementation of reinforcement phase in the Risk.
 * <p> The ReinforcementPhase class will allow players add armies to their<br>
 * to their country, based on the countries under control.</p >
 *
 */
public class FortificationPhaseView extends JDialog{


    //components in this window
    JLabel playerLabel;
    JLabel countryLabelFrom;
    JLabel countryLabelTo;
    JLabel turnLabel;
    JLabel phaseLabel;
    JTree treeCountryFrom;
    JTree treeCountryTo;
    JLabel promptLabelFrom, promptLabelTo;
    JComboBox<Object> armyNumberCombo;
    JScrollPane scrollPaneForCountryFrom;
    JScrollPane scrollPaneForCountryTo;
    JButton enterBtn;
    private int width= 880,height = 560;
    
    private String selCountryNameFrom,selCountryNameTo;

    private Player player;
    private RiskGame myGame;

    private NodeRecord[] localCountries;
    private Map<Integer,ArrayList<Integer>> localAdjacencyList;


    public int state=0; //0-Cancel, 1-confirm

    /**
     * Constructor of class ReinforcePhaseView to generate reinforce phase UI
     * @param player The player that who is in turn
     * @param game The game reinforce phase is in
     */
    public FortificationPhaseView(Player player, RiskGame game){
        this.player = player;
        this.myGame = game;

        setTitle("Fortification Phase");

        setSize(width,height);
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        setLocation((screenWidth-width)/2, (screenHeight-height)/2);
        //set exit program when close the window
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);

        //not capable adjust windows size
        setResizable(false);
        setLayout(null);
        setModal(true);
        setVisible(false);

        Dimension size;    

        localCountries = new NodeRecord[player.getCountries().size()];
        int j = 0;
        for (Country loopCountry:player.getCountries()){
            localCountries[j++] = new NodeRecord(loopCountry.countryID, loopCountry.armyNumber);
        }
        
		localAdjacencyList = new HashMap<Integer,ArrayList<Integer>>();
		for (Country loopCountry: player.getCountries()){
			localAdjacencyList.put(loopCountry.countryID, new ArrayList<Integer>());
			for (Integer neighbour: myGame.getGameMap().adjacencyList.get(loopCountry.countryID)){
				if (myGame.getGameMap().findCountryByID(neighbour).belongToPlayer.getName().equals(player.getName())){
					localAdjacencyList.get(loopCountry.countryID).add(neighbour);
				}
			}
		}

        turnLabel = new JLabel("TURN "+myGame.getTurn()+": ");
        add(turnLabel);
        turnLabel.setFont(new java.awt.Font("dialog",1,24));
        turnLabel.setForeground(Color.BLACK);
        size = turnLabel.getPreferredSize();
        turnLabel.setBounds(15,15,size.width,size.height);

        playerLabel =  new JLabel(player.getName());
        add(playerLabel);
        playerLabel.setFont(new java.awt.Font("dialog",1,18));
        playerLabel.setForeground(player.getMyColor());
        playerLabel.setBounds(turnLabel.getBounds().x+size.width+20,15,size.width,size.height);
        
        phaseLabel = new JLabel("Fortification Phase");
        add(phaseLabel);
        phaseLabel.setFont(new java.awt.Font("dialog",1,18));
        phaseLabel.setForeground(Color.BLACK);
        size = phaseLabel.getPreferredSize();
        phaseLabel.setBounds(20,60,size.width,size.height);        

        countryLabelFrom = new JLabel("Territories ("+player.getCountries().size()+"):");
        add(countryLabelFrom);
        countryLabelFrom.setFont(new java.awt.Font("dialog",1,15));
        size = countryLabelFrom.getPreferredSize();
        countryLabelFrom.setBounds(15,100,size.width,size.height);

        DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
        for (int i=0;i<localCountries.length;i++) {
            Country loopCountry = myGame.getGameMap().findCountryByID(localCountries[i].ID);
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.countryName
                    +" (In "+loopCountry.belongToContinent.continentName+", "+localCountries[i].Number+" armies)"));
        }
        treeCountryFrom = new JTree(myTreeRoot);
        treeCountryFrom.addMouseListener( new  MouseAdapter(){
        	public void mousePressed(MouseEvent e){
        		int selRow = treeCountryFrom.getRowForLocation(e.getX(), e.getY());
        		TreePath selPath = treeCountryFrom.getPathForLocation(e.getX(), e.getY());
        		if (selRow!=-1 && (e.getButton() == 1)){
        			treeCountryFrom.setSelectionPath(selPath);
        			if (selPath!=null) {
        				if (selPath.getParentPath().getParentPath()==null){//continents
           					selCountryNameFrom = selPath.getLastPathComponent().toString().trim();
           					selCountryNameFrom = selCountryNameFrom.substring(0, selCountryNameFrom.indexOf("(")-1);
           					myGame.getGameMap().findPath(localAdjacencyList,myGame.getGameMap().findCountry(selCountryNameFrom).countryID);
           					reloadGUI();
        				}
        			}
        			//popupMenu.show(e.getComponent(), e.getX(), e.getY());
        		}
        	}	
		}); 

        treeCountryFrom.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));

        scrollPaneForCountryFrom= new JScrollPane(treeCountryFrom,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForCountryFrom);
        scrollPaneForCountryFrom.setBounds(15,125,385,330);

        //String moveArmies[]= new String[leftArmies];
        //String moveArmies[] = null;
        /*for (int i=0;i<leftArmies;i++) {
            moveArmies[i] = String.valueOf(i+1);
        }*/
        armyNumberCombo = new JComboBox<Object>();
        //armyNumberCombo.setSelectedIndex(leftArmies-1);
        add(armyNumberCombo);
        size = armyNumberCombo.getPreferredSize();
        armyNumberCombo.setBounds(scrollPaneForCountryFrom.getBounds().x+scrollPaneForCountryFrom.getSize().width+10,260,size.width+20,size.height);
        
        
        countryLabelTo = new JLabel("Territories have a path (0):");
        add(countryLabelTo);
        countryLabelTo.setFont(new java.awt.Font("dialog",1,15));
        size = countryLabelTo.getPreferredSize();
        countryLabelTo.setBounds(armyNumberCombo.getBounds().x+armyNumberCombo.getSize().width+10,100,size.width,size.height);

        DefaultMutableTreeNode myTreeRootTo = new DefaultMutableTreeNode("Countries");
        treeCountryTo = new JTree(myTreeRootTo);

        treeCountryTo.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));

        scrollPaneForCountryTo= new JScrollPane(treeCountryTo,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForCountryTo);
        scrollPaneForCountryTo.setBounds(countryLabelTo.getBounds().x,125,385,330);
        
        
        promptLabelFrom = new JLabel("Click one country to move armies out.");
        add(promptLabelFrom);
        promptLabelFrom.setFont(new java.awt.Font("dialog",1,13));
        promptLabelFrom.setForeground(Color.RED);
        size = promptLabelFrom.getPreferredSize();
        promptLabelFrom.setBounds(15,458,size.width,size.height);
        
        promptLabelTo = new JLabel("Double Click one country to move armies in.");
        add(promptLabelTo);
        promptLabelTo.setFont(new java.awt.Font("dialog",1,13));
        promptLabelTo.setForeground(Color.RED);
        size = promptLabelTo.getPreferredSize();
        promptLabelTo.setBounds(countryLabelTo.getBounds().x,458,size.width,size.height);


        enterBtn = new JButton("Finish");
        add(enterBtn);
        size = enterBtn.getPreferredSize();
        enterBtn.setBounds(815,540,size.width,size.height);
        //enterBtn.addActionListener(new enterBtnHandler());
    }

    /**
     * The method to refresh the UI
     */
    public void reloadGUI(){

        Country curCountry = myGame.getGameMap().findCountry(selCountryNameFrom);
    	armyNumberCombo.removeAllItems();
        for (int i=1;i<curCountry.armyNumber;i++) {
            armyNumberCombo.addItem(i);
        }
        armyNumberCombo.setSelectedIndex(curCountry.armyNumber-2);
 

        

        DefaultMutableTreeNode myTreeRootTo = new DefaultMutableTreeNode("Countries");
        int countriesAvailable = 0;
        for (Country loopCountry : player.getCountries()){
        	if (loopCountry.flagDFS&&loopCountry.countryID!=curCountry.countryID){
        		countriesAvailable++;
        		myTreeRootTo.add(new DefaultMutableTreeNode(loopCountry.countryName
                        +" (In "+loopCountry.belongToContinent.continentName+", "+loopCountry.armyNumber+" armies)"));
        	}
        }
        countryLabelTo.setText("Territories have a path ("+countriesAvailable+"):");
        add(countryLabelTo);
        countryLabelTo.setFont(new java.awt.Font("dialog",1,15));
        Dimension size = countryLabelTo.getPreferredSize();
        countryLabelTo.setBounds(armyNumberCombo.getBounds().x+armyNumberCombo.getSize().width+10,100,size.width,size.height);
        
        treeCountryTo = new JTree(myTreeRootTo);
        treeCountryTo.addMouseListener(new  MouseAdapter(){
        	public void mousePressed(MouseEvent e){
        		int selRow = treeCountryTo.getRowForLocation(e.getX(), e.getY());
        		TreePath selPath = treeCountryTo.getPathForLocation(e.getX(), e.getY());
        		if (selRow!=-1 && (e.getClickCount()==2)){
        			treeCountryTo.setSelectionPath(selPath);
        			if (selPath!=null) {
        				if (selPath.getParentPath().getParentPath()==null){//continents
           					selCountryNameTo = selPath.getLastPathComponent().toString().trim();
           					selCountryNameTo = selCountryNameTo.substring(0, selCountryNameTo.indexOf("(")-1); 
           					if (armyNumberCombo.getSelectedIndex()!=-1){
           						myGame.getGameMap().findCountry(selCountryNameFrom).armyNumber-=(armyNumberCombo.getSelectedIndex()+1);
           						myGame.getGameMap().findCountry(selCountryNameTo).armyNumber+=(armyNumberCombo.getSelectedIndex()+1);
           						state=1;
           						setVisible(false);
           					}
        				}
        			}
        			//popupMenu.show(e.getComponent(), e.getX(), e.getY());
        		}
        	}	
		});


        treeCountryTo.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));

        scrollPaneForCountryTo.getViewport().removeAll();
        scrollPaneForCountryTo.getViewport().add(treeCountryTo);
    }



    /**
     * To add action listener to the enter button
     */
    /*private class enterBtnHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (player.getState()==1){
                player.setTotalArmies(player.getTotalArmies()+totalArmies);
                if (cardExchanged) player.setCards(myCards);
                if (localCountries!=null){
                    for (int j=0;j<localCountries.length;j++){
                        myGame.getGameMap().findCountryByID(localCountries[j].ID).armyNumber = localCountries[j].Number;
                    }
                }
            }
            state = 1;
            setVisible(false);
        }
    }*/
}


