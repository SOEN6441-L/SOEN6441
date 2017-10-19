package mapelements;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CountryTest {
	private Country country;
	
	@Before
	public void setCountry(){
		country = new Country(1,"2",null);
		System.out.println("Start test");
	}
	
	@Test
	public void testChangeName(){
		country.changeName("1");
		assertEquals("1",country.countryName);
		System.out.println("Test change name success");
	}

}
