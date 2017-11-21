package gameelements;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kings
 *
 */
public class RiskGameTest {

	RiskGame risk_game;
	
	@Before
	public void setUp() throws Exception {
		risk_game=new RiskGame();
		System.out.println("@Before");
	}

	@Test
	public void test() {
		String file_name="Europe.map";
		boolean state=risk_game.loadMapFile(file_name);
		assertTrue(state);
		
	}
	
	@After
	public void tearDown(){
		System.out.println("@After");
	}

}
