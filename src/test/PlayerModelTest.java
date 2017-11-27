package test;
import static org.junit.Assert.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;
import mapmodels.CountryModel;
import mapmodels.RiskMapModel;
import org.junit.Before;
import org.junit.Test;

/**
 * To test calculation of reinforcement armies
 */
public class PlayerModelTest {
    private PlayerModel player,player2;
    private RiskGameModel game;
    private RiskMapModel map;
    CountryModel country1;
    CountryModel country2;
    CountryModel country3;
    CountryModel country4;
    CountryModel country5;
    /**
     * Set player before testing
     */
    @Before
    public void setPlayer(){
    	game = new RiskGameModel();
        map = new RiskMapModel();
        map.initMapModel("testMap");
        game.setGameMap(map);
        map.addContinent("test1", 10);
        map.addContinent("test2", 5);
        map.addCountry("A", "test1",0,0);
        country1 = map.findCountry("A");
        map.addCountry("B", "test1",0,0);
        map.addCountry("C", "test1",0,0);
        map.addCountry("D", "test2",0,0);
        map.addCountry("E", "test2",0,0);
        country2 = map.findCountry("B");
        country3 = map.findCountry("C");
        country4 = map.findCountry("D");
        country5 = map.findCountry("E");
        player = new PlayerModel("1", Color.red, game);
        player2 = new PlayerModel("2", Color.BLACK, game);
        player.addCountry(country1);
        country1.setOwner(player);
        player.addCountry(country2);
        country2.setOwner(player);
        player2.addCountry(country3);
        country3.setOwner(player2);
        player2.addCountry(country4);
        country4.setOwner(player2);
        player2.addCountry(country5);
        country5.setOwner(player2);
        game.checkContinentOwner();
    }

    /**
     * Test calculation of reinforcement armies
     */
    @Test
    public void ReinforcementArmies(){
        player.calculateArmyNumber();
        player2.calculateArmyNumber();
        int i = player.getTotalReinforcement();
        assertEquals("3",String.valueOf(i));
        i = player2.getTotalReinforcement();
        assertEquals("6",String.valueOf(i));
        System.out.println("success of calculation of reinforcement armies");
    }

    /**
     * Test add cards method
     */
    @Test
    public void testAddCard() {
        int [] cards = {1,2,3};
        player.setCards(cards);
        int [] expect = {1,2,3};
        boolean result = Arrays.equals(expect,player.getCards());
        assertTrue(result);
        System.out.println("add cards success");
    }

    /**
     * To test if it is forced to trade in cards
     */
    @Test
    public void testIfForceExchange(){
        int [] cards = {1,2,3};
        player.setCards(cards);
        boolean result = player.ifForceExchange();
        assertTrue(result);
        System.out.println("if force Exchange success");
    }
    
    /**
     * To test if it is a correct fortification case
     */
    @Test
    public void testFortification(){
        //test connection
        country1.setOwner(player);
        country2.setOwner(player);
        country3.setOwner(player);
        country4.setOwner(player);
        country5.setOwner(player);
        map.addConnections("A","B");
        map.addConnections("B","D");
        map.addConnections("A","C");
        //map.addConnection("A","E");
        assertTrue(map.getAdjacencyList().get(country1).contains(country2));
        //test same owner
        assertEquals(player,country1.getOwner());
        assertEquals(player,country4.getOwner());
        //test armies
        assertFalse(country1.getArmyNumber()>1);
        System.out.println("success test of fortification phase");
    }
    
	/**
	 * Test the Serialization of player class
	 */
	@Test
	public void playerSerializableTest(){

		try {
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("player.bin"));
		    output.writeObject(player);
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		 
		try {
		    @SuppressWarnings("resource")
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("player.bin"));
		    PlayerModel newPlayer = (PlayerModel) input.readObject();
		    assertEquals("1",newPlayer.getName());
		    assertEquals(2,newPlayer.getCountries().size());
		    assertEquals("a",newPlayer.getMyGame().getGameMap().findCountry("A").getName());
		} catch (Exception e) {
		    e.printStackTrace();
		} 
		
		System.out.println("Player Test: Serializability test finished");
	}	
}
