package gameviewsremote;


import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.List;

/**
 * 
 * Class to monitor the trade cards process
 *
 */
public class TradeInCardsViewRemote extends JFrame {

	private static final long serialVersionUID = 1L;
	JLabel Label1;
    JLabel Label2;
    JLabel Label3; 
    
	SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
		/**
		 * Method to define background job
		 */
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
		/**
		 * Method to handle process
		 * @param chunks the info
		 */
		@Override
		protected void process(List<Integer> chunks) {
		}
		/**
		 * Method to handle done
		 */
		@Override
		protected void done() {
			dispose();
		}
	};
	
	/**
	 * Constructor for trading in cards View
	 */
    public TradeInCardsViewRemote(){
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
    
	/**
	 * Rewrite update method.
	 * @param player observable object
	 * @param type an argument passed by the notifyObservers method.
	 */
    public boolean update(String info1, String info2, String info3, String info4, int type) throws RemoteException{
    	boolean result = true;
    	if (type == 4){//update player phase info
    		Label1.setText(MessageFormat.format("{0} now has {1}",info1,info2));
    		if (!Boolean.parseBoolean(info3)){
   	        	Label2.setText(MessageFormat.format("<HTML>{0} can't exchange cards,<br>this view will closed in 2 seconds.</HTML>",info1));
   	        	result = false;
   	        	worker.execute();
    		}
    		else{
    			Label2.setText("Waiting for exchange information ...");    			
    		}
		}
    	else if (type==6){
       		Label1.setText(MessageFormat.format("{0} has {1}",info1,info2));
    		if (info3.equals("Exchange Cards ... finished.")){
           		Label2.setText("<HTML>Exchange Cards ... finished,<br>this view will closed in 2 seconds.</HTML>");
           		result = false;
           		worker.execute();
    		}
    		else {
    			Label2.setText(info3);
        		if (!info4.isEmpty())
        			Label3.setText(info4+" }");
    		}
    	}
    	else if (type==7){
       		Label2.setText("<HTML>Player begin to place armies,<br>this view will closed in 2 seconds.</HTML>");
       		result = false;
       		worker.execute();
    	}
    	return result;
    }
}
