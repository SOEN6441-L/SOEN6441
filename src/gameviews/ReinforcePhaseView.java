package gameviews;


import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
public class ReinforcePhaseView extends JDialog{

	private static final long serialVersionUID = 1L;
	//components in this window
    JLabel playerLabel;
    JLabel countryLabel;
    JLabel InitialArmy;
    JLabel cardsLabel;
    JLabel turnLabel;
    public JTree treeCountry;
    JLabel promptLabel;
    public JComboBox<Object> armyNumberCombo;
    JScrollPane scrollPaneForCountry;
    public JButton exchangeBtn;
    //JButton cancelBtn;
    public JButton enterBtn;
    private int width= 420,height = 560;

    private PlayerModel player;
    private RiskGameModel myGame;
    
    private int mode;
    public int leftArmies;
    public NodeRecord[] localCountries;

    public int state=0; //0-Cancel, 1-confirm

    /**
     * Constructor of class ReinforcePhaseView to generate reinforce phase UI
     * @param player The player that who is in turn
     * @param mode 0-normal 1-aggressive 2-benevolent 3-random
     */
    public ReinforcePhaseView(PlayerModel player,int mode){
        this.player = player;
        this.myGame = player.getMyGame();
        this.mode = mode;
        
        boolean changeCards = false;

        if (player.ifForceExchange()){
            if (mode==0) JOptionPane.showMessageDialog(null, "You have more than 5 cards, so you have to exchange them for armies until less than 5.");
        	player.setExchangeStatus("Forced to exchange Cards ...");
        	TradeInCards exchangeView = new TradeInCards(player,mode);
            exchangeView.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            if (mode==0) exchangeView.setVisible(true);//human
            else if (mode==1){//aggressive
            	while (player.canExchange()){
           			exchangeView.exhangeAuto();          			
            	}            	
            }
            else if (mode==2){//benevolent
            	while (player.ifForceExchange()){
            		exchangeView.exhangeAuto();
            	}
            }
            else if (mode==3){//random
            	while (player.ifForceExchange()){
            		exchangeView.exhangeAuto();
            	}
            	while (player.canExchange()){
            		int randomNum = (int)(Math.random()*2);
            		if (randomNum==1) {
            			exchangeView.exhangeAuto();          			
            		}
            		else break;
            	}
            }
            exchangeView.dispose();
            changeCards = true;
            player.setExchangeStatus("Exchange Cards ... finished.");
        }

        leftArmies = player.getTotalReinforcement();

        setTitle("Reinforcement Phase");

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

        Dimension size;

        localCountries = new NodeRecord[player.getCountries().size()];
        int j = 0;
        for (CountryModel loopCountry:player.getCountries()){
            localCountries[j++] = new NodeRecord(loopCountry.getShowName(), loopCountry.getArmyNumber());
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

        InitialArmy = new JLabel("Reinforcement armies: ");
        add(InitialArmy);
        InitialArmy.setFont(new java.awt.Font("dialog",1,18));
        InitialArmy.setForeground(Color.BLACK);
        size = InitialArmy.getPreferredSize();
        InitialArmy.setBounds(20,60,size.width,size.height);

        String moveArmies[]= new String[leftArmies];
        for (int i=0;i<leftArmies;i++) {
            moveArmies[i] = String.valueOf(i+1);
        }
        armyNumberCombo = new JComboBox<Object>(moveArmies);
        armyNumberCombo.setSelectedIndex(leftArmies-1);
        add(armyNumberCombo);
        size = armyNumberCombo.getPreferredSize();
        armyNumberCombo.setBounds(InitialArmy.getBounds().x+InitialArmy.getSize().width+2,60,size.width+20,size.height);

        countryLabel = new JLabel("Territories ("+player.getCountries().size()+"):");
        add(countryLabel);
        countryLabel.setFont(new java.awt.Font("dialog",1,15));
        size = countryLabel.getPreferredSize();
        countryLabel.setBounds(15,100,size.width,size.height);

        DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
        for (int i=0;i<localCountries.length;i++) {
            CountryModel loopCountry = myGame.getGameMap().findCountry(localCountries[i].getName());
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getShowName()
                    +" (In "+loopCountry.getBelongTo().getShowName()+", "+localCountries[i].getNumber()+" armies)"));
        }
        treeCountry= new JTree(myTreeRoot);
        treeCountry.addMouseListener( new  MouseAdapter(){
            public void mousePressed(MouseEvent e){
                int selRow = treeCountry.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = treeCountry.getPathForLocation(e.getX(), e.getY());
                if (selRow>0 && (e.getClickCount()==2)){
                    treeCountry.setSelectionPath(selPath);
                    if (selPath!=null) {
                        if (selPath.getParentPath()==null){ //root node
                        }
                        else if (selPath.getParentPath().getParentPath()==null){//countries
                        	reinforceArmy(selRow-1);
                        }
                    }
                }
                //popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        treeCountry.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));

        scrollPaneForCountry= new JScrollPane(treeCountry,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPaneForCountry);
        scrollPaneForCountry.setBounds(15,125,385,330);

        promptLabel = new JLabel("Double click one country to place armies you have choosen.");
        add(promptLabel);
        promptLabel.setFont(new java.awt.Font("dialog",1,13));
        promptLabel.setForeground(Color.RED);
        size = promptLabel.getPreferredSize();
        promptLabel.setBounds(15,458,size.width,size.height); 
        
        /*cancelBtn = new JButton("Cancel");
        cancelBtn.setMnemonic('c');
        cancelBtn.setDisplayedMnemonicIndex(0);
        add(cancelBtn);
        size = cancelBtn.getPreferredSize();
        cancelBtn.setBounds(scrollPaneForCountry.getBounds().x+scrollPaneForCountry.getSize().width-size.width-1,484,size.width,size.height);
        cancelBtn.addActionListener(new cancelBtnHandler());*/

        exchangeBtn = new JButton("Exchange");
        exchangeBtn.setMnemonic('e');
        exchangeBtn.setDisplayedMnemonicIndex(0);
        add(exchangeBtn);
        size = exchangeBtn.getPreferredSize();
        exchangeBtn.setBounds(scrollPaneForCountry.getBounds().x+scrollPaneForCountry.getSize().width-size.width-1,484,size.width,size.height);
        exchangeBtn.setEnabled(!changeCards&&player.canExchange());
        exchangeBtn.addActionListener(new exchangeHandler());
       
        cardsLabel = new JLabel(player.getCardsString(1));
        cardsLabel.setFont(new java.awt.Font("dialog",1,13));
        cardsLabel.setForeground(Color.BLUE);
        size = cardsLabel.getPreferredSize();
        cardsLabel.setBounds(exchangeBtn.getBounds().x-size.width-30,exchangeBtn.getBounds().y+3,size.width,size.height);
        add(cardsLabel);

        enterBtn = new JButton("Confirm");
        add(enterBtn);
        size = enterBtn.getPreferredSize();
        enterBtn.setBounds(scrollPaneForCountry.getBounds().x+scrollPaneForCountry.getSize().width-size.width-1,123,size.width,size.height);
        enterBtn.setVisible(false);
        enterBtn.addActionListener(new enterBtnHandler());
        
        if (exchangeBtn.isEnabled()&&(mode==1||mode==3)){
        	exchangeCards();
        }        
        
    }

    /**
     * Method to reinforce army once
     * @param selRow selected country
     */
    public void reinforceArmy (int selRow){
    	leftArmies-=armyNumberCombo.getSelectedIndex()+1;
    	localCountries[selRow].setNumber(localCountries[selRow].getNumber() + armyNumberCombo.getSelectedIndex()+1);
    	player.setPutArmyStr("Places "+(armyNumberCombo.getSelectedIndex()+1)+" armies on "+localCountries[selRow].getName());
    	myGame.myLog.setLogStr("    "+player.getDiscription()+" places "+(armyNumberCombo.getSelectedIndex()+1)+" armies on "+localCountries[selRow].getName()+"\n");
       	reloadGUI();
    }
    
    /**
     * The method to refresh the UI
     */
    public void reloadGUI(){

        exchangeBtn.setEnabled(false);

        if (leftArmies == 0){
            InitialArmy.setText("Reinforcement Phase done!");
            Dimension size = InitialArmy.getPreferredSize();
            InitialArmy.setBounds(20,60,size.width,size.height);
            countryLabel.setVisible(false);
            armyNumberCombo.setVisible(false);
            scrollPaneForCountry.getViewport().removeAll();
            scrollPaneForCountry.setVisible(false);
            promptLabel.setVisible(false);
            enterBtn.setVisible(true);
            //cancelBtn.setBounds(cancelBtn.getBounds().x,123,cancelBtn.getSize().width,cancelBtn.getSize().height);
            setSize(width,205);
            return;
        }

        armyNumberCombo.removeAllItems();
        for (int i=0;i<leftArmies;i++) {
            armyNumberCombo.addItem(i+1);
        }
        armyNumberCombo.setSelectedIndex(leftArmies-1);
        Dimension size = armyNumberCombo.getPreferredSize();
        armyNumberCombo.setBounds(InitialArmy.getBounds().x+InitialArmy.getSize().width+2,60,size.width+20,size.height);

        countryLabel.setText("Territories ("+player.getCountries().size()+"):");

        DefaultMutableTreeNode myTreeRoot = new DefaultMutableTreeNode("Countries");
        for (int i=0;i<localCountries.length;i++) {
            CountryModel loopCountry = myGame.getGameMap().findCountry(localCountries[i].getName());
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getShowName()
                    +" (In "+loopCountry.getBelongTo().getShowName()+", "+localCountries[i].getNumber()+" armies)"));
        }
        treeCountry = null;
        treeCountry= new JTree(myTreeRoot);
        treeCountry.addMouseListener( new  MouseAdapter(){
            public void mousePressed(MouseEvent e){
                int selRow = treeCountry.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = treeCountry.getPathForLocation(e.getX(), e.getY());
                if (selRow>0 && (e.getClickCount()==2)){
                    treeCountry.setSelectionPath(selPath);
                    if (selPath!=null) {
                        if (selPath.getParentPath()==null){ //root node
                        }
                        else{//countries
                            leftArmies-=armyNumberCombo.getSelectedIndex()+1;
                            localCountries[selRow-1].setNumber(localCountries[selRow-1].getNumber() + armyNumberCombo.getSelectedIndex()+1);
                            player.setPutArmyStr("Places "+(armyNumberCombo.getSelectedIndex()+1)+" armies on "+localCountries[selRow-1].getName());
                            myGame.myLog.setLogStr("    "+player.getDiscription()+" places "+(armyNumberCombo.getSelectedIndex()+1)+" armies on "+localCountries[selRow-1].getName()+"\n");
                            reloadGUI();
                        }
                    }
                }
                //popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        treeCountry.setCellRenderer(new CountryNodeRenderer(player.getMyColor()));
        scrollPaneForCountry.getViewport().removeAll();
        scrollPaneForCountry.getViewport().add(treeCountry);
    }

    /**
     * To add action listener to the enter button
     * @see ActionListener
     */
    private class enterBtnHandler implements ActionListener {
    	/**
    	 * method to perform the action from user
    	 * @param e event of action
    	 */
        public void actionPerformed(ActionEvent e) {
        	confirm();
            setVisible(false);
        }
    }
    
    /**
     * Method to confirm the reinforcement result and finish phase
     */
    public void confirm(){
    	if (player.getState()){
    		if (localCountries!=null){
    			for (int j=0;j<localCountries.length;j++){
    				myGame.getGameMap().findCountry(localCountries[j].getName()).setArmyNumber(localCountries[j].getNumber());
    			}
    		}
    	}
    }	
    
    /**
     * method to change the cards in hand
     */
    public void exchangeCards(){
        player.setExchangeStatus("Exchanging Cards ...");
		myGame.myLog.setLogStr("\n    "+player.getDiscription()+ " begin to exhanging cards ...\n");
		myGame.myLog.setLogStr("    "+player.getDiscription()+ " now have "+player.getCardsString(1)+"\n");
        TradeInCards exchangeView = new TradeInCards(player,mode);
        if (mode==0) exchangeView.setVisible(true);
        else if (mode==1){//aggressive
        	while (player.canExchange()){
       			exchangeView.exhangeAuto();          			
        	}            	
        }
        else if (mode==3){//random
        	while (player.canExchange()){
        		int randomNum = (int)(Math.random()*2);
        		if (randomNum==1) {
        			exchangeView.exhangeAuto();          			
        		}
        		else break;
        	}
        }
        
        cardsLabel.setText(player.getCardsString(1));
        exchangeBtn.setEnabled(false);
        armyNumberCombo.removeAllItems();
        leftArmies = player.getTotalReinforcement();
        for (int i=0;i<leftArmies;i++) {
            armyNumberCombo.addItem(i+1);
        }
        armyNumberCombo.setSelectedIndex(leftArmies-1);
        exchangeView.dispose();
        myGame.myLog.setLogStr("    "+player.getDiscription()+ " now have "+player.getCardsString(1)+"\n");
        myGame.myLog.setLogStr("    "+player.getDiscription()+ " now reinforcement armies = "+player.getTotalReinforcement()+"\n");
        player.setExchangeStatus("Exchange Cards ... finished.");
    }

    /**
     * To add action listener to the exchange card handler
     * @see ActionListener
     */
    private class exchangeHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	exchangeCards();
        }
    }
}


