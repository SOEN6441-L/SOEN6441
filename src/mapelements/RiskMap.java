package mapelements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * This is class RiskMap to achieve the function of edit map
 */
public class RiskMap {
	private int globalIndex, findCountries;
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
	 * The constructor of class RiskMap without params
	 */
	public RiskMap(){
		this.globalIndex = 1;
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

	public boolean addContinent(String continentName, int controlNum){
		if (findContinent(continentName)!=null) {
			JOptionPane.showMessageDialog(null,"Continnet <"+continentName+"> already exists");
			return false;
		}
		Continent newContinent = new Continent(globalIndex++,continentName);
		newContinent.controlNum = controlNum;
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

	public boolean addCountry(String countryName,String continentName,int coordinateX, int coordinateY){
		Continent targetContinent = findContinent(continentName);
		if (targetContinent==null) {
			JOptionPane.showMessageDialog(null,"Continnet <"+continentName+"> does not exists");
			return false;
		}

		if (findCountry(countryName)!=null){
			JOptionPane.showMessageDialog(null,"Country <"+countryName+"> already exists");
			return false;
		}

		Country newCountry = new Country(globalIndex++,countryName,targetContinent);
		newCountry.coordinate[0] = coordinateX;
		newCountry.coordinate[1] = coordinateY;
		countries.get(targetContinent.continentID).add(newCountry);
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

	public boolean checkConnection(Map<Integer,ArrayList<Integer>> localAdjacencyList) {
		if (localAdjacencyList.size()==0) return true;
		int sourceNode = -1;
		for (int loopCountry : localAdjacencyList.keySet()) {
			findCountryByID(loopCountry).flagBFS = false;
			if (sourceNode==-1) sourceNode = loopCountry;
		}
		findCountries = 1;
		findCountryByID(sourceNode).flagBFS = true;
		BFS(localAdjacencyList,sourceNode);

		if (findCountries==localAdjacencyList.size()) return true;
		else return false;
	}

	public void BFS(Map<Integer,ArrayList<Integer>> localAdjacencyList, int sourceNode) {
		ArrayList<Integer> myChildren = new ArrayList<Integer>();
		for (int targetNode : localAdjacencyList.get(sourceNode)){
			Country targetCountry = findCountryByID(targetNode);
			if (!targetCountry.flagBFS){
				findCountries++;
				targetCountry.flagBFS = true;
				myChildren.add(targetNode);
			}
		}
		for (int targetNode : myChildren){
			BFS(localAdjacencyList,targetNode);
		}
	}

	public boolean checkValid(int mode){ //mode 0-upon save, 1-upon load
		String errorMessage = null;
		//Check map connectivity
		if (!checkConnection(adjacencyList)) errorMessage="Important error: The whole map is not a connected graph.\n";
		//Check continents connectivity
		for (Continent loopContinent: continents){
			if (countries.get(loopContinent.continentID).size()==0){
				errorMessage+="Important error: The continent <"+loopContinent.continentName+"> has no country in it.\n";
				continue;
			}
			Map<Integer,ArrayList<Integer>> loopAdjacencyList = new HashMap<Integer,ArrayList<Integer>>();
			for (Country loopCountry: countries.get(loopContinent.continentID)){
				loopAdjacencyList.put(loopCountry.countryID, new ArrayList<Integer>());
				for (Integer neighbour: adjacencyList.get(loopCountry.countryID)){
					if (findCountryByID(neighbour).belongToContinent.continentID==loopContinent.continentID){
						loopAdjacencyList.get(loopCountry.countryID).add(neighbour);
					}
				}
			}
			if (!checkConnection(loopAdjacencyList)) errorMessage+="Important error: The continent <"+loopContinent.continentName+"> is not a connected graph.\n";
		}

		if (errorMessage!=null) {
			if (mode==1){
				if (JOptionPane.showConfirmDialog(null,
						errorMessage+"Do you still want to open the map and correct them?",
						"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
					return false;
			}
			else {
				JOptionPane.showMessageDialog(null,errorMessage);
				return false;
			}
		}

		boolean warn = false;
		if ((this.author==null)||(this.wrap==null)||(this.scroll==null)||(this.image==null)||(this.warn==null)){
			warn = true;
		}
		else {
			Set<String> validValues = new HashSet<String>();
			validValues.add("yes");
			validValues.add("no");
			if (!validValues.contains(this.wrap)||!validValues.contains(this.warn)){
				warn = true;
			}
			validValues.clear();
			validValues.add("horizontal");
			validValues.add("vertical");
			validValues.add("none");
			if (!validValues.contains(this.scroll)){
				warn = true;
			}
		}
		if (warn) {
			JOptionPane.showMessageDialog(null,"There are some warnings");
		}
		return true;
	}


	public boolean loadMapFile(String mapFileName) {
		BufferedReader br = null;
		String inputLine = null;
		int rowNumber = 0;
		String dateArea = "none";//none, Map, Continents, Territories;
		Map<String,ArrayList<String>> countriesList = new HashMap<String,ArrayList<String>>();
		try{
			br = new BufferedReader(new FileReader(mapFileName));
			this.riskMapName = (mapFileName.substring(mapFileName.lastIndexOf("\\")+1,mapFileName.lastIndexOf(".")));
			while ((inputLine = br.readLine()) != null){
				rowNumber++;
				inputLine = inputLine.trim();
				int index;
				if (!inputLine.isEmpty()){
					switch (inputLine){
						case "[Map]":
							dateArea = "Map";
							break;
						case "[Continents]":
							dateArea = "Continents";
							break;
						case "[Territories]":
							dateArea = "Territories";
							break;
						default:
							switch (dateArea){
								case "Map":
									index = inputLine.indexOf("=");
									if (index!=-1){
										String keyword = inputLine.substring(0,index).trim().toLowerCase();
										String value = inputLine.substring(index+1).trim();
										if (!keyword.isEmpty()){
											switch (keyword){
												case "author":
													if (!value.isEmpty()) this.author = value;
													break;
												case "warn":
													if (!value.isEmpty()) this.warn = value;
													break;
												case "image":
													if (!value.isEmpty()) this.image = value;
													break;
												case "wrap":
													if (!value.isEmpty()) this.wrap = value;
													break;
												case "scroll":
													if (!value.isEmpty()) this.scroll = value;
													break;
											}
										}
									}
									break;

								case "Continents":
									index = inputLine.indexOf("=");
									if (index!=-1){
										String continentName = inputLine.substring(0,index).trim();
										String controlNum = inputLine.substring(index+1).trim();
										if (!continentName.isEmpty()){
											Pattern pattern = Pattern.compile("[0-9]*");
											if (pattern.matcher(controlNum).matches()){
												if (!this.addContinent(continentName,Integer.parseInt(controlNum))) return false;
											}
											else {
												JOptionPane.showMessageDialog(null,"Continent <"+continentName+">'s control number is invalid.");
												return false;
											}
										}
									}
									break;

								case "Territories":
									String[] countryInfo = inputLine.split(",");
									if (countryInfo.length>=4){
										String countryName = countryInfo[0].trim();
										String belongContinentName = countryInfo[3].trim();
										if (countryName.isEmpty()){
											JOptionPane.showMessageDialog(null,"Fatal error in line "+rowNumber+", country name can't be empty");
											return false;
										}
										if (belongContinentName.isEmpty()){
											JOptionPane.showMessageDialog(null,"Fatal error in line "+rowNumber+", continent name can't be empty");
											return false;
										}
										Pattern pattern = Pattern.compile("[0-9]*");
										if (!pattern.matcher(countryInfo[1].trim()).matches()||!pattern.matcher(countryInfo[2].trim()).matches()){
											JOptionPane.showMessageDialog(null,"Fatal error in line "+rowNumber+", coordinate must be integer.");
											return false;
										}
										if (!this.addCountry(countryName,belongContinentName,Integer.parseInt(countryInfo[1].trim()),
												Integer.parseInt(countryInfo[2].trim()))) return false;
										countriesList.put(countryName, new ArrayList());
										for (int i=4;i<countryInfo.length;i++){
											if (countriesList.get(countryName).contains(countryInfo[i].trim())){
												JOptionPane.showMessageDialog(null,"Fatal error in line "+rowNumber+", duplicate record in adjacency list.");
												return false;
											}
											countriesList.get(countryName).add(countryInfo[i].trim());
										}
									}
									else{
										JOptionPane.showMessageDialog(null,"Fatal error in line "+rowNumber+", not enough data.");
										return false;
									}
							}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		for (String loopCountry : countriesList.keySet()) {
			ArrayList<String> neighbours = countriesList.get(loopCountry);
			for (String loopNeighbour:neighbours){
				if (!countriesList.containsKey(loopNeighbour)){
					JOptionPane.showMessageDialog(null,"Fatal error, country <"+loopNeighbour+"> does not exist.");
					return false;
				}
				if (!countriesList.get(loopNeighbour).contains(loopCountry)){
					JOptionPane.showMessageDialog(null,"Fatal error, the connection between country <"+loopCountry+"> and <"+loopNeighbour+"> is not paired.");
					return false;
				}
				adjacencyList.get(findCountry(loopCountry).countryID).add(findCountry(loopNeighbour).countryID);
			}
		}

		return checkValid(1);
	}

	public void saveToFile(String mapFileName) {
		File outputFile = new File(mapFileName);
		FileWriter fw = null;
		try{
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\n");
			fw.write("author="+this.author+"\r\n");
			fw.write("warn="+this.warn+"\r\n");
			fw.write("image="+this.image+"\r\n");
			fw.write("wrap="+this.wrap+"\r\n");
			fw.write("scroll="+this.scroll+"\r\n");
			fw.write("\r\n");
			fw.write("[Continents]\r\n");
			fw.close();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
