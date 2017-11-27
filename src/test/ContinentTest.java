package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import gamemodels.PlayerModel;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;

/**
 * Test Class to test all methods defined in class Continent.
 * Including 7 methods and 24 test cases.
 */
public class ContinentTest {

	private ContinentModel continent;
	private PlayerModel player ,player2;
	private CountryModel country, country2;

	/**
	 * Set test environment before each test.
	 */
	@Before
	public void setEnvironment(){
		continent = new ContinentModel("testContinent");
		country = new CountryModel(1,"testCountry",continent);
		country2 = new CountryModel(2,"testCountry2",continent);
		player = new PlayerModel("testPlayer", Color.RED,null);
		player2 = new PlayerModel("testPlayer2", Color.DARK_GRAY,null);
	}
	
	/**
	 * Test constructor, getName(), getOwner(), getCountryList().<br>
	 * case 1: check all variables of the continent object created in @before step.<br>
	 * case 2: create another continent object with random name, check all variables again.
	 */
	@Test
	public void constructorTest() {
		//case 1:
		assertEquals("testContinent",continent.getShowName());
		assertEquals("testcontinent",continent.getName());
		assertEquals(0,continent.getControlNum());
		assertEquals(null,continent.getOwner());
		assertEquals(0,continent.getCountryList().size());
		//case 2:
		String name = "testContinent"+(int)(Math.random()*10);
		continent = new ContinentModel(name);
		assertEquals(name,continent.getShowName());
		assertEquals(name.toLowerCase(),continent.getName());
		assertEquals(0,continent.getControlNum());
		assertEquals(null,continent.getOwner());
		assertEquals(0,continent.getCountryList().size());
		System.out.println("Continent Test: constructor finished.");
	}

	/**
	 * Test setName(String).<br>
	 * case 1: check continent's name right after create.<br>
	 * case 2: call setName(String), then check continent's name again.
	 */
	@Test
	public void setNameTest() {
		//case 1:
		assertEquals("testContinent",continent.getShowName());
		assertEquals("testcontinent",continent.getName());
		//case 2:
		continent.setName("testContinent2");
		assertEquals("testContinent2",continent.getShowName());
		assertEquals("testcontinent2",continent.getName());
		System.out.println("Continent Test: setName(String) finished.");		
	}	
	
	/**
	 * Test setControlNum(int).<br>
	 * case 1: check continent's control number right after create.<br>
	 * case 2: call setControlNum(int), then check continent's control number again.
	 */
	@Test
	public void setControlNumTest() {
		//case 1:
		assertEquals(0,continent.getControlNum());
		continent.setControlNum(12);
		assertEquals(12,continent.getControlNum());
		System.out.println("Continent Test: setControlNum(int) finished.");
	}	

	/**
	 * Test checkOwner() and getOwner().<br>
	 * case 1: add two countries(without owner) to continent, call checkOwner(), then check the owner of continent.<br>
	 * case 2: assign both countries to the same player, call checkOwner(), then check the owner of continent.<br>
	 * case 3: assign both countries to another player, call checkOwner(), then check the owner of continent.<br>
	 * case 4: assign two countries to different player, call checkOwner(), then check the owner of continent.
	 */
	@Test
	public void checkOwnerTest() {
		//case 1:
		continent.addCountry(country);
		continent.addCountry(country2);
		continent.checkOwner();
		assertEquals(null,continent.getOwner());
		//case 2:
		country.setOwner(player);
		country2.setOwner(player);		
		continent.checkOwner();
		assertEquals(player,continent.getOwner());
		//case 3:
		country.setOwner(player2);
		country2.setOwner(player2);		
		continent.checkOwner();
		assertEquals(player2,continent.getOwner());
		//case 4:
		country.setOwner(player);
		country2.setOwner(player2);		
		continent.checkOwner();
		assertEquals(null,continent.getOwner());	
		System.out.println("Continent Test: checkOwner() and getOwner() finished.");
	}
	
	/**
	 * Test getCountryList().<br>
	 * case 1: check the CountryList size for an empty continent.<br>
	 * case 2: add a country to continent, check the CountryList size and member.<br>
	 * case 3: delete the first country, check the CountryList size again.<br>
	 * case 4: add another country to continent, check the CountryList size and member.<br>
	 * case 5: delete the second country, check the CountryList size again.<br>
	 * case 6: add both countries to continent, check the CountryList size and all members.
	 */
	@Test
	public void getCountryListTest() {
		//case 1:
		assertEquals(0,continent.getCountryList().size());
		//case 2:
		continent.addCountry(country);
		assertEquals(1,continent.getCountryList().size());
		assertEquals(country,continent.getCountryList().get(0));
		//case 3:
		continent.deleteCountry(country);
		assertEquals(0,continent.getCountryList().size());
		//case 4:
		continent.addCountry(country2);
		assertEquals(1,continent.getCountryList().size());
		assertEquals(country2,continent.getCountryList().get(0));
		//case 5:
		continent.deleteCountry(country2);
		assertEquals(0,continent.getCountryList().size());
		//case 6:
		continent.addCountry(country);
		continent.addCountry(country2);
		assertEquals(2,continent.getCountryList().size());
		assertEquals(country,continent.getCountryList().get(0));
		assertEquals(country2,continent.getCountryList().get(1));
		System.out.println("Continent Test: getCountryList() finished.");		
	}
	
	/**
	 * Test findCountry(String).<br>
	 * case 1: try to find a country in an empty continent.<br>
	 * case 2: add a country to continent, try to find this country and another country in the continent.<br>
	 * case 3: add another country to continent too, try to find this two countries in the continent.<br>
	 * case 4: delete the first country from continent, try to find this two countries in the continent.
	 */
	@Test
	public void findCountryTest() {
		//case 1:
		assertEquals(null,continent.findCountry(country.getName()));
		//case 2:
		continent.addCountry(country);
		assertEquals(country,continent.findCountry(country.getName()));
		assertEquals(null,continent.findCountry(country2.getName()));
		//case 3:
		continent.addCountry(country2);
		assertEquals(country,continent.findCountry(country.getName()));
		assertEquals(country2,continent.findCountry(country2.getName()));
		//case 4:
		continent.deleteCountry(country);
		assertEquals(null,continent.findCountry(country.getName()));
		assertEquals(country2,continent.findCountry(country2.getName()));
		System.out.println("Continent Test: findCountry(String) finished.");	
	}
	
	/**
	 * Test findCountryByID(int).<br>
	 * case 1: try to find a country in an empty continent.<br>
	 * case 2: add a country to continent, try to find this country and another country in the continent.<br>
	 * case 3: add another country to continent too, try to find this two countries in the continent.<br>
	 * case 4: delete the first country from continent, try to find this two countries in the continent.
	 */
	@Test
	public void findCountryByIDTest() {
		//case 1:
		assertEquals(null,continent.findCountryByID(country.getCountryId()));
		//case 2:
		continent.addCountry(country);
		assertEquals(country,continent.findCountryByID(country.getCountryId()));
		assertEquals(null,continent.findCountryByID(country2.getCountryId()));
		//case 3:
		continent.addCountry(country2);
		assertEquals(country,continent.findCountryByID(country.getCountryId()));
		assertEquals(country2,continent.findCountryByID(country2.getCountryId()));
		//case 4:
		continent.deleteCountry(country);
		assertEquals(null,continent.findCountryByID(country.getCountryId()));
		assertEquals(country2,continent.findCountryByID(country2.getCountryId()));
		System.out.println("Continent Test: findCountryByID(int) finished.");	
	}
	
	/**
	 * Test the Serialization of continent class
	 */
	@Test
	public void continentSerializableTest(){
		continent.addCountry(country);
		continent.addCountry(country2);
		country.setOwner(player);
		country2.setOwner(player);
		continent.checkOwner();
		try {
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("continent.bin"));
		    output.writeObject(continent);
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		 
		try {
		    @SuppressWarnings("resource")
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("continent.bin"));
		    ContinentModel newContinent = (ContinentModel) input.readObject();
		    assertEquals("testContinent",newContinent.getShowName());
		    assertEquals("testPlayer",newContinent.getOwner().getName());
		    assertEquals("testCountry",newContinent.getCountryList().get(0).getShowName());
		    assertEquals("testCountry2",newContinent.getCountryList().get(1).getShowName());
		} catch (Exception e) {
		    e.printStackTrace();
		} 
		
		System.out.println("Continent Test: Serializability test finished");
	}	
}