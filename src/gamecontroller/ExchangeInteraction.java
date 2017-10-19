package gamecontroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 *   This class is GUI for Exchange cards to armies.
 */

public class ExchangeInteraction extends JDialog{
    JPanel ContentPanel = new JPanel();

    //Four buttons
    JButton b1 = new JButton("infantry");//creating instance of JButton
    JButton b2 = new JButton("cavalry");
    JButton b3 = new JButton("artillery");
    JButton submitButton = new JButton("submit");

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
    boolean[] isAvailiable = new boolean[3];

    //Array to store cards
    private int[] myCards;

    boolean state;

    /**
     *  This method is to set layout if GUI
     *	@param cards player's cards
     */
    public ExchangeInteraction(int[] cards){
    	myCards = cards;
        ContentPanel.setLayout(null);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
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
        b1.setBounds(35,300,100,40);
        b2.setBounds(205,300,100,40);
        b3.setBounds(375,300,100,40);
        submitButton.setBounds(545,300,100,40);
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
        state = false;
    }

   /**
    *  This method is to check if label is available to store picture
    *
    *  @return label name when available
    */

    public JLabel getAndSetAvailable (){
        for (int i = 0; i < 3; i++) {
            if (isAvailiable[i] == false) {
                isAvailiable[i] = true;
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
     *  This method is to Listen b1(infantry) b2(cavalry) b3(artillery")
     *
     */
    public void Mylistener(){
        b1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
           		if(myCards[0] > 0){
           			JLabel al = getAndSetAvailable();
           			if(al != null) {
           				al.setIcon(infantryIcon);
           				al.setText("infantry");
           				myCards[0]--;
           				SetButtonLabel();
           			}
           		}	
            	else{
            		//JOptionPane.showMessageDialog(null, " You need more infantry! ", "Error", JOptionPane.ERROR_MESSAGE);
            	}
            }

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        b2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(b2.isEnabled()&&myCards[1] > 0){
                    JLabel al = getAndSetAvailable();
                    if(al != null) {
                        al.setIcon(cavalryIcon);
                        al.setText("cavalry");
                        myCards[1]--;
                        SetButtonLabel();
                    }
                }
                else{
                    //JOptionPane.showMessageDialog(null, " You need more cavalry! ", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        b3.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(b3.isEnabled()&&myCards[2] > 0){
                    JLabel al = getAndSetAvailable();
                    if(al != null) {
                        al.setIcon(artilleryIcon);
                        al.setText("artillery");
                        myCards[2]--;
                        SetButtonLabel();
                    }
                }
                else{
                    //JOptionPane.showMessageDialog(null, " You need more artillery! ", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        submitButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(GetLabel(Label1)!=-1 && GetLabel(Label2)!=-1 && GetLabel(Label3)!=-1){
                    if(IfLegal()){
                        ClearLabel();
                        state = true;
                        setVisible(false);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Must be three same cards or different cards! ", " Error ", JOptionPane.ERROR_MESSAGE);
                    	//int i = GetLabel(Label1);
                        //int j = GetLabel(Label2);
                        //int k = GetLabel(Label3);
                        //myCards[i]++;
                        //myCards[j]++;
                        //myCards[k]++;
                        //SetButtonLabel();
                        //ClearLabel();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, " You need three cards! ", " Error ", JOptionPane.ERROR_MESSAGE);
                }


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Label1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAvailiable[0] == true){
                    Label1.setIcon(nullIcon);
                    isAvailiable[0] = false;
                    int id = GetLabel(Label1);
                    myCards[id]++;
                    SetButtonLabel();
                    Label1.setText("null");
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Label2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAvailiable[1] == true){
                    Label2.setIcon(nullIcon);
                    isAvailiable[1] = false;
                    int id = GetLabel(Label2);
                    myCards[id]++;
                    SetButtonLabel();
                    Label2.setText("null");
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Label3.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAvailiable[2] == true){
                    Label3.setIcon(nullIcon);
                    isAvailiable[2] = false;
                    int id = GetLabel(Label3);
                    myCards[id]++;
                    SetButtonLabel();
                    Label3.setText("null");
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

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
            isAvailiable[i] = false;
        }
        Label1.setText("null");
        Label2.setText("null");
        Label3.setText("null");

    }

    /**
     *   To check name of labels
     *
     *   @return number of labels
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
     *   To check if exchange is legal
     *
     *   @return true if it is legal
     */
    public boolean IfLegal(){
        if(Label1.getText()!="null"&&Label2.getText()!="null"&&Label3.getText()!="null"){
            if(Label1.getText() == Label2.getText() && Label3.getText() == Label2.getText() ){
                return true;
            }
            else if (Label1.getText() != Label2.getText() && Label3.getText() != Label2.getText() && Label1.getText() != Label3.getText()){
                return true;
            }
            else
                return false;
        }
        return false;
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

    }
}
