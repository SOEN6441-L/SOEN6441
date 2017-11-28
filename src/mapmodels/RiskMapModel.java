package mapmodels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * This is class RiskMap module to represent a map in Risk Game.
 */
public class RiskMapModel extends Observable implements Serializable{

	private static final long serialVersionUID = 2L;
	/**indicate if the map has been changed since last save*/
	private boolean modified;
    private String riskMapName; 
    /**basic info needed by map file*/
    private String author, warn, image, wrap, scroll;
    /**all the continents on this map*/
    private ArrayList<ContinentModel> continents; 
    /**how many countries on this map*/
    private int countryNum; 
    /**store all the connections*/
    private Map<CountryModel,ArrayList<CountryModel>> adjacencyList;
    private int globalID;

    /**
     * The constructor of class RiskMap.
     */
    public RiskMapModel(){
    	this.globalID = 1;
    	this.setAuthor("");
		this.setWarn("");
		this.setWrap("");
		this.setImage("");
		this.setScroll("");   
		
    	this.setModified(false);
    	this.continents = new ArrayList<ContinentModel>();
    	this.countryNum = 0;
    	this.adjacencyList = new HashMap<CountryModel,ArrayList<CountryModel>>();
    }

	/**
	 * Method to initialize map object.
	 * @param name the name of map
	 */
    public void initMapModel(String name) {
		this.riskMapName = name;
		this.globalID = 1;
		this.setAuthor("Invincible Team Four");
		this.setWarn("yes");
		this.setWrap("no");
		this.setImage("none");
		this.setScroll("none");
		
		this.setModified(false);
		this.continents.clear();;
		this.countryNum = 0;
		this.adjacencyList.clear();
		
		setChanged();
		notifyObservers(4);
    }
    
	/**
	 * Method to get map's name.
	 * @return map's name
	 */
	public String getRiskMapName() {
		return riskMapName;
	}
	
	/**
	 * Method to check if the map has been modified since last save.
	 * @return modification status
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Method to set modification status, indicates whether the map has been modified since last save.
	 * @param modified modification status
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * Method to get how many countries in this map.
	 * @return countries number
	 */
	public int getCountryNum() {
		return countryNum;
	}

	/**
	 * Method to get map's author.
	 * @return map's author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Method to set map's author.
	 * @param author name
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * Method to set map's parameter "warn".
	 * @param warn value of parameter "warn"
	 */	
	public void setWarn(String warn) {
		this.warn = warn;
	}
	
	/**
	 * Method to get map's parameter "warn".
	 * @return map's parameter "warn"
	 */	
	public String getWarn() {
		return warn;
	}

	/**
	 * Method to set map's parameter "image".
	 * @param image value of parameter "image"
	 */	
	public void setImage(String image) {
		this.image = image;
	}
	
	/**
	 * Method to get map's parameter "image".
	 * @return map's parameter "image"
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Method to set map's parameter "wrap".
	 * @param wrap value of parameter "wrap"
	 */		
	public void setWrap(String wrap) {
		this.wrap = wrap;
	}
	
	/**
	 * Method to get map's parameter "wrap".
	 * @return map's parameter "wrap"
	 */
	public String getWrap() {
		return wrap;
	}
	
	/**
	 * Method to set map's parameter "scroll".
	 * @param scroll value of parameter "scroll"
	 */	
	public void setScroll(String scroll) {
		this.scroll = scroll;
	}

	/**
	 * Method to get map's parameter "scroll".
	 * @return map's parameter "scroll"
	 */
	public String getScroll() {
		return scroll;
	}	

	/**
	 * Method to get map's continent list.
	 * @return continent list
	 */
	public ArrayList<ContinentModel> getContinents() {
		return continents;
	}
	
	/**
	 * Method to get map's Adjacency List.
	 * @return Adjacency List
	 */
	public Map<CountryModel,ArrayList<CountryModel>> getAdjacencyList() {
		return adjacencyList;
	}
	
	/**
	 * Method to find the country according to the country's name.
	 * @param countryName country's name
	 * @return country that found or null if not exits
	 */
	public CountryModel findCountry(String countryName) {
		CountryModel country = null;
		for (ContinentModel loopContinent : getContinents()) {
			country = loopContinent.findCountry(countryName);
			if (country!=null) return country; 
		}
		return null;
	}
	
	/**
	 * Method to find the country according to the country's ID.
	 * @param countryID country's unique ID
	 * @return country that found or null if not exits
	 */
	public CountryModel findCountryByID(int countryID) {
		CountryModel country = null;
		for (ContinentModel loopContinent : getContinents()) {
			country = loopContinent.findCountryByID(countryID);
			if (country!=null) return country; 
		}
		return null;
	}	

	/**
	 * Method to find the continent according to the continent's name.
	 * @param continentName continent's name
	 * @return continent that found or null if not exits
	 */
	public ContinentModel findContinent(String continentName) {
		continentName = continentName.toLowerCase();
		for (ContinentModel loopContinent:getContinents()){
			if (loopContinent.getName().equals(continentName)){
				return loopContinent;
			}
		}
		return null;
	}

    /**
     * Method to add a new continent.
     * @param continentName name of the new continent
     * @param controlNum control number of the new continent
     * @return succeed or failed with an error message
     */
	public ErrorMsg addContinent(String continentName, int controlNum){
    	if (findContinent(continentName)!=null) {
    		return new ErrorMsg(1, "Continnet <"+continentName+"> already exists");
    	}
    	ContinentModel newContinent = new ContinentModel(continentName);
    	newContinent.setControlNum(controlNum);
    	getContinents().add(newContinent);
    	this.setModified(true);
    	setChanged();
    	notifyObservers(0);
		return new ErrorMsg(0, null);
    }
	
	/**
	 * Method to rename a continent.
	 * @param continentName name of continent want to rename
	 * @param newName new name for the continent
	 * @return succeed or failed with an error message
	 */
	public ErrorMsg renameContinent(String continentName, String newName){
		ContinentModel changeContinent = findContinent(continentName);
		if(changeContinent == null){
			return new ErrorMsg(1, "Continent  '"+continentName+"'  you want to change does not exists");
		}
		if (findContinent(newName)!=null){
			return new ErrorMsg(2, "Continent '"+newName+"' already exits");
		};
		
		changeContinent.setName(newName);
		this.setModified(true);
		setChanged();
		notifyObservers(0);
		return new ErrorMsg(0, null);
	}

	/**
	 * Method to change the control number of a continent.
	 * @param continentName name of the continent want to change
	 * @param controlNum new control number
	 * @return succeed or failed with an error message
	 */
	public ErrorMsg changeControlNum(String continentName, int controlNum){
		ContinentModel changeContinent = findContinent(continentName);
		if(changeContinent == null){
			return new ErrorMsg(1, "Continent  '"+continentName+"'  you want to change does not exists");
		}
		
		changeContinent.setControlNum(controlNum);
		this.setModified(true);
		setChanged();
		notifyObservers(0);
		return new ErrorMsg(0, null);
	}

	/**
	 * Method to delete a continent.
	 * @param continentName name of the continent want to delete
	 * @return succeed or failed with an error message
	 */
	public ErrorMsg deleteContinent(String continentName){
		ContinentModel deleteContinent = findContinent(continentName);
		if(deleteContinent != null){
			if(deleteContinent.getCountryList().size()>0){
				return new ErrorMsg(1, "Continent '"+continentName+"' is not empty, you need to delete/move all the countries in it.");
			}
			getContinents().remove(deleteContinent);
			deleteContinent = null;
			this.setModified(true);
			setChanged();
			notifyObservers(0);
		}
		return new ErrorMsg(0, null);
	}

    /**
     * Method to add a new country to an existing continent.
     * @param countryName name of the new country
     * @param continentName name of the existing continent that the new country adding in
     * @param coordinateX the X coordinate of the country
     * @param coordinateY the Y coordinate of the country
     * @return succeed or failed with an error message
     */
	public ErrorMsg addCountry(String countryName,String continentName,int coordinateX, int coordinateY){		
    	ContinentModel targetContinent = findContinent(continentName);
    	if (targetContinent==null) {
    		return new ErrorMsg(1, "Continnet <"+continentName+"> does not exists");
    	}

    	if (findCountry(countryName)!=null){
    		return new ErrorMsg(2,"Country <"+countryName+"> already exists");
    	}

    	CountryModel newCountry = new CountryModel(globalID++,countryName,targetContinent);
    	newCountry.setCoordinate(coordinateX, coordinateY);
    	
    	targetContinent.addCountry(newCountry);
    	getAdjacencyList().put(newCountry, new ArrayList<CountryModel>());
    	
    	countryNum ++;
    	this.setModified(true);
    	setChanged();
    	notifyObservers(2);
    	return new ErrorMsg(0, null);
    }
	
	/**
	 * Method to rename a country.
	 * @param countryName name of the country want to rename
	 * @param newName new name for the Country
	 * @return succeed or failed with an error message
	 */
	public ErrorMsg renameCountry(String countryName, String newName){
		CountryModel changeCountry = findCountry(countryName);
		if(changeCountry == null){
			return new ErrorMsg(1,"Country  '"+countryName+"' you want to change does not exists");
		}
		
		if (findCountry(newName)!=null){
			return new ErrorMsg(2,"Country '"+newName+"' already exits");
		};
		
		changeCountry.setName(newName);
		this.setModified(true);
    	setChanged();
    	notifyObservers(2);
		return new ErrorMsg(0, null);
	}

	/**
	 * Method to move a country to another continent.
	 * @param toContinentName name of the continent want to move the country in
	 * @param countryName name of the country that need to be moved
	 * @return succeed or failed with an error message
	 */
	public ErrorMsg moveCountry(String toContinentName, String countryName) {
		CountryModel moveCountry = findCountry(countryName);
		if(moveCountry == null){
			return new ErrorMsg(1, "Country  '"+moveCountry+"' you want to move does not exists");
		}
		
		ContinentModel toContinent = findContinent(toContinentName);
		if(toContinent == null){
			return new ErrorMsg(2, "Continent  '"+toContinentName+"'  you want move to does not exists");
		}
		
		moveCountry.getBelongTo().getCountryList().remove(moveCountry);
		if (moveCountry.getBelongTo()!=toContinent){
			toContinent.getCountryList().add(moveCountry);
			moveCountry.setBelongTo(toContinent);
			this.setModified(true);
	    	setChanged();
	    	notifyObservers(2);
		}	
		return new ErrorMsg(0, null);
	}
	
	/**
	 * Method to delete an existing country.
	 * @param countryName name of country want to delete
	 * @return succeed or failed
	 */
	public boolean deleteCountry(String countryName){
		CountryModel deleteCountry = findCountry(countryName);
		if(deleteCountry == null) return false;
		
		deleteCountry.getBelongTo().deleteCountry(deleteCountry);

		//delete relative connections
		ArrayList<CountryModel> neighbours = getAdjacencyList().get(deleteCountry);
		for(CountryModel neighbour: neighbours){
			getAdjacencyList().get(neighbour).remove(deleteCountry);
		}
		getAdjacencyList().remove(deleteCountry);
		countryNum --;
		deleteCountry = null;
		this.setModified(true);
		setChanged();
		notifyObservers(2);
		return true;
	}
	
	/**
	 * Method to add connection between two countries.
	 * @param countryNameFrom name of the country that the connection is from
	 * @param countryNameTo name of the country that the connection is to
	 * @return succeed or failed with an error message
	 */
	public ErrorMsg addConnections(String countryNameFrom,String countryNameTo){
		if (countryNameFrom.equals(countryNameTo)){
			return new ErrorMsg(2, "Country  '"+countryNameFrom+"' can not have self-connection");			
		}
		CountryModel fromCountry = findCountry(countryNameFrom);
		if (fromCountry==null){
			return new ErrorMsg(1, "Country  '"+countryNameFrom+"' does not exists");
		}
		
		CountryModel toCountry = findCountry(countryNameTo);
		if (toCountry==null){
			return new ErrorMsg(1, "Country  '"+countryNameTo+"' does not exists");
		}
		if (getAdjacencyList().get(fromCountry).contains(toCountry)&&getAdjacencyList().get(toCountry).contains(fromCountry)){
			return new ErrorMsg(0, "Connection from '"+countryNameFrom+" to "+countryNameTo+"' already exists");			
		}
		if (getAdjacencyList().get(fromCountry).contains(toCountry)||getAdjacencyList().get(toCountry).contains(fromCountry)){
			getAdjacencyList().get(fromCountry).remove(toCountry);
			getAdjacencyList().get(toCountry).remove(fromCountry);
		}
		getAdjacencyList().get(fromCountry).add(toCountry);
		getAdjacencyList().get(toCountry).add(fromCountry);
		this.setModified(true);
		return new ErrorMsg(0, null);
	}

	/**
	 * Method to add completed connections.
	 */
	public void addCompletedConnection(){
		for (CountryModel loopCountry:getAdjacencyList().keySet()){
			ArrayList<CountryModel> loopCountryList = getAdjacencyList().get(loopCountry);
			loopCountryList.clear();
			for (CountryModel candidateCountry:getAdjacencyList().keySet()){
				if (candidateCountry!=loopCountry){
					loopCountryList.add(candidateCountry);
				}
			}
		}
		setChanged();
		notifyObservers(1);
		this.setModified(true);
	}

	/**
	 * Method to remove the connection between two countries.
	 * @param countryNameFrom name of the country that the connection is from
	 * @param countryNameTo name of the country that the connection is to
	 * @return succeed or failed with an error message
	 */
	public ErrorMsg removeConnection(String countryNameFrom,String countryNameTo){
		CountryModel fromCountry = findCountry(countryNameFrom);
		if (fromCountry==null){
			return new ErrorMsg(1, "Country  '"+countryNameFrom+"' does not exists");
		}
		
		CountryModel toCountry = findCountry(countryNameTo);
		if (toCountry==null){
			return new ErrorMsg(1, "Country  '"+countryNameTo+"' does not exists");
		}
		
		if (countryNameFrom.equals(countryNameTo)){
			if (getAdjacencyList().get(fromCountry).remove(toCountry)) this.setModified(true);
			return new ErrorMsg(2, "Country  '"+countryNameFrom+"' should not have self-connection");			
		}
				
		if (getAdjacencyList().get(fromCountry).remove(toCountry)) this.setModified(true);
		//if (getAdjacencyList().get(toCountry).remove(fromCountry)) this.setModified(true);
		return new ErrorMsg(0, null);
	}

	/**
	 * Method to remove all connections.
	 */
	public void removeAllConnection(){
		for (ArrayList<CountryModel> loopCountryList:getAdjacencyList().values()){
			loopCountryList.clear();
		}
		setChanged();
		notifyObservers(1);
		this.setModified(true);
	}

    /**
     * Method to check connectivity of a graph (can be the map or a subgraph of the map).
     * @param localAdjacencyList adjacency list to be checked
     * @return connected or not
     */
	public boolean checkConnection(Map<CountryModel,ArrayList<CountryModel>> localAdjacencyList) {
		return (findPath(localAdjacencyList, null)==localAdjacencyList.size());
	}
	
    /**
     * Method to initialize a DFS search to find path or check connectivity.
     * @param localAdjacencyList adjacency list to be checked
     * @param sourceNode node from which to start DFS search 
     * @return the number of nodes can be reached from sourceNode
     */	
	public int findPath(Map<CountryModel,ArrayList<CountryModel>> localAdjacencyList,CountryModel sourceNode) {
		if (localAdjacencyList.size()==0) return 0;
		for (CountryModel loopCountry : localAdjacencyList.keySet()) {
			loopCountry.setFlagDFS(false);
			if (sourceNode==null) sourceNode = loopCountry;
		}		
		return DFS(localAdjacencyList,sourceNode,0);
	}
	
    /**
     * Method of Depth First Search, recursive implementation.
     * @param localAdjacencyList adjacency list to be checked
     * @param sourceNode node from which to continue DFS search 
     * @param findCountries the number of nodes already visited before this step 
     * @return the number of nodes already visited after this step
     */
	public int DFS(Map<CountryModel,ArrayList<CountryModel>> localAdjacencyList, CountryModel sourceNode, int findCountries) {
		sourceNode.setFlagDFS(true);
		findCountries++;
		for (CountryModel targetNode : localAdjacencyList.get(sourceNode)){
			if (!targetNode.isFlagDFS()){
				findCountries = DFS(localAdjacencyList,targetNode,findCountries);
			}
		}
		return findCountries;
	}

    /**
     * Method to check whether the map has errors, often called after successfully call loadMapFile method, 
     * concerning the connectivity of whole map and each continent, also report empty continents.
     * @return succeed or failed with an error message
     * @see #checkWarnings
     * @see #loadMapFile(String)
     * @see #saveToFile(String)
     */
	public ErrorMsg checkErrors(){
		String errorMessage = null;
		if ((getContinents().size()==0)||(getAdjacencyList().size()==0)){
			errorMessage = "Error: There is no countries.\n";
		}
		else{
			//Check map connectivity
			if (!checkConnection(getAdjacencyList())) errorMessage="Error: The whole map is not a connected graph.\n";
			//Check continents connectivity
			for (ContinentModel loopContinent: getContinents()){
				if (loopContinent.getCountryList().size()==0){
					if (errorMessage == null)
						errorMessage = "Error: The continent <"+loopContinent.getShowName()+"> has no country in it.\n";
					else
						errorMessage += "Error: The continent <"+loopContinent.getShowName()+"> has no country in it.\n";
					continue;
				}
				Map<CountryModel,ArrayList<CountryModel>> loopAdjacencyList = new HashMap<CountryModel,ArrayList<CountryModel>>();
				for (CountryModel loopCountry: loopContinent.getCountryList()){
					loopAdjacencyList.put(loopCountry, new ArrayList<CountryModel>());
					for (CountryModel neighbour: getAdjacencyList().get(loopCountry)){
						if (neighbour.getBelongTo()==loopContinent){
							loopAdjacencyList.get(loopCountry).add(neighbour);
						}
					}
				}
				if (!checkConnection(loopAdjacencyList)){
					if (errorMessage == null)
						errorMessage="Error: The continent <"+loopContinent.getShowName()+"> is not a connected graph.\n";
					else errorMessage+="Error: The continent <"+loopContinent.getShowName()+"> is not a connected graph.\n";
				}
			}
		}
		return new ErrorMsg(errorMessage==null?0:1,errorMessage);
	}	
		
	/**
	 * Method to check whether the map has warnings, concerning the basic information of a map: author, warn, wrap, image and scroll.
	 * @return 5 bits integer, each bit contains the warning status of each of 5 parameters
	 * @see #checkErrors
     * @see #loadMapFile(String)
     * @see #saveToFile(String)
	 */
	public int checkWarnings(){
		int checkWarning = 0;
		if (this.getAuthor()==null||this.getAuthor().isEmpty()){
			checkWarning += 1;
			this.setAuthor("anonymous");
		}	
		
		Set<String> validValues = new HashSet<String>();
		validValues.add("yes");
		validValues.add("no");	
		
		if (this.warn==null||this.warn.isEmpty()||!validValues.contains(this.warn)){
			checkWarning += 2;
			this.setWarn("yes");
		}
	
		if (this.image==null||this.image.isEmpty()){
			checkWarning += 4;
			this.setImage("none");
		}		
		if (this.wrap==null||this.wrap.isEmpty()||!validValues.contains(this.wrap)){
			checkWarning += 8;
			this.setWrap("no");
		}
		
		validValues.clear();
		validValues.add("horizontal");
		validValues.add("vertical");
		validValues.add("none");		
		
		if (this.scroll==null||this.scroll.isEmpty()||!validValues.contains(this.scroll)){
			checkWarning += 16;
			this.setScroll("none");
		}
		
		return checkWarning;		
	}

    /**
     * Method to save map to a file, need to call checkErrors() and checkWarnings() firstly, 
     * failed due to file operation errors.
     * @param mapFileName The name of map file
     * @return succeed or failed with error message
     * @see #checkErrors()
     * @see #checkWarnings()
     * @see #loadMapFile(String)
     */
	public ErrorMsg saveToFile(String mapFileName) {
		File outputFile = new File(mapFileName);
		FileWriter fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\n");
			fw.write("author="+this.getAuthor()+"\r\n");
			fw.write("warn="+this.warn+"\r\n");
			fw.write("image="+this.image+"\r\n");
			fw.write("wrap="+this.wrap+"\r\n");
			fw.write("scroll="+this.scroll+"\r\n");
			fw.write("\r\n");
			fw.write("[Continents]\r\n");
			for (ContinentModel loopContinent : getContinents()){
				fw.write(loopContinent.getShowName()+"="+loopContinent.getControlNum()+"\r\n");
			}
			fw.write("\r\n");
			fw.write("[Territories]\r\n");
			for (ContinentModel loopContinent : getContinents()){
				for (CountryModel loopCountry : loopContinent.getCountryList()){
					ArrayList<CountryModel> neighbours = getAdjacencyList().get(loopCountry);
					fw.write(loopCountry.getShowName()+","+loopCountry.getCoordinate()[0]+","+loopCountry.getCoordinate()[1]+","+loopContinent.getShowName());
					for (CountryModel neighbour : neighbours){
						fw.write(","+neighbour.getShowName());
					}
					fw.write("\r\n");
				}
				fw.write("\r\n");
			}	
			fw.close();
			this.riskMapName = mapFileName.substring(mapFileName.lastIndexOf("\\")+1,mapFileName.lastIndexOf(".")); 
			this.setModified(false);
		}catch (IOException e) {
			//e.printStackTrace();
			return new ErrorMsg(1,"Fatal error: Not a file or File access error.");			
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		setChanged();
		notifyObservers(0);
		return new ErrorMsg(0, null);
	}
	
    /**
     * Method to load map file data into defined data structure, get ready for further validation for errors and warnings.
     * Failed due to fatal errors, such as duplicate continents, duplicate countries, 
     * duplicate or invalid countries in adjacency list, connection not paired, or invalid data format.
     * @param mapFileName The name of map file
     * @return succeed or not with the error message
	 * @see #checkErrors
     * @see #checkWarnings
     * @see #saveToFile(String)
     */
	public ErrorMsg loadMapFile(String mapFileName) { 
		ErrorMsg errorMsg;
		BufferedReader br = null;
		String inputLine = null;
		int rowNumber = 0;
		String dateArea = "none";//none, Map, Continents, Territories;
		Map<String,ArrayList<String>> countriesList = new HashMap<String,ArrayList<String>>();
		//begin to handle file line by line, check format errors, duplicate continent or country error
		try{
			br = new BufferedReader(new FileReader(mapFileName));
			if (mapFileName.lastIndexOf("\\")+1>=mapFileName.lastIndexOf("."))
				this.riskMapName = mapFileName.substring(mapFileName.lastIndexOf("\\")+1);
			else
				this.riskMapName = mapFileName.substring(mapFileName.lastIndexOf("\\")+1,mapFileName.lastIndexOf("."));
			while ((inputLine = br.readLine()) != null){
				rowNumber++;
				inputLine = inputLine.trim();
				int index;
				if (!inputLine.isEmpty()){
					//first level: distinguish data section
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
						case "Map"://for [map] section
							index = inputLine.indexOf("=");
							if (index!=-1){
								String keyword = inputLine.substring(0,index).trim().toLowerCase();
								String value = inputLine.substring(index+1).trim();
								if (!keyword.isEmpty()){
									switch (keyword){
									case "author":
										if (!value.isEmpty()) this.setAuthor(value);
										break;
									case "warn":
										if (!value.isEmpty()) this.setWarn(value);
										break;
									case "image":
										if (!value.isEmpty()) this.setImage(value);
										break;
									case "wrap":
										if (!value.isEmpty()) this.setWrap(value);
										break;
									case "scroll":
										if (!value.isEmpty()) this.setScroll(value);
										break;
									default:
										return new ErrorMsg(1,"Fatal error in line "+rowNumber+": Invalid format.");
									}
								}
							}
							else{
								return new ErrorMsg(1,"Fatal error in line "+rowNumber+": Invalid format.");
							}
							break;
							
						case "Continents"://for [continent] section
							index = inputLine.indexOf("=");
							if (index!=-1){
								String continentName = inputLine.substring(0,index).trim();
								String controlNum = inputLine.substring(index+1).trim();
								if (continentName==null||controlNum==null||continentName.isEmpty()||controlNum.isEmpty())
									return new ErrorMsg(1, "Fatal error in line "+rowNumber+": Invalid format.");							
								Pattern pattern = Pattern.compile("[0-9]*");
								if (pattern.matcher(controlNum).matches()){
									if (!(errorMsg = this.addContinent(continentName,Integer.parseInt(controlNum))).isResult())
										return new ErrorMsg(2,"Fatal error in line "+rowNumber+": "+errorMsg.getMsg());
								}
								else {
									return new ErrorMsg(3,"Fatal error in line "+rowNumber+": Continent <"+continentName+">'s control number must be integer.");
								}
							}	
							else{
								return new ErrorMsg(1,"Fatal error in line "+rowNumber+": Invalid format.");							
							}							
							break;

						case "Territories"://for [territories] section
							String[] countryInfo = inputLine.split(",");
							if (countryInfo.length>=4){
								String countryName = countryInfo[0].trim();
								String belongContinentName = countryInfo[3].trim();
								if (countryName.isEmpty())
									return new ErrorMsg(4,"Fatal error in line "+rowNumber+": Country name can't be empty");
								if (belongContinentName.isEmpty())
									return new ErrorMsg(5,"Fatal error in line "+rowNumber+": Continent name can't be empty");
								Pattern pattern = Pattern.compile("[0-9]*");
								if (!pattern.matcher(countryInfo[1].trim()).matches()||!pattern.matcher(countryInfo[2].trim()).matches())
									return new ErrorMsg(6,"Fatal error in line "+rowNumber+": Coordinates must be integer.");
								if (!(errorMsg=this.addCountry(countryName,belongContinentName,Integer.parseInt(countryInfo[1].trim()),
										Integer.parseInt(countryInfo[2].trim()))).isResult())									
									return new ErrorMsg(7,"Fatal error in line "+rowNumber+": "+errorMsg.getMsg());									
								countriesList.put(countryName.toLowerCase(), new ArrayList<String>());
								for (int i=4;i<countryInfo.length;i++){
									if (countriesList.get(countryName.toLowerCase()).contains(countryInfo[i].trim().toLowerCase()))
										return new ErrorMsg(8,"Fatal error in line "+rowNumber+": Duplicate record in adjacency list.");
									countriesList.get(countryName.toLowerCase()).add(countryInfo[i].trim().toLowerCase());
								}
							}
							else{
								return new ErrorMsg(9,"Fatal error in line "+rowNumber+": Not enough data.");
							}
							break;
							
						default://others
							return new ErrorMsg(1,"Fatal error in line "+rowNumber+": Invalid format.");	
						}
					}
				}
			}
		} catch (IOException e) {
			return new ErrorMsg(13,"Fatal error: File not exist or file access error.");
			//e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}   
		//check other fatal errors
		for (String loopCountry : countriesList.keySet()) {
			ArrayList<String> neighbours = countriesList.get(loopCountry);
			for (String loopNeighbour:neighbours){
				if (loopCountry.equals(loopNeighbour))
					return new ErrorMsg(12, "Fatal error: Country <"+loopCountry+"> can't have self-connection.");
				if (!countriesList.containsKey(loopNeighbour))
					return new ErrorMsg(10, "Fatal error: Country <"+loopNeighbour+"> in <"+loopCountry+">'s adjacency list is not exist.");
				//if (!countriesList.get(loopNeighbour).contains(loopCountry))
				//	return new ErrorMsg(11, "Fatal error: The connection between country <"+loopCountry+"> and <"+loopNeighbour+"> is not paired.");
				getAdjacencyList().get(findCountry(loopCountry)).add(findCountry(loopNeighbour));
			}
		}

		return new ErrorMsg(0,null);
	}

	/**
	 * method to generate notify
	 */	
	public void generateNotify() {
		setChanged();
		notifyObservers(2);
	}
}
