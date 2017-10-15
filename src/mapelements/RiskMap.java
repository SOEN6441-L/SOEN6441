package mapelements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 * This is class RiskMap to achieve the function of edit map
 */
public class RiskMap {
	private int globalIndex;
	public String riskMapName;
	public String author;
	public String warn,image,wrap,scroll;
	public boolean modified;
	public ArrayList<Continent> continents;
	public int countryNum;
	public Map<Integer,ArrayList<Country>> countries;
	public Map<Integer,ArrayList<Integer>> adjacencyList;

	/**
	 * The constructor of class RiskMap
	 * @param name The name of the Map that is editing
	 */
	public RiskMap(String name){
		this.globalIndex = 1;
		this.riskMapName = name;
		this.author = "Invincible Team Four";
		this.warn = "yes";
		this.wrap = "no";
		this.image = "none";
		this.scroll = "no";
		this.modified = false;
		this.continents = new ArrayList<Continent>();
		this.countryNum = 0;
		this.countries = new HashMap<Integer,ArrayList<Country>>();
		this.adjacencyList = new HashMap<Integer,ArrayList<Integer>>();
	}


	/**
	 * Function to find the Country according to the Country's name
	 * @param countryName Country's name
	 * @return Country that found
	 */
	public Country findCountry(String countryName) {
		for (ArrayList<Country> loopList : countries.values()) {
			for (Country loopCountry:loopList){
				if (loopCountry.countryName.equals(countryName)){
					return loopCountry;
				}
			}
		}
		return null;
	}

	/**
	 * Function to find the Continent according to the Continent's name
	 * @param continentName Continent's name
	 * @return Continent's name
	 */
	public Continent findContinent(String continentName) {
		for (Continent loopContinent:continents){
			if (loopContinent.continentName.equals(continentName)){
				return loopContinent;
			}
		}
		return null;
	}

	/**
	 * Function to add new Continent
	 * @param continentName The name of the Continent want to add
	 * @return succeed or not
	 */
	public boolean addContinent(String continentName){
		if (findContinent(continentName)!=null) {
			JOptionPane.showMessageDialog(null,"Continnet '"+continentName+"' already exists");
			return false;
		}
		Continent newContinent = new Continent(globalIndex++,continentName);
		continents.add(newContinent);
		countries.put(newContinent.continentID, new ArrayList<Country>());
		this.modified = true;
		return true;
	}

	/**
	 * The function to add new Country in existing continent
	 * @param countryName The name of the country want to add
	 * @param continentName THe name of the existing Continent that the new country adding in
	 * @return succeed or not
	 */
	public boolean addCountry(String countryName,String continentName){
		int continentID = findContinent(continentName).continentID;
		ArrayList<Country> targetCountryList = countries.get(continentID);
		if (targetCountryList==null) {
			JOptionPane.showMessageDialog(null,"Continnet '"+continentName+"' does not exists");
			return false;
		}
		if (findCountry(countryName)!=null){
			JOptionPane.showMessageDialog(null,"Country '"+countryName+"' already exists");
			return false;
		}

		Country newCountry = new Country(globalIndex++,countryName,findContinent(continentName));
		targetCountryList.add(newCountry);
		adjacencyList.put(newCountry.countryID, new ArrayList<Integer>());
		countryNum++;
		this.modified = true;
		return true;
	}

	/**
	 * The function to add connection between two countries
	 * @param countryNameFrom The name of the country want to add from
	 * @param countryNameTo The name of the country want to add to
	 * @return succeed or not
	 */
	public boolean addConnection(String countryNameFrom,String countryNameTo){
		Country fromCountry = findCountry(countryNameFrom);
		if (fromCountry==null){
			JOptionPane.showMessageDialog(null,"Country  '"+countryNameFrom+"' does not exists");
			return false;
		}
		Country toCountry = findCountry(countryNameTo);
		if (toCountry==null){
			JOptionPane.showMessageDialog(null,"Country  '"+countryNameTo+"' does not exists");
			return false;
		}
		adjacencyList.get(fromCountry.countryID).add(toCountry.countryID);
		adjacencyList.get(toCountry.countryID).add(fromCountry.countryID);
		this.modified = true;
		return true;
	}

	/**
	 * The function to add a completed connection
	 */
	public void addCompletedConnection(){
		for (int loopCountryID:adjacencyList.keySet()){
			ArrayList<Integer> loopCountryList = adjacencyList.get(loopCountryID);
			loopCountryList.clear();
			for (int candidateID:adjacencyList.keySet()){
				if (candidateID!=loopCountryID){
					loopCountryList.add(candidateID);
				}
			}
		}
		this.modified = true;
	}

	/**
	 * The function to remove the connection between two Countries
	 * @param countryNameFrom The name of the Country that want to remove connection with
	 * @param countryNameTo The name of the Country that want to remove connection to
	 * @return succeed or not
	 */
	public boolean removeConnection(String countryNameFrom,String countryNameTo){
		Country fromCountry = findCountry(countryNameFrom);
		if (fromCountry==null){
			JOptionPane.showMessageDialog(null,"Country  '"+countryNameFrom+"' does not exists");
			return false;
		}
		Country toCountry = findCountry(countryNameTo);
		if (toCountry==null){
			JOptionPane.showMessageDialog(null,"Country  '"+countryNameTo+"' does not exists");
			return false;
		}
		adjacencyList.get(fromCountry.countryID).remove(adjacencyList.get(fromCountry.countryID).indexOf(toCountry.countryID));
		adjacencyList.get(toCountry.countryID).remove(adjacencyList.get(toCountry.countryID).indexOf(fromCountry.countryID));
		this.modified = true;
		return true;
	}

	/**
	 * The function to remove all connections
	 */
	public void removeAllConnection(){
		for (ArrayList<Integer> loopCountryList:adjacencyList.values()){
			loopCountryList.clear();
		}
		this.modified = true;
	}

	/**
	 * The function to rename the Country
	 * @param countryName The name of the Country want to rename
	 * @param newName The new name for the Country
	 * @return
	 */
	public boolean renameCountry(String countryName, String newName){
		Country changeCountry = findCountry(countryName);
		if(changeCountry == null){
			JOptionPane.showMessageDialog(null,"Country  '"+countryName+"'  you want change does not exists");
			return false;
		}
		if (findCountry(newName)!=null){
			JOptionPane.showMessageDialog(null,"Country '"+newName+"' already exits");
			return false;
		};
		changeCountry.changeName(newName);
		this.modified = true;
		return true;
	}

	/**
	 * The function to delete a existing Country
	 * @param countryName The name of Country want to delete
	 * @return succeed or not
	 */
	public boolean deleteCountry(String countryName){
		//delete country from HashMap of countries
		Country deleteCountry = findCountry(countryName);
		if(deleteCountry == null){
			JOptionPane.showMessageDialog(null,"Country  '"+deleteCountry+"' you want delete does not exists");
			return false;
		}
		countries.get(deleteCountry.belongToContinent.continentID).remove(deleteCountry);

		//delete relative connections
		ArrayList<Integer> relativeCountries = adjacencyList.get(deleteCountry.countryID);
		for(int relativeCountryID: relativeCountries){
			adjacencyList.get(relativeCountryID).remove(adjacencyList.get(relativeCountryID).indexOf(deleteCountry.countryID));
		}
		adjacencyList.remove(deleteCountry.countryID);
		countryNum--;
		this.modified = true;
		return true;
	}

	/**
	 * The funtion to rename the Continent
	 * @param continentName The name of Continent want to rename
	 * @param newName The new name for the Continent
	 * @return succeed or not
	 */
	public boolean renameContinent(String continentName, String newName){
		Continent changeContinent = findContinent(continentName);
		if(changeContinent == null){
			JOptionPane.showMessageDialog(null,"Continent  '"+continentName+"'  you want change does not exists");
			return false;
		}
		if (findContinent(newName)!=null){
			JOptionPane.showMessageDialog(null,"Continent '"+newName+"' already exits");
			return false;
		};
		changeContinent.changeName(newName);
		this.modified = true;
		return true;
	}

	/**
	 * The function to change the number of armies player can have after take the whole Continent
	 * @param continentName The name of the Continent want to change
	 * @param controlNum The new number of the army
	 * @return succeed or not
	 */
	public boolean changeContinentControl(String continentName, int controlNum){
		Continent changeContinent = findContinent(continentName);
		if(changeContinent == null){
			JOptionPane.showMessageDialog(null,"Continent  '"+continentName+"'  you want change does not exists");
			return false;
		}
		changeContinent.changeControlNum(controlNum);
		this.modified = true;
		return true;
	}

	/**
	 * The function to delete the Continent
	 * @param continentName The name of the Continent want to delete
	 * @return succeed
	 */
	public boolean deleteContinent(String continentName){
		Continent deleteContinent = findContinent(continentName);
		if(deleteContinent == null){
			JOptionPane.showMessageDialog(null,"Continent  '"+continentName+"'  you want delete does not exists");
			return false;
		}

		if(countries.get(deleteContinent.continentID).size()>0){
			JOptionPane.showMessageDialog(null,"Continent '"+continentName+"' is not empty, you need to delete/move all the countries in it.");
			return false;
		}
		else{
			continents.remove(deleteContinent);
			countries.remove(deleteContinent.continentID);
			this.modified = true;
			return true;
		}
	}

	/**
	 * The function to move the Country to a other Continent
	 * @param toContinentName The name of the Continent want to move the Country to
	 * @param countryName The name of the Country that need to be moved
	 * @return
	 */
	public boolean moveContinentCountry(String toContinentName, String countryName) {
		Country moveCountry = findCountry(countryName);
		if(moveCountry == null){
			JOptionPane.showMessageDialog(null,"Country  '"+moveCountry+"' you want move does not exists");
			return false;
		}
		Continent movetoContinent = findContinent(toContinentName);

		if(movetoContinent == null){
			JOptionPane.showMessageDialog(null,"Continent  '"+toContinentName+"'  you want move to does not exists");
			return false;
		}
		countries.get(moveCountry.belongToContinent.continentID).remove(moveCountry);
		countries.get(movetoContinent.continentID).add(moveCountry);
		moveCountry.belongToContinent = movetoContinent;
		this.modified = true;
		return true;
	}

}
