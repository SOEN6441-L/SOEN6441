package mapelements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;


public class RiskMap {
	public String riskMapName;
	public ArrayList<Continent> continents;
	public int countryNum;
	public Map<String,ArrayList<Country>> countries;
	
	public RiskMap(String name){
		this.riskMapName = name;
		this.continents = new ArrayList<Continent>();
		this.countryNum = 0;
		this.countries = new HashMap<String,ArrayList<Country>>();
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
//		if (countries.get(continentName)!=null) {
		if (countries.keySet().contains(continentName)){
			JOptionPane.showMessageDialog(null,"Continnet '"+continentName+"' already exists");
			return false;
		}	
		else {
        	Continent newContinent = new Continent(continentName);
        	continents.add(newContinent);
        	countries.put(continentName, new ArrayList<Country>());

        	return true;
		}
	}
	
	public boolean addCountry(String countryName,String continentName){
		ArrayList<Country> targetCountryList = countries.get(continentName);
		if (targetCountryList==null) {
			JOptionPane.showMessageDialog(null,"Continnet '"+continentName+"' does not exists");
			return false;
		}	
		
		if (findCountry(countryName)!=null){
			JOptionPane.showMessageDialog(null,"Country '"+countryName+"' already exists");
			return false;
		}
		
		Country newCountry = new Country(countryName,continentName);
		targetCountryList.add(newCountry);
		
		countryNum++;
		return true;
	}
}
