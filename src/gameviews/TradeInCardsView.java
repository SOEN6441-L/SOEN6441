package gameviews;


import gamecontrollers.MonitorInterface;
import gamemodels.PlayerModel;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * Class to monitor the trade cards process
 * @see Observer
 *
 */
public class TradeInCardsView implements Observer {
    MonitorInterface server;
 
	/**
	 * Constructor for trading in cards View
	 * @param server remote server
	 * @param x x coordinate
	 * @param y y coordinate
	 */
    public TradeInCardsView(MonitorInterface server, int x, int y){
        //this.setLocation((screenWidth-500)/3, (screenHeight-300)/3);
    	try {
			server.createTradeInCardsView(x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
        this.server = server;
   }
    
	/**
	 * Rewrite update method.
	 * @param obs observable object
	 * @param x an argument passed by the notifyObservers method.
	 */
    public void update(Observable obs, Object x){
    	try {
    		int type = (int)x;
    		PlayerModel player = (PlayerModel) obs;
    		String info1="",info2="",info3="",info4="";
        	if (type == 4){//update player phase info
        		info1 = player.getName();
        		info2 = player.getCardsString(1);
        		info3 = String.valueOf(player.canExchange());
    		}
        	else if (type==6){
        		info1 = player.getName();
        		info2 = player.getCardsString(1);
        		info3 = player.getExchangeStatus();
        		info4 = player.getExchangeCardStr();
        	}
			Boolean result = server.updateCardsView(info1,info2,info3,info4,type);
			if (!result) player.deleteObserver(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    }
}
