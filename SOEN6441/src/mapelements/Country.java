package mapelements;

public class Country {
	public int countryID;
	public String countryName;
	public Continent belongToContinent;
	public boolean flagBFS;
	
	public Country(int ID, String name, Continent continent){
		this.countryID = ID;
		this.countryName = name;
		this.belongToContinent = continent;
		flagBFS = false;
	}
	
}