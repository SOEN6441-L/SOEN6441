package mapelements;

import gameelements.Player;

public class Country{

	public int countryID;
	public String countryName;
	public Continent belongToContinent;
	public Player belongToPlayer = null;
	public int[] coordinate ={0,0};
	public boolean flagDFS;
	public int armyNumber;
	
	public Country(int ID, String name, Continent continent){
		this.countryID = ID;
		this.countryName = name;
		this.belongToContinent = continent;
		this.flagDFS = false;
		this.armyNumber = 0;
	}
	
	public void changeName(String name){
		this.countryName = name;
	}	
	
	public void changeContinent(Continent toContinent){
		this.belongToContinent = toContinent;
	}	
	
}