package mapelements;

public class Country {
	public int countryID;
	public String countryName;
	public Continent belongToContinent;
	public int[] coordinate ={0,0};
	public boolean flagBFS;
	public int armyNumber;
	
	public Country(int ID, String name, Continent continent){
		this.countryID = ID;
		this.countryName = name;
		this.belongToContinent = continent;
		this.flagBFS = false;
		this.armyNumber = 0;
	}
	
	public void changeName(String name){
		this.countryName = name;
	}	
	
	public void changeContinent(Continent toContinent){
		this.belongToContinent = toContinent;
	}	
	
}

