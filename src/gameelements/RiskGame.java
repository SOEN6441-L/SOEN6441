package gameelements;

import java.awt.Color;

import javax.swing.JOptionPane;

import gamecontroller.StartupPhaseView;
import mapelements.Continent;
import mapelements.Country;
import mapelements.ErrorMsg;
import mapelements.RiskMap;

/**
 * Class RiskGame to set up the game
 */
public class RiskGame {
    private static Color[] colors = {Color.RED,Color.BLUE,Color.MAGENTA,new Color(0,142,0),new Color(128,0,64),new Color(134,13,255)};
    private static int[] initialArmies = {1,40,35,30,25,20};
	private RiskMap gameMap;
    private Player[] players;
    private int gameStage = 0; //0-blank 1-with map 2-with players 3-after startup 4-in game
    private int turn, curPlayer;
    private int initialArmy;

    public RiskMap getGameMap() {
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

    /**
     * The function to clear current players
     */
    public void clearPlayers() {
        for(int i=0;i<players.length;i++){
            for(Country loopCountry:players[i].getCountries()){
                loopCountry.setOwner(null);
                loopCountry.setArmyNumber(0);
            }
            players[i].removeAllCountrie();
        }
        players = null;
    }
    
    public int getInitialArmies() {
    	return initialArmy;
    };

    /**
     * The function to reset player's information
     */
    public void resetPlayersInfo() {
        for (int i=0;i<players.length;i++){
            if (players[i].getCountries().size()!=0){
                players[i].setTotalArmies(players[i].getCountries().size());
                players[i].addArmies(initialArmy);
                for (Country loopCountry:players[i].getCountries()){
                    loopCountry.setArmyNumber(1);
                }
            }
        }
    }

    public int getGameStage() {
        return gameStage;
    }

    public void setGameStage(int gameStage) {
        this.gameStage = gameStage;
    }

    /**
     * The function to load the map file
     * @param inputFileName The name of the map file
     * @return succeed or not
     */
    public boolean loadMapFile(String inputFileName){
        RiskMap existingMap = new RiskMap();
        ErrorMsg errorMsg;
        if ((errorMsg = existingMap.loadMapFile(inputFileName)).isResult()){
        	if ((errorMsg = existingMap.checkErrors()).isResult()){
        		existingMap.checkWarnings();
        		gameMap = null;
        		gameMap = existingMap;
        		gameMap.setModified(false);
        		gameStage = 1;
        		return true;
        	}
        	else {
        		JOptionPane.showMessageDialog(null, errorMsg.getMsg());
        		return false;
        	}
        }	
        else{
        	JOptionPane.showMessageDialog(null, errorMsg.getMsg());
        	return false;
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
            for (Continent loopContinent:gameMap.getContinents()){
                if (!founded){
                    for (Country loopCountry:loopContinent.getCountryList()){
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
        initialArmy = initialArmies[Math.min(playerNum,gameMap.getCountryNum())-1];
        for (int i=0;i<players.length;i++){
            if (players[i].getState()){
                players[i].setTotalArmies(players[i].getCountries().size());
                players[i].addArmies(initialArmy);
                for (int j=0;j<5;j++){ // initial cards, maybe removed for later build
                    int randomCard = (int)(Math.random()*3);
                    players[i].increaseCard(randomCard);
                }
            }
        }
        gameStage = 2;
    }


    /**
     * The function to call startUp phase's UI
     * @return succeed or not
     */
    public boolean startupPhase() {
        StartupPhaseView startupPhase = new StartupPhaseView(this);
        startupPhase.setVisible(true);
        int state = startupPhase.state;
        startupPhase.dispose();
        if (state ==1){
            gameStage = 3;
            return true;
        }
        else return false;
    }

    /**
     * The function to start the game
     * @return the which turn
     */
    public int startGame() {
        turn = 1;
        curPlayer = 0;
        this.checkContinentOwner();
        for (int i=0;i<players.length;i++){
        	if (players[i].winGame(gameMap.getCountryNum())){
        		JOptionPane.showMessageDialog(null,players[i].getName()+" has win the game!");
        		return 1;
        	}
        }	
        int result = playerTurn();
        if (result == 0) {
            gameStage =4;
        }
        return result;
    }


    /**
     * The function to check the state of the current player's turn
     * @return which state
     */
    public int playerTurn(){ //0-succeed 1-winGame 2-user cancel in reinforcement
        while (!players[curPlayer].getState()){
            int tempPlayer=(curPlayer+1)%players.length;
            if (tempPlayer<curPlayer) turn++;
            curPlayer = tempPlayer;
        }
        players[curPlayer].calculateArmyNumber(getGameMap().getContinents());
        if (!players[curPlayer].reinforcementPhase(this)) return 2;
        //players[curPlayer].attackPhase(getGameMap());
        this.checkContinentOwner();
        if (players[curPlayer].winGame(gameMap.getCountryNum())){
            JOptionPane.showMessageDialog(null,players[curPlayer].getName()+" has win the game!");
            return 1;
        }
        players[curPlayer].fortificationPhase(this);

        int tempPlayer=(curPlayer+1)%players.length;
        if (tempPlayer<curPlayer) turn++;
        curPlayer = tempPlayer;
        return 0;
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
        for (Continent loopCountinent:gameMap.getContinents()){
            loopCountinent.checkOwner();
        }
    }
}
