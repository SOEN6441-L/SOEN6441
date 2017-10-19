package mapelements;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ContinentTest {

	RiskMap risk_map;
	
	@Before
	public void setUp() throws Exception {
		risk_map=new RiskMap();
		System.out.println("@Before");
	}

	@Test
	public void testAddContinent() {
		String temp_str="New Continent";
		boolean state=risk_map.addContinent(temp_str);

		assertTrue(state);
	}
	

	@After
	public void tearDown() throws Exception {
		System.out.println("@After");
	}
}
