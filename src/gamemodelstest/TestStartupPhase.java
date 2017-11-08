package gamemodelstest;
import static org.junit.Assert.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;
import mapmodels.ContinentModel;
import mapmodels.CountryModel;
import mapmodels.RiskMapModel;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is to test start up phase
 */
public class TestStartupPhase {
    private RiskGameModel game;
    private RiskMapModel map;

    /**
     * set up environment
     */
    @Before
    public void setUp() {
        map = new RiskMapModel();
        map.initMapModel("testMap");
        map.addContinent("testContinent",1);
        map.addCountry("A","testContinent",0,0);
        map.addCountry("B","testContinent",0,0);
        game = new RiskGameModel();
        game.setGameMap(map);
    }

    /**
     * Test if players are created
     */
    @Test
    public void createPlayer(){
        game.createPlayers(2);
        assertEquals("2",String.valueOf(game.getPlayers().length));
    }

    /**
     * Test if countries are assigned
     */
    @Test
    public void assignCountries(){
        game.createPlayers(2);
        game.assignCountries();
        int i = game.getPlayers()[0].getCountries().size();
        assertEquals("0",String.valueOf(i));
    }

    /**
     * Test if armies are initiated
     */
    @Test
    public void initiateArmies(){
        game.createPlayers(2);
        Observer AssignCountryLabel = new Observer() {
            @Override
            public void update(Observable o, Object arg) {

            }
        };
        game.setObserverLabel(AssignCountryLabel);

        boolean b = game.putInitialArmy();
        assertTrue(b);
    }
}
