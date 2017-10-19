package mapelements;

/**
 * This is a base class used to record all id and number of continents and aountries.
 */
public class NodeRecord {
	public int ID;
	public int Number;
	
	/**
	 * Constructor for the class.
	 * @param ID	ID of countries or continents.
	 * @param number	Number of countries in continent.
	 */
	public NodeRecord(int ID, int number){
		this.ID = ID;
		this.Number = number;
	}
}
