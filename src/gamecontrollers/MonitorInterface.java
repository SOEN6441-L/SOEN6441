package gamecontrollers;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;


public interface MonitorInterface extends Remote {
	public void updatePhaseView(Observable arg0, int current, int type) throws RemoteException;
	public void updatePhaseView(String info, Color color, int type) throws RemoteException;
	public void updateLogWindow(String newLog) throws RemoteException;
	public void updateDomiView(int num,int[] size, Color[] colors) throws RemoteException;
	public boolean updateCardsView(String info1, String info2, String info3, String info4, int type) throws RemoteException;	
	public void updateLabel(String info) throws RemoteException;
	public void createTradeInCardsView(int x,int y) throws RemoteException;
	
}
