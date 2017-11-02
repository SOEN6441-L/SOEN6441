package gameelements;

import java.awt.Color;
import java.util.ArrayList;

import java.util.Observable;
import gamecontroller.putInitialArmyView;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;
import mapmodels.ErrorMsg;
import mapmodels.RiskMapModel;

/**
 * This is the Class RiskGame to represent and control a game
 */
public class RiskGame extends Observable{
	private RiskMapModel gameMap;
    private Player[] players;
	private String phaseString = "";
    private int gameStage; //0-blank 1-with map 2-with players 3-after startup 4-in game

    /**Pre-defined colors array to be assigned to different players.*/
    private static Color[] colors = {Color.RED,Color.BLUE,Color.MAGENTA,new Color(0,142,0),new Color(128,0,64),new Color(134,13,255)};
    /**Pre-defined initial armies according to the rules.*/
    private static int[] initialArmies = {1,40,35,30,25,20};

    private int turn, curPlayer;
    private int initialArmyNum;
    
    public RiskMapModel getGameMap() {
        return gameMap;
    }

    /**
     * The function to clear the game map
     */
    public void clearGameMap() {
        gameMap = null;
    }

    public Player[] getPlayers() {
        return players;
    }
    
    public int getValidPlayers() {
        int total = players.length;
        int result = 0;
    	for (int i=0;i<total;i++){
    		if (players[i].getState()) result++;
    	}
    	return result;
    }    

    /**
     * The function to clear players, used when re-create players.
     */
    public void clearPlayers() {
    	int continentNum = gameMap.getContinents().size();
        for(int i=0;i<continentNum;i++){
        	ArrayList<CountryModel> loopCountries = gameMap.getContinents().get(i).getCountryList();
        	int countryNum = loopCountries.size();
            for(int j=0;j<countryNum;j++){
            	loopCountries.get(j).setOwner(null);
            	loopCountries.get(j).setArmyNumber(0);
            }
        }
        players = null;
    }
    
    public int getInitialArmies() {
    	return initialArmyNum;
    };

    /**
     * The function to reset player's information, used when re-assigning countries to players.
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
        players = new Player[playerNum];
        for (int i=0;i<players.length;i++)
            players[i] = new Player("Player"+String.valueOf(i+1),colors[i%colors.length]);
        //JOptionPane.showMessageDialog(null,"We are assigning all countries to these players.");
        int curPlayerIndex = (int)(Math.random()*players.length);
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
                                curPlayerIndex++;
                                toAssigned--;
                                founded = true;
                                break;
                            }
                            else step++;
                        }
                    }
                }
            }
        }
        this.checkContinentOwner();
        initialArmyNum = initialArmies[Math.min(playerNum,gameMap.getCountryNum())-1];
        for (int i=0;i<players.length;i++){
            if (players[i].getState()){
                players[i].setTotalArmies(players[i].getCountries().size());
                players[i].addArmies(initialArmyNum);
                for (int j=0;j<5;j++){ // initial cards, maybe removed for later build
                    int randomCard = (int)(Math.random()*3);
                    players[i].increaseCard(randomCard);
                }
            }
        }
        setGameStage(20);
    }


    /**
     * The function to call startUp phase's UI
     * @return succeed or not
     */
    public boolean putInitialArmy() {
        putInitialArmyView startupPhase = new putInitialArmyView(this);
        startupPhase.setVisible(true);
        int state = startupPhase.state;
        startupPhase.dispose();
        if (state ==1){
        	setGameStage(30);
            return true;
        }
        else return false;
    }

    /**
     * The function to start the game
     * @return the which turn
     */
    public ErrorMsg startGame() {
        turn = 1;
        curPlayer = -1;
        setGameStage(40);
        setPhaseString("Game Started");
        for (int i=0;i<players.length;i++){
        	if (players[i].winGame(gameMap.getCountryNum())){
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
            if (tempPlayer<curPlayer) turn++;
            curPlayer = tempPlayer;
        }while (!players[curPlayer].getState());
        
        players[curPlayer].calculateArmyNumber(getGameMap().getContinents());
        players[curPlayer].reinforcementPhase(this);
        //players[curPlayer].attackPhase(getGameMap());
        this.checkContinentOwner();
        if (players[curPlayer].winGame(gameMap.getCountryNum())){
            return new ErrorMsg(1,players[curPlayer].getName()+" has win the game!");
        }
        players[curPlayer].fortificationPhase(this);
        return new ErrorMsg(0,null);
    }

    public int getTurn() {
        return turn;
    }


    public void setTurn(int turn) {
        this.turn = turn;
    }


    public int getCurPlayer() {
        return curPlayer;
    }


    public void setCurPlayer(int curPlayer) {
        this.curPlayer = curPlayer;
    }

    /**
     * The function to check if the whole continent is owned by a player
     */
    private void checkContinentOwner(){
        for (ContinentModel loopCountinent:gameMap.getContinents()){
            loopCountinent.checkOwner();
        }
    }

	public int getGameStage() {
		return gameStage;
	}

	public void setGameStage(int gameStage) {
		this.gameStage = gameStage;
		setChanged();
		notifyObservers(1);		
	}

	public String getPhaseString() {
		return phaseString;
	}

	public void setPhaseString(String phaseString) {
		this.phaseString = phaseString;
		setChanged();
		notifyObservers(0);
	}
}
