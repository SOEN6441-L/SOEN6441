package test;
import static org.junit.Assert.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import gamemodels.Aggressive;
import gamemodels.Benevolent;
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
        game.createPlayers(2);
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
        player = game.getPlayers()[0];
        player2 = game.getPlayers()[1];
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
    public void testAttack(){
        //test connection
        map.addConnections("A","B");
        map.addConnections("B","D");
        map.addConnections("A","C");
        map.addConnections("A","D");
        map.addConnections("A","E");
        //map.addConnection("A","E");
        country1.setArmyNumber(100);
        country3.setArmyNumber(1);
        country4.setArmyNumber(1);
        country5.setArmyNumber(1);

        player.setStrategy(new Aggressive());
        player2.setStrategy(new Benevolent());
        
        ArrayList<CountryModel> countryList = player.getAttackingCountry(0);
    	Boolean found = false;
    	for (int i=0;i<countryList.size();i++){
    		if (countryList.get(i).equals(country1)){
    			found = true;
    			break; 
    		}
    	}
    	assertTrue(found);
    	player.attackPhase();
    	assertEquals(3,country3.getArmyNumber());
    	assertEquals(3,country4.getArmyNumber());
    	assertEquals(3,country5.getArmyNumber());
    	assertTrue(player.winGame(game.getGameMap().getCountryNum()));
        //test same owner
        System.out.println("success test of Attack phase");
    }    
    
    /**
     * To test if it is a correct fortification case
     */
    @Test
    public void testFortification(){
        //test connection
        map.addConnections("A","B");
        map.addConnections("B","D");
        map.addConnections("A","C");
        //map.addConnection("A","E");
        player.addCountry(country4);
        country4.setOwner(player);
        player2.removeCountry(country4);
        country1.setArmyNumber(5);
        country2.setArmyNumber(1);
        country4.setArmyNumber(2);
        player.setStrategy(new Aggressive());
		Map<CountryModel,ArrayList<CountryModel>> localAdjacencyList = new HashMap<CountryModel,ArrayList<CountryModel>>();
		for (CountryModel loopCountry: player.getCountries()){
			localAdjacencyList.put(loopCountry, new ArrayList<CountryModel>());
			for (CountryModel neighbour: map.getAdjacencyList().get(loopCountry)){
				if (neighbour.getOwner()==player){
					localAdjacencyList.get(loopCountry).add(neighbour);
				}
			}
		}
		map.findPath(localAdjacencyList, country1);
        assertTrue(country4.isFlagDFS());
		player.fortificationPhase();
		assertEquals(6,country1.getArmyNumber());

        //test same owner
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
		    assertEquals("Player1",newPlayer.getName());
		    assertEquals(2,newPlayer.getCountries().size());
		    assertEquals("a",newPlayer.getMyGame().getGameMap().findCountry("A").getName());
		} catch (Exception e) {
		    e.printStackTrace();
		} 
		
		System.out.println("Player Test: Serializability test finished");
	}	
}
