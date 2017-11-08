package test;
import static org.junit.Assert.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;
import mapmodels.RiskMapModel;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is to test AttackPhase
 */
public class AttackPhase {
    private PlayerModel player1;
    private PlayerModel player2;
    private RiskGameModel game;
    private RiskMapModel map;
    private ContinentModel continent;
    private CountryModel country;
    private CountryModel country2;
    private CountryModel country3;
    private CountryModel country4;
    private CountryModel country5;


    /**
     * set up environment
     */
    @Before
    public void setUp(){
        map = new RiskMapModel();
        map.initMapModel("testMap");
        game = new RiskGameModel();
        game.setGameMap(map);
        player1 = new PlayerModel("1", Color.red, game);
        player2 = new PlayerModel("2",Color.BLACK,game);
        continent = new ContinentModel("test1");
        map.addContinent("test1",1);
        country = new CountryModel(1,"A",continent);
        country2 = new CountryModel(2,"B",continent);
        country3 = new CountryModel(3,"C",continent);
        country4 = new CountryModel(4,"D",continent);
        country5 = new CountryModel(5,"E",continent);
        country.setOwner(player1);
        country2.setOwner(player1);
        country3.setOwner(player1);
        country4.setOwner(player2);
        country5.setOwner(player2);
        map.addConnection("A","B");
        map.addConnection("A","D");
    }

    /**
     * To test Validation of Attacker/defender
     */
    @Test
    public void testAttackerDenfender(){
        //check valid connection between two countries
        assertEquals(2,map.getAdjacencyList().get(country).size());
        assertTrue(map.getAdjacencyList().get(country).contains(country2));
        assertFalse(map.getAdjacencyList().get(country2).contains(country4));

        //check if armies are enough to attack
        int i = country.getArmyNumber();
        assertFalse(i>1);
    }

    /**
     * To test if it is a valid remove after conquering
     */
    @Test
    public void testRemove(){
        //case 1:
        assertEquals(player1,country.getOwner());
        //case 2:
        country.setOwner(player2);
        assertEquals(player2,country.getOwner());
        //case 3:
        assertEquals(player2,country4.getOwner());
        System.out.println("Country Test: setOwner(Player), getOwner() finished");
    }
}
