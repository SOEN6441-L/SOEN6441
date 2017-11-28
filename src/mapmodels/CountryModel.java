package mapmodels;

import java.io.Serializable;

import gamemodels.PlayerModel;

/**
 * This is the class for defining country module.
 * 
 * <p> This class contains all information of a country.</p>
 */
public class CountryModel implements Serializable{
	private static final long serialVersionUID = 4L;
	private int countryId;
	private String countryName;//country's name, unique and can be changed.
	private String showName;
	/**continent which contains this country*/
	private ContinentModel belongTo;
	/**player who owns this country*/
	private PlayerModel owner;
	private int coordinateX;
	private int coordinateY;
	/**used in DFS algorithm to find path or check connectivity*/
	private boolean flagDFS;
	private int armyNumber;
	
	/**
	 * Constructor of Country class.
	 * @param countryID country's unique ID
	 * @param countryName name of the new country
	 * @param continent	continent object which contains this country
	 */
	public CountryModel(int countryID, String countryName, ContinentModel continent){
		this.setCountryId(countryID);
		this.setName(countryName);
		this.setBelongTo(continent);
		this.setOwner(null);
		this.setFlagDFS(false);
		this.setCoordinate(0,0);
		this.setArmyNumber(0);
	}

	/**
	 * Method to get country's name.
	 * @return country's Name
	 */
	public String getName() {
		return countryName;
	}

	/**
	 * Method to initialize or change country's name, is used when 
	 * creating a new country or changing an existing country's name.
	 * @param countryName country's new name
	 */
	public void setName(String countryName) {
		this.showName = countryName;
		this.countryName = countryName.toLowerCase();
	}
	
	/**
	 * Method to get continent which contains this country.
	 * @return continent object which contains this country
	 */
	public ContinentModel getBelongTo() {
		return belongTo;
	}
	
	/**
	 * Method to initialize or change the continent which contains this country, is used when
	 * creating a new country or moving one country to another continent.
	 * @param belongTo continent object to contain the country
	 */
	public void setBelongTo(ContinentModel belongTo) {
		this.belongTo = belongTo;
	}

	/**
	 * Method to get country's owner.
	 * @return country's current owner
	 */
	public PlayerModel getOwner() {
		return owner;
	}

	/**
	 * Method to set country's owner, is used at the beginning
	 * of a game or during an attack phase.
	 * @param owner country's new owner
	 */
	public void setOwner(PlayerModel owner) {
		this.owner = owner;
	}

	/**
	 * Method to get country's coordinate.
	 * @return country's coordinate in a array of integer
	 */
	public int[] getCoordinate() {
		return new int[]{coordinateX,coordinateY};
	}

	/**
	 * Method to initialize or change country's coordinate on a map, is used mainly when
	 * loading map from a map file.
	 * @param coordinateX coordinate X
	 * @param coordinateY coordinate Y
	 */
	public void setCoordinate(int coordinateX, int coordinateY) {
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
	}

	/**
	 * Method to get country's DFS Flag, used in DFS algorithm to indicate if this node has been visited.
	 * @return country's DFS Flag
	 */
	public boolean isFlagDFS() {
		return flagDFS;
	}

	/**
	 * Method to change the DFS Flag, used in DFS algorithm to construct a DFS tree
	 * @param flagDFS DFS Flag
	 */
	public void setFlagDFS(boolean flagDFS) {
		this.flagDFS = flagDFS;
	}

	/**
	 * Method to get the army number on this country.
	 * @return  army number on this country
	 */
	public int getArmyNumber() {
		return armyNumber;
	}

	/**
	 * Method to change the army number on this country, is used in all phases of a game.
	 * @param armyNumber number of armies
	 */
	public void setArmyNumber(int armyNumber) {
		this.armyNumber = armyNumber;
	}
	
	/**
	 * Method to change the army number on this country, is used in all phases of a game.
	 */
	public void lostArmy() {
		this.armyNumber--;
		this.owner.lostArmies();
	}


	/**
	 * Method to get the unique ID of this country.
	 * @return  unique ID
	 */
	public int getCountryId() {
		return countryId;
	}

	/**
	 * Method to set country's unique ID
	 * @param countryId unique ID
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	/**
	 * Method to get show name
	 * @return show name
	 */
	public String getShowName() {
		return showName;
	}
}