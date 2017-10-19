package gameelements;

import java.awt.Color;

import javax.swing.JOptionPane;

import gamecontroller.StartupPhaseView;
import mapelements.Continent;
import mapelements.Country;
import mapelements.RiskMap;

/**
 * Class RiskGame
 */
public class RiskGame {
    private RiskMap gameMap;
    private Player[] players;
    private int gameStage; //0-blank 1-with map 2-with players 3-after startup 4-in game
    private Color[] colors;
    private int turn, curPlayer;

    public RiskGame(){
        gameStage = 0;
        colors = new Color[10];
        colors[0] = Color.RED;
        colors[1] = Color.BLUE;
        colors[2] = Color.MAGENTA;
        colors[3] = new Color(0,142,0);
        colors[4] = new Color(128,0,64);
        colors[5] = new Color(134,13,255);
        colors[6] = new Color(247,117,21);
        colors[7] = new Color(255,0,128);
        colors[8] = Color.BLACK;
        colors[9] = new Color(100,100,100);;
    }


    public RiskMap getGameMap() {
        return gameMap;
    }

    public void clearGameMap() {
        gameMap = null;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void clearPlayers() {
        for(int i=0;i<players.length;i++){
            for(Country loopCountry:players[i].getCountries()){
                loopCountry.belongToPlayer = null;
                loopCountry.armyNumber = 0;
            }
            players[i].removeAllCountrie();
        }
        players = null;
    }

    public void resetPlayersInfo() {
        for (int i=0;i<players.length;i++){
            if (players[i].getCountries().size()!=0){
                players[i].setTotalArmies(players[i].getCountries().size());
                players[i].setInitialArmies(players.length);
                for (Country loopCountry:players[i].getCountries()){
                    loopCountry.armyNumber = 1;
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

    public boolean loadMapFile(String inputFileName){
        RiskMap existingMap = new RiskMap();
        if (existingMap.loadMapFile(inputFileName,2)){
            gameMap = null;
            gameMap = existingMap;
            gameMap.modified = false;
            gameStage = 1;
            return true;
        }
        else return false;
    };

    public void createPlayers(String inputWord){
        players = new Player[Integer.parseInt(inputWord)];
        for (int i=0;i<players.length;i++)
            players[i] = new Player("Player"+String.valueOf(i+1),colors[i%colors.length]);
        //JOptionPane.showMessageDialog(null,"We are assigning all countries to these players.");
        int curPlayerIndex = (int)(Math.random()*players.length);
        int toAssigned = gameMap.countryNum;
        while (toAssigned>0){
            int randomIndex = (int)(Math.random()*toAssigned);
            int step=0;
            boolean founded=false;
            for (Continent loopContinent:gameMap.continents){
                if (!founded){
                    for (Country loopCountry:gameMap.countries.get(loopContinent.continentID)){
                        if (loopCountry.belongToPlayer==null){
                            if (step==randomIndex){
                                players[curPlayerIndex%players.length].addCountrie(loopCountry);
                                loopCountry.belongToPlayer = players[curPlayerIndex%players.length];
                                loopCountry.armyNumber = 1;
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
        for (int i=0;i<players.length;i++){
            if (players[i].getCountries().size()!=0){
                players[i].setState(1);
                players[i].setTotalArmies(players[i].getCountries().size());
                players[i].setInitialArmies(players.length);
                for (int j=0;j<5;j++){ // initial cards, maybe removed for later build
                    int randomCard = (int)(Math.random()*3);
                    players[i].addCard(randomCard);
                }
            }
        }
        gameStage = 2;
    }


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

    public int startGame() {
        turn = 1;
        curPlayer = 0;
        int result = playerTurn();
        if (result == 0) {
            gameStage =4;
        }
        return result;
    }

    public int playerTurn(){ //0-succeed 1-winGame 2-user cancel in reinforcement
        this.checkContinentOwner();
        while (players[curPlayer].getState()!=1){
            int tempPlayer=(curPlayer+1)%players.length;
            if (tempPlayer<curPlayer) turn++;
            curPlayer = tempPlayer;
        }
        if (players[curPlayer].winGame(gameMap.countryNum)){
            JOptionPane.showMessageDialog(null,players[curPlayer].getName()+" has win the game!");
            return 1;
        }
        players[curPlayer].calculateArmyNumber(getGameMap());
        if (!players[curPlayer].reinforcementPhase(this)) return 2;
        players[curPlayer].fortificationPhase();

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

    private void checkContinentOwner(){
        for (Continent loopCountinent:gameMap.continents){
            loopCountinent.checkOwner(gameMap);
        }
    }
}
