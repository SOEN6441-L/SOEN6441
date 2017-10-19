package mapelements;

import gameelements.Player;

/**
 * This is class Continent.
 * 
 * <p> A continent can include several countries, and <br>
 * it contain all information of a continent</p>
 */
public class Continent {
	public int continentID;
	public String continentName;
	public int controlNum;
	public Player owner;
	
	/**
	 * Constructor for Continent.
	 * @param ID	Continent id which can used to distinct continent.
	 * @param name	Name of continent.
	 */
	public Continent(int ID,String name){
		this.continentID = ID;
		this.continentName = name;
		controlNum = 0;
		owner = null;
	}
	
	public void changeName(String name){
		this.continentName = name;
	}
	
	public void changeControlNum(int control){
		this.controlNum = control;
	}	
	
	/**
	 * This method is used for checking the owner of continent.
	 * @param myMap	Object of RiskMap
	 */
	public void checkOwner(RiskMap myMap){
		Player player = null;
		boolean owned = true;
		for (Country country:myMap.countries.get(this.continentID)){
			if (player == null) player = country.belongToPlayer;
			else if (!player.getName().equals(country.belongToPlayer.getName())){
				owned = false;
				break;
			}
		}
		if (owned) this.owner = player;
	}
}
