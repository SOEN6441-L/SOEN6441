package gamecontrollers;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

/**
 * The interface for the monitor interface
 */
public interface MonitorInterface extends Remote {
	/** interface definition of update phaseView
	 * @param arg0 the variable observer from observable class
	 * @param current the number of current game
	 * @param type the game type
	 * @throws RemoteException Remote Exceptions
	 */
	public void updatePhaseView(Observable arg0, int current, int type) throws RemoteException;
	/**
	 * interface definition of update phaseView
	 * @param info the information of phase need to update
	 * @param color the color of the phase
	 * @param type the type number of the phase
	 * @throws RemoteException Remote Exceptions
	 */
	public void updatePhaseView(String info, Color color, int type) throws RemoteException;
	/**
	 * interface definition of update LogWindow
	 * @param newLog the log window of the update information
	 * @throws RemoteException Remote Exceptions
	 */
	public void updateLogWindow(String newLog) throws RemoteException;
	/**
	 * interface definition of update DomiView
	 * @param num the number of the domination view
	 * @param size the size of the domination view
	 * @param colors the color the domination view
	 * @throws RemoteException Remote Exceptions
	 */
	public void updateDomiView(int num,int[] size, Color[] colors) throws RemoteException;
	/**
	 * interface definition of update CardsView
	 * @param info1 the information of card 1
	 * @param info2 the information of card 2
	 * @param info3 the information of card 3
	 * @param info4 the information of card 4
	 * @param type the number of type
	 * @return whether it success or not
	 * @throws RemoteException Remote Exceptions
	 */
	public boolean updateCardsView(String info1, String info2, String info3, String info4, int type) throws RemoteException;	
	/**
	 * interface definition of update Label
	 * @param info the information of the update label
	 * @throws RemoteException Remote Exceptions
	 */
	public void updateLabel(String info) throws RemoteException;
	/**
	 * interface definition of create card view
	 * @param x the location x for the phase
	 * @param y the location y for the phase
	 * @throws RemoteException Remote Exceptions
	 */
	public void createTradeInCardsView(int x,int y) throws RemoteException;
	
}
