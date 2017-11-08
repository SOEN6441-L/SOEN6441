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
 * This class is to test fortification phase
 */
public class FortificationPhaseTest {
    private PlayerModel player;
    private RiskGameModel game;
    private RiskMapModel map;
    private ContinentModel continent;
    private CountryModel country1;
    private CountryModel country2;
    private CountryModel country3;
    private CountryModel country4;
    private CountryModel country5;

    /**
     * set up environment
     */
    @Before
    public void setUp() {
        map = new RiskMapModel();
        map.initMapModel("testMap");
        game = new RiskGameModel();
        game.setGameMap(map);
        player = new PlayerModel("1", Color.red, game);
        country1 = new CountryModel(1,"A",continent);
        country2 = new CountryModel(2,"B",continent);
        country3 = new CountryModel(3,"C",continent);
        country4 = new CountryModel(4,"D",continent);
        country5 = new CountryModel(5,"E",continent);
        country1.setOwner(player);
        country2.setOwner(player);
        country3.setOwner(player);
        country4.setOwner(player);
        country5.setOwner(player);
        map.addConnection("A","B");
        map.addConnection("B","D");
        map.addConnection("A","C");
        map.addConnection("A","E");
    }

    /**
     * To test if it is a correct fortification case
     */
    @Test
    public void testFortification(){
        //test connection
        assertTrue(map.getAdjacencyList().get(country1).contains(country4));
        //test same owner
        assertEquals(player,country1.getOwner());
        assertEquals(player,country4.getOwner());
        //test armies
        assertFalse(country1.getArmyNumber()>1);
        System.out.println("success test of fortification phase");
    }
}
