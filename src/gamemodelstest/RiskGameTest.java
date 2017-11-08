package gamemodelstest;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import gamemodels.RiskGameModel;
import mapmodels.RiskMapModel;

public class RiskGameTest {
	private RiskMapModel riskmap;
	private RiskGameModel myGame;
	@Before
	public void set(){
		myGame = new RiskGameModel();
		//riskmap = new RiskMapModel("1");
	}
	
	@Test
	public void testCalclateReinforcement(){
		myGame.loadMapFile(".\\src\\map\\Europe.map");
		//myGame.createPlayers("2");
		//myGame.getPlayers()[0].calculateArmyNumber(myGame.getGameMap());
		assertTrue(myGame.getPlayers()[0].getTotalReinforcement()==13);
		System.out.println("Success test of CalclateReinforcement");
	}	
}
