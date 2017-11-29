package gameviews;

import javax.swing.*;

import gamemodels.PlayerModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 *   This class is GUI for Exchange cards to armies.
 *   @see JDialog
 */

public class TradeInCards extends JDialog{
	private static final long serialVersionUID = 1L;

	JPanel ContentPanel = new JPanel();

    //Four buttons
    JButton b1 = new JButton("infantry");//creating instance of JButton
    JButton b2 = new JButton("cavalry");
    JButton b3 = new JButton("artillery");
    JButton submitButton = new JButton("Exchange");
    JButton exitButton = new JButton("Exit");

    //Three Icons of three images of three cards
    ImageIcon infantryIcon = new ImageIcon("src/images/infantry.png");
    ImageIcon cavalryIcon = new ImageIcon("src/images/cavalry.png");
    ImageIcon artilleryIcon = new ImageIcon("src/images/artillery.png");
    ImageIcon nullIcon = new ImageIcon("src/images/null(1).png");

    //Three labels to store pictures of three cards
    JLabel Label1 = new JLabel("null");
    JLabel Label2 = new JLabel("null");
    JLabel Label3 = new JLabel("null");


    //To test if Label is empty
    public boolean[] isAvailiable = new boolean[]{true,true,true};
    public int mode;
    
    //Array to store cards
    public int[] myCards = new int[3];
    private PlayerModel player;

    /**
     *  This method is to set layout if GUI
     *	@param player player object
     *	@param mode 0-normal others-silent
     */
    public TradeInCards(PlayerModel player,int mode){
    	this.player = player;
    	this.mode = mode;
    	player.copyCards(myCards);
        ContentPanel.setLayout(null);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Exchange Cards");
		
		Label1.setIcon(nullIcon);
        Label2.setIcon(nullIcon);
        Label3.setIcon(nullIcon);
        
        ContentPanel.add(Label1);
        ContentPanel.add(Label2);
        ContentPanel.add(Label3);
        
        Label1.setBounds(60,20,175,265);
        Label2.setBounds(255,20,175,265);
        Label3.setBounds(450,20,175,265);
        
        ContentPanel.add(b1);
        ContentPanel.add(b2);
        ContentPanel.add(b3);
        
        ContentPanel.add(submitButton);
        ContentPanel.add(exitButton);
        
        b1.setBounds(35,300,100,40);
        b2.setBounds(175,300,100,40);
        b3.setBounds(320,300,100,40);
        
        submitButton.setBounds(545,300,100,40);
        submitButton.setEnabled(false);
        
        exitButton.setBounds(465,300,75,40);
        exitButton.setVisible(!player.ifForceExchange());
        
        ContentPanel.setBounds(0,0,685,400);
        ContentPanel.setBackground(Color.LIGHT_GRAY);
        
        this.setLayout(null);
        this.add(ContentPanel);
        
        SetButtonLabel();
        
        this.setSize(685,400);
		int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		setLocation((screenWidth-685)/2, (screenHeight-400)/2);  
        
		this.setVisible(false);
        this.Mylistener();
    }

   /**
    * This method is to check if label is available to store picture
    * @return label name when available
    */
    public JLabel getAndSetAvailable (){
        for (int i = 0; i < 3; i++) {
            if (isAvailiable[i]) {
                isAvailiable[i] = false;
                switch (i) {
                    case 0:
                        return Label1;
                    case 1:
                        return Label2;
                    case 2:
                        return Label3;
                }
            }
        }
        return null;
    }
 
    /**
     * Method to change the cards automatically
     */
    public void exhangeAuto(){
		if (Math.min(myCards[0], Math.min(myCards[1], myCards[2]))>=1){
			b1Click();
			b2Click();
			b3Click();
			submit();
		}
		else if (myCards[0]>=3){
			b1Click();
			b1Click();
			b1Click();
			submit();           			
		}
		else if (myCards[1]>=3){
			b2Click();
			b2Click();
			b2Click();
			submit();              			
		}
		else if (myCards[2]>=3){
			b3Click();
			b3Click();
			b3Click();
			submit();             			
		}
    }
    
    /**
     * Method to submit players' action
     */
    public void submit(){
        if(submitButton.isEnabled()){
        	player.setCards(myCards);                	
        	int army = player.CalExchangeArmies();
        	player.increaseTotalReinforcement(army);
        	int card = IfLegal();
        	String tempStr="";
        	switch (card){
        		case 0:tempStr= "3 infantries";
        		break;
        		case 1:tempStr= "3 cavalries";
        		break;
        		case 2:tempStr= "3 artilleries";
        		break;
        		case 3:tempStr= "3 different cards";
        		break;
        	}
        	player.setExchangeCardStr(String.valueOf(army));
        	player.setReinforcementStr(player.getBaseReinforceStr()
        			+"<br>+"+player.getExchangeCardStr()+" } = "
        			+player.getTotalReinforcement()+" armies</HTML>");
        	player.setExchangeStatus("("+player.getMyGame().getChangeCardTimes()+" times), Exchange "+tempStr+" for "+army+" armies");
        	
        	ClearLabel();
        	submitButton.setEnabled(false);
        	if (player.ifForceExchange())
        		if (mode==0) JOptionPane.showMessageDialog(null, "Exchange "+tempStr+" for "+army+" armies,\n"+
        				"and you need to continue exchanging cards until less than 5.");	
        	else if (player.canExchange()){
        		if (mode==0) JOptionPane.showMessageDialog(null, "Exchange "+tempStr+" for "+army+" armies,\n"+
        				"and you can continue exchanging cards or exit to finish this step.");
        		exitButton.setVisible(true);
        	}	
        	else {
        		if (mode==0) JOptionPane.showMessageDialog(null, "Exchange "+tempStr+" for "+army+" armies,\n"+
            				"and no more cards can be exchanged.");
           		setVisible(false);
        	}	
        }    	
    }

    /**
     * b1 click action, infantry clicked
     */
    public void b1Click(){
   		if(b1.isEnabled()&&myCards[0] > 0){
   			JLabel al = getAndSetAvailable();
            if (al!=null){
            	al.setIcon(infantryIcon);
            	al.setText("infantry");
            	myCards[0]--;
            	SetButtonLabel();
            }	
   		}	
    }
    
    /**
     * b2 click action, cavalry clicked
     */
    public void b2Click(){
        if(b2.isEnabled()&&myCards[1] > 0){
            JLabel al = getAndSetAvailable();
            if (al!=null){
            	al.setIcon(cavalryIcon);
            	al.setText("cavalry");
            	myCards[1]--;
            	SetButtonLabel();
            }	
        }	
    }
    
    /**
     * b3 click action,artillery clicked
     */
    public void b3Click(){
        if(b3.isEnabled()&&myCards[2] > 0){
            JLabel al = getAndSetAvailable();
            if (al!=null){
            	al.setIcon(artilleryIcon);
            	al.setText("artillery");
            	myCards[2]--;
            	SetButtonLabel();
            }	
        }  	
    }



    /**
     *  This method is to Listen b1(infantry) b2(cavalry) b3(artillery)
     *
     */
    private void Mylistener(){
        b1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	b1Click();
            }
        });
        b2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	b2Click();
            }
        });
        b3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	b3Click();
            }
        });
        Label1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isAvailiable[0]){
                    isAvailiable[0] = true;
                    myCards[GetLabel(Label1)]++;
                    Label1.setIcon(nullIcon);
                    Label1.setText("null");
                    SetButtonLabel();
                }

            }
        });
        Label2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isAvailiable[1]){
                    isAvailiable[1] = true;
                    myCards[GetLabel(Label2)]++;
                    Label2.setIcon(nullIcon);
                    Label2.setText("null");
                    SetButtonLabel();
                }
            }
        });
        Label3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isAvailiable[2]){
                    isAvailiable[2] = true;
                    myCards[GetLabel(Label3)]++;
                    Label3.setIcon(nullIcon);
                    Label3.setText("null");
                    SetButtonLabel();
                }
            }
        });        
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	submit();
            }    
        });

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(exitButton.isVisible()){
        			if (mode==0&&player.canExchange()){
        				if (JOptionPane.showConfirmDialog(null,
        						"You can still exchange cards, do you really want exit and keep these cards for next turn?",
        						"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
        					return;	
        			}
                	setVisible(false);
                }
            }
        });

    }

   /**
    *To clear three Labels
    */
    public void ClearLabel(){
        Label1.setIcon(nullIcon);
        Label2.setIcon(nullIcon);
        Label3.setIcon(nullIcon);
        for (int i = 0; i < 3; i++) {
            isAvailiable[i] = true;
        }
        Label1.setText("null");
        Label2.setText("null");
        Label3.setText("null");
    }

    /**
     *  To check name of labels
     *	@param label JLabel object
     *  @return number of labels
     */
    public int GetLabel(JLabel label){
        if (label.getText() == "infantry"){
            return 0;
        }
        else if (label.getText() == "cavalry"){
            return 1;
        }
        else if (label.getText() == "artillery"){
            return 2;
        }
        else
            return -1;
    }

    /**
     *   To check if exchange is legal or not
     *
     *   @return 0- 3 infantry,1-3 cavalry ,2- 3 artillery, 3- 1+1+1, -1: none 
     */
    public int IfLegal(){
        if(Label1.getText()!="null"&&Label2.getText()!="null"&&Label3.getText()!="null"){
            if(Label1.getText() == Label2.getText() && Label3.getText() == Label2.getText() ){
                return GetLabel(Label1);
            }
            else if (Label1.getText() != Label2.getText() && Label3.getText() != Label2.getText() && Label1.getText() != Label3.getText()){
                return 3;
            }
            else
                return -1;
        }
        return -1;
    }

    /**
     *  To set numbers on button relating to numbers of this kind of cards
     */
    public void SetButtonLabel(){

        b1.setText("infantry:"+this.myCards[0]);
        b1.setEnabled(this.myCards[0]>0);
        b2.setText("cavalry:"+this.myCards[1]);
        b2.setEnabled(this.myCards[1]>0);
        b3.setText("artillery:"+this.myCards[2]);
        b3.setEnabled(this.myCards[2]>0);
        submitButton.setEnabled(this.IfLegal()!=-1);
    }
}
