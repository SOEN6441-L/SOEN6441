package gameviews;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import gamecontrollers.MonitorInterface;
import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;


/**
 * PhaseView is the GUI for monitors to see the progress of a game
 * 
 * @see Observer
 */
public class PhaseView implements Observer{
	MonitorInterface server;
	ObserverLabel gameStageLabel4;
	
	/**
	 * Constructor for PhaseView class.
	 * @param server remote server
	 */
	public PhaseView(MonitorInterface server){
		this.server = server;
		gameStageLabel4 = new ObserverLabel();
		gameStageLabel4.server = server;
	}
	/**
	 * Method to get the observer label for the assign countries progress.
	 * @return observer label
	 */
	public ObserverLabel getAssignCountryLable(){
		return gameStageLabel4;
	}


	/**
	 * Method to be called by Observable's notifyObservers method.
	 * @param obs the observable object
	 * @param x an argument passed by the notifyObservers method.
	 */	
	@Override
	public void update(Observable obs, Object x) {
	  int type = (Integer) x;
	  try{	
		if (type == 0){//update for top level phase information. startup and game started
			server.updatePhaseView(((RiskGameModel)obs).getPhaseString(),null,type);
		}
		else if (type == 1){//update detail step information.
			int stage = ((RiskGameModel)obs).getGameStage();
			if (stage>=1000){ //special for assigning countries process
				server.updatePhaseView(obs, 0, type);
				return;
			}
			else if (stage>=230){ //special for assigning countries process
				server.updatePhaseView(String.valueOf(stage), null, type);
				return;
			}
			switch (stage){
				case 0://initial status, no map, no players, when beginning or back from 10
					server.updatePhaseView(obs, 0, type);
					break;
				case 1://ready to load a map
					server.updatePhaseView(String.valueOf(stage), null, type);
					break;
				case 2://load map failed
					server.updatePhaseView(String.valueOf(stage), null, type);
					break;	
				case 3://load map canceled by user
					server.updatePhaseView(String.valueOf(stage), null, type);
					break;						
				case 10://load map successfully, from 1, or back from 20
					server.updatePhaseView(obs, 0, type);
					break;	
				case 11://ready to create players
					server.updatePhaseView(String.valueOf(stage), null, type);
					break;
				case 12://create players canceled by user
					server.updatePhaseView(String.valueOf(stage), null, type);
					break;			
				case 20://create players successfully, from 11, or back from 30
					server.updatePhaseView(obs, 0, type);
					break;	
				case 21://ready to assign countries randomly
					server.updatePhaseView(String.valueOf(stage), null, type);
					break;
				case 30://ready to place initial armies
					server.updatePhaseView(obs, 0, type);
					break;	
				case 31:
					server.updatePhaseView(String.valueOf(stage), null, type);
					break;	
				case 32:
					server.updatePhaseView(String.valueOf(stage), null, type);
					break;					
				case 40://startup phase finished
					server.updatePhaseView(obs, 0, type);
					break;
				case 50://game started, can not go back to previous steps now
					server.updatePhaseView(obs, 0, type);
					break;	
				case 51://reinforcement finished
					server.updatePhaseView(obs, 0, type);
					break;
				case 52://attack finished
					server.updatePhaseView(obs, 0, type);
					break;
				case 53://fortification finished
					server.updatePhaseView(obs, 0, type);
					break;
			}
		}
		else if (type == 2){//update turn info
			server.updatePhaseView(String.valueOf(((RiskGameModel)obs).getTurn()), null, type);
		}
		else if (type == 3){//update player info
			int curPlayer = ((RiskGameModel)obs).getCurPlayer();
			Color color = ((RiskGameModel)obs).getPlayers()[curPlayer].getMyColor();
			server.updatePhaseView(String.valueOf(curPlayer), color, type);
		}
		else if (type == 4){//update player phase info
			server.updatePhaseView(((PlayerModel)obs).getPhaseString(), null, type);
		}
		else if (type == 5){//update reinforcement Status
			int curPlayer = ((PlayerModel)obs).getMyGame().getCurPlayer();
			server.updatePhaseView(obs,curPlayer,type);
		}
		else if (type == 6){//update exchange cards info
			int curPlayer = ((PlayerModel)obs).getMyGame().getCurPlayer();
			server.updatePhaseView(obs,curPlayer,type);
		}
		else if (type == 7){//update reinforce cards info
			server.updatePhaseView(((PlayerModel)obs).getPutArmyStr(), null, type);
		}	
		else if (type == 8){//update attack info
			server.updatePhaseView(((PlayerModel)obs).getAttackInfo(), null, type);
		}
		else if (type == 9){//update attack info
			server.updatePhaseView(((PlayerModel)obs).getAttackInfo(), null, type);
		}
		else if (type == 10){//update attack info
			server.updatePhaseView(((PlayerModel)obs).getAttackStepInfo(), null, type);
		}
	  } catch (Exception e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	  }	
	}
}

/**
 * Class to define a label can act as an observer, 
 * to show the internal progress of an operation.
 * @see Observer
 */
class ObserverLabel implements Observer{

	MonitorInterface server;

	/**
	 * Method to be called by Observable's notifyObservers method.
	 * @param arg0 the observable object
	 * @param arg1 an argument passed by the notifyObservers method.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		try {
			server.updateLabel(String.valueOf(arg1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
}