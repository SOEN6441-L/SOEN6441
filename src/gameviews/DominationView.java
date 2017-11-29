package gameviews;

import gamecontrollers.MonitorInterface;
import gamemodels.RiskGameModel;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Class to display the domination status
 * @see Observer
 */
public class DominationView implements Observer{
	MonitorInterface server;

	/**
	 * This is the constructor of class DominationView
	 * @param server remote server
	 */
	public DominationView(MonitorInterface server){  
		this.server = server;
    }

    /**
     * This is for update the state of the chart
     * @param ob the notifying Observable from model
     * @param arg the notifying object from model
     */
    public void update(Observable ob, Object arg) {
    	if ((int)arg==111){
    		try {
    			if (server!=null){
    				int num = 0;
    				int [] countriesNum = null;
    				Color [] colors = null;
    				if (((RiskGameModel)ob).getPlayers()!=null){
    					num = ((RiskGameModel)ob).getPlayers().length;
    					countriesNum = new int[num];
    					colors = new Color[num];
    					for (int i=0;i<num;i++){
    						countriesNum[i] = ((RiskGameModel)ob).getPlayers()[i].getCountries().size();
    						colors[i] = ((RiskGameModel)ob).getPlayers()[i].getMyColor();
    					}
    				}	
    				server.updateDomiView(num,countriesNum,colors);
    			}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
    	}	
    }
}
