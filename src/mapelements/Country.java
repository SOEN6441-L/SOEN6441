package mapelements;

public class Country {
	public String countryName;
	public String belongToContinentName;
	public boolean flagBFS;
	
	public Country(String name, String continentName){
		this.countryName = name;
		this.belongToContinentName = continentName;
		flagBFS = false;
	}
	
}
