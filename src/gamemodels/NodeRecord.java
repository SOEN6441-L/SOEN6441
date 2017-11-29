package gamemodels;

import java.util.Observable;

/**
 * This is a base class used to record brief information of 
 * a country, is used in all phases of a game.
 */
public class NodeRecord extends Observable{

	private String name;
	private int Number;
	
	/**
	 * Constructor for the class.
	 * @param name	name of country
	 * @param number armies number on a country
	 */
	public NodeRecord(String name, int number){
		this.setName(name);
		this.setNumber(number);
	}
	/**
	 * The getter of Name, used to return name value
	 * @return name name of node record
	 */
	public String getName() {
		return name;
	}
	/**
	 * The setter of Name, used to set name value
	 * @param name node's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * The getter of Number, used to return number value
	 * @return number number of node record
	 */
	public int getNumber() {
		return Number;
	}
	/**
	 * The setter of Number, used to set number value
	 * @param number node's number
	 */
	public void setNumber(int number) {
		Number = number;
	}
}
