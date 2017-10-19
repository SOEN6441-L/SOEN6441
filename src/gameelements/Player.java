package gameelements;
import mapelements.*;

import java.awt.Color;
import java.util.ArrayList;

import gamecontroller.ReinforcePhaseView;
import gamecontroller.StartupPhaseView;

/**
 *   This class is to construct player;
 *   This class is to calculate how many armies are given according to numbers of cards.
 *   @Author Shirley / XUEYING LI
 *   @CreateTime 07.OCT.2017
 *   @param Name Name of player
 *   @param cards Store numbers of three cards, infantry, cavalry,artillery respectively
 *   @param changeCardTimes Store times of cards exchange
 *   @param countries Store countries of the player
 *   @param totalArmies Total armies of player
 *   @param initialArmies Initial armies of player
 *   @param myColor colors of a player
 *   @
 */
public class Player {
	private String name;
	private int[] cards;
	private int changeCardTimes;
	private ArrayList <Country> countries;
    private int totalArmies;
    private int initialArmies;
    private Color myColor;
    
    private int baseArmies;
    private ArrayList<Continent> continents;
    private int totalReinforcement;
    
    private int state;//0-not in game, 1- in game(have countries)
    /**
     *   This method is a class constructor.
     *
     */
    public Player(String newName,Color color){
        name = newName;
        cards = new int[3];
        for (int i=0;i<3;i++) cards[i] = 0;
        countries = new ArrayList<Country>();
        myColor = color;
        baseArmies = 0;
        totalReinforcement = 0;
        continents = new ArrayList<Continent>();
        changeCardTimes=0;
        setState(0);
     }

    // get name of player
    public String getName() {
        return name;
    }

    public void getCards(int[] myCards) {
        for(int i=0;i<3;i++) myCards[i] = cards[i];
    }
    
    public void setCards(int[] myCards) {
        for(int i=0;i<3;i++) cards[i] = myCards[i];
    }
    
    public String getCardsString() {
        return String.valueOf(cards[0])+"+"+String.valueOf(cards[1])+"+"+String.valueOf(cards[2])+"="+String.valueOf(cards[0]+cards[1]+cards[2]);
    }    

    //get Change Card Times
    public int getChangeCardTimes() {
        return changeCardTimes;
    }

    //get countries of player
    public ArrayList<Country> getCountries() {
        return countries;
    }
    
    //add a countries to player
    public void addCountrie(Country newCountry) {
    	this.countries.add(newCountry);
    }   
    
    //remove a countries from player
    public void removeCountrie(Country newCountry) {
    	this.countries.remove(newCountry);
    }   
    
    //remove all countries from player
    public void removeAllCountrie() {
    	this.countries.clear();
    }   

    //add cards
    public void addCard(int type){
        this.cards[type]++;
    }

    /**
     *  This method is to judge if player must exchange cards with armies.
     *
     *  @return true if player is forced to exchange cards
     */
    public boolean ifForceExchange(){
        if ((cards[0]+cards[1]+cards[2]) >= 5)
            return true;
        else
            return false;
    }

    /**
     *   To calculate how many armies after exchanging and change number of armies of player
     *
     */
    public int CalExchangeArmies(){
    	return 5*(this.changeCardTimes+1);
    }

	public Color getMyColor() {
		return myColor;
	}



	public int getTotalArmies() {
		return totalArmies;
	}

	public void setTotalArmies(int totalArmies) {
		this.totalArmies = totalArmies;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public boolean winGame(int countryNum){
		if (this.countries.size()==countryNum)
			return true;
		else return false;
	}
	
	public boolean reinforcementPhase(RiskGame myGame){
		calculateArmyNumber(myGame.getGameMap());
		ReinforcePhaseView reinforcementPhase = new ReinforcePhaseView(this, myGame, totalReinforcement);
		reinforcementPhase.setVisible(true);
		int state = reinforcementPhase.state;
		reinforcementPhase.dispose();
		return (state == 1);
	}
	
	public boolean fortificationPhase(){
		return true;
	}

	public int getInitialArmies() {
		return initialArmies;
	}

	public void setInitialArmies(int initialArmies) {
		this.initialArmies = initialArmies;
		this.totalArmies += initialArmies;
	}	
	
    /**
     * The function to calculate how many armies player get this turn
     * @return the number of armies
     */
    public void calculateArmyNumber(RiskMap myMap) {
    	this.baseArmies = Math.floorDiv(this.countries.size(), 3);
    	this.totalReinforcement = baseArmies;
    	continents.clear();
    	for (Continent loopContinent:myMap.continents){
        	if (loopContinent.owner!=null&&loopContinent.owner.getName().equals(this.name)){
        		totalReinforcement+=loopContinent.controlNum;
        		continents.add(loopContinent);
        	}
        }
    	if (ifForceExchange()) totalReinforcement+=this.CalExchangeArmies();
    }
    
    public boolean canExchange(int[] myCards) {
    	return (Math.max(myCards[0], Math.max(myCards[1], myCards[2]))>=3
    			||Math.min(myCards[0], Math.min(myCards[1], myCards[2]))>=1);
    }

    public void increaseChangeCardTimes() {
        changeCardTimes++;
    }
    
}

