package gameelements;
import mapelements.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;

import gamecontroller.FortificationPhaseView;
import gamecontroller.ReinforcePhaseView;
import gamecontroller.StartupPhaseView;

/**
 *   This is class for defining player.
 *   A player belongs to a RiskGame object, has amount of armies, can own several countries.
 */
public class Player extends Observable {
	private String name;
	private int[] cards;
	private int changeCardTimes;
	private ArrayList <Country> countries;
    private int totalArmies;
    private int initialArmies;
    private Color myColor;

    
    private String reinforcementStr;
    private int totalReinforcement;

    /**
     * Constructor for Player class.
     * @param newName the name of the new player
     * @param color the player's color
     */
    public Player(String newName,Color color){
        name = newName;
        cards = new int[3];
        for (int i=0;i<3;i++) cards[i] = 0;
        countries = new ArrayList<Country>();
        myColor = color;
        totalArmies = 0;
        totalReinforcement = 0;
        changeCardTimes=0;
    }

    /**
     * Method to get the name of player.
     * @return player's name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Method to get a copy of cards.
     * @param myCards integer array to store the copy of cards
     */
    public int[] getCards() {
       return cards;
    }
    
    /**
     * Method to get a description of cards.
     * @return description of cards
     */
    public String getCardsString() {
        return String.valueOf(cards[0])+"+"+String.valueOf(cards[1])+"+"+String.valueOf(cards[2])+"="+String.valueOf(cards[0]+cards[1]+cards[2]);
    }
    
    /**
     * Method to set cards from a copy. The copy may have been modified by other method.
     * @param myCards copy of cards
     */
    public void setCards(int[] myCards) {
        for(int i=0;i<3;i++) cards[i] = myCards[i];
        setChanged();
        notifyObservers();
    }
    
    /**
     * Method to add one type of card.
     * @param type card type
     */
    public void increaseCard(int type){
        this.cards[type]++;
    }
    
    /**
     * Method to get exchange cards times.
     * @return exchange cards times
     */
    public int getChangeCardTimes() {
        return changeCardTimes;
    }    
 
    /**
     * Method to increase exchange cards times by 1.
     */
    public void increaseChangeCardTimes() {
        changeCardTimes++;
    }
   
    /**
     * Method to get country list of player.
     * @return country list of the player
     */
    public ArrayList<Country> getCountries() {
        return countries;
    }
    
    /**
     * Method to add a country to player.
     * @param newCountry country object adding to this player
     */
    public void addCountry(Country newCountry) {
    	this.countries.add(newCountry);
    }
    
    /**
     * Method to remove a country from player.
     * @param country country object to be removed
     */
    public void removeCountrie(Country country) {
    	this.countries.remove(country);
    }

    /**
     * Method to remove all countries from player.
     */
    public void removeAllCountrie() {
    	this.countries.clear();
    }
    
    /**
     * Method to get display color of player.
     * @return player's color
     */    
    public Color getMyColor() {
		return myColor;
	}
    
    /**
     * Method to check if player is still in game.
     * @return player's in game state
     */
	public boolean getState() {
		return countries.size()>0;
	}

    /**
     * Method to get armies number of this player.
     * @return total armies number of player
     */
	public int getTotalArmies() {
		return totalArmies;
	}

	/**
     * Method to set armies number of this player.
     * @param totalArmies armies number of player
     */
	public void setTotalArmies(int totalArmies) {
		this.totalArmies = totalArmies;
	}
	
	public void addArmies(int initialArmies) {
		this.totalArmies += initialArmies;
	}	    
	
    /**
     *  This function is to judge if player must exchange cards with armies.
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
     *   The function calculate how many armies after exchanging and change number of armies of player
     *   @return numbers of exchanged armies
     */
    public int CalExchangeArmies(){
    	return 5*(this.changeCardTimes+1);
    }
	

    /**
     * The function to judge if player can exchange cards
     * @param myCards array of my cards
     * @return true if can exchange
     */
    public boolean canExchange(int[] myCards) {
    	return (Math.max(myCards[0], Math.max(myCards[1], myCards[2]))>=3
    			||Math.min(myCards[0], Math.min(myCards[1], myCards[2]))>=1);
    }	
    
    


    /**
     * The function to calculate how many armies player get this turn
     * @param continents the continent list to be checked
     */
    public void calculateArmyNumber(ArrayList<Continent> continents) {
    	this.totalReinforcement = Math.floorDiv(this.countries.size(), 3);;
    	for (Continent loopContinent:continents){
        	if (loopContinent.getOwner()!=null&&loopContinent.getOwner().getName().equals(this.name)){
        		totalReinforcement+=loopContinent.getControlNum();
        	}
        }
    	if (ifForceExchange()) totalReinforcement+=this.CalExchangeArmies();
    }	
	
	/**
     * get the number of reinforcement armies of player
     * @return name
     */
    public int getTotalReinforcement()
    {
        return totalReinforcement;
    }    

    /**
     * The function to judge if player win
     * @param countryNum number of countries
     * @return true if player win
     */

	public boolean winGame(int countryNum){
		if (this.countries.size()==countryNum)
			return true;
		else return false;
	}

	/**
     * The function to judge if complete reinforcement phase
     * @param myGame the current RiskGame object
     * @return if complete or not
     *
	 */
	public boolean reinforcementPhase(RiskGame myGame){
		calculateArmyNumber(myGame.getGameMap().getContinents());
		ReinforcePhaseView reinforcementPhase = new ReinforcePhaseView(this, myGame, totalReinforcement);
		reinforcementPhase.setVisible(true);
		int state = reinforcementPhase.state;
		reinforcementPhase.dispose();
		return (state == 1);
	}

    /**
     * The function to judge if complete fortification phase
     * @param myGame the current RiskGame object
     */
    public void fortificationPhase(RiskGame myGame){
        FortificationPhaseView fortiPhase = new FortificationPhaseView(this, myGame);
        fortiPhase.setVisible(true);
        int state = fortiPhase.state;
        fortiPhase.dispose();
    }
}

