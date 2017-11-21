package test;
import static org.junit.Assert.*;



import gamemodels.RiskGameModel;
import gameviews.putInitialArmyView;
import mapmodels.RiskMapModel;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is to test start up phase
 */
public class RiskGameModalTest {
    private RiskGameModel game;
    private RiskMapModel map;
    private putInitialArmyView startupPhase;

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
}
