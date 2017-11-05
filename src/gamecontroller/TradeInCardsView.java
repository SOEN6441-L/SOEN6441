package gamecontroller;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import gameelements.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.MessageFormat;
import java.util.Observable;
import java.util.Observer;

public class TradeInCardsView extends JFrame implements Observer {

    JLabel Label1;
    JLabel Label2;

    public TradeInCardsView(){
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        this.setLocation((screenWidth-685)/3, (screenHeight-400)/3);
        this.setSize(500,300);
        this.setLayout(null);
        this.setTitle("Exchange Card View");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        Label1 = new JLabel();
        Label1.setForeground(Color.DARK_GRAY);
        Label1.setBounds(15,15,470,20);
        this.add(Label1);
        Label1.setFont(new java.awt.Font("dialog",4,15));

        Label2 = new JLabel("Exchanging...");
        Label2.setForeground(Color.DARK_GRAY);
        Label2.setBounds(15,35,470,50);
        this.add(Label2);
        Label2.setFont(new java.awt.Font("dialog",4,15));

        this.setVisible(false);

    }

    public void update(Observable obs, Object x){
        int[] cards = new int [3];
        ((Player) obs).getCards(cards);
        int armies =  ((Player) obs).getTotalArmies();
        int exchangeCardTimes = ((Player) obs).getChangeCardTimes();
        try {
            Thread.sleep(1000);
            Label1.setText(MessageFormat.format("Your cards are {0} + {1} + {2}", cards[0],cards[1],cards[2]));
            Label2.setText(MessageFormat.format("Your cards have been change {0} times and you have {1} armies", exchangeCardTimes,armies));
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        if(((Player) obs).reinforcementPhase())
    }

    public void closeTradeInCardsView(){
        try {
            Thread.sleep(5000);
            this.dispose();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

    }

//    public static void main(String args[]){
//        TradeInCardsView T = new TradeInCardsView();
//        Player P = new Player("12",Color.RED);
//
//        T.setVisible(true);
//
//        P.addObserver(T);
//        int[] cards = {6,2,3};
//        P.setCards(cards);
//
//        int[] cards2 = {1,3,3};
//        P.setCards(cards2);
//
//        T.closeTradeInCardsView();
//
//    }
}
