package mapelements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;


public class RiskMap {
	private int globalIndex;
	public String riskMapName;
	public ArrayList<Continent> continents;
	public int countryNum;
	public Map<Integer,ArrayList<Country>> countries;
	public Map<Integer,ArrayList<Integer>> adjacencyList;//add
	
	public RiskMap(String name){
		this.globalIndex = 1;
		this.riskMapName = name;
		this.continents = new ArrayList<Continent>();
		this.countryNum = 0;
		this.countries = new HashMap<Integer,ArrayList<Country>>();
		this.adjacencyList = new HashMap<Integer,ArrayList<Integer>>();//add
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
		if (findContinent(continentName)!=null){ 	
			JOptionPane.showMessageDialog(null,"Continnet '"+continentName+"' already exists");
			return false;
		}	
		else {
        	Continent newContinent = new Continent(globalIndex++,continentName);
        	continents.add(newContinent);
        	countries.put(newContinent.continentID, new ArrayList<Country>());
        	return true;
		}
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
		adjacencyList.put(newCountry.countryID, new ArrayList<Integer>());//add
		
		countryNum++;
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
		return true;
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
		return true;
	}	
	
	public boolean deleteCountry(String countryName){
		Country deleteCountry = findCountry(countryName);
		if(deleteCountry == null){
			JOptionPane.showMessageDialog(null,"Country  '"+deleteCountry+"' does not exists");
			return false;
		}
		//countries.get(deleteCountry.countryID).remove(deleteCountry);

		for (ArrayList<Country> countryList: countries.values()) {
			for(Country deleateCountry: countryList){
				if(deleateCountry.countryName.equals(countryName)){
					countryList.remove(deleateCountry);
				}
			}
		}
		
		return true;
	}

	public boolean deleteContinent(String continentName){
		Continent deleteContinent = findContinent(continentName);
		if(findContinent(continentName) == null){
			JOptionPane.showMessageDialog(null,"Continent  '"+continentName+"'  you want delete does not exists");
			return false;
		}else {
			if(countries.get(deleteContinent.continentID) != null){
				moveContinentCountry(deleteContinent);
				continents.remove(deleteContinent);
				countries.remove(deleteContinent.continentID);
				return true;
			}else{
				continents.remove(deleteContinent);
				countries.remove(deleteContinent.continentID);
				return true;
			}
		}

	}

	private boolean moveContinentCountry(Continent deleteContinent) {
		String moveToContinentName = JOptionPane.showInputDialog("Please enter which Continent you want move to");

		if(findContinent(moveToContinentName) == null){
			JOptionPane.showMessageDialog(null,"Continent  '"+moveToContinentName+"'  you want move to does not exists");
			return false;
		}
		ArrayList<Country> countriesNeedMove = countries.get(deleteContinent.continentID);

		countries.put(findContinent(moveToContinentName).continentID, countriesNeedMove);

		return true;
	}

}
