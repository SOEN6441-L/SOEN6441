package gameviews;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;
import mapmodels.CountryModel;

/**
 * This class is the implementation of attack dice in the Risk.
 *@see JDialog
 */
public class AttackDiceView extends JDialog{
	private static final long serialVersionUID = 1L;
	//components in this window
    JLabel playerLabel1,playerLabel2;
    JLabel VSLabel;
    ImageIcon[] diceIcons  = new ImageIcon[6];
    JLabel [] attackingDice = new JLabel[3];
    JLabel [] attackedDice = new JLabel[2];
    
    JComboBox<Object> attackingDices,attackedDices; 
    
    JButton attackBtn;
    JButton enterBtn;
    private int width= 870,height = 360;
    

    private PlayerModel player1, player2;
    private CountryModel country1,country2;
    private RiskGameModel myGame;
    private int lastDice = 0;
    private int mode;

    /**
     * Constructor of class ReinforcePhaseView to generate reinforce phase UI
     * @param country1 The player that who is in turn
     * @param country2 The game reinforce phase is in
     * @param mode 0-normal 1-aggressive 3-random
     */
    public AttackDiceView(CountryModel country1, CountryModel country2, int mode){
        player1 = country1.getOwner();
        player2 = country2.getOwner();
        myGame = player1.getMyGame();
        this.mode = mode;
        this.country1 = country1;
        this.country2 = country2;

        setTitle("Play Dice");

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
        
        int attackingArmy = country1.getArmyNumber()-1;
        int attackedArmy = country2.getArmyNumber();        

        playerLabel1 =  new JLabel(country1.getShowName()+" ("+attackingArmy+" armies) owned by "+player1.getName());
        add(playerLabel1);
        playerLabel1.setFont(new java.awt.Font("dialog",1,18));
        playerLabel1.setForeground(player1.getMyColor());
        size = playerLabel1.getPreferredSize();
        playerLabel1.setBounds(15,15,size.width,size.height);
        
        VSLabel =  new JLabel("VS");
        add(VSLabel);
        VSLabel.setFont(new java.awt.Font("dialog",1,20));
        size = VSLabel.getPreferredSize();
        VSLabel.setBounds(playerLabel1.getBounds().x+playerLabel1.getSize().width+40,15,size.width,size.height);
        
        playerLabel2 =  new JLabel(country2.getShowName()+" ("+attackedArmy+" armies) owned by "+player2.getName());
        add(playerLabel2);
        playerLabel2.setFont(new java.awt.Font("dialog",1,18));
        playerLabel2.setForeground(player2.getMyColor());
        size = playerLabel2.getPreferredSize();
        playerLabel2.setBounds(VSLabel.getBounds().x+VSLabel.getSize().width+40,15,size.width,size.height);
        
        for (int i=0;i<6;i++){
        	diceIcons[i] = new ImageIcon("src/images/"+(i+1)+".jpg");
        }
 
        for (int i=0;i<3;i++){
        	attackingDice[i] = new JLabel("");
        	attackingDice[i].setIcon(diceIcons[5]);
            add(attackingDice[i]);
            attackingDice[i].setEnabled(attackingArmy>(2-i));
            attackingDice[i].setBounds(playerLabel1.getBounds().x+playerLabel1.getSize().width-(3-i)*77+10,65,57,57);
        }
        
        attackingDices = new JComboBox<Object>();
        //armyNumberCombo.setSelectedIndex(leftArmies-1);
        size = attackingDices.getPreferredSize();
        attackingDices.setBounds(attackingDice[2].getBounds().x+attackingDice[2].getSize().width-size.width-20,141,size.width+20,size.height);       
        attackingDices.removeAllItems();
        for (int i=1;i<=Math.min(3,attackingArmy);i++) {
        	attackingDices.addItem(i);
        }
        attackingDices.setSelectedIndex(attackingDices.getItemCount()-1);
        add(attackingDices);
        attackingDices.addActionListener(new ButtonHandler());
        
        for (int i=0;i<2;i++){
        	attackedDice[i] = new JLabel("");
        	attackedDice[i].setIcon(diceIcons[0]);
            add(attackedDice[i]);
            attackedDice[i].setEnabled(attackedArmy>(1-i));
            attackedDice[i].setBounds(playerLabel2.getBounds().x+10+i*77,65,57,57);
        }        

        attackedDices = new JComboBox<Object>();
        //armyNumberCombo.setSelectedIndex(leftArmies-1);
        size = attackedDices.getPreferredSize();
        attackedDices.setBounds(attackedDice[0].getBounds().x,141,size.width+20,size.height);       
        attackedDices.removeAllItems();
        for (int i=1;i<=Math.min(2,attackedArmy);i++) {
        	attackedDices.addItem(i);
        }
        attackedDices.setSelectedIndex(attackedDices.getItemCount()-1);
        add(attackedDices);
        attackedDices.addActionListener(new ButtonHandler());
        
        attackBtn = new JButton("Begin");
        add(attackBtn);
        attackBtn.setEnabled(true);
        attackBtn.addActionListener(new ButtonHandler());  
        attackBtn.setBounds(VSLabel.getBounds().x+(VSLabel.getSize().width)/2-50,191,100,30);
        
        enterBtn = new JButton("Finish");
        add(enterBtn);
        enterBtn.setBounds(attackBtn.getBounds().x,231,100,30);
        enterBtn.addActionListener(new ButtonHandler());  
    }    
  
    /**
     * Method to reload GUI 
     */
    private void reloadGUI(){
        Dimension size;    
        
        int attackingArmy = country1.getArmyNumber()-1;
        int attackedArmy = country2.getArmyNumber();        
        
		if (attackingArmy==0){
			if (mode==0) JOptionPane.showMessageDialog(null, player1.getName()+" failed.");
			myGame.myLog.setLogStr("    "+player1.getDiscription()+" attack failed.\n");
			myGame.myLog.setLogStr("    Now, "+country1.getOwner().getName()+" ("+country1.getShowName()+" "+country1.getArmyNumber()+
					" armies) and "+country2.getOwner().getName()+" ("+country2.getShowName()+" "+country2.getArmyNumber()+" armies)\n");
			player1.setAttackInfo(player1.getName()+" failed.");
			setVisible(false);
    	}
    	else if (attackedArmy==0){
    		country2.setOwner(player1);
    		player1.addCountry(country2);
    		player2.removeCountry(country2);
    		if (!player1.conquered){//add a random card for conquer a country, only once for one turn
				int randomCard = (int)(Math.random()*3);
				player1.increaseCard(randomCard);
    			player1.conquered = true;
    		}	
    		myGame.changeDominationView();
    		if (!player2.getState()) player1.addCards(player2.getCards());
			
			if (mode==0){
				JOptionPane.showMessageDialog(null, player1.getName()+" grasps "+country2.getShowName()+" owned by "+player2.getName());
				String [] choice = new String [country1.getArmyNumber()-lastDice];
				int index=0;
				for (int i=lastDice;i<country1.getArmyNumber();i++) {
					choice[index++] = String.valueOf(i);
				}
				
				JComboBox<Object> armyInput = new JComboBox<Object>(choice);
				Object[] message = {
						"How many armies you want to left on new country:", armyInput
				};  

				boolean retry = true;
				while (retry){
					int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
					if (option!=JOptionPane.OK_OPTION||armyInput.getSelectedIndex()==-1){
						JOptionPane.showMessageDialog(null,"You must left some armies on the new conquared country.");
					}
					else {
						int armyNum = Integer.parseInt((String)armyInput.getItemAt(armyInput.getSelectedIndex()));
						player1.moveArmies(country2,country1,armyNum);
						myGame.myLog.setLogStr("        "+player1.getName()+" puts "+armyNum+" armies on "+country2.getShowName()+"\n");
						retry = false;
					}
				}
			}
			else if (mode == 1){
				player1.moveArmies(country2,country1,lastDice);
				myGame.myLog.setLogStr("        "+player1.getName()+" puts "+lastDice+" armies on "+country2.getShowName()+"\n");
			}	
			else if (mode == 3){
				int randomNum=(int)(Math.random()*(country1.getArmyNumber()-lastDice))+lastDice;
				player1.moveArmies(country2,country1,randomNum);
				myGame.myLog.setLogStr("        "+player1.getName()+" puts "+lastDice+" armies on "+country2.getShowName()+"\n");
			}
			
			myGame.myLog.setLogStr("    "+player1.getDiscription()+" grasps "+country2.getShowName()+" owned by "+player2.getName()+" before\n");
			myGame.myLog.setLogStr("    Now, "+country1.getOwner().getName()+" ("+country1.getShowName()+" "+country1.getArmyNumber()+
					" armies) and "+country2.getOwner().getName()+" ("+country2.getShowName()+" "+country2.getArmyNumber()+" armies)\n");
			setVisible(false);
    	}

        playerLabel1.setText(country1.getShowName()+" ("+attackingArmy+" armies) owned by "+player1.getName());
        size = playerLabel1.getPreferredSize();
        playerLabel1.setBounds(15,15,size.width,size.height);
        size = VSLabel.getPreferredSize();
        VSLabel.setBounds(playerLabel1.getBounds().x+playerLabel1.getSize().width+40,15,size.width,size.height);
       
        playerLabel2.setText(country2.getShowName()+" ("+attackedArmy+" armies) owned by "+player2.getName());
        size = playerLabel2.getPreferredSize();
        playerLabel2.setBounds(VSLabel.getBounds().x+VSLabel.getSize().width+40,15,size.width,size.height);
   
        attackingDices.removeAllItems();
        for (int i=1;i<=Math.min(3,attackingArmy);i++) {
        	attackingDices.addItem(i);
        }
        attackingDices.setSelectedIndex(attackingDices.getItemCount()-1);        
        for (int i=0;i<3;i++){
            attackingDice[i].setEnabled(attackingArmy>(2-i));
            attackingDice[i].setBounds(playerLabel1.getBounds().x+playerLabel1.getSize().width-(3-i)*77+10,65,57,57);
        }
        
        attackedDices.removeAllItems();
        for (int i=1;i<=Math.min(2,attackedArmy);i++) {
        	attackedDices.addItem(i);
        }
        attackedDices.setSelectedIndex(attackedDices.getItemCount()-1);        
        for (int i=0;i<2;i++){
            attackedDice[i].setEnabled(attackedArmy>(1-i));
            attackedDice[i].setBounds(playerLabel2.getBounds().x+10+i*77,65,57,57);
        }        
           
        attackBtn.setEnabled(true);
        attackBtn.setBounds(VSLabel.getBounds().x+(VSLabel.getSize().width)/2-50,191,100,30);
        
        enterBtn.setBounds(attackBtn.getBounds().x,231,100,30);
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

			if (e.getSource()==attackingDices){
				if (attackingDices.getSelectedIndex()!=-1){
					int selected = attackingDices.getSelectedIndex();
					for (int i=0;i<3;i++){
			            attackingDice[i].setEnabled(selected>(1-i));
					}
					if (attackedDices.getSelectedIndex()!=-1) attackBtn.setEnabled(true);
				}
				return;
			}
			else if (e.getSource()==attackedDices){
				if (attackedDices.getSelectedIndex()!=-1){
					int selected = attackedDices.getSelectedIndex();
					for (int i=0;i<2;i++){
			            attackedDice[i].setEnabled(selected>(0-i));
					}
					if (attackingDices.getSelectedIndex()!=-1) attackBtn.setEnabled(true);
				}		
				return;
			}
			String buttonName = e.getActionCommand();
			switch (buttonName){
			case "Begin":
				attackOnce();
				break;	
			case "Finish":
				betray();
				break;				
			}
		}
	}	

	/**
	 * Method to implement betray.
	 */
	public void betray(){
		if (mode==0) JOptionPane.showMessageDialog(null, player1.getName()+" betrayed.");
		myGame.myLog.setLogStr("    "+player1.getDiscription()+" betrayed.");
		myGame.myLog.setLogStr("    Now, "+country1.getOwner().getName()+" ("+country1.getShowName()+" "+country1.getArmyNumber()+
			" armies) and "+country2.getOwner().getName()+" ("+country2.getShowName()+" "+country2.getArmyNumber()+" armies)\n");
		player1.setAttackInfo(player1.getName()+" betrayed.");
		setVisible(false);
	}	
	
	/**
	 * Method to implement one attack
	 */
    public void attackOnce(){
		int [] attacking = new int[]{0,0,0};
		int [] attacked = new int[]{0,0};

		if (attackedDices.getSelectedIndex()!=-1&&attackingDices.getSelectedIndex()!=-1){
			int attackingArmy = attackingDices.getSelectedIndex()+1;
			lastDice = attackingArmy;
			int attackedArmy = attackedDices.getSelectedIndex()+1;
			
			//generate dice result randomly
			String attackingStr = "";
			for (int i=3-attackingArmy;i<3;i++){
				attacking[i] = (int)(Math.random()*6)+1;
				attackingStr += String.valueOf(attacking[i]+" ");
				attackingDice[i].setIcon(diceIcons[attacking[i]-1]);
			}
			String attackedStr = "";
			for (int i=2-attackedArmy;i<2;i++){
				attacked[i] = (int)(Math.random()*6)+1;
				attackedStr += String.valueOf(attacked[i]+" ");
				attackedDice[i].setIcon(diceIcons[attacked[i]-1]);
			}
			//begin to analyze dice result for the max dice
			int biggestAttacking = attacking[2];
			int biggestAttackingIndex=2;
			if (attacking[1]>biggestAttacking) {
				biggestAttacking = attacking[1];
				biggestAttackingIndex = 1;
			}
			if (attacking[0]>biggestAttacking) {
				biggestAttacking = attacking[0];
				biggestAttackingIndex = 0;
			}
			
			int biggestAttacked = attacked[1];
			int biggestAttackedIndex=1;
			if (attacked[0]>biggestAttacked) {
				biggestAttacked = attacked[0];
				biggestAttackedIndex = 0;
			}
			PlayerModel firstLoser=null;
			PlayerModel secondLoser=null;
			if (biggestAttacking>biggestAttacked){//attacked lost one army
				country2.lostArmy();					
				firstLoser = player2;
			}
			else{//attacking lost one army
				country1.lostArmy();	
				firstLoser = player1;
			}
			//begin to analyze the second dice result
			if (attackedArmy == 2 && attackingArmy>1){ //
				int secondAttacked = attacked[1-biggestAttackedIndex];
				int secondAttacking = 0;
				switch (biggestAttackingIndex) {
				case 0: 
					secondAttacking = Math.max(attacking[1],attacking[2]);
					break;
				case 1:
					secondAttacking = Math.max(attacking[0],attacking[2]);
					break;
				case 2:
					secondAttacking = Math.max(attacking[0],attacking[1]);
					break;								
				}
				if (secondAttacking>secondAttacked){//attacked lost one more army
					secondLoser = player2;
					country2.lostArmy();
				}
				else{//attacking lost one more army
					secondLoser = player1;
					country1.lostArmy();
				}
			}
			
			
			if (secondLoser==null){
				if (mode ==0) JOptionPane.showMessageDialog(null, firstLoser.getName()+" lost one army.");
	        	myGame.myLog.setLogStr("        The dices result: "+player1.getName()+" ( "+attackingStr+"), "+player2.getName()+" ( "+attackedStr+") ------ "
	        			+firstLoser.getName()+" lost one army.\n");
	        	player1.setAttackStepInfo(firstLoser.getName()+" lost one army.");
			}	
			else {
				if (firstLoser==secondLoser){
					if (mode ==0) JOptionPane.showMessageDialog(null, firstLoser.getName()+" lost 2 armies.");
		        	myGame.myLog.setLogStr("        The dices result: "+player1.getName()+" ( "+attackingStr+"), "+player2.getName()+" ( "+attackedStr+") ------ "
		        			+firstLoser.getName()+" lost 2 armies.\n");
					player1.setAttackStepInfo(firstLoser.getName()+" lost 2 armies.");
				}
				else {
					if (mode ==0) JOptionPane.showMessageDialog(null, firstLoser.getName()+" lost one army, "+secondLoser.getName()+" lost one army.");
		        	myGame.myLog.setLogStr("        The dices result: "+player1.getName()+" ( "+attackingStr+"), "+player2.getName()+" ( "+attackedStr+") ------ "
		        			+firstLoser.getName()+" lost one army, "+secondLoser.getName()+" lost one army.\n");
					player1.setAttackStepInfo(firstLoser.getName()+" lost one army, "+secondLoser.getName()+" lost one army.");
				}
			}
			reloadGUI();
		}
	}
}
