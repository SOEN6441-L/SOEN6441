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
 * This class is to test fortification phase
 */
public class FortificationPhaseTest {
    private PlayerModel player;
    private RiskGameModel game;
    private RiskMapModel map;
    private ContinentModel continent;

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
        CountryModel country1 = new CountryModel(1,"A",continent);
        CountryModel country2 = new CountryModel(2,"B",continent);
        CountryModel country3 = new CountryModel(3,"C",continent);
        CountryModel country4 = new CountryModel(4,"D",continent);
        CountryModel country5 = new CountryModel(5,"E",continent);
        country1.setOwner(player);
        country2.setOwner(player);
        country3.setOwner(player);
        country4.setOwner(player);
        country5.setOwner(player);
        map.addConnection("A","B");
        map.addConnection("A","D");
        map.addConnection("A","C");
        map.addConnection("A","E");
    }

    /**
     * To test if it is a correct fortification case
     */
    @Test
    public void testFortification(){

    }
}
