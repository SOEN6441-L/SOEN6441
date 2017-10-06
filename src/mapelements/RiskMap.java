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
		if (countries.get(continentName)!=null) {
		//if (continents.contains(continentName) ){
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
		//default the country just added doesn't exist
		boolean nonExist = true;

		//checking all the existing countries, see if there's one has the same name as the new on
		for (ArrayList<Country> existingCountryList: countries.values()) {
			for (Country existingCountry: existingCountryList) {
				if(existingCountry.countryName == countryName){
					//if there's one has the same name, then default noExit become false
					nonExist = nonExist && false;
				}

			}

		}

		//if the country just added has already existed
		if(nonExist == false){
			//popup warning that the country exist already
			JOptionPane.showMessageDialog(null,"Country '"+countryName+"' exists");
			//adding failed
			return false;
		}else{//otherwise
			//if the country just added belong to a existing continent
			if(countries.containsKey(continentName)){
				//Then adding the country to the existing continent
				countries.get(continentName).add(new Country(countryName, continentName));
			}else{
				//if the country just added belong to a nonexisting continent
				//then create a new continent and put the country in it
				countries.put(continentName,new ArrayList<Country>().add(new Country(countryName, continentName)));
			}
			// adding succeed
			countryNum++;
			return true;
		}



	}

}
