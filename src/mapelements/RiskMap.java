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
		if (findContinent(continentName)!=null) {
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
}
