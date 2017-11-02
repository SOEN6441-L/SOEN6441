package gameelements;

/**
 * This is a base class used to record brief information of 
 * a country, is used in all phases of a game.
 */
public class NodeRecord {
	public String name;
	public int Number;
	
	/**
	 * Constructor for the class.
	 * @param name	name of country
	 * @param number armies number on a country
	 */
	public NodeRecord(String name, int number){
		this.name = name;
		this.Number = number;
	}
}

