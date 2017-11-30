package gamecontrollers;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;

import gameviewsremote.DominationViewRemote;
import gameviewsremote.LogWindowRemote;
import gameviewsremote.PhaseViewRemote;
import gameviewsremote.TradeInCardsViewRemote;

/**
 * The monitor interface of risk game
 */
public class RiskGameMonitor implements MonitorInterface{
	private PhaseViewRemote phaseView;
	private DominationViewRemote domiView;
	private LogWindowRemote logWindow;
	private TradeInCardsViewRemote cardsWindow;
	
	/**
	 * Constructor of class
	 */
	public RiskGameMonitor(){
		phaseView = new PhaseViewRemote();
		domiView = new DominationViewRemote();
		logWindow = new LogWindowRemote();
		phaseView.setVisible(true);
	}

	/**
	 * The Server for export location
	 * @param location server location
	 * @throws Exception remote Exceptions
	 */
	public void exportServer(String location) throws Exception {
		Remote obj = UnicastRemoteObject.exportObject(this, 2021);
		Registry r;
		try{
			r = LocateRegistry.createRegistry(2020);
		}catch (ExportException e){
			r= LocateRegistry.getRegistry(2020);
		}
		r.rebind(location, obj);
	}
	
	/**
	 * The main method of the class Risk Monitor
	 * @param args The array to storage the variables
	 * @throws Exception remote error 
	 */	
	public static void main(String[] args) throws Exception {
		//create two views, one model and one controller
		new RiskGameMonitor().exportServer("Monitor");
	}
	/**
	 * Implement the updatePhaseView method
	 * @param arg0 the variable observer from observable class
	 * @param current the number of current game
	 * @param type the game type
	 * @throws RemoteException remote error
	 */
	@Override
	public void updatePhaseView(Observable arg0,int current, int type) throws RemoteException {
		// TODO Auto-generated method stub
		phaseView.update(arg0,current, type);
	}
	
	/**
	 * Implement the updatePhaseView method
	 * @param info the information of phase need to update
	 * @param color the color of the phase
	 * @param type the type number of the phase
	 * @throws RemoteException remote error
	 */
	@Override
	public void updatePhaseView(String info, Color color, int type) throws RemoteException {
		// TODO Auto-generated method stub
		phaseView.update(info,color, type);
	}	
	/**
	 * Implement the updateLogWindow method
	 * @param newLog the log window of the update information
	 * @throws RemoteException  Remote Exceptions
	 */	
	@Override
	public void updateLogWindow(String newLog) throws RemoteException {
		// TODO Auto-generated method stub
		logWindow.update(newLog);
	}
	/**
	 * Implement the updatePhaseView method
	 * @param num the number of the domination view
	 * @param size the size of the domination view
	 * @param colors the color the domination view
	 * @throws RemoteException  Remote Exceptions
	 */	
	@Override
	public void updateDomiView(int num,int[] size, Color[] colors) throws RemoteException {
		// TODO Auto-generated method stub
		domiView.update(num,size,colors);
	}
	/**
	 * Implement the updateCardsView method
	 * @param info1 the information of card 1
	 * @param info2 the information of card 2
	 * @param info3 the information of card 3
	 * @param info4 the information of card 4
	 * @param type the number of type
	 * @return whether it success or not
	 * @throws RemoteException Remote Exceptions
	 */	
	@Override
	public boolean updateCardsView(String info1, String info2, String info3, String info4, int type) throws RemoteException {
		// TODO Auto-generated method stub
		if (cardsWindow!=null){
			return cardsWindow.update(info1, info2, info3, info4, type);
		}
		return false;
	}
	/**
	 * Implement the createTradeInCardsView method
	 * @param x the location x for the phase
	 * @param y the location y for the phase
	 */	
	@Override
	public void createTradeInCardsView(int x, int y) {
		// TODO Auto-generated method stub
		cardsWindow = new TradeInCardsViewRemote();
		cardsWindow.setLocation(phaseView.getLocation().x+x,phaseView.getLocation().y+y);
		cardsWindow.setVisible(true);
	}
	/**
	 * Implement the updateLabel method
	 * @param info the information of the update label
	 * @throws RemoteException Remote Exceptions
	 */	
	@Override
	public void updateLabel(String info) throws RemoteException {
		// TODO Auto-generated method stub
		phaseView.setLabel(info);
	}
}
