package test;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gamemodels.RiskGameModel;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is to test start up phase
 */
public class RiskGameModalTest {
    private RiskGameModel game;

    /**
     * set up environment
     */
    @Before
    public void setUp() {
        game = new RiskGameModel();
        game.loadMapFile("./src/map/5.map");
        game.createPlayers(5);
        game.assignCountriesManual();
        //game.putInitialArmy();
    }

    /**
     * Test if players are created
     */
    @Test
    public void createPlayer(){
        assertEquals(5,game.getPlayers().length);
        System.out.println("Success creation of initial armies");
    }

    /**
     * Test if countries are assigned
     */
    @Test
    public void assignCountries(){
        assertEquals(1,game.getPlayers()[0].getCountries().size());
        System.out.println("Success test of assigning armies");
    }

    /**
     * Test if armies are initiated
     */
    @Test
    public void initiateArmies(){
        assertEquals(26,game.getPlayers()[0].getTotalArmies());
        System.out.println("Success test of initiating armies");
    }
    
	/**
	 * Test the Serialization of game class
	 */
	@Test
	public void gameSerializableTest(){

		try {
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("game.bin"));
		    output.writeObject(game);
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		 
		try {
		    @SuppressWarnings("resource")
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("game.bin"));
		    RiskGameModel newGame = (RiskGameModel) input.readObject();
		    assertEquals(5,newGame.getGameMap().getCountryNum());
		    assertEquals(5,newGame.getPlayers().length);
		} catch (Exception e) {
		    e.printStackTrace();
		} 
		
		System.out.println("Game Test: Serializability test finished");
	}	
}
