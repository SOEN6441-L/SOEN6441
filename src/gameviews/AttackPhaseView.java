package gameviews;


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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import gamemodels.NodeRecord;
import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;
import mapmodels.CountryModel;

/**
 * This class is the implementation of reinforcement phase in the Risk.
 * <p> The ReinforcementPhase class will allow players add armies to their<br>
 * to their country, based on the countries under control.</p >
 *@see JDialog
 */
public class AttackPhaseView extends JDialog{
	private static final long serialVersionUID = 1L;
	//components in this window
    JLabel playerLabel;
    JLabel countryLabelFrom;
    JLabel countryLabelTo;
    JLabel turnLabel;
    JLabel phaseLabel;
    JTree treeCountryFrom;
    JTree treeCountryTo;
    JLabel promptLabelFrom, promptLabelTo;
    JButton attackBtn;
    JScrollPane scrollPaneForCountryFrom;
    JScrollPane scrollPaneForCountryTo;
    JButton enterBtn;
    private int width= 910,height = 560;
    
    private String selCountryNameFrom,selCountryNameTo;

    private PlayerModel player;
    private RiskGameModel myGame;

    private NodeRecord[] localCountries;
    private Map<CountryModel,ArrayList<CountryModel>> localAdjacencyList;


    public int state=0; //0-Cancel, 1-confirm

    /**
     * Constructor of class ReinforcePhaseView to generate reinforce phase UI
     * @param player The player that who is in turn
     * @param game The game reinforce phase is in
     */
    public AttackPhaseView(PlayerModel player, RiskGameModel game){
        this.player = player;
        this.myGame = game;

        setTitle("Attack Phase");

        setSize(width,height);
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        setLocation((screenWidth-width)/2, (screenHeight-height)/2);
        //set exit program when close the window
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        //not capable adjust windows size
        setResizable(false);
        setLayout(null);
        setModal(true);
        setVisible(false);

        Dimension size;    
        int attackingCountry = player.getAttackingCountry();
        
        if (attackingCountry == 0){
        	player.setAttackInfo("No more territories can attacking, attack phase finished");
        	this.dispose();
        }
        else {
        	player.setAttackInfo("");
        }

        localCountries = new NodeRecord[attackingCountry];
        int j = 0;
        for (CountryModel loopCountry:player.getCountries()){
        	if (loopCountry.getArmyNumber()>1){
				ArrayList<CountryModel> neighbors = myGame.getGameMap().getAdjacencyList().get(loopCountry);
				for (CountryModel neighbor:neighbors){
					if (neighbor.getBelongTo()!=loopCountry.getBelongTo()){
						localCountries[j++] = new NodeRecord(loopCountry.getName(), loopCountry.getArmyNumber());
						break;
					}
				}	
        	}
        }
        
		localAdjacencyList = new HashMap<CountryModel,ArrayList<CountryModel>>();
		for (CountryModel loopCountry: player.getCountries()){
			localAdjacencyList.put(loopCountry, new ArrayList<CountryModel>());
			for (CountryModel neighbour: myGame.getGameMap().getAdjacencyList().get(loopCountry)){
				if (neighbour.getOwner()!=player){
					localAdjacencyList.get(loopCountry).add(neighbour);
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
        
        phaseLabel = new JLabel("Attack Phase");
        add(phaseLabel);
        phaseLabel.setFont(new java.awt.Font("dialog",1,18));
        phaseLabel.setForeground(Color.BLACK);
        size = phaseLabel.getPreferredSize();
        phaseLabel.setBounds(20,60,size.width,size.height);        

        countryLabelFrom = new JLabel("Territories can attacking ("+attackingCountry+"):");
        add(countryLabelFrom);
        countryLabelFrom.setFont(new java.awt.Font("dialog",1,15));
        size = countryLabelFrom.getPreferredSize();
        countryLabelFrom.setBounds(15,100,size.width,size.height);

        DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
        for (int i=0;i<localCountries.length;i++) {
            CountryModel loopCountry = myGame.getGameMap().findCountry(localCountries[i].getName());
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getName()
                    +" (In "+loopCountry.getBelongTo().getName()+", "+localCountries[i].getNumber()+" armies)"));
        }
        treeCountryFrom = new JTree(myTreeRoot);
        treeCountryFrom.addMouseListener( new  MouseAdapter(){
        	public void mousePressed(MouseEvent e){
        		int selRow = treeCountryFrom.getRowForLocation(e.getX(), e.getY());
        		TreePath selPath = treeCountryFrom.getPathForLocation(e.getX(), e.getY());
        		if (selRow>0 && (e.getButton() == 1)){
        			treeCountryFrom.setSelectionPath(selPath);
        			if (selPath!=null) {
        				if (selPath.getParentPath().getParentPath()==null){//continents
           					selCountryNameFrom = selPath.getLastPathComponent().toString().trim();
           					selCountryNameFrom = selCountryNameFrom.substring(0, selCountryNameFrom.indexOf("(")-1);
           					//myGame.getGameMap().findPath(localAdjacencyList,myGame.getGameMap().findCountry(selCountryNameFrom));
           					reloadAttacked();
        				}
        			}
        			//popupMenu.show(e.getComponent(), e.getX(), e.getY());
        		}
        	}	
		}); 

        treeCountryFrom.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));

        scrollPaneForCountryFrom= new JScrollPane(treeCountryFrom,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForCountryFrom);
        scrollPaneForCountryFrom.setBounds(15,125,385,340);

        attackBtn = new JButton("Attack");
        size = attackBtn.getPreferredSize();
        add(attackBtn);
        attackBtn.setEnabled(false);
        attackBtn.setBounds(scrollPaneForCountryFrom.getBounds().x+scrollPaneForCountryFrom.getSize().width+10,260,size.width+20,size.height);
        attackBtn.addActionListener(new attackBtnHandler());
        
        countryLabelTo = new JLabel("Territories can be attacked (0):");
        add(countryLabelTo);
        countryLabelTo.setFont(new java.awt.Font("dialog",1,15));
        size = countryLabelTo.getPreferredSize();
        countryLabelTo.setBounds(attackBtn.getBounds().x+attackBtn.getSize().width+10,100,size.width,size.height);

        DefaultMutableTreeNode myTreeRootTo = new DefaultMutableTreeNode("Countries");
        treeCountryTo = new JTree(myTreeRootTo);

        treeCountryTo.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));

        scrollPaneForCountryTo= new JScrollPane(treeCountryTo,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForCountryTo);
        scrollPaneForCountryTo.setBounds(countryLabelTo.getBounds().x,125,385,340);

        enterBtn = new JButton("Finish");
        add(enterBtn);
        size = enterBtn.getPreferredSize();
        enterBtn.setBounds(815,540,size.width,size.height);
        enterBtn.addActionListener(new enterBtnHandler());
        
        
    }

    /**
     * The method to reload attack phase
     */
    public void reloadAttacked(){
        DefaultMutableTreeNode myTreeRootTo = new DefaultMutableTreeNode("Countries");
        int countriesAvailable = 0;
        for (CountryModel loopCountry : localAdjacencyList.get(myGame.getGameMap().findCountry(selCountryNameFrom))){
       		countriesAvailable++;
       		myTreeRootTo.add(new DefaultMutableTreeNode(loopCountry.getName()
                        +" (Owned by "+loopCountry.getOwner().getName()+", "+loopCountry.getArmyNumber()+" armies)"));
       	}
        countryLabelTo.setText("Territories can be attacked ("+countriesAvailable+"):");
        add(countryLabelTo);
        countryLabelTo.setFont(new java.awt.Font("dialog",1,15));
        Dimension size = countryLabelTo.getPreferredSize();
        countryLabelTo.setBounds(attackBtn.getBounds().x+attackBtn.getSize().width+10,100,size.width,size.height);
        treeCountryTo = new JTree(myTreeRootTo);
        treeCountryTo.addMouseListener( new  MouseAdapter(){
        	public void mousePressed(MouseEvent e){
        		int selRow = treeCountryTo.getRowForLocation(e.getX(), e.getY());
        		TreePath selPath = treeCountryTo.getPathForLocation(e.getX(), e.getY());
        		if (selRow>0 && (e.getButton() == 1)){
        			treeCountryTo.setSelectionPath(selPath);
        			if (selPath!=null) {
        				if (selPath.getParentPath().getParentPath()==null){//continents
           					selCountryNameTo = selPath.getLastPathComponent().toString().trim();
           					selCountryNameTo = selCountryNameTo.substring(0, selCountryNameTo.indexOf("(")-1);
           					//myGame.getGameMap().findPath(localAdjacencyList,myGame.getGameMap().findCountry(selCountryNameFrom));
           					attackBtn.setEnabled(true);
        				}
        			}
        			//popupMenu.show(e.getComponent(), e.getX(), e.getY());
        		}
        	}	
		}); 
        treeCountryTo.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));
        scrollPaneForCountryTo.getViewport().removeAll();
        scrollPaneForCountryTo.getViewport().add(treeCountryTo);
        attackBtn.setEnabled(false);
    }

    /**
     * The method to refresh the UI
     */
    public void reloadGUI(){

        int attackingCountry = player.getAttackingCountry();
        
        localCountries = new NodeRecord[attackingCountry];
        int j = 0;
        for (CountryModel loopCountry:player.getCountries()){
        	if (loopCountry.getArmyNumber()>1){
				ArrayList<CountryModel> neighbors = myGame.getGameMap().getAdjacencyList().get(loopCountry);
				for (CountryModel neighbor:neighbors){
					if (neighbor.getBelongTo()!=loopCountry.getBelongTo()){
						localCountries[j++] = new NodeRecord(loopCountry.getName(), loopCountry.getArmyNumber());
						break;
					}
				}	
        	}
        }
        
		localAdjacencyList = new HashMap<CountryModel,ArrayList<CountryModel>>();
		for (CountryModel loopCountry: player.getCountries()){
			localAdjacencyList.put(loopCountry, new ArrayList<CountryModel>());
			for (CountryModel neighbour: myGame.getGameMap().getAdjacencyList().get(loopCountry)){
				if (neighbour.getOwner()!=player){
					localAdjacencyList.get(loopCountry).add(neighbour);
				}
			}
		}  

        countryLabelFrom.setText("Territories can attacking ("+attackingCountry+"):");

        DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
        for (int i=0;i<localCountries.length;i++) {
            CountryModel loopCountry = myGame.getGameMap().findCountry(localCountries[i].getName());
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getName()
                    +" (In "+loopCountry.getBelongTo().getName()+", "+localCountries[i].getNumber()+" armies)"));
        }
        treeCountryFrom = new JTree(myTreeRoot);
        treeCountryFrom.addMouseListener( new  MouseAdapter(){
        	public void mousePressed(MouseEvent e){
        		int selRow = treeCountryFrom.getRowForLocation(e.getX(), e.getY());
        		TreePath selPath = treeCountryFrom.getPathForLocation(e.getX(), e.getY());
        		if (selRow>0 && (e.getButton() == 1)){
        			treeCountryFrom.setSelectionPath(selPath);
        			if (selPath!=null) {
        				if (selPath.getParentPath().getParentPath()==null){//continents
           					selCountryNameFrom = selPath.getLastPathComponent().toString().trim();
           					selCountryNameFrom = selCountryNameFrom.substring(0, selCountryNameFrom.indexOf("(")-1);
           					//myGame.getGameMap().findPath(localAdjacencyList,myGame.getGameMap().findCountry(selCountryNameFrom));
           					reloadAttacked();
        				}
        			}
        			//popupMenu.show(e.getComponent(), e.getX(), e.getY());
        		}
        	}	
		}); 

        treeCountryFrom.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));

        scrollPaneForCountryFrom.getViewport().removeAll();
        scrollPaneForCountryFrom.getViewport().add(treeCountryFrom);

        attackBtn.setEnabled(false);
        
        
        countryLabelTo.setText("Territories can be attacked (0):");

        DefaultMutableTreeNode myTreeRootTo = new DefaultMutableTreeNode("Countries");
        treeCountryTo = new JTree(myTreeRootTo);

        treeCountryTo.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));

        scrollPaneForCountryTo.getViewport().removeAll();
        scrollPaneForCountryTo.getViewport().add(treeCountryTo);
    }
    /**
     * 
     * Class to define the action when user finish attack phase
     *
     */
    private class enterBtnHandler implements ActionListener {
    	/**
    	 * Method to define action performed according to different users' action.
    	 * @param e the action event of user.
    	 */	
		@Override
        public void actionPerformed(ActionEvent e) {
			player.setAttackInfo("Attack phase terminated.");
            setVisible(false);
        }
    }
    /**
     * 
     * Class to define the action when user begin to attack
     *
     */   
    private class attackBtnHandler implements ActionListener {
    	/**
    	 * Method to define action performed according to different users' action.
    	 * @param e the action event of user.
    	 */	
		@Override
        public void actionPerformed(ActionEvent e) {
			player.setAttackInfo(selCountryNameFrom+" attacking "+selCountryNameTo);
            //setVisible(false);
            reloadGUI();
        }
    }
}


