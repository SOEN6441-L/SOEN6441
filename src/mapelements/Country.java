package mapelements;

import gameelements.Player;

/**
 * This is the class Country.
 * <p> This class contain all information of a country</p>
 */
public class Country{

	public int countryID;
	public String countryName;
	public Continent belongToContinent;
	public Player belongToPlayer = null;
	public int[] coordinate ={0,0};
	public boolean flagDFS;
	public int armyNumber;
	
	/**
	 * Constructor of Country.
	 * @param ID	ID of the country
	 * @param name	Name of country
	 * @param continent	Relationship with continent
	 */
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