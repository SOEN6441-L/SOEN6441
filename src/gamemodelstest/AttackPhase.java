package gamemodelstest;
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
        CountryModel country1 = new CountryModel(1,"A",continent);
        CountryModel country2 = new CountryModel(2,"B",continent);
        CountryModel country3 = new CountryModel(3,"C",continent);
        CountryModel country4 = new CountryModel(4,"D",continent);
        CountryModel country5 = new CountryModel(5,"E",continent);
        country1.setOwner(player1);
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
        
    }

    /**
     * To test if it is a valid remove after conquering
     */
    @Test
    public void testRemove(){

    }
}
