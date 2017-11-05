package gamecontroller;


import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import gameelements.NodeRecord;
import gameelements.Player;
import gameelements.RiskGame;
import javafx.beans.Observable;
import mapelements.Continent;
import mapelements.Country;

/**
 * This class is the implementation of reinforcement phase in the Risk.
 * <p> The ReinforcementPhase class will allow players add armies to their<br>
 * to their country, based on the countries under control.</p >
 *
 */
public class ReinforcePhaseView extends JDialog {


    //components in this window
    JLabel playerLabel;
    JLabel countryLabel;
    JLabel InitialArmy;
    JLabel turnLabel;
    JTree treeCountry;
    JLabel promptLabel;
    JComboBox<Object> armyNumberCombo;
    JScrollPane scrollPaneForCountry;
    JButton exchangeBtn;
    JButton cancelBtn;
    JButton enterBtn;
    private int width= 420,height = 560;

    private Player player;
    private RiskGame myGame;

    private int leftArmies, sourceArmies, totalArmies;
    private NodeRecord[] localCountries;
    private int[] myCards;
    private boolean cardExchanged;
    private int exchangeArmies;
    private TradeInCardsView tradeInCardsView;

    public int state=0; //0-Cancel, 1-confirm

    /**
     * Constructor of class ReinforcePhaseView to generate reinforce phase UI
     * @param player The player that who is in turn
     * @param game The game reinforce phase is in
     * @param armies The number of armies this reinforce phase has
     */
    public ReinforcePhaseView(Player player, RiskGame game, int armies){
        this.player = player;
        this.myGame = game;
        myCards = new int[3];
        cardExchanged = false;
        exchangeArmies = 0;
        player.getCards(myCards);
        tradeInCardsView = new TradeInCardsView();
        this.player.addObserver(tradeInCardsView);

        if (player.ifForceExchange()){

            while (!cardExchanged){

                JOptionPane.showMessageDialog(null, "You have more than 5 cards, so you have to exchange 3 card for armies!  "
                        + "\r\nThis is your "+(player.getChangeCardTimes()+1)+" time to exchange, can get "+player.CalExchangeArmies()+" armies.");

                tradeInCardsView.setVisible(true);

                TradeInCards exchangeView = new TradeInCards(myCards);
                exchangeView.setVisible(true);

                cardExchanged = exchangeView.state;
                if (cardExchanged)
                {
//                    System.out.println("haha");
                    exchangeArmies = player.CalExchangeArmies();
                }
                exchangeView.dispose();
            }

            sourceArmies = armies;
            leftArmies = Math.max(sourceArmies, 3);
            totalArmies = Math.max(sourceArmies, 3);

        setTitle("Reinforcement Phase");

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
            localCountries[j++] = new NodeRecord(loopCountry.getName(), loopCountry.getArmyNumber());
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
            Country loopCountry = myGame.getGameMap().findCountry(localCountries[i].name);
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getName()
                    +" (In "+loopCountry.getBelongTo().getName()+", "+localCountries[i].Number+" armies)"));
        }
        treeCountry= new JTree(myTreeRoot);
        treeCountry.addMouseListener( new  MouseAdapter(){
            public void mousePressed(MouseEvent e){
                int selRow = treeCountry.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = treeCountry.getPathForLocation(e.getX(), e.getY());
                if (selRow!=-1 && (e.getClickCount()==2)){
                    treeCountry.setSelectionPath(selPath);
                    if (selPath!=null) {
                        if (selPath.getParentPath()==null){ //root node
                        }
                        else if (selPath.getParentPath().getParentPath()==null){//countries
                            leftArmies-=armyNumberCombo.getSelectedIndex()+1;
                            localCountries[selRow-1].Number+=armyNumberCombo.getSelectedIndex()+1;
                            reloadGUI();
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

        cancelBtn = new JButton("Cancel");
        cancelBtn.setMnemonic('c');
        cancelBtn.setDisplayedMnemonicIndex(0);
        add(cancelBtn);
        size = cancelBtn.getPreferredSize();
        cancelBtn.setBounds(scrollPaneForCountry.getBounds().x+scrollPaneForCountry.getSize().width-size.width-1,484,size.width,size.height);
        cancelBtn.addActionListener(new cancelBtnHandler());

        exchangeBtn = new JButton("Exchange");
        exchangeBtn.setMnemonic('e');
        exchangeBtn.setDisplayedMnemonicIndex(0);
        add(exchangeBtn);
        size = exchangeBtn.getPreferredSize();
        exchangeBtn.setBounds(cancelBtn.getBounds().x-size.width-10,484,size.width,size.height);
        exchangeBtn.setVisible(player.canExchange(myCards));
        exchangeBtn.addActionListener(new exchangeHandler());


        enterBtn = new JButton("Confirm");
        add(enterBtn);
        size = enterBtn.getPreferredSize();
        enterBtn.setBounds(cancelBtn.getBounds().x-size.width-10,123,size.width,size.height);
        enterBtn.setVisible(false);
        enterBtn.addActionListener(new enterBtnHandler());
        }


    }

    /**
     * The method to refresh the UI
     */
    public void reloadGUI(){

        exchangeBtn.setVisible(false);

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
            cancelBtn.setBounds(cancelBtn.getBounds().x,123,cancelBtn.getSize().width,cancelBtn.getSize().height);
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
            Country loopCountry = myGame.getGameMap().findCountry(localCountries[i].name);
            myTreeRoot.add(new DefaultMutableTreeNode(loopCountry.getName()
                    +" (In "+loopCountry.getBelongTo().getName()+", "+localCountries[i].Number+" armies)"));
        }
        treeCountry = null;
        treeCountry= new JTree(myTreeRoot);
        treeCountry.addMouseListener( new  MouseAdapter(){
            public void mousePressed(MouseEvent e){
                int selRow = treeCountry.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = treeCountry.getPathForLocation(e.getX(), e.getY());
                if (selRow!=-1 && (e.getClickCount()==2)){
                    treeCountry.setSelectionPath(selPath);
                    if (selPath!=null) {
                        if (selPath.getParentPath()==null){ //root node
                        }
                        else if (selPath.getParentPath().getParentPath()==null){//countries
                            leftArmies-=armyNumberCombo.getSelectedIndex()+1;
                            localCountries[selRow-1].Number+=armyNumberCombo.getSelectedIndex()+1;
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
     * To add action listener to the cancel button
     */
    private class cancelBtnHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            state = 0;
            setVisible(false);
        }
    }

    /**
     * To add action listener to the enter button
     */
    private class enterBtnHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (player.getState()){
                player.addArmies(totalArmies);
                if (cardExchanged) {
                    player.increaseChangeCardTimes();
                    player.setCards(myCards);
                }
                if (localCountries!=null){
                    for (int j=0;j<localCountries.length;j++){
                        myGame.getGameMap().findCountry(localCountries[j].name).setArmyNumber(localCountries[j].Number);
                    }
                }
            }
            state = 1;
            setVisible(false);
            tradeInCardsView.closeTradeInCardsView();
            player.deleteObserver(tradeInCardsView);
        }
    }

    /**
     * To add action listener to the exchange card handler
     */
    private class exchangeHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (cardExchanged) return;
            JOptionPane.showMessageDialog(null, "This is your "+(player.getChangeCardTimes()+1)+" time to exhange, can get "+player.CalExchangeArmies()+" armies.");

            TradeInCards exchangeView = new TradeInCards(myCards);
            TradeInCardsView tradeInCardsView = new TradeInCardsView();
            tradeInCardsView.setVisible(true);
            player.addObserver(tradeInCardsView);
            exchangeView.setVisible(true);

            cardExchanged = exchangeView.state;
            if (cardExchanged) {
                exchangeArmies = player.CalExchangeArmies();
                sourceArmies+=exchangeArmies;
                leftArmies = Math.max(sourceArmies, 3);
                totalArmies = Math.max(sourceArmies, 3);
                reloadGUI();
            }
            exchangeView.dispose();
//            player.deleteObserver(tradeInCardsView);

            tradeInCardsView.closeTradeInCardsView();
        }
    }
}


