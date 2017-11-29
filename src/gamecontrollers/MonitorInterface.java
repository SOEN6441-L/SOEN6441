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
	 * 
	 * @param arg0
	 * @param current
	 * @param type
	 * @throws RemoteException
	 */
	public void updatePhaseView(Observable arg0, int current, int type) throws RemoteException;
	/**
	 * 
	 * @param info
	 * @param color
	 * @param type
	 * @throws RemoteException
	 */
	public void updatePhaseView(String info, Color color, int type) throws RemoteException;
	/**
	 * 
	 * @param newLog
	 * @throws RemoteException
	 */
	public void updateLogWindow(String newLog) throws RemoteException;
	/**
	 * 
	 * @param num
	 * @param size
	 * @param colors
	 * @throws RemoteException
	 */
	public void updateDomiView(int num,int[] size, Color[] colors) throws RemoteException;
	/**
	 * 
	 * @param info1
	 * @param info2
	 * @param info3
	 * @param info4
	 * @param type
	 * @return
	 * @throws RemoteException
	 */
	public boolean updateCardsView(String info1, String info2, String info3, String info4, int type) throws RemoteException;	
	/**
	 * 
	 * @param info
	 * @throws RemoteException
	 */
	public void updateLabel(String info) throws RemoteException;
	/**
	 * 
	 * @param x
	 * @param y
	 * @throws RemoteException
	 */
	public void createTradeInCardsView(int x,int y) throws RemoteException;
	
}
