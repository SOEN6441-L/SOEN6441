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
 * To test calculation of reinforcement armies
 */
public class PlayerTestReinforcementArmies {
    private PlayerModel player;
    private RiskGameModel game;
    private RiskMapModel map;
    private ContinentModel continent;
    /**
     * Set player before testing
     */
    @Before
    public void setPlayer(){
        map = new RiskMapModel();
        map.initMapModel("testMap");
        game = new RiskGameModel();
        game.setGameMap(map);
        continent = new ContinentModel("test1");
        map.addContinent("test1",1);
        CountryModel country1 = new CountryModel(1,"A",continent);
        CountryModel country2 = new CountryModel(2,"B",continent);
        CountryModel country3 = new CountryModel(3,"C",continent);
        CountryModel country4 = new CountryModel(4,"D",continent);
        CountryModel country5 = new CountryModel(5,"E",continent);
        player = new PlayerModel("1", Color.red, game);
        player.addCountry(country1);
        player.addCountry(country2);
        player.addCountry(country3);
        player.addCountry(country4);
        player.addCountry(country5);
    }

    /**
     * To test add Countries
     */
    @Test
    public void AddCountries(){
        ArrayList<CountryModel> a = player.getCountries();
        assertEquals("5",String.valueOf(a.size()));
    }

    /**
     * Test calculation of reinforcement armies
     */
    @Test
    public void ReinforcementArmies(){
        player.calculateArmyNumber();
        int i = player.getTotalReinforcement();
        assertEquals("3",String.valueOf(i));
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
        System.out.println("if force Exchange sucess");
    }

}
