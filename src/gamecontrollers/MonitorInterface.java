package gamecontrollers;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

/**
 * The interface for the monitor interface
 */
public interface MonitorInterface extends Remote {
	/**
	 * @param arg0 the variable observer from observable class
	 * @param current the number of current game
	 * @param type the game type
	 * @throws RemoteException
	 */
	public void updatePhaseView(Observable arg0, int current, int type) throws RemoteException;
	/**
	 *
	 * @param info the information of phase need to update
	 * @param color the color of the phase
	 * @param type the type number of the phase
	 * @throws RemoteException
	 */
	public void updatePhaseView(String info, Color color, int type) throws RemoteException;
	/**
	 * 
	 * @param newLog the log window of the update information
	 * @throws RemoteException
	 */
	public void updateLogWindow(String newLog) throws RemoteException;
	/**
	 *
	 * @param num the number of the domination view
	 * @param size the size of the domination view
	 * @param colors the color the domination view
	 * @throws RemoteException
	 */
	public void updateDomiView(int num,int[] size, Color[] colors) throws RemoteException;
	/**
	 *
	 * @param info1 the information of card 1
	 * @param info2 the information of card 2
	 * @param info3 the information of card 3
	 * @param info4 the information of card 4
	 * @param type the number of type
	 * @return whether it success or not
	 * @throws RemoteException
	 */
	public boolean updateCardsView(String info1, String info2, String info3, String info4, int type) throws RemoteException;	
	/**
	 * 
	 * @param info the information of the update label
	 * @throws RemoteException
	 */
	public void updateLabel(String info) throws RemoteException;
	/**
	 * 
	 * @@param x the location x for the phase
	 * @param y the location y for the phase
	 * @throws RemoteException
	 */
	public void createTradeInCardsView(int x,int y) throws RemoteException;
	
}
