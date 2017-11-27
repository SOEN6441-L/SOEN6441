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
	 * @param 
	 * @throws RemoteException remote error
	 */
	@Override
	public void updatePhaseView(Observable arg0,int current, int type) throws RemoteException {
		// TODO Auto-generated method stub
		phaseView.update(arg0,current, type);
	}
	
	/**
	 * Implement the updatePhaseView method
	 * @param 
	 * @throws RemoteException remote error
	 */
	@Override
	public void updatePhaseView(String info, Color color, int type) throws RemoteException {
		// TODO Auto-generated method stub
		phaseView.update(info,color, type);
	}	
	/**
	 * Implement the updatePhaseView method
	 * @param
	 * @throws RemoteException 
	 */	
	@Override
	public void updateLogWindow(String newLog) throws RemoteException {
		// TODO Auto-generated method stub
		logWindow.update(newLog);
	}
	/**
	 * Implement the updatePhaseView method
	 * @param
	 * @throws RemoteException 
	 */	
	@Override
	public void updateDomiView(int num,int[] size, Color[] colors) throws RemoteException {
		// TODO Auto-generated method stub
		domiView.update(num,size,colors);
	}
	/**
	 * Implement the updatePhaseView method
	 * @param
	 * @throws RemoteException 
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
	 * Implement the updatePhaseView method
	 * @param
	 * @throws RemoteException 
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
	 * @param
	 * @throws RemoteException 
	 */	
	@Override
	public void updateLabel(String info) throws RemoteException {
		// TODO Auto-generated method stub
		phaseView.setLabel(info);
	}
}
