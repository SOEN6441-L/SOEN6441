package mapelements;

import java.util.ArrayList;
import gameelements.Player;

/**
 * This is the class for defining Continent.
 * 
 * <p> A continent can include several countries, and 
 * contains all information of a continent.</p>
 */
public class Continent {
	private String continentName;//continent's name, unique and can be changed.
	private int controlNum;//Bonus armies during reinforcement phase when the whole continent is owned by one player.
	private Player owner;//The player who owns the whole continent.
	private ArrayList<Country> countryList;
	/**
	 * Constructor for Continent class.
	 * @param continentName	name of the new continent
	 */
	public Continent(String continentName){
		this.setName(continentName);
		this.setControlNum(0);
		this.owner = null; //no owner.
		this.countryList = new ArrayList<Country>();
	}
	
	/**
	 * Method to get continent's name.
	 * @return continent's name
	 */
	public String getName() {
		return continentName;
	}

	/**
	 * Method to initialize or change continent's name, is used when
	 * creating a new continent or changing an existing contient's name.
	 * @param continentName continent's name
	 */
	public void setName(String continentName) {
		this.continentName = continentName;
	}

	/**
	 * Method to get continent's control number. 
	 * @return continent's control number
	 */
	public int getControlNum() {
		return controlNum;
	}

	/**
	 * Method to set continent's control number. 
	 * @param controlNum continent's control number
	 */
	public void setControlNum(int controlNum) {
		this.controlNum = controlNum;
	}

	/**
	 * Method to add a country to countryList.
	 * @param country new country object
	 */
	public void addCountry(Country country) {
		countryList.add(country);
	}
	
	/**
	 * Method to delete a country from countryList.
	 * @param country country object to delete
	 */
	public void deleteCountry(Country country) {
		countryList.remove(country);
	}	
	
	/**
	 * Method to get the list of countries which belong to this continent.
	 * @return country list of this continent
	 */
	public ArrayList<Country> getCountryList() {
		return countryList;
	}
	
	/**
	 * Method to get the owner of this continent. 
	 * @return the owner of this continent or null if no owner
	 */
	public Player getOwner() {
		return owner;
	}	
	
	/**
	 * Method to check if this continent has been owned by one player.
	 */
	public void checkOwner(){
		Player player = null;
		boolean owned = true;
		for (Country loopCountry:countryList){
			if (loopCountry.getOwner()!=null){
				if (player == null) player = loopCountry.getOwner();
				else if (!player.getName().equals(loopCountry.getOwner().getName())){
					owned = false;
					break;
				}
			}
			else{ //each country should have an owner, this branch should not happen in run time. only for protection.
				owned = false;
				break;				
			}
		}
		if (owned) owner = player;
		else owner = null;
	}
	
	/**
	 * Method to find a country in this continent.
	 * @param countryName the name of the country
	 * @return the country object found in this continent or null if can't find
	 */	
	public Country findCountry(String countryName){
		for (Country loopCountry:countryList){
			if (loopCountry.getName().equals(countryName)){
				return loopCountry;
			}	
		}
		return null;
	}
}