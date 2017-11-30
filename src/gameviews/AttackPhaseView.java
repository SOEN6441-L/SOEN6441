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
 * This class is the implementation of attack phase in the Risk.
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
    
    public String selCountryNameFrom,selCountryNameTo;

    private PlayerModel player;
    private RiskGameModel myGame;

    private int mode;
    public NodeRecord[] localCountries;
    public Map<CountryModel,ArrayList<CountryModel>> localAdjacencyList;


    public int state=0; //0-Cancel, 1-confirm

    /**
     * Constructor of class ReinforcePhaseView to generate reinforce phase UI
     * @param player The player that who is in turn
     * @param mode 0-normal 1-aggressive 3-random
     */
    public AttackPhaseView(PlayerModel player, int mode){
        this.player = player;
        this.myGame = player.getMyGame();
        this.mode = mode;

        setTitle("Attack Phase");

        setSize(width,height);
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        setLocation((screenWidth-width)/2, (screenHeight-height)/2);
        //set exit program when close the window
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        //not capable adjust windows size
        setResizable(false);
        setLayout(null);
        setModal(true);
        setVisible(false);
        
        player.conquered = false;

        Dimension size;    
        ArrayList<CountryModel> attackingCountry = player.getAttackingCountry(0);
       	player.setAttackInfo("");

        localCountries = new NodeRecord[attackingCountry.size()];
        int j = 0;
        for (CountryModel loopCountry:attackingCountry){
        	localCountries[j++] = new NodeRecord(loopCountry.getShowName(), loopCountry.getArmyNumber());
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

        countryLabelFrom = new JLabel("Territories can attacking ("+attackingCountry.size()+"):");
        add(countryLabelFrom);
        countryLabelFrom.setFont(new java.awt.Font("dialog",1,15));
        size = countryLabelFrom.getPreferredSize();
        countryLabelFrom.setBounds(15,100,size.width,size.height);

        DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
        for (int i=0;i<localCountries.length;i++) {
            CountryModel loopCountry = myGame.getGameMap().findCountry(localCountries[i].getName());
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getShowName()
                    +" (In "+loopCountry.getBelongTo().getShowName()+", "+localCountries[i].getNumber()+" armies)"));
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
        attackBtn.addActionListener(new ButtonHandler());
        
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
        enterBtn.setBounds(scrollPaneForCountryTo.getBounds().x+scrollPaneForCountryTo.getSize().width-size.width,488,size.width,size.height);
        enterBtn.addActionListener(new ButtonHandler());
    }

    /**
     * The method to refresh the attacked countries
     */
    public void reloadAttacked(){
        DefaultMutableTreeNode myTreeRootTo = new DefaultMutableTreeNode("Countries");
        int countriesAvailable = 0;
        for (CountryModel loopCountry : localAdjacencyList.get(myGame.getGameMap().findCountry(selCountryNameFrom))){
       		countriesAvailable++;
       		myTreeRootTo.add(new DefaultMutableTreeNode(loopCountry.getShowName()
                        +" (Owned by "+loopCountry.getOwner().getName()+", "+loopCountry.getArmyNumber()+" armies)"));
       	}
        countryLabelTo.setText("Territories can be attacked ("+countriesAvailable+"):");
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
     * This method is to reload GUI
     */
    public void reloadGUI(){

        ArrayList<CountryModel> attackingCountry = player.getAttackingCountry(0);
        
        if (attackingCountry.size()==0){
        	player.setAttackInfo("No more territories can attack, attack phase finished");
        	myGame.myLog.setLogStr(player.getDiscription()+", no more territories can attack, attack phase finished\n");
        	setVisible(false);
        	return;
        }
        else {
        	player.setAttackInfo("");
        }
        
        localCountries = new NodeRecord[attackingCountry.size()];
        int j = 0;
        for (CountryModel loopCountry:attackingCountry){
			localCountries[j++] = new NodeRecord(loopCountry.getShowName(), loopCountry.getArmyNumber());
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

        countryLabelFrom.setText("Territories can attacking ("+attackingCountry.size()+"):");

        DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
        for (int i=0;i<localCountries.length;i++) {
            CountryModel loopCountry = myGame.getGameMap().findCountry(localCountries[i].getName());
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getShowName()
                    +" (In "+loopCountry.getBelongTo().getShowName()+", "+localCountries[i].getNumber()+" armies)"));
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
     * Method to finish attack phase
     */
    public void finishPhase(){
    	player.setAttackInfo("Attack phase terminated.");
    	myGame.myLog.setLogStr(player.getDiscription()+" Attack phase terminated.\n");
    }	
    
    /**
     * Method to attack once;
     */
    public void attackOneCountry(){
    	player.setAttackInfo(selCountryNameFrom+" attacking "+selCountryNameTo);
    	CountryModel country1 = myGame.getGameMap().findCountry(selCountryNameFrom);
    	CountryModel country2 = myGame.getGameMap().findCountry(selCountryNameTo);
    	myGame.myLog.setLogStr("\n    "+player.getDiscription()+" ("+selCountryNameFrom+" "+country1.getArmyNumber()+
    			" armies) attacking "+country2.getOwner().getDiscription()+" ("+selCountryNameTo+" "+country2.getArmyNumber()+" armies)\n");
    	AttackDiceView diceView = new AttackDiceView(myGame.getGameMap().findCountry(selCountryNameFrom),
    			myGame.getGameMap().findCountry(selCountryNameTo), mode);
    	if (mode == 0) diceView.setVisible(true);
    	else if (mode ==1){ //attack till end for aggressive
    		while (myGame.getGameMap().findCountry(selCountryNameFrom).getArmyNumber()>1&&
    				myGame.getGameMap().findCountry(selCountryNameTo).getArmyNumber()>0&&
    				myGame.getGameMap().findCountry(selCountryNameFrom).getOwner()!=myGame.getGameMap().findCountry(selCountryNameTo).getOwner()){
    			diceView.attackOnce();
    		}	
    	}
    	else{//attack random for random
    		while (myGame.getGameMap().findCountry(selCountryNameFrom).getArmyNumber()>1&&
    				myGame.getGameMap().findCountry(selCountryNameTo).getArmyNumber()>0&&
    				myGame.getGameMap().findCountry(selCountryNameFrom).getOwner()!=myGame.getGameMap().findCountry(selCountryNameTo).getOwner()){
    			int randomNum = (int)(Math.random()*2);
    			if (randomNum==1) diceView.attackOnce();
    			else {
    				diceView.betray();
    				break;
    			}
    		}
    	}
    	reloadGUI();
    }	
    
	/**
	 * Class to define action Listener.
	 * @see ActionListener
	 */
	private class ButtonHandler implements ActionListener { 
		/**
		 * Method to define action performed according to different users' action.
		 * @param e the action event of user.
		 */	
		public void actionPerformed(ActionEvent e) {

			String buttonName = e.getActionCommand();
			switch (buttonName){
			case "Finish":	
				finishPhase();
	            setVisible(false);
				break;	
			case "Attack":  
				attackOneCountry();
	            break;
			}
		}
	}
}


