package mapelements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;


public class RiskMap {
	public String riskMapName;
	public int continentNum;
	public ArrayList<Continent> continents;
	public int countryNum = 0;
	public Map<String,ArrayList<Country>> countries;

	
	
	public RiskMap(String name){
		this.riskMapName = name;
		this.continentNum = 0;
		this.continents = new ArrayList<Continent>();
		this.countryNum = 0;
		this.countries = new HashMap<String,ArrayList<Country>>();
	}
	
	public boolean addContinent(String continentName){
		//if (countries.get(name)!=null) {
		if (continents.contains(continentName) ){
				JOptionPane.showMessageDialog(null,"Continnet '"+continentName+"' exists");
			return false;

		}	
		else {
        	Continent newContinent = new Continent(continentName);
        	continents.add(newContinent);
        	continentNum++;
        	countries.put(continentName, new ArrayList<Country>());

        	//countryNum++;
			return true;
		}

	}
	public boolean addCountry(String continentName, String countryName){
		if (countries.get(continentName)  ){
			JOptionPane.showMessageDialog(null,"Country '"+name+"' exists");
			return false;
		}else {

		}
	}

}
