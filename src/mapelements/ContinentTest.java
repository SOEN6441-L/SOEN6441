package mapelements;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ContinentTest {

	private Continent continent;
	
	@Before
	public void setContinent(){
		continent = new Continent(1,"4");
		System.out.println("Start test");
	}
	
	@Test
	public void test() {
		continent.changeName("1");
		assertEquals("1",continent.continentName);
		System.out.println("Test change name success");
	}

}
