package mapelements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;


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

	public Continent findContinent(String continentName) {
		for (Continent loopContinent:continents){
			if (loopContinent.continentName.equals(continentName)){
				return loopContinent;
			}
		}
		return null;
	}

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

	public void removeAllConnection(){
		for (ArrayList<Integer> loopCountryList:adjacencyList.values()){
			loopCountryList.clear();
		}
		this.modified = true;
	}

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
