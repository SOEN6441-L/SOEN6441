package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import mapmodels.ContinentModel;
import mapmodels.CountryModel;
import mapmodels.ErrorMsg;
import mapmodels.RiskMapModel;

/**
 * Test Class to test all methods defined in class RiskMap.
 * Including 20 methods and 94 test cases.
 */
public class RiskMapTest {
	private RiskMapModel newMap, existingMap;

	/**
	 * Set test environment before each test.
	 */
	@Before
	public void setEnvironment(){
		newMap = new RiskMapModel();
		newMap.initMapModel("testMap");
	}
	
	/**
	 * Test constructor, initMapModel(String), getAuthor(), getWarn(), getWrap(), getImage(), getScroll(), isModified()
	 * , getContinent(), getCountryNum(), getAdjacencyList().<br>
	 * case 1: check all variables of the RiskMap object created with a name in @before step.<br>
	 * case 2: create another RiskMap object with random name, check all variables again.<br>
	 * case 3: create a new RiskMap object without a name, check all variables again.
	 */
	@Test
	public void constructorWithNameTest() {
		//case 1:
		assertEquals("testMap", newMap.getRiskMapName());
		assertEquals("Invincible Team Four",newMap.getAuthor());
		assertEquals("yes",newMap.getWarn());
		assertEquals("no",newMap.getWrap());
		assertEquals("none",newMap.getImage());
		assertEquals("none",newMap.getScroll());
		assertFalse(newMap.isModified());
		assertEquals(0,newMap.getContinents().size());
		assertEquals(0,newMap.getCountryNum());
		assertEquals(0,newMap.getAdjacencyList().size());
		//case 2:
		String name = "testMap"+(int)(Math.random()*10);
		newMap = new RiskMapModel();
		newMap.initMapModel(name);
		assertEquals(name, newMap.getRiskMapName());
		assertEquals("Invincible Team Four",newMap.getAuthor());
		assertEquals("yes",newMap.getWarn());
		assertEquals("no",newMap.getWrap());
		assertEquals("none",newMap.getImage());
		assertEquals("none",newMap.getScroll());
		assertFalse(newMap.isModified());
		assertEquals(0,newMap.getContinents().size());
		assertEquals(0,newMap.getCountryNum());
		assertEquals(0,newMap.getAdjacencyList().size());	
		System.out.println("RiskMap Test: constructor with name, getAuthor(),"
				+ " getWarn(), getWrap(), getImage(), getScroll(), isModified(),"
				+ " getContinent(), getCountryNum(), getAdjacencyList()finished.");
		//case 3:
		existingMap = new RiskMapModel();
		assertEquals(null,existingMap.getRiskMapName());
		assertTrue(existingMap.getAuthor().isEmpty());
		assertTrue(existingMap.getWarn().isEmpty());
		assertTrue(existingMap.getWrap().isEmpty());
		assertTrue(existingMap.getImage().isEmpty());
		assertTrue(existingMap.getScroll().isEmpty());
		assertFalse(existingMap.isModified());
		assertEquals(0,existingMap.getContinents().size());
		assertEquals(0,existingMap.getCountryNum());
		assertEquals(0,existingMap.getAdjacencyList().size());
		System.out.println("RiskMap Test: constructor without name finished.");
	}
	
	/**
	 * Test findCountry(String).<br>
	 * case 1: add two continents each has one country in it, call findCountry(String) 
	 * to find these two countries, check the name and belongTo parameter.<br>  
	 * case 2: try to find a country which is not in the map.
	 */
	@Test
	public void findCountryTest() {
		//case 1:
		newMap.addContinent("testContinent", 12);
		newMap.addCountry("testCountry", "testContinent", 10, 20);
		newMap.addContinent("testContinent2", 10);
		newMap.addCountry("testCountry2","testContinent2",30,40);
		
		ContinentModel firstContinent =  newMap.getContinents().get(0);
		ContinentModel secondContinent =  newMap.getContinents().get(1);
		
		CountryModel firstCountry = firstContinent.getCountryList().get(0);
		CountryModel secondCountry = secondContinent.getCountryList().get(0);		
		
		assertEquals(2,newMap.getContinents().size());
		assertEquals(2,newMap.getCountryNum());
		assertEquals(2,newMap.getAdjacencyList().size());
		
		assertEquals(firstCountry,newMap.findCountry(firstCountry.getShowName()));
		assertEquals(firstContinent,newMap.findCountry(firstCountry.getShowName()).getBelongTo());
		assertEquals(secondCountry,newMap.findCountry(secondCountry.getShowName()));
		assertEquals(secondContinent,newMap.findCountry(secondCountry.getShowName()).getBelongTo());
		//case 2:
		CountryModel thirdCountry = new CountryModel(3,"testCountry3",firstContinent);
		assertEquals(null,newMap.findCountry(thirdCountry.getShowName()));
		System.out.println("RiskMap Test: findCountry(String) finished.");
	}

	/**
	 * Test findCountryByID(int).<br>
	 * case 1: add two continents each has one country in it, call findCountryByID(int 
	 * to find these two countries, check the name and belongTo parameter.<br>  
	 * case 2: try to find a country which is not in the map.
	 */
	@Test
	public void findCountryByIDTest() {
		//case 1:
		newMap.addContinent("testContinent", 12);
		newMap.addCountry("testCountry", "testContinent", 10, 20);
		newMap.addContinent("testContinent2", 10);
		newMap.addCountry("testCountry2","testContinent2",30,40);
		
		ContinentModel firstContinent =  newMap.getContinents().get(0);
		ContinentModel secondContinent =  newMap.getContinents().get(1);
		
		CountryModel firstCountry = firstContinent.getCountryList().get(0);
		CountryModel secondCountry = secondContinent.getCountryList().get(0);		
		
		assertEquals(2,newMap.getContinents().size());
		assertEquals(2,newMap.getCountryNum());
		assertEquals(2,newMap.getAdjacencyList().size());
		
		assertEquals(firstCountry,newMap.findCountryByID(firstCountry.getCountryId()));
		assertEquals(firstContinent,newMap.findCountryByID(firstCountry.getCountryId()).getBelongTo());
		assertEquals(secondCountry,newMap.findCountryByID(secondCountry.getCountryId()));
		assertEquals(secondContinent,newMap.findCountryByID(secondCountry.getCountryId()).getBelongTo());
		//case 2:
		CountryModel thirdCountry = new CountryModel(13,"testCountry3",firstContinent);
		assertEquals(null,newMap.findCountryByID(thirdCountry.getCountryId()));
		System.out.println("RiskMap Test: findCountryByID(int) finished.");
	}
	
	/**
	 * Test findContinent(String).<br>
	 * case 1: add two continents to map, call findContinent(String) to find these two continents.<br>  
	 * case 2: try to find a continent which is not in the map.
	 */
	@Test
	public void findContinentTest() {
		//case 1:
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 10);
		
		ContinentModel continent1 = newMap.getContinents().get(0);
		ContinentModel continent2 = newMap.getContinents().get(1);
		
		assertEquals(2,newMap.getContinents().size());
		assertEquals(0,newMap.getCountryNum());
		assertEquals(0,newMap.getAdjacencyList().size());
		
		assertEquals(continent1,newMap.findContinent(continent1.getName()));
		assertEquals(continent2,newMap.findContinent(continent2.getName()));
		//case 2:
		ContinentModel continent3 = new ContinentModel("testContinent3");
		assertEquals(null,newMap.findContinent(continent3.getName()));		
		System.out.println("RiskMap Test: findContinent(String) finished.");
	}

	/**
	 * Test addContinent(String, int), isModified().<br>
	 * case 1: add a continent, check the return value from addContinent(String, int) and continentList size.<br>
	 * case 2: add a continent with the same name, check the return value from addContinent(String, int) and continentList size.<br>
	 * case 3: add a continent with different name, check the return value from addContinent(String, int) and continentList size.
	 */
	@Test
	public void addContinentTest(){
		//case 1:
		assertEquals(null,newMap.findContinent("testContinent"));
		assertEquals(null,newMap.findContinent("testContinent2"));
		
		assertFalse(newMap.isModified());
		assertTrue(newMap.addContinent("testContinent", 12).isResult());
		assertTrue(newMap.isModified());
		assertEquals(1,newMap.getContinents().size());
		ContinentModel continent = newMap.getContinents().get(0);
		assertEquals(continent, newMap.findContinent(continent.getName()));
		//case 2:
		newMap.setModified(false);
		assertEquals(1,newMap.addContinent("testContinent", 13).getResult());
		assertFalse(newMap.isModified());
		assertEquals(1,newMap.getContinents().size());
		//case 3:
		newMap.setModified(false);
		assertTrue(newMap.addContinent("testContinent2", 12).isResult());
		assertTrue(newMap.isModified());
		assertEquals(2,newMap.getContinents().size());
		ContinentModel continent2 = newMap.getContinents().get(1);
		assertEquals(continent2, newMap.findContinent(continent2.getName()));	
		assertEquals(0,newMap.getCountryNum());
		assertEquals(0,newMap.getAdjacencyList().size());
		System.out.println("RiskMap Test: addContinentTest(String, int),isModified() finished.");		
	}

	/**
	 * Test renameContinent(String, String), isModified().<br>
	 * prepare: add a continent.<br>
	 * case 1: try to rename a non-existing continent, check the return value and relative result.<br>
	 * case 2: add a new continent, try to rename the first continent to second continent's name, check the return value and relative result.<br>
	 * case 3: rename the first continent to an new name, check the return value and relative result.
	 */
	@Test
	public void renameContinentTest(){
		//prepare:
		newMap.addContinent("testContinent", 12);	
		assertEquals("testContinent",newMap.getContinents().get(0).getShowName());
		assertEquals("testcontinent",newMap.getContinents().get(0).getName());
		//case 1:
		newMap.setModified(false);
		assertEquals(1,newMap.renameContinent("testContinent2", "newName").getResult());
		assertFalse(newMap.isModified());
		assertEquals("testContinent",newMap.getContinents().get(0).getShowName());
		assertEquals("testcontinent",newMap.getContinents().get(0).getName());
		//case 2:
		newMap.addContinent("newName", 12);
		newMap.setModified(false);
		assertEquals(2,newMap.renameContinent("testContinent", "newName").getResult());
		assertFalse(newMap.isModified());
		assertEquals("testContinent",newMap.getContinents().get(0).getShowName());	
		assertEquals("testcontinent",newMap.getContinents().get(0).getName());
		//case 3:
		assertTrue(newMap.renameContinent("testContinent", "newName2").isResult());
		assertTrue(newMap.isModified());
		assertEquals("newName2",newMap.getContinents().get(0).getShowName());
		assertEquals("newname2",newMap.getContinents().get(0).getName());
		System.out.println("RiskMap Test: renameContinent(String, String), isModified() finished.");		
	}

	/**
	 * Test changeControlNum(String, int), isModified().<br>
	 * prepare: add a continent.<br>
	 * case 1: try to change the control number of a non-existing continent, check the return value and relative result.<br>
	 * case 2: change the control number of right continent, check the return value and relative result.
	 */
	@Test
	public void changeControlNumTest(){
		//prepare:
		newMap.addContinent("testContinent", 12);	
		assertEquals(12,newMap.getContinents().get(0).getControlNum());
		//case 1:
		newMap.setModified(false);
		assertEquals(1,newMap.changeControlNum("testContinent2", 14).getResult());
		assertFalse(newMap.isModified());
		assertEquals(12,newMap.getContinents().get(0).getControlNum());
		//case 2:
		newMap.setModified(false);
		assertTrue(newMap.changeControlNum("testContinent", 14).isResult());
		assertTrue(newMap.isModified());
		assertEquals(14,newMap.getContinents().get(0).getControlNum());	
		System.out.println("RiskMap Test: changeControlNum(String, int), isModified() finished.");		
	}
	
	/**
	 * Test deleteContinent(String), isModified().<br>
	 * prepare: add a continent with two countries in it, add an empty continent then.<br>
	 * case 1: try to delete a non-existing continent, check the return value and relative result.<br>
	 * case 2: try to delete the continent with two countries in it, check the return value and relative result.<br>
	 * case 3: delete one country, try case 2 again, check the return value and relative result.<br>
	 * case 4: move another country to the empty continent, try case 2 again, check the return value and relative result.
	 */
	@Test
	public void deleteContinentTest(){
		//prepare:
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 14);
		newMap.addCountry("testCountry","testContinent",1,2);
		newMap.addCountry("testCountry2","testContinent",5,6);
		//case 1:
		newMap.setModified(false);
		assertTrue(newMap.deleteContinent("testContinent3").isResult());
		assertFalse(newMap.isModified());
		assertEquals(2,newMap.getContinents().size());
		//case 2:
		assertEquals(1,newMap.deleteContinent("testContinent").getResult());
		assertFalse(newMap.isModified());
		assertEquals(2,newMap.getContinents().size());
		//case 3:
		newMap.deleteCountry("testCountry");
		newMap.setModified(false);		
		assertEquals(1,newMap.deleteContinent("testContinent").getResult());
		assertFalse(newMap.isModified());
		assertEquals(2,newMap.getContinents().size());
		//case 4:
		newMap.moveCountry("testContinent2","testCountry2");
		newMap.setModified(false);
		assertTrue(newMap.deleteContinent("testContinent").isResult());
		assertTrue(newMap.isModified());
		assertEquals(1,newMap.getContinents().size());		
		System.out.println("RiskMap Test: deleteContinent(String), isModified() finished.");		
	}	

	/**
	 * Test addCountry(String, String, int, int), isModified().<br>
	 * case 1: add two continents, add a country to first continent, check the return value and relative result.<br>
	 * case 2: try to add a country with same or different name to a non-exiting continent, check the return value.<br>
	 * case 3: try to add a country with same name to each continent, check the return value.<br>
	 * case 4: add a country with different name to one continent, check the return value and relative result.<br>
	 * case 5: add a country with same name again to another continent, check the return value.
	 */
	@Test
	public void addCountryTest(){
		//case 1:
		assertFalse(newMap.isModified());
		assertTrue(newMap.addContinent("testContinent", 12).isResult());
		assertEquals(1,newMap.getContinents().size());
		assertTrue(newMap.addContinent("testContinent2", 13).isResult());
		assertEquals(2,newMap.getContinents().size());

		assertEquals(null, newMap.findCountry("testCountry"));
		assertEquals(null, newMap.findCountry("testCountry2"));
		
		newMap.setModified(false);
		assertTrue(newMap.addCountry("testCountry","testContinent",1,2).isResult());
		assertTrue(newMap.isModified());
		assertEquals(1,newMap.getCountryNum());
		assertEquals(1,newMap.getAdjacencyList().size());
		CountryModel country = newMap.getContinents().get(0).getCountryList().get(0);
		assertEquals(country, newMap.findCountry(country.getShowName()));
		//case 2:
		newMap.setModified(false);
		assertEquals(1,newMap.addCountry("testCountry","testContinent3",1,2).getResult());
		assertFalse(newMap.isModified());
		assertEquals(1,newMap.getCountryNum());
		assertEquals(1,newMap.getAdjacencyList().size());		
		assertEquals(1,newMap.addCountry("testCountry2","testContinent3",1,2).getResult());
		assertFalse(newMap.isModified());
		assertEquals(1,newMap.getCountryNum());
		assertEquals(1,newMap.getAdjacencyList().size());
		//case 3:
		assertEquals(2,newMap.addCountry("testCountry","testContinent",1,2).getResult());
		assertFalse(newMap.isModified());
		assertEquals(1,newMap.getCountryNum());
		assertEquals(1,newMap.getAdjacencyList().size());
		
		assertEquals(2,newMap.addCountry("testCountry","testContinent2",1,2).getResult());
		assertFalse(newMap.isModified());
		assertEquals(1,newMap.getCountryNum());
		assertEquals(1,newMap.getAdjacencyList().size());
		//case 4:
		assertTrue(newMap.addCountry("testCountry2","testContinent2",1,2).isResult());
		assertTrue(newMap.isModified());
		assertEquals(2,newMap.getCountryNum());
		assertEquals(2,newMap.getAdjacencyList().size());
		CountryModel country2 = newMap.getContinents().get(1).getCountryList().get(0);
		assertEquals(country2, newMap.findCountry(country2.getShowName()));
		//case 5:
		newMap.setModified(false);
		assertEquals(2,newMap.addCountry("testCountry2","testContinent",1,2).getResult());
		assertFalse(newMap.isModified());
		assertEquals(2,newMap.getCountryNum());
		assertEquals(2,newMap.getAdjacencyList().size());

		System.out.println("RiskMap Test: addCountry(String, String, int, int), isModified() finished.");		
	}

	/**
	 * Test renameCountry(String, String), isModified().<br>
	 * prepare: add a continent and country.<br>
	 * case 1: try to rename a non-existing country, check the return value and relative result.<br>
	 * case 2: add a new country, try to rename the first country to second country's name, check the return value and relative result.<br>
	 * case 3: rename the first country to an new name, check the return value and relative result.
	 */
	@Test
	public void renameCountryTest(){
		//prepare:
		newMap.addContinent("testContinent", 12);	
		newMap.addCountry("testCountry","testContinent", 1,2);
		assertEquals("testCountry", newMap.getContinents().get(0).getCountryList().get(0).getShowName());
		assertEquals("testcountry", newMap.getContinents().get(0).getCountryList().get(0).getName());
		//case 1:
		newMap.setModified(false);
		assertEquals(1,newMap.renameCountry("testCountry2", "newName").getResult());
		assertFalse(newMap.isModified());
		assertEquals("testCountry", newMap.getContinents().get(0).getCountryList().get(0).getShowName());
		assertEquals("testcountry", newMap.getContinents().get(0).getCountryList().get(0).getName());
		//case 2:
		newMap.addCountry("newName","testContinent", 2,4);
		newMap.setModified(false);
		assertEquals(2,newMap.renameCountry("testCountry", "newName").getResult());
		assertFalse(newMap.isModified());
		assertEquals("testCountry", newMap.getContinents().get(0).getCountryList().get(0).getShowName());
		assertEquals("testcountry", newMap.getContinents().get(0).getCountryList().get(0).getName());		
		//case 3:
		newMap.setModified(false);
		assertTrue(newMap.renameCountry("testCountry", "newName2").isResult());
		assertTrue(newMap.isModified());
		assertEquals("newName2", newMap.getContinents().get(0).getCountryList().get(0).getShowName());	
		assertEquals("newname2", newMap.getContinents().get(0).getCountryList().get(0).getName());
		System.out.println("RiskMap Test: renameCountry(String, String), isModified() finished.");		
	}
	
	/**
	 * Test moveCountry(String, String), isModified().<br>
	 * prepare: add two continents and two countries for each continent.<br>
	 * case 1: exchange two countries to the other continent, check the return value and relative result.<br>
	 * case 2: try to move a non-existing country, check the return value and relative result.<br>
	 * case 3: try to move a country to a non-existing continent, check the return value and relative result.<br>
	 * case 4: try to move a country to current continent, check the return value and relative result.
	 */
	@Test
	public void moveCountryTest(){
		//prepare:
		newMap.addContinent("testContinent", 12);	
		newMap.addContinent("testContinent2", 12);
		newMap.addCountry("testCountry","testContinent", 1,2);
		newMap.addCountry("testCountry2","testContinent2", 2,4);
		assertEquals("testCountry",newMap.findContinent("testContinent").getCountryList().get(0).getShowName());
		assertEquals("testCountry2",newMap.findContinent("testContinent2").getCountryList().get(0).getShowName());
		assertEquals("testContinent",newMap.findCountry("testCountry").getBelongTo().getShowName());
		assertEquals("testContinent2",newMap.findCountry("testCountry2").getBelongTo().getShowName());
		//case 1:
		newMap.setModified(false);
		assertTrue(newMap.moveCountry("testContinent2", "testCountry").isResult());
		assertTrue(newMap.isModified());
		assertEquals("testCountry",newMap.findContinent("testContinent2").getCountryList().get(1).getShowName());
		assertEquals("testContinent2",newMap.findCountry("testCountry").getBelongTo().getShowName());
		assertEquals(0,newMap.findContinent("testContinent").getCountryList().size());
		assertEquals(2,newMap.findContinent("testContinent2").getCountryList().size());
		newMap.setModified(false);
		assertTrue(newMap.moveCountry("testContinent", "testCountry2").isResult());
		assertTrue(newMap.isModified());
		assertEquals("testCountry2",newMap.findContinent("testContinent").getCountryList().get(0).getShowName());
		assertEquals("testContinent",newMap.findCountry("testCountry2").getBelongTo().getShowName());
		assertEquals(1,newMap.findContinent("testContinent").getCountryList().size());		
		assertEquals(1,newMap.findContinent("testContinent2").getCountryList().size());	
		//case 2:
		newMap.setModified(false);
		assertEquals(1,newMap.moveCountry("testContinent2", "testCountry3").getResult());
		assertFalse(newMap.isModified());	
		//case 3:
		assertEquals(2,newMap.moveCountry("testContinent3", "testCountry").getResult());
		assertFalse(newMap.isModified());	
		//case 4:
		assertTrue(newMap.moveCountry("testContinent2", "testCountry").isResult());
		assertFalse(newMap.isModified());	
		System.out.println("RiskMap Test: moveCountry(String, String), isModified() finished.");		
	}	

	/**
	 * Test deleteCountry(String), isModified().<br>
	 * prepare: add a continent with one country in it.<br>
	 * case 1: try to delete a non-existing country, check the return value and relative result.<br>
	 * case 2: delete the right country, check the return value and relative result.
	 */
	@Test
	public void deleteCountryTest(){
		//prepare:
		newMap.addContinent("testContinent", 12);
		newMap.addCountry("testCountry","testContinent",1,2);
		//case 1:
		newMap.setModified(false);
		assertFalse(newMap.deleteCountry("testCountry2"));
		assertFalse(newMap.isModified());
		assertEquals(1,newMap.getCountryNum());
		//case 2:
		newMap.setModified(false);
		assertTrue(newMap.deleteCountry("testCountry"));
		assertTrue(newMap.isModified());
		assertEquals(0,newMap.getCountryNum());
		System.out.println("RiskMap Test: deleteCountry(String), isModified() finished.");		
	}	
	
	/**
	 * Test addConnection(String, String), isModified().<br>
	 * prepare:add two continents and three countries(country1 and country2 in continent1,country3 in continent2).<br>
	 * case 1: add a connection between country1 and country2, check the return value and relative result.<br>
	 * case 2: try to add the same connection as case 1, check the return value and relative result.<br>
	 * case 3: try to add a connection between same country(self-connection), check the return value and relative result.<br>
	 * case 4: try to add a connection from or to non-existing country, check the return value and relative result.<br>
	 * case 5: add connections between country 1 and 3, also between 2 and 3, check the return value and relative result.<br>
	 * case 6: using internal method, add an unpaired connection between country 1 and 2, then add this connection normally, check the return value.
	 */
	@Test
	public void addConnectionTest(){
		//prepare:
		assertEquals(0,newMap.getAdjacencyList().size());
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 12);
		newMap.addCountry("testCountry","testContinent", 1,1);
		CountryModel country1 = newMap.getContinents().get(0).getCountryList().get(0);
		newMap.addCountry("testCountry2","testContinent", 1,1);
		CountryModel country2 = newMap.getContinents().get(0).getCountryList().get(1);
		newMap.addCountry("testCountry3","testContinent2", 1,1);
		CountryModel country3 = newMap.getContinents().get(1).getCountryList().get(0);
		assertEquals(3,newMap.getAdjacencyList().size());
		//case 1:
		newMap.setModified(false);
		assertTrue(newMap.addConnections("testCountry", "testCountry2").isResult());
		assertTrue(newMap.isModified());
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country2));
		assertEquals(1,newMap.getAdjacencyList().get(country2).size());
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country1));
		assertEquals(0,newMap.getAdjacencyList().get(country3).size());
		//case 2:
		newMap.setModified(false);
		assertEquals(0,newMap.addConnections("testCountry", "testCountry2").getResult());
		assertFalse(newMap.isModified());
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country2));
		assertEquals(1,newMap.getAdjacencyList().get(country2).size());
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country1));
		//case 3:
		assertEquals(2,newMap.addConnections("testCountry", "testCountry").getResult());
		assertEquals(2,newMap.addConnections("testCountry2", "testCountry2").getResult());
		assertEquals(2,newMap.addConnections("testCountry3", "testCountry3").getResult());
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country2));
		assertEquals(1,newMap.getAdjacencyList().get(country2).size());
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country1));
		//case 4:
		assertEquals(1,newMap.addConnections("testCountry4", "testCountry5").getResult());		
		assertEquals(1,newMap.addConnections("testCountry", "testCountry4").getResult());
		assertEquals(1,newMap.addConnections("testCountry4", "testCountry2").getResult());
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country2));
		assertEquals(1,newMap.getAdjacencyList().get(country2).size());
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country1));	
		//case 5:
		assertTrue(newMap.addConnections("testCountry", "testCountry3").isResult());		
		assertTrue(newMap.addConnections("testCountry2", "testCountry3").isResult());
		assertEquals(2,newMap.getAdjacencyList().get(country1).size());
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country2));
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country3));
		assertEquals(2,newMap.getAdjacencyList().get(country2).size());
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country1));		
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country3));	
		assertEquals(2,newMap.getAdjacencyList().get(country3).size());
		assertTrue(newMap.getAdjacencyList().get(country3).contains(country1));		
		assertTrue(newMap.getAdjacencyList().get(country3).contains(country2));	
		//case 6:
		newMap.removeAllConnection();
		newMap.getAdjacencyList().get(country1).add(country2);
		newMap.setModified(false);
		assertEquals(0,newMap.addConnections("testCountry", "testCountry2").getResult());		
		assertTrue(newMap.isModified());
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		newMap.setModified(false);
		assertTrue(newMap.addConnections("testCountry", "testCountry2").isResult());		
		assertFalse(newMap.isModified());
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		assertEquals(1,newMap.getAdjacencyList().get(country2).size());
		System.out.println("RiskMap Test: addConnection(String, String), isModified() finished.");
	}
	
	/**
	 * Test addCompletedConnection(), isModified().<br>
	 * prepare:add two continents and three countries(country1 and country2 in continent1,country3 in continent2).<br>
	 * case 1: call addCompletedConnection(), check relative results.
	 */
	@Test
	public void addCompletedConnectionTest(){
		//prepare:
		assertEquals(0,newMap.getAdjacencyList().size());
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 12);
		newMap.addCountry("testCountry","testContinent", 1,1);
		CountryModel country1 = newMap.getContinents().get(0).getCountryList().get(0);
		newMap.addCountry("testCountry2","testContinent", 1,1);
		CountryModel country2 = newMap.getContinents().get(0).getCountryList().get(1);
		newMap.addCountry("testCountry3","testContinent2", 1,1);
		CountryModel country3 = newMap.getContinents().get(1).getCountryList().get(0);
		assertEquals(3,newMap.getAdjacencyList().size());
		//case 1:
		newMap.setModified(false);
		newMap.addCompletedConnection();
		assertTrue(newMap.isModified());
		assertEquals(2,newMap.getAdjacencyList().get(country1).size());
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country2));
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country3));
		assertEquals(2,newMap.getAdjacencyList().get(country2).size());
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country1));		
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country3));	
		assertEquals(2,newMap.getAdjacencyList().get(country3).size());
		assertTrue(newMap.getAdjacencyList().get(country3).contains(country1));		
		assertTrue(newMap.getAdjacencyList().get(country3).contains(country2));
		System.out.println("RiskMap Test: addCompletedConnection(), isModified() finished.");		
	}
	
	/**
	 * Test removeConnection(String, String), isModified().<br>
	 * prepare:add two continents and three countries(country1 and country2 
	 * in continent1,country3 in continent2), add all connections.<br>
	 * case 1: remove connection between country 1 and 2, check return value and relative result.<br>
	 * case 2: remove the same connection as in case 1, check return value.<br>
	 * case 3: using internal method, add an unpaired connection between country 1 and 2, remove this connection normally, check return value.<br>
	 * case 4: remove a self-connection, check return value.<br>
	 * case 5: add a self-connection using internal method, remove it normally, check return value.<br>
	 * case 6: remove a connection from or to a non-existing country, check return value.<br>
	 * case 7: remove all connections, check return value and relative result.
	 */
	@Test
	public void removeConnectionTest(){
		assertEquals(0,newMap.getAdjacencyList().size());
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 12);
		newMap.addCountry("testCountry","testContinent", 1,1);
		CountryModel country1 = newMap.getContinents().get(0).getCountryList().get(0);
		newMap.addCountry("testCountry2","testContinent", 1,1);
		CountryModel country2 = newMap.getContinents().get(0).getCountryList().get(1);
		newMap.addCountry("testCountry3","testContinent2", 1,1);
		CountryModel country3 = newMap.getContinents().get(1).getCountryList().get(0);
		assertEquals(3,newMap.getAdjacencyList().size());
		newMap.addCompletedConnection();

		//case 1: 
		newMap.setModified(false);
		assertTrue(newMap.removeConnection("testCountry", "testCountry2").isResult());
		assertTrue(newMap.isModified());
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		assertFalse(newMap.getAdjacencyList().get(country1).contains(country2));
		assertTrue(newMap.getAdjacencyList().get(country1).contains(country3));
		assertEquals(2,newMap.getAdjacencyList().get(country2).size());
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country1));		
		assertTrue(newMap.getAdjacencyList().get(country2).contains(country3));	
		assertEquals(2,newMap.getAdjacencyList().get(country3).size());
		//case 2:
		newMap.setModified(false);
		assertTrue(newMap.removeConnection("testCountry", "testCountry2").isResult());
		assertFalse(newMap.isModified());		
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		assertEquals(2,newMap.getAdjacencyList().get(country2).size());
		assertEquals(2,newMap.getAdjacencyList().get(country3).size());
		//case 3:
		newMap.getAdjacencyList().get(country1).add(country2);
		newMap.setModified(false);
		assertTrue(newMap.removeConnection("testCountry", "testCountry2").isResult());
		assertTrue(newMap.isModified());
		assertEquals(1,newMap.getAdjacencyList().get(country1).size());
		//case 4:
		newMap.setModified(false);
		assertEquals(2,newMap.removeConnection("testCountry2", "testCountry2").getResult());
		assertFalse(newMap.isModified());
		//case 5:
		newMap.removeConnection("testCountry2", "testCountry");
		newMap.getAdjacencyList().get(country2).add(country2);
		assertEquals(2,newMap.getAdjacencyList().get(country2).size());
		newMap.setModified(false);
		assertEquals(2,newMap.removeConnection("testCountry2", "testCountry2").getResult());
		assertTrue(newMap.isModified());
		assertEquals(1,newMap.getAdjacencyList().get(country2).size());
		//case 6:
		assertEquals(1,newMap.removeConnection("testCountry", "testCountry4").getResult());
		assertEquals(1,newMap.removeConnection("testCountry4", "testCountry2").getResult());
		//case 7:
		assertTrue(newMap.removeConnection("testCountry", "testCountry3").isResult());
		assertTrue(newMap.removeConnection("testCountry3", "testCountry2").isResult());
		assertEquals(0,newMap.getAdjacencyList().get(country1).size());
		assertEquals(1,newMap.getAdjacencyList().get(country2).size());
		assertEquals(1,newMap.getAdjacencyList().get(country3).size());	
		assertTrue(newMap.removeConnection("testCountry3", "testCountry").isResult());
		assertTrue(newMap.removeConnection("testCountry2", "testCountry3").isResult());
		assertEquals(0,newMap.getAdjacencyList().get(country1).size());
		assertEquals(0,newMap.getAdjacencyList().get(country2).size());
		assertEquals(0,newMap.getAdjacencyList().get(country3).size());	
		System.out.println("RiskMap Test: removeConnection(String, String), isModified() finished.");	
	}
	
	/**
	 * Test checkConnection(Map), findPath(Map,Country), DFS(Map, Country, int).<br>
	 * prepare:add two continents and three countries(country1 and country2 
	 * in continent1,country3 in continent2), add all connections.<br>
	 * case 1: find path from three country separately, check the result.<br>
	 * case 2: remove connection between country 1 and 2, find path from three country separately, check the result.<br>
	 * case 3: remove connection between country 2 and 3, find path from three country separately, check the result.<br>
	 * case 4: remove connection between country 1 and 3, find path from three country separately, check the result.
	 */
	@Test
	public void findPathTest(){
		//prepare:
		assertEquals(0,newMap.getAdjacencyList().size());
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 12);
		newMap.addCountry("testCountry","testContinent", 1,1);
		CountryModel country1 = newMap.getContinents().get(0).getCountryList().get(0);
		newMap.addCountry("testCountry2","testContinent", 1,1);
		CountryModel country2 = newMap.getContinents().get(0).getCountryList().get(1);
		newMap.addCountry("testCountry3","testContinent2", 1,1);
		CountryModel country3 = newMap.getContinents().get(1).getCountryList().get(0);
		assertEquals(3,newMap.getAdjacencyList().size());
		newMap.addCompletedConnection();
		//case 1:
		assertTrue(newMap.checkConnection(newMap.getAdjacencyList()));
		assertEquals(3,newMap.findPath(newMap.getAdjacencyList(),country1));
		assertEquals(3,newMap.findPath(newMap.getAdjacencyList(),country2));
		assertEquals(3,newMap.findPath(newMap.getAdjacencyList(),country3));
		//case 2:
		newMap.removeConnection("testCountry", "testCountry2");
		newMap.removeConnection("testCountry2", "testCountry");
		assertTrue(newMap.checkConnection(newMap.getAdjacencyList()));
		assertEquals(3,newMap.findPath(newMap.getAdjacencyList(),country1));
		assertEquals(3,newMap.findPath(newMap.getAdjacencyList(),country2));
		assertEquals(3,newMap.findPath(newMap.getAdjacencyList(),country3));
		//case 3:
		newMap.removeConnection("testCountry2", "testCountry3");
		newMap.removeConnection("testCountry3", "testCountry2");
		assertFalse(newMap.checkConnection(newMap.getAdjacencyList()));
		assertEquals(2,newMap.findPath(newMap.getAdjacencyList(),country1));
		assertEquals(1,newMap.findPath(newMap.getAdjacencyList(),country2));
		assertEquals(2,newMap.findPath(newMap.getAdjacencyList(),country3));	
		//case 4:
		newMap.removeConnection("testCountry", "testCountry3");
		newMap.removeConnection("testCountry3", "testCountry");
		assertFalse(newMap.checkConnection(newMap.getAdjacencyList()));
		assertEquals(1,newMap.findPath(newMap.getAdjacencyList(),country1));
		assertEquals(1,newMap.findPath(newMap.getAdjacencyList(),country2));
		assertEquals(1,newMap.findPath(newMap.getAdjacencyList(),country3));		
		System.out.println("RiskMap Test: checkConnection(Map<Country,ArrayList<Country>>), findPath(Map<Country,ArrayList<Country>>,Country), DFS(Map<Country,ArrayList<Country>>, Country, int) finished.");			
	}
	
	/**
	 * Test checkErrors().<br>
	 * case 1: add two continents, check errors, should report no countries error.<br>
	 * case 2: add two countries to continent1, check errors, should report 3 errors.<br>
	 * case 3: add connection between country 1 and 2, check errors, should report continent2 empty error.<br>
	 * case 4: add country3 to continent2, check errors, should report whole map not connected error.<br>
	 * case 5: add country4 to continent2, and connected to country3, check errors, should report whole map not connected error.<br>
	 * case 6: connect country 1 and 3, 2 and 4, remove connection between 1 and 2, check errors, should report continent1 not connected error.<br>
	 * case 7: connect country 1 and 2, remove connection between 1 and 3, check errors, should no error.
	 */
	@Test
	public void checkErrorsTest(){
		ErrorMsg errorMsg;
		//case 1:
		assertEquals(0,newMap.getAdjacencyList().size());
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 12);
		assertEquals(1,(errorMsg=newMap.checkErrors()).getResult());
		assertTrue(errorMsg.getMsg().equals("Error: There is no countries.\n"));
		//case 2:
		newMap.addCountry("testCountry","testContinent", 1,1);
		newMap.addCountry("testCountry2","testContinent", 1,1);
		assertEquals(1,(errorMsg=newMap.checkErrors()).getResult());
		assertTrue(errorMsg.getMsg().equals("Error: The whole map is not a connected graph.\n"
				+ "Error: The continent <testContinent> is not a connected graph.\n"
				+ "Error: The continent <testContinent2> has no country in it.\n"));
		//case 3��
		newMap.addConnections("testCountry", "testCountry2");
		assertEquals(1,(errorMsg=newMap.checkErrors()).getResult());
		assertTrue(errorMsg.getMsg().equals("Error: The continent <testContinent2> has no country in it.\n"));
		//case 4:
		newMap.addCountry("testCountry3","testContinent2", 1,1);
		assertEquals(1,(errorMsg=newMap.checkErrors()).getResult());
		assertTrue(errorMsg.getMsg().equals("Error: The whole map is not a connected graph.\n"));
		//case 5:
		newMap.addCountry("testCountry4","testContinent2", 1,1);
		newMap.addConnections("testCountry3", "testCountry4");
		assertEquals(1,(errorMsg=newMap.checkErrors()).getResult());
		assertTrue(errorMsg.getMsg().equals("Error: The whole map is not a connected graph.\n"));
		//case 6:
		newMap.addConnections("testCountry", "testCountry3");
		newMap.addConnections("testCountry2", "testCountry4");
		newMap.removeConnection("testCountry", "testCountry2");
		newMap.removeConnection("testCountry2", "testCountry");
		assertEquals(1,(errorMsg=newMap.checkErrors()).getResult());
		assertTrue(errorMsg.getMsg().equals("Error: The continent <testContinent> is not a connected graph.\n"));		
		//case 7:
		newMap.addConnections("testCountry", "testCountry2");
		newMap.removeConnection("testCountry", "testCountry3");
		newMap.removeConnection("testCountry3", "testCountry");
		assertTrue(newMap.checkErrors().isResult());
		System.out.println("RiskMap Test: checkErrors() finished.");					
	}
	
	/**
	 * Test checkWarnings().<br>
	 * cases: five bits to represent warnings, 
	 * scroll: bit 5, wrap bit 4, image bit 3, warn bit 2, author bit 1, 
	 * try to combine warnings, check the return value.
	 */
	@Test
	public void checkWarningsTest(){
		//case 1:
		existingMap = new RiskMapModel();
		assertEquals(31,existingMap.checkWarnings());
		assertEquals(0,existingMap.checkWarnings());
		//case 2:
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setAuthor("");
		assertEquals(31,existingMap.checkWarnings());		
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setAuthor("testAuthor");
		assertEquals(30,existingMap.checkWarnings());
		//case 3:
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setWarn("");
		assertEquals(31,existingMap.checkWarnings());	
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setWarn("pp");
		assertEquals(31,existingMap.checkWarnings());	
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setWarn("yes");
		assertEquals(29,existingMap.checkWarnings());
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setWarn("no");
		assertEquals(29,existingMap.checkWarnings());
		//case 4:
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setImage("");
		assertEquals(31,existingMap.checkWarnings());
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setImage("none");
		assertEquals(27,existingMap.checkWarnings());
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setImage("dsafsdafdsf");
		assertEquals(27,existingMap.checkWarnings());
		//case 5:
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setWrap("");
		assertEquals(31,existingMap.checkWarnings());	
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setWrap("every");
		assertEquals(31,existingMap.checkWarnings());	
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setWrap("yes");
		assertEquals(23,existingMap.checkWarnings());
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setWrap("no");
		assertEquals(23,existingMap.checkWarnings());	
		//case 6:
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setScroll("");
		assertEquals(31,existingMap.checkWarnings());	
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setScroll("every");
		assertEquals(31,existingMap.checkWarnings());	
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setScroll("horizontal");
		assertEquals(15,existingMap.checkWarnings());
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setScroll("vertical");
		assertEquals(15,existingMap.checkWarnings());
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setScroll("none");
		assertEquals(15,existingMap.checkWarnings());
		//case 7:
		existingMap = null;
		existingMap = new RiskMapModel();
		existingMap.setAuthor("testAuthor");
		existingMap.setScroll("none");
		assertEquals(14,existingMap.checkWarnings());
		
		System.out.println("RiskMap Test: checkWarnings() finished.");		
	}
	
	/**
	 * Test saveToFile(String).<br>
	 * prepare: create a map with two continents and four countries, add completed connections.<br>
	 * case 1: try to save map to empty file path, check return value and error message.<br>
	 * case 2: try to save map to a directory, check return value and error message.<br>
	 * case 3: save map to a valid file, check file content, then create a new RiskMap object, load this file, check result.<br>
	 * case 4: edit the map, repeat case 3, see if can rewrite the map file.
	 */
	@Test	
	public void saveToFileTest() {
		//prepare:
		assertEquals(0,newMap.getAdjacencyList().size());
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 12);
		newMap.addCountry("testCountry","testContinent", 1,1);
		newMap.addCountry("testCountry2","testContinent", 1,1);
		newMap.addCountry("testCountry3","testContinent2", 1,1);
		newMap.addCountry("testCountry4","testContinent2", 1,1);
		assertEquals(4,newMap.getAdjacencyList().size());
		newMap.addCompletedConnection();
		assertTrue(newMap.checkErrors().isResult());
		assertEquals(0,newMap.checkWarnings());		
		//case 1:
		assertEquals(1,newMap.saveToFile("").getResult());
		//case 2:
		assertEquals(1,newMap.saveToFile("./src/map/test").getResult());
		//case 3:
		assertTrue(newMap.saveToFile("./src/map/test/test.map").isResult());
		existingMap = new RiskMapModel();
		assertTrue(existingMap.loadMapFile("./src/map/test/test.map").isResult());
		assertTrue(existingMap.checkErrors().isResult());
		assertEquals(0,existingMap.checkWarnings());	
		//case 4:
		newMap.removeConnection("testCountry", "testCountry3");
		newMap.removeConnection("testCountry3", "testCountry");
		assertTrue(newMap.saveToFile("./src/map/test/test.map").isResult());
		existingMap = null;
		existingMap = new RiskMapModel();
		assertTrue(existingMap.loadMapFile("./src/map/test/test.map").isResult());
		assertTrue(existingMap.checkErrors().isResult());
		assertEquals(0,existingMap.checkWarnings());	
		System.out.println("RiskMap Test: saveToFile(String) finished.");	
	}
	
	/**
	 * Test loadMapFile(String).<br>
	 * prepare: create a map with two continents and four countries, add completed connections, save to test.map.<br>
	 * case 1: try to load map from an empty file path, check return value.<br>
	 * case 2: try to load map from a directory or non-existing file, check return value.<br>
	 * case 3: load a valid map file, check return value and relative result.<br>
	 * case 4: create map file with no [Map] label, load file, check return value.<br>
	 * case 5: create map file with wrong format data, load file, check return value.<br>
	 * case 6: create map file with wrong author label, load file, check return value.<br>
	 * case 7: create map file with empty author data, load file, check return value and relative value.<br>
	 * case 8,9: create map file with wrong continent format, load file, check return value and relative value.<br>
	 * case 10: create map file with wrong control number, load file, check return value and relative value.<br>
	 * case 11: create map file with duplicated continents, load file, check return value and relative value.<br>
	 * case 12: create map file without enough country parameters, load file, check return value and relative value.<br>
	 * case 13: create map file with empty country name, load file, check return value and relative value.<br>
	 * case 14: create map file with empty continent name, load file, check return value and relative value.<br>
	 * case 15: create map file with wrong coordinate, load file, check return value and relative value.<br>
	 * case 16: create map file with non-existing continent, load file, check return value and relative value.<br>
	 * case 17: create map file with duplicated countries, load file, check return value and relative value.<br>
	 * case 18: create map file with duplicated countries in adjacency list, load file, check return value and relative value.<br>
	 * case 19: create map file with non-existing country in adjacency list, load file, check return value and relative value.<br>
	 * case 20: create map file with self-connection, load file, check return value and relative value.<br>
	 * case 21: create map file with one-way connection, load file, check return value and relative value.<br>
	 * case 22: create map file with a unconnected graph, load file, check return value and relative value.<br>
	 * case 23: create map file with lower case letters for country name and continent name in adjacency list. 
	 */
	@Test	
	public void loadMapFileTest() {
		//prepare:
		assertEquals(0,newMap.getAdjacencyList().size());
		newMap.addContinent("testContinent", 12);
		newMap.addContinent("testContinent2", 12);
		newMap.addCountry("testCountry","testContinent", 1,1);
		newMap.addCountry("testCountry2","testContinent", 1,1);
		newMap.addCountry("testCountry3","testContinent2", 1,1);
		newMap.addCountry("testCountry4","testContinent2", 1,1);
		assertEquals(4,newMap.getAdjacencyList().size());
		newMap.addCompletedConnection();
		assertTrue(newMap.checkErrors().isResult());
		assertEquals(0,newMap.checkWarnings());	
		assertTrue(newMap.saveToFile("./src/map/test/test.map").isResult());
		
		//case 1:
		existingMap = new RiskMapModel();
		assertEquals(13,existingMap.loadMapFile("").getResult());
		//case 2:
		assertEquals(13,existingMap.loadMapFile("./src/map/test").getResult());
		assertEquals(13,existingMap.loadMapFile("./src/map/test/tt.map").getResult());
		//case 3:
		assertTrue(existingMap.loadMapFile("./src/map/test/test.map").isResult());		
		assertTrue(existingMap.checkErrors().isResult());
		assertEquals(0,existingMap.checkWarnings());	
		//case 4:
		File outputFile = new File("./src/map/test/test4.map");
		FileWriter fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("author=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=no\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=3\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(1,existingMap.loadMapFile("./src/map/test/test4.map").getResult());
		//case 5:
		outputFile = new File("./src/map/test/test5.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\ndssdfdsa\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=no\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=3\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(1,existingMap.loadMapFile("./src/map/test/test5.map").getResult());
		//case 6:
		outputFile = new File("./src/map/test/test6.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthors=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=no\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=3\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(1,existingMap.loadMapFile("./src/map/test/test6.map").getResult());
		//case 7:
		outputFile = new File("./src/map/test/test7.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=3\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertTrue(existingMap.loadMapFile("./src/map/test/test7.map").isResult());		
		assertTrue(existingMap.checkErrors().isResult());
		assertEquals(1,existingMap.checkWarnings());
		//case 8:
		outputFile = new File("./src/map/test/test8.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(1,existingMap.loadMapFile("./src/map/test/test8.map").getResult());		
		//case 9:
		outputFile = new File("./src/map/test/test9.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(1,existingMap.loadMapFile("./src/map/test/test9.map").getResult());	
		//case 10:
		outputFile = new File("./src/map/test/test10.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=er\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(3,existingMap.loadMapFile("./src/map/test/test10.map").getResult());	
		//case 11:
		outputFile = new File("./src/map/test/test11.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(2,existingMap.loadMapFile("./src/map/test/test11.map").getResult());	
		//case 12:
		outputFile = new File("./src/map/test/test12.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(9,existingMap.loadMapFile("./src/map/test/test12.map").getResult());
		//case 13:
		outputFile = new File("./src/map/test/test13.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\n,1,2,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(4,existingMap.loadMapFile("./src/map/test/test13.map").getResult());
		//case 14:
		outputFile = new File("./src/map/test/test14.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2, ,32\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(5,existingMap.loadMapFile("./src/map/test/test14.map").getResult());
		//case 15:
		outputFile = new File("./src/map/test/test15.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2c,testContinent\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(6,existingMap.loadMapFile("./src/map/test/test15.map").getResult());
		//case 16:
		outputFile = new File("./src/map/test/test16.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2,testContinent3\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(7,existingMap.loadMapFile("./src/map/test/test16.map").getResult());
		//case 17:
		outputFile = new File("./src/map/test/test17.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2,testContinent\r\ntestCountry,1,2,testContinent2\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(7,existingMap.loadMapFile("./src/map/test/test17.map").getResult());	
		//case 18:
		outputFile = new File("./src/map/test/test18.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2,testContinent,testCountry2,testCountry2\r\ntestCountry2,1,2,testContinent2\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(8,existingMap.loadMapFile("./src/map/test/test18.map").getResult());	
		//case 19:
		outputFile = new File("./src/map/test/test19.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2,testContinent,testCountry3\r\ntestCountry2,1,2,testContinent2\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(10,existingMap.loadMapFile("./src/map/test/test19.map").getResult());
		//case 20:
		outputFile = new File("./src/map/test/test20.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2,testContinent,testCountry\r\ntestCountry2,1,2,testContinent2\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(12,existingMap.loadMapFile("./src/map/test/test20.map").getResult());
		//case 21:
		outputFile = new File("./src/map/test/test21.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2,testContinent,testCountry2\r\ntestCountry2,1,2,testContinent2\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(0,existingMap.loadMapFile("./src/map/test/test21.map").getResult());
		
		System.out.println("RiskMap Test: loadMapFile(String) finished.");	
		//case 22:
		outputFile = new File("./src/map/test/test22.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,2,testContinent\r\ntestCountry2,1,2,testContinent2\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertTrue(existingMap.loadMapFile("./src/map/test/test22.map").isResult());
		assertFalse(existingMap.checkErrors().isResult());
		
		//case 23:
		outputFile = new File("./src/map/test/test23.map");
		fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=testAuthor\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\ntestContinent=12\r\ntestContinent2=24\r\n\r\n");
			fw.write("[Territories]\r\ntestCountry,1,1,testcontinent,testcountry2\r\ntestCountry2,1,1,testcontinent2,testcountry\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		existingMap = null;
		existingMap = new RiskMapModel();
		assertEquals(0,existingMap.loadMapFile("./src/map/test/test23.map").getResult());	
		
		System.out.println("RiskMap Test: loadMapFile(String) finished.");
	}
	
	/**
	 * Test the Serialization of map class
	 */
	@Test
	public void mapSerializableTest(){
		newMap.addContinent("testContinent", 12);
		newMap.addCountry("testCountry", "testContinent", 10, 20);
		newMap.addContinent("testContinent2", 10);
		newMap.addCountry("testCountry2","testContinent2",30,40);
		
		assertEquals(2,newMap.getContinents().size());
		assertEquals(2,newMap.getCountryNum());
		assertEquals(2,newMap.getAdjacencyList().size());		
			
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("map.bin"));
		    output.writeObject(newMap);
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		 
		try {
		    @SuppressWarnings("resource")
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("map.bin"));
		    RiskMapModel readMap = (RiskMapModel) input.readObject();
			assertEquals(2,readMap.getContinents().size());
			assertEquals(2,readMap.getCountryNum());
			assertEquals(2,readMap.getAdjacencyList().size());
		} catch (Exception e) {
		    e.printStackTrace();
		} 
		
		System.out.println("RiskMap Test: Serializability test finished");
	}	
}
