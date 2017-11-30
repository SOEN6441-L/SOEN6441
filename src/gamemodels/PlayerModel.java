package gamemodels;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import mapmodels.*;

/**
 *   This is class for defining player.
 *   A player belongs to a RiskGame object, has amount of armies, can own several countries.
 */
public class PlayerModel extends Observable  implements Serializable {

	private static final long serialVersionUID = 5L;
	private String name;
    private Color myColor;
	private int[] cards;
	private ArrayList <CountryModel> countries;

	private int totalArmies;
    private int totalReinforcement;

    private RiskGameModel myGame;

    private String phaseString="", exchangeStatus = "";
    private String reinforcementStr="", baseReinforceStr="", exchangeCardStr="", putArmyStr="";
    private String attackInfo="", attackStepInfo="";
    public boolean conquered;
    
    public Strategy strategy;


    /**
     * Constructor for Player class.
     * @param newName the name of the new player
     * @param color the player's color
     * @param game the current game object
     */
    public PlayerModel(String newName,Color color, RiskGameModel game){
        name = newName;
        cards = new int[]{0,0,0};
        countries = new ArrayList<CountryModel>();
        myColor = color;
        myGame = game;
        totalArmies = 0;
        totalReinforcement = 0;
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
     * Method to get the description of player.
     * @return player's name
     */
    public String getDiscription()
    {
		String strategy = this.strategy.getClass().getName();
		strategy = strategy.substring(strategy.indexOf('.')+1);
        return name+" ("+strategy+")";
    }    
    
    /**
     * Method to get a copy of cards.
     * @param myCards integer array to store the copy of cards
     */
    public void copyCards(int[] myCards) {
        for(int i=0;i<3;i++) myCards[i] = cards[i];
    }
    
    /**
     * Method to get cards.
     * @return cards
     */
    public int[] getCards() {
        return cards;
    }
    
    /**
     * Method to set cards from a copy. The copy may have been modified by other method.
     * @param myCards copy of cards
     */
    public void setCards(int[] myCards) {
        for(int i=0;i<3;i++) cards[i] = myCards[i];
        //setChanged();
        //notifyObservers();
    }
    
    /**
     * Method to add one type of card.
     * @param type card type
     */
    public void increaseCard(int type){
        this.cards[type]++;
    }  
    
    /**
     * Method to add other player's cards to this player.
     * @param source card type
     */
    public void addCards(int[] source){
    	for (int i=0;i<3;i++){
    		cards[i]+=source[i];
    		source[i]=0;
    	}       
    }  
    
    /**
     * Method to remove all the cards of this user.
     */
    public void removeCards() {
        for(int i=0;i<3;i++) cards[i] = 0;
    } 
    
    /**
     * Method to get a description of cards.
     * @param mode 0-simple, 1-detail
     * @return description of cards
     */
    public String getCardsString(int mode) {
        if (mode==0) return String.valueOf(cards[0])+"+"+String.valueOf(cards[1])+"+"+String.valueOf(cards[2])+"="+String.valueOf(cards[0]+cards[1]+cards[2]);
        else return String.valueOf(cards[0])+" Infantry, "+String.valueOf(cards[1])+" Cavalry and "+String.valueOf(cards[2])+" Artillery";
    }

    /**
     * Method to get country list of player.
     * @return country list of the player
     */
    public ArrayList<CountryModel> getCountries() {
        return countries;
    }
    
    /**
     * Method to add a country to player.
     * @param newCountry country object adding to this player
     */
    public void addCountry(CountryModel newCountry) {
    	this.countries.add(newCountry);
    }
    
    /**
     * Method to remove a country from player.
     * @param country country object to be removed
     */
    public void removeCountry(CountryModel country) {
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
	
	/**
     * Method to add armies to player.
     * @param armies armies to add
     */
	public void addArmies(int armies) {
		this.totalArmies += armies;
	}	    
	
	
	/**
     * Method to add armies to player.
     */
	public void lostArmies() {
		this.totalArmies--;
	}	
    /**
     *  This function is to judge if player must exchange cards with armies.
     *
     *  @return true if player is forced to exchange cards
     */
    public boolean ifForceExchange(){
    	return (cards[0]+cards[1]+cards[2]) >= 5;
    }

    /**
     * The function to judge if player can exchange cards
     * @return true if can exchange
     */
    public boolean canExchange() {
    	return (Math.max(cards[0], Math.max(cards[1], cards[2]))>=3
    			||Math.min(cards[0], Math.min(cards[1], cards[2]))>=1);
    }	

    /**
     * The function to calculate how many armies player get this turn.
     */
    public void calculateArmyNumber() {
    	ArrayList<ContinentModel> continents = myGame.getGameMap().getContinents();
    	this.totalReinforcement = Math.floorDiv(this.countries.size(), 3);
    	String tempstr = "<HTML>Floor ("+ this.countries.size()+" countries / 3) = "+totalReinforcement;
    	String continentStr = "";
    	int continentNum = 0;
    	for (ContinentModel loopContinent:continents){
        	if (loopContinent.getOwner()!=null&&loopContinent.getOwner().getName().equals(this.name)){
        		continentNum++;
        		totalReinforcement+=loopContinent.getControlNum();
        		if (continentStr.isEmpty())
        			continentStr =loopContinent.getShowName()+"("+loopContinent.getControlNum()+")";
        		else
        			continentStr += "+"+loopContinent.getShowName()+"("+loopContinent.getControlNum()+")";
        	}
        }
    	if (continentNum>0){
    		tempstr+="<br> + "+continentNum+" continents { "+continentStr+" } = "+totalReinforcement;
    	}
    	totalReinforcement = Math.max(totalReinforcement, 3);
    	totalArmies+=totalReinforcement;
    	tempstr+="<br>compare with 3 = "+totalReinforcement+" armies";
    	this.setReinforcementStr(tempstr+"</HTML>");
    	this.setBaseReinforceStr(tempstr);
    	this.setExchangeCardStr("");
    	//if (ifForceExchange()) totalReinforcement+=this.CalExchangeArmies();
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
     * increase the number of reinforcement armies of player
     * @param armies army's number
     */
    public void increaseTotalReinforcement(int armies)
    {
        totalReinforcement += armies;
        totalArmies += armies;
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
     * The function to this player to play a whole turn, stop when finished or win the game
     * @return succeed of failed with error message
     */	
	public ErrorMsg playTurn(){
		myGame.setGameStage(50);
		reinforcementPhase();
		myGame.setGameStage(51);
        attackPhase();
		myGame.setGameStage(52);
        if (winGame(myGame.getGameMap().getCountryNum())){
    		myGame.setGameStage(54);
    		setPhaseString("Game Over");
    		myGame.myLog.setLogStr("\n"+getDiscription()+" has win the game!\n");
            return new ErrorMsg(1,getName()+" has win the game!");
        }
        fortificationPhase();
		myGame.setGameStage(53);
        return new ErrorMsg(0,null);
	}
	
	/**
	 * Method to set player's strategy
	 * @param strategy player's strategy
	 */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
	
	/**
     * The function to judge if complete reinforcement phase
	 */
	public void reinforcementPhase(){	
		strategy.reinforcementPhase(this);
    	myGame.myLog.setLogStr(getName()+" reinforcement finished.\n");
	}

    /**
     * The function to judge if complete attack phase
     */
    public void attackPhase(){
		this.setPhaseString("Attack Phase");
		myGame.myLog.setLogStr("\n"+getDiscription()+" attack phase begin\n");
		int result = strategy.attackPhase(this);
		if (result == 1){
        	setAttackInfo("No more territories can attack, attack phase finished");
        	myGame.myLog.setLogStr(getDiscription()+", no more territories can attack, attack phase finished\n");
		}
    }
    /**
     * The function to judge if complete fortification phase
     */
    public void fortificationPhase(){
		this.setPhaseString("Fortification Phase");
		strategy.fortificationPhase(this);
    }
	/**
	 * The getter of PhaseString, used to return phase info
	 * @return phaseString phase info
	 */
	public String getPhaseString() {
		return phaseString;
	}
	/**
	 * The setter of phaseString, used to set phase info
	 * @param phaseString current phaseString
	 */
	public void setPhaseString(String phaseString) {
		this.phaseString = phaseString;
		setChanged();
		notifyObservers(4);
	}

	/**
	 * The getter of ReinforcementStr, used to return reinforcement info
	 * @return reinforcementStr reinforcement info
	 */
	public String getReinforcementStr() {
		return reinforcementStr;
	}
	/**
	 * The setter of ReinforcementStr, used to set reinforcement info
	 * @param reinforcementStr current reinforcementStr
	 */
	public void setReinforcementStr(String reinforcementStr) {
		this.reinforcementStr = reinforcementStr;	
	}
	
    /**
     *   The function calculate how many armies can exchange this time, and increase the change card times.
     *   @return numbers of exchanged armies
     */
    public int CalExchangeArmies(){
    	return myGame.CalExchangeArmies();
    }
	/**
	 * The getter of BaseReinforceStr, used to return baseReinforce info
	 * @return baseReinforceStr baseReinforce info
	 */
	public String getBaseReinforceStr() {
		return baseReinforceStr;
	}
	/**
	 * The setter of baseReinforceStr, used to set baseReinforce info
	 * @param baseReinforceStr current baseReinforceStr
	 */
	public void setBaseReinforceStr(String baseReinforceStr) {
		this.baseReinforceStr = baseReinforceStr;
		setChanged();
		notifyObservers(5);
	}
	/**
	 * The getter of ExchangeCardStr, used to return card exchange info
	 * @return exchangeCardStr card exchange info
	 */
	public String getExchangeCardStr() {
		return exchangeCardStr;
	}

	/**
	 * The setter of ExchangeCardStr, used to set card exchange info
	 * @param exchangeCardStr current exchangeCardStr
	 */
	public void setExchangeCardStr(String exchangeCardStr) {
		if (exchangeCardStr==null||exchangeCardStr.isEmpty()) this.exchangeCardStr = "";
		else {
			if (this.exchangeCardStr.isEmpty()){
				this.exchangeCardStr = "Exchange Cards { "+exchangeCardStr;				
			}
			else{
				this.exchangeCardStr += "+"+exchangeCardStr;
			}
		}
	}
	
	/**
	 * The getter of ExchangeStatus, used to return status of exchange
	 * @return exchangeStatus status of exchange
	 */
	public String getExchangeStatus() {
		return exchangeStatus;
	}
	
	/**
	 * The setter of ExchangeStatus, used to set the status of exchange
	 * @param exchangeStatus new exchangeStatus
	 */
	public void setExchangeStatus(String exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
		setChanged();
		notifyObservers(6);
	}
	/**
	 * The getter of PutArmyStr, used to return army putting info
	 * @return putArmyStr army putting info
	 */
	public String getPutArmyStr() {
		return putArmyStr;
	}
	/**
	 * The setter of PutArmyStr, used to set the army putting info
	 * @param putArmyStr army putting info
	 */	
	public void setPutArmyStr(String putArmyStr) {
		this.putArmyStr = putArmyStr;
		setChanged();
		notifyObservers(7);
	}

	/**
	 * Method to get game object of this player
	 * @return game object
	 */
	public RiskGameModel getMyGame() {
		return myGame;
	}
	
	/**
	 * This method is to get movable country
	 * @return count of movable country
	 */
	public int getMovableCountry() {
		int count = 0;
		for(CountryModel loopCountry:this.getCountries()){
			if (loopCountry.getArmyNumber()>1) count++;
		}
		return count;
	}
	
	/**
	 * This method is to get valid attacking countries
	 * @param mode 0- army number greater than 1, 1- no army number limit
	 * @return valid attacking countries
	 */
	public ArrayList<CountryModel> getAttackingCountry(int mode) {
		ArrayList<CountryModel> countryList = new ArrayList<CountryModel>();
		for(CountryModel loopCountry:this.getCountries()){
			if (mode==1||loopCountry.getArmyNumber()>1){
				ArrayList<CountryModel> neighbors = myGame.getGameMap().getAdjacencyList().get(loopCountry);
				for (CountryModel neighbor:neighbors){
					if (neighbor.getOwner()!=loopCountry.getOwner()){
						countryList.add(loopCountry);
						break;
					}
				}
			}
		}
		return countryList;
	}	

	/**
	 * This method is to get attack information
	 * @return attack info
	 */
	public String getAttackInfo() {
		return attackInfo;

	}

	/**
	 * This method is to set attack information
	 * @param attackInfo attack info
	 */
	public void setAttackInfo(String attackInfo) {
		this.attackInfo = attackInfo;
		setChanged();
		if (attackInfo.isEmpty()) {
			notifyObservers(8);
		}
		else notifyObservers(9);
	}

	/**
	 * This method is to get attack step information
	 * @return step attack information
	 */
	public String getAttackStepInfo() {
		return attackStepInfo;
	}

	/**
	 * This method is to set attack information
	 * @param attackStepInfo attack step information
	 */
	public void setAttackStepInfo(String attackStepInfo) {
		this.attackStepInfo = attackStepInfo;
		setChanged();
		notifyObservers(10);
	}
	
	/**
	 * This method is to move armies from one country to another
	 * @param country1 armies moved to
	 * @param country2 armies moved from
	 * @param armies armies number
	 */
	public void moveArmies(CountryModel country1,CountryModel country2, int armies){
		country1.setArmyNumber(country1.getArmyNumber()+armies);
		country2.setArmyNumber(country2.getArmyNumber()-armies);
	}
}

