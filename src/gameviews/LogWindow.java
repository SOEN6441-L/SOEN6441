package gameviews;

import java.util.Observable;
import java.util.Observer;

import gamecontrollers.MonitorInterface;
/**
 * 
 * Class to define log window
 *@see Observer
 */

public class LogWindow implements Observer{
	MonitorInterface server;
	/**
	 * Constructor of class
	 * @param server remote server
	 */
	public LogWindow (MonitorInterface server){
		this.server = server;
	}
	/**
	 * Method to be called by Observable's notifyObservers method.
	 * @param arg0 the observable object
	 * @param arg1 an argument passed by the notifyObservers method.
	 */	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		try {
			if (server!=null)
				server.updateLogWindow((String)arg1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
}
