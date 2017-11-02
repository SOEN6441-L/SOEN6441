package mapmodelstest;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import gameelements.Player;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;
/**
 * Test Class to test all methods defined in class Country.
 * Including 7 methods and 16 test cases.
 */
public class CountryTest {
	private CountryModel country;
	private ContinentModel continent;
	
	/**
	 * Set test environment before each test.
	 */
	@Before
	public void setCountry(){
		continent = new ContinentModel("testContinent");
		country = new CountryModel("testCountry",continent);
	}
	
	/**
	 * Test constructor, getBelongTo(), getOwner(), isFlagDFS(), getCoordinate() and getArmyNumber().<br>
	 * case 1: check all variables of the country object created in @before step.<br>
	 * case 2: create another country with random name and belongs to another continent, check all variables again.
	 */
	@Test
	public void constructorTest(){
		//case 1:
		assertEquals("testCountry",country.getName());
		assertEquals(continent,country.getBelongTo());
		assertEquals(null,country.getOwner());
		assertFalse(country.isFlagDFS());
		assertArrayEquals(new int[]{0,0},country.getCoordinate());
		assertEquals(0,country.getArmyNumber());
		//case 2:
		String name= "testCountry"+(int)(Math.random()*10);
		ContinentModel continent2 = new ContinentModel("testContinent2");
		country = new CountryModel(name,continent2);
		assertEquals(name,country.getName());
		assertEquals(continent2,country.getBelongTo());
		assertEquals(null,country.getOwner());
		assertFalse(country.isFlagDFS());
		assertArrayEquals(new int[]{0,0},country.getCoordinate());
		assertEquals(0,country.getArmyNumber());	
		System.out.println("Country Test: constructor finished.");
	}	
	
	/**
	 * Test setName(String).<br>
	 * case 1: check country's name right after create.<br>
	 * case 2: call setName(String), then check country's name again.
	 */
	@Test
	public void setNameTest(){
		//case 1:
		assertEquals("testCountry",country.getName());
		//case 2:
		country.setName("changeName");
		assertEquals("changeName",country.getName());
		System.out.println("Country Test: setName(String) finished.");
	}
	
	/**
	 * Test setBelongTo(Continent), getBelongTo().<br>
	 * case 1: check continent that the country belongs to right after create.<br>
	 * case 2£º call setBelongTo(Continent) to assign country to another continent, then check again.
	 */	
	@Test
	public void setBelongToTest(){
		//case 1:
		assertEquals(continent,country.getBelongTo());
		//case 2:
		ContinentModel changeContinent = new ContinentModel("changeContinent");
		country.setBelongTo(changeContinent);
		assertEquals(changeContinent,country.getBelongTo());
		System.out.println("Country Test: setBelongTo(Continent), getBelongTo() finished");
	}

	/**
	 * Test setOwner(Player), getOwner().<br>
	 * case 1: check country's owner before assigning this country to some player.<br>
	 * case 2: call setOwner(Player) to assign country to one player, check the owner again.<br>
	 * case 3: call setOwner(Player) to assign country to another player, check the owner again.
	 */	
	@Test
	public void setOwnerTest(){
		//case 1:
		assertEquals(null,country.getOwner());
		//case 2:
		Player player = new Player("testPlayer",Color.RED);
		country.setOwner(player);
		assertEquals(player,country.getOwner());
		//case 3:
		Player player2 = new Player("testPlayer2",Color.DARK_GRAY);
		country.setOwner(player2);
		assertEquals(player2,country.getOwner());
		System.out.println("Country Test: setOwner(Player), getOwner() finished");
	}
	
	/**
	 * Test setCoordinate(int, int), getCoordinate().<br>
	 * case 1: check country's coordinates right after create.<br>
	 * case 2: call setCoordinate(int, int) to set country's coordinates to random number, then check coordinates again.
	 */	
	@Test
	public void setCoordinateTest(){
		//case 1:
		assertArrayEquals(new int[]{0,0},country.getCoordinate());
		//case 2:
		int x = (int)(Math.random()*100);
		int y = (int)(Math.random()*100);
		country.setCoordinate(x, y);
		assertArrayEquals(new int[]{x,y},country.getCoordinate());	
		System.out.println("Country Test: setCoordinate(int, int), getCoordinate() finished");
	}
	
	/**
	 * Test setFlagDFS(boolean), isFlagDFS().<br>
	 * case 1: check country's flagDFS right after create.<br>
	 * case 2: call setFlagDFS(boolean) to set flagDFS to true, check flagDFS again.<br>
	 * case 3: call setFlagDFS(boolean) to set flagDFS to false, check flagDFS again.
	 */	
	@Test
	public void setFlagDFSTest(){
		//case 1:
		assertFalse(country.isFlagDFS());
		//case 2:
		country.setFlagDFS(true);
		assertTrue(country.isFlagDFS());
		//case 3:
		country.setFlagDFS(false);
		assertFalse(country.isFlagDFS());
		System.out.println("Country Test: setFlagDFS(boolean), isFlagDFS() finished");
	}

	/**
	 * Test setArmyNumber(int), getArmyNumber().<br>
	 * case 1: check country's army number right after create.<br>
	 * case 2: call setArmyNumber(int) to set army to a random number, then check again.
	 */	
	@Test
	public void setArmyNumberTest(){
		//case 1:
		assertEquals(0,country.getArmyNumber());
		//case 2:
		int number = (int)(Math.random()*100);
		country.setArmyNumber(number);
		assertEquals(number,country.getArmyNumber());	
		System.out.println("Country Test: setArmyNumber(int), getArmyNumber() finished");
	}
}