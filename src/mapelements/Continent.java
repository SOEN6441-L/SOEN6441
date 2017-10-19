package mapelements;

import gameelements.Player;

public class Continent {
	public int continentID;
	public String continentName;
	public int controlNum;
	public Player owner;
	
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
