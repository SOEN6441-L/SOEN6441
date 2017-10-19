package mapelements;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CountryTest {
	private Country country;
	private Continent continent;
	
	@Before
	public void setCountry(){
		country = new Country(1,"2",null);
		continent = new Continent(1,"1");
		System.out.println("Start test");
	}
	
	@Test
	public void testChangeName(){
		country.changeName("1");
		assertEquals("1",country.countryName);
		System.out.println("Test change name success");
	}
	
	@Test
	public void testChangeContinent(){
		country.changeContinent(continent);
		assertEquals("1",country.belongToContinent.continentName);
		System.out.println("Test changeContinet success");
	}
	
}
