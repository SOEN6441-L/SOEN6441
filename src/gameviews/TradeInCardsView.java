package gameviews;


import javax.swing.*;

import gamemodels.PlayerModel;

import java.awt.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TradeInCardsView extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	JLabel Label1;
    JLabel Label2;
    JLabel Label3; 
    
	SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
		@Override
		protected Void doInBackground() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void process(List<Integer> chunks) {
		}
		@Override
		protected void done() {
			dispose();
		}
	};
    
    public TradeInCardsView(){
        //this.setLocation((screenWidth-500)/3, (screenHeight-300)/3);
        this.setSize(400,230);
        this.setLayout(null);
        this.setTitle("Exchange Card View");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        Label1 = new JLabel();
        Label1.setForeground(Color.DARK_GRAY);
        Label1.setBounds(15,15,470,20);
        this.add(Label1);
        Label1.setFont(new java.awt.Font("dialog",4,15));

        Label2 = new JLabel("");
        Label2.setForeground(Color.RED);
        Label2.setBounds(15,45,470,60);
        this.add(Label2);
        Label2.setFont(new java.awt.Font("dialog",4,15));
        
        Label3 = new JLabel("");
        Label3.setForeground(Color.BLUE);
        Label3.setBounds(15,105,470,20);
        this.add(Label3);
        Label3.setFont(new java.awt.Font("dialog",4,15));

        this.setVisible(false);
    }

    public void update(Observable obs, Object x){
    	int type = (Integer) x;
    	if (type == 4){//update player phase info
    		Label1.setText(MessageFormat.format("{0} now has {1}",((PlayerModel)obs).getName(),((PlayerModel)obs).getCardsString(1)));
    		if (!((PlayerModel)obs).canExchange()){
   	        	Label2.setText(MessageFormat.format("<HTML>{0} can't exchange cards,<br>this view will closed in 2 seconds.</HTML>",((PlayerModel)obs).getName()));
   	        	((PlayerModel)obs).deleteObserver(this);
   	        	worker.execute();
    		}
    		else{
    			Label2.setText("Waiting for exchange information ...");    			
    		}
		}
    	else if (type==6){
       		Label1.setText(MessageFormat.format("{0} has {1}",((PlayerModel)obs).getName(),((PlayerModel)obs).getCardsString(1)));
    		String tempStr = ((PlayerModel)obs).getExchangeStatus();
    		if (tempStr.equals("Exchange Cards ... finished.")){
           		Label2.setText("<HTML>Exchange Cards ... finished,<br>this view will closed in 2 seconds.</HTML>");
           		((PlayerModel)obs).deleteObserver(this);
           		worker.execute();
    		}
    		else {
    			Label2.setText((((PlayerModel)obs).getExchangeStatus()));
        		if (!((PlayerModel)obs).getExchangeCardStr().isEmpty())
        			Label3.setText(((PlayerModel)obs).getExchangeCardStr()+" }");
    		}
    	}
    	else if (type==7){
       		Label2.setText("<HTML>Player begin to place armies,<br>this view will closed in 2 seconds.</HTML>");
       		((PlayerModel)obs).deleteObserver(this);
       		worker.execute();
    	}
    }
    
    public void closeTradeInCardsView(){
        try {
            Thread.sleep(6000);
            this.dispose();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String args[]){
        TradeInCardsView T = new TradeInCardsView();
        PlayerModel P = new PlayerModel("12",Color.RED,null);

        T.setVisible(true);

        P.addObserver(T);
        int[] cards = {6,2,3};
        P.setCards(cards);

        int[] cards2 = {1,3,3};
        P.setCards(cards2);

        T.closeTradeInCardsView();
    }
}
