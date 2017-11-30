package gamemodels;


import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingWorker;

import basicclasses.Log;
import gamecontrollers.MonitorInterface;
import gameviews.PhaseView;
import gameviews.TradeInCardsView;
import gameviews.putInitialArmyView;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;
import mapmodels.ErrorMsg;
import mapmodels.RiskMapModel;

/**
 * This is the Class RiskGame to represent and control a game
 */
public class RiskGameModel extends Observable implements Serializable{

	private static final long serialVersionUID = 1L;
	private RiskMapModel gameMap;
    private PlayerModel[] players;
	private String phaseString = "";
    private int gameStage; //0-blank 1-with map 2-with players 3-after startup 4-in game

    
    /**Pre-defined colors array to be assigned to different players.*/
    private static Color[] colors = {Color.RED,Color.BLUE,Color.MAGENTA,new Color(0,142,0),new Color(128,0,64),new Color(134,13,255)};
    /**Pre-defined initial armies according to the rules.*/
    private static int[] initialArmies = {1,40,35,30,25,20};

    private int turn, curPlayer;
	private int changeCardTimes;
    private int initialArmyNum;
	
    private transient Observer AssignCountryLabel;
	private transient ObservableNodes localCountries;
	private transient PhaseView phaseView;
	private transient MonitorInterface server;
	public Log myLog = new Log();
 	
	/**
	 * Method to get the map object of the game
	 * @return map object
	 */
    public RiskMapModel getGameMap() {
        return gameMap;
    }
    
    /**
     * Method to set the map object of the game
     * @param gameMap map object
     */
    public void setGameMap(RiskMapModel gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * The function to clear the game map
     */
    public void clearGameMap() {
        gameMap = null;
    }
    /**
     * The getter of Players, used to return players
     * @return players players info
     */
    public PlayerModel[] getPlayers() {
        return players;
    }
    /**
     * The getter of ValidPlayers, used to return the result of valid players
     * @return result the number of valid players
     */   
    public int getValidPlayers() {
        int total = players.length;
        int result = 0;
    	for (int i=0;i<total;i++){
    		if (players[i].getState()) result++;
    	}
    	return result;
    }    

    /**
     * The function to clear players, used when re-assign countries.
     */
    public void clearPlayers() {
    	int continentNum = gameMap.getContinents().size();
        //clear countries' info
    	for(int i=0;i<continentNum;i++){
        	ArrayList<CountryModel> loopCountries = gameMap.getContinents().get(i).getCountryList();
        	int countryNum = loopCountries.size();
            for(int j=0;j<countryNum;j++){
            	loopCountries.get(j).setOwner(null);
            	loopCountries.get(j).setArmyNumber(0);
            }
            this.checkContinentOwner();
        }
    	//clear players' info
        for (int i=0;i<players.length;i++){
        	players[i].removeAllCountrie();
        	players[i].setTotalArmies(0);
        	players[i].removeCards();
        }
        this.changeDominationView();
    }
    
    /**
     * The function to delete players, used when re-create players.
     */
    public void deletePlayers() {
        players = null;
        this.changeDominationView();
    }    
    /**
     * The getter of InitialArmies, used to return the number of initial armies
     * @return initialArmyNum the number of initial armies
     */  
    public int getInitialArmies() {
    	return initialArmyNum;
    };

    /**
     * The function to reset player's information, used when re-puttinging initial armies to players.
     */
    public void resetPlayersInfo() {
        for (int i=0;i<players.length;i++){
            if (players[i].getCountries().size()!=0){
                players[i].setTotalArmies(players[i].getCountries().size());
                players[i].addArmies(initialArmyNum);
                for (CountryModel loopCountry:players[i].getCountries()){
                    loopCountry.setArmyNumber(1);
                }
            }
        }
    }

    /**
     * The function to load the map file
     * @param inputFileName The name of the map file
     * @return succeed or failed with error message
     */
    public ErrorMsg loadMapFile(String inputFileName){
        RiskMapModel existingMap = new RiskMapModel();
        ErrorMsg errorMsg;
        if ((errorMsg = existingMap.loadMapFile(inputFileName)).isResult()){
        	if ((errorMsg = existingMap.checkErrors()).isResult()){
        		existingMap.checkWarnings();
        		gameMap = null;
        		gameMap = existingMap;
        		gameMap.setModified(false);
        		setGameStage(10);
        		return new ErrorMsg(0,null);
        	}
        	else {
        		return new ErrorMsg(errorMsg.getResult(),errorMsg.getMsg());
        	}
        }	
        else{
        	return new ErrorMsg(errorMsg.getResult(),errorMsg.getMsg());
        }	
    };

    /**
     * The function to create numbers of players
     * @param playerNum The number of players want to create
     */
    public void createPlayers(int playerNum){
        players = new PlayerModel[playerNum];
        for (int i=0;i<players.length;i++){
            players[i] = new PlayerModel("Player"+String.valueOf(i+1),colors[i%colors.length],this);
        }    
    }    

    /**
     * The function to assign countries to players randomly.
     */
    public void assignCountries(){
		SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() {
				setGameStage(21);
		        int curPlayerIndex = (int)(Math.random()*players.length);
				setGameStage(230+curPlayerIndex);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
		        int toAssigned = gameMap.getCountryNum();
				while (toAssigned>0){
					int randomIndex = (int)(Math.random()*toAssigned);
					int step=0;
					boolean founded=false;
					for (ContinentModel loopContinent:gameMap.getContinents()){
						if (!founded){
							for (CountryModel loopCountry:loopContinent.getCountryList()){
								if (loopCountry.getOwner()==null){
									if (step==randomIndex){
										players[curPlayerIndex%players.length].addCountry(loopCountry);
										loopCountry.setOwner(players[curPlayerIndex%players.length]);
										loopCountry.setArmyNumber(1);
										setGameStage(loopCountry.getCountryId()*1000+(curPlayerIndex%players.length));
								        changeDominationView();
								        myLog.setLogStr("    Assign "+loopCountry.getShowName()+" to "+players[curPlayerIndex%players.length].getName()+"\n");
										publish(1);
										curPlayerIndex++;
										toAssigned--;
										founded = true;
										break;
									}
									else step++;
								}
							}
						}
						else break;
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				checkContinentOwner();
				initialArmyNum = initialArmies[Math.min(players.length,gameMap.getCountryNum())-1];
				for (int i=0;i<players.length;i++){
					if (players[i].getState()){
						players[i].setTotalArmies(players[i].getCountries().size());
						players[i].addArmies(initialArmyNum);
						for (int j=0;j<4;j++){ // initial cards, maybe removed for later build
							int randomCard = (int)(Math.random()*3);
							players[i].increaseCard(randomCard);
						}
					}
				}
				return null;
			}
			@Override
			protected void process(List<Integer> chunks) {
			}
			@Override
			protected void done() {
				setGameStage(30);
			}
		};
		worker.execute();
    }    
    
    
    /**
     * The function to assign countries to players randomly, only for test.
     */
    public void assignCountriesManual(){
		setGameStage(21);
        int curPlayerIndex = (int)(Math.random()*players.length);
		setGameStage(230+curPlayerIndex);
        int toAssigned = gameMap.getCountryNum();
		while (toAssigned>0){
			int randomIndex = (int)(Math.random()*toAssigned);
			int step=0;
			boolean founded=false;
			for (ContinentModel loopContinent:gameMap.getContinents()){
				if (!founded){
					for (CountryModel loopCountry:loopContinent.getCountryList()){
						if (loopCountry.getOwner()==null){
							if (step==randomIndex){
								players[curPlayerIndex%players.length].addCountry(loopCountry);
								loopCountry.setOwner(players[curPlayerIndex%players.length]);
								loopCountry.setArmyNumber(1);
								setGameStage(loopCountry.getCountryId()*1000+(curPlayerIndex%players.length));
						        changeDominationView();
								curPlayerIndex++;
								toAssigned--;
								founded = true;
								break;
							}
							else step++;
						}
					}
				}
				else break;
			}
		}
		checkContinentOwner();
		initialArmyNum = initialArmies[Math.min(players.length,gameMap.getCountryNum())-1];
		for (int i=0;i<players.length;i++){
			if (players[i].getState()){
				players[i].setTotalArmies(players[i].getCountries().size());
				players[i].addArmies(initialArmyNum);
				for (int j=0;j<4;j++){ // initial cards, maybe removed for later build
					int randomCard = (int)(Math.random()*3);
					players[i].increaseCard(randomCard);
				}
			}
		}
    }    
    
    /**
     * The function to call putInitialArm phase's UI
     * @param mode 0-normal 1-silent
     * @return succeed or not
     */
    public boolean putInitialArmy(int mode) {
    	if (localCountries!=null){
    		localCountries.deleteObservers();
    		localCountries = null;
    	}
        localCountries = new ObservableNodes();
        localCountries.initDimensionX(getPlayers().length);
        for (int i=0;i<getPlayers().length;i++){
        	localCountries.initDimensionY(i, getPlayers()[i].getCountries().size());
        	int y=0;
        	for(CountryModel loopCountry:getPlayers()[i].getCountries()){
        		localCountries.initValue(i,y++,loopCountry.getShowName(),loopCountry.getArmyNumber());
        	}
        }
        localCountries.addObserver(AssignCountryLabel);
        putInitialArmyView startupPhase = new putInitialArmyView(this);
        if (mode==0) startupPhase.setVisible(true);
        else {
        	startupPhase.byComputerManul();
        	startupPhase.confirmInput();
        }
        int state = startupPhase.state;
        startupPhase.dispose();
        if (state ==1){
    		for(int i=0;i<players.length;i++){
    			if (localCountries.getNodes()[i]!=null){
    				for (int j=0;j<localCountries.getNodes()[i].length;j++){
    					gameMap.findCountry(localCountries.getNodes()[i][j].getName()).setArmyNumber(localCountries.getNodes()[i][j].getNumber());
    				}
    			}
    		}        	
    		localCountries.deleteObservers();
        	localCountries = null;
        	setGameStage(40);
	        myLog.setLogStr("\nPut initial armies succeed\n");
        }
        return (state==1);
    }

    /**
     * The function to start the game
     * @return the which turn
     */
    public ErrorMsg startGame() {
        setGameStage(50);
        changeCardTimes = 0;
        turn = 0;
        curPlayer = -1;
        setPhaseString("Game Started");
        increaseTurn();
        for (int i=0;i<players.length;i++){
        	if (players[i].winGame(gameMap.getCountryNum())){
        		setGameStage(54);
        		setPhaseString("Game Over");
        		myLog.setLogStr("\n"+players[i].getDiscription()+" has win the game!\n");
        		return new ErrorMsg(1,players[i].getName()+" has win the game!");

        	}
        }
        return playerTurn();
    }


    /**
     * The function to check the state of the current player's turn
     * @return which state
     */
    public ErrorMsg playerTurn(){ //0-succeed 1-winGame
        do {
            int tempPlayer=(curPlayer+1)%players.length;
            if (tempPlayer<curPlayer) increaseTurn();
            setCurPlayer(tempPlayer);
        }while (!players[curPlayer].getState());
        
        players[curPlayer].addObserver(phaseView);
        
        if (players[curPlayer].strategy.getClass().getName().indexOf("Cheater")==-1){
        	int x = 380+(curPlayer/2)*360;
        	int y = 36+(1-curPlayer%2)*252;  
        	TradeInCardsView cardView = new TradeInCardsView(server,x,y);

        	players[curPlayer].addObserver(cardView);
        }	
        
        //cardView.setVisible(true);
        ErrorMsg errorMsg = players[curPlayer].playTurn();
        checkContinentOwner();
        players[curPlayer].deleteObservers();
        return errorMsg;
    }
    /**
     * The getter of turn, used to return turn count
     * @return turn the count of turn
     */
    public int getTurn() {
        return turn;
    }
    /**
     * Method to increase the count of turn
     */
    public void increaseTurn() {
        turn++;
		setChanged();
		notifyObservers(2);
    }
    /**
     * The getter of CurPlayer, used to return the current player
     * @return curPlayer the current player
     */
    public int getCurPlayer() {
        return curPlayer;
    }
    /**
     * The setter of CurPlayer, used to set the current player
     * @param curPlayer current player
     */
    public void setCurPlayer(int curPlayer) {
        this.curPlayer = curPlayer;
        setChanged();
        notifyObservers(3);
    }

    /**
     * The function to check if the whole continent is owned by a player
     */
    public void checkContinentOwner(){
        for (ContinentModel loopCountinent:gameMap.getContinents()){
            loopCountinent.checkOwner();
        }
    }
	/**
	 * The getter of GameStage, used to return the stage of name
	 * @return gameStage the stage of game
	 */
	public int getGameStage() {
		return gameStage;
	}
	/**
	 * The setter of GFameStage, used to set the stage of game.
	 * @param gameStage current gameStage
	 */
	public void setGameStage(int gameStage) {
		this.gameStage = gameStage;
		setChanged();
		notifyObservers(1);		
	}
	/**
	 * The getter of PhaseString, used to return phase
	 * @return phaseString phase string
	 */
	public String getPhaseString() {
		return phaseString;
	}
	/**
	 * The setter of PhaseString, used to set phase string
	 * @param phaseString current phaseString
	 */
	public void setPhaseString(String phaseString) {
		this.phaseString = phaseString;
		setChanged();
		notifyObservers(0);
	}
	/**
	 * The getter of LocalCountries, used to return local countries
	 * @return localCountries local countries
	 */	
	public ObservableNodes getLocalCountries(){
		return localCountries;
	}
	
	/**
	 * Set observer label
	 * @param observerLabel the observer Label object
	 */
	public void setObserverLabel(Observer observerLabel) {
		this.AssignCountryLabel = observerLabel;
	}
	/**
	 * Set observer label
	 * @param phaseView the phase view object
	 * @param server MonitorInterface object
	 */
	public void setPhaseView(PhaseView phaseView, MonitorInterface server) {
		this.phaseView = phaseView;
		this.server = server;
	}
	
    /**
     * Method to get exchange cards times.
     * @return exchange cards times
     */
    public int getChangeCardTimes() {
        return changeCardTimes;
    }   
    
    /**
     *   The function calculate how many armies can exchange this time, and increase the change card times.
     *   @return numbers of exchanged armies
     */
    public int CalExchangeArmies(){
    	return 5*(++changeCardTimes);
    }  
    /**
     * changeDominationView
     */
    public void changeDominationView() {
    	setChanged();
    	notifyObservers(111);
    }   
    /**
     * Method to connect to log window
     * @param logWindow log observer 
     * @param mode 0-normal, 1-reconnect
     */
    public void addLog(Observer logWindow, int mode){
    	myLog.addObserver(logWindow);
    	if (mode==0)
    		myLog.setLogStr("\n*********************************    New Game Started   ********************************* \n");
    	else 
    		myLog.setLogStr("\n****************************************************************************************\n");
    }   	
}

