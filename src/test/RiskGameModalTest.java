package test;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import gamemodels.Aggressive;
import gamemodels.Cheater;
import gamemodels.RiskGameModel;
import gameviews.PhaseView;
import gameviews.RiskGameView;

import org.junit.Before;
import org.junit.Test;

/**
 * This class is to test start up phase
 */
public class RiskGameModalTest {
    private RiskGameModel game,game2;
    private Cheater cheater;
    private Aggressive aggressive;
    private RiskGameView gameView;
    private PhaseView phaseView;

    /**
     * set up environment
     */
    @Before
    public void setUp() {
    	gameView = new RiskGameView();
    	phaseView = new PhaseView(null);
    	    
        game = new RiskGameModel();
        game.loadMapFile("./src/map/5.map");
        game.createPlayers(5);
        game.assignCountriesManual();
        //game.putInitialArmy();
        
        game2 = new RiskGameModel();
        game2.loadMapFile("./src/map/5.map");
        game2.addObserver(phaseView);
    	game2.setPhaseView(phaseView,null);
    	game2.setObserverLabel(phaseView.getAssignCountryLable());
    	game2.addObserver(gameView);
        game2.createPlayers(2);
        game2.getPlayers()[0].setStrategy(new Cheater());
        game2.getPlayers()[1].setStrategy(new Aggressive());
        game2.assignCountriesManual();
        game2.putInitialArmy();

        
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
	
	/**
	 * Test the load of the game
	 */
	@Test
	public void gameLoadTest(){
//
//		try {
//		    @SuppressWarnings("resource")
//			ObjectInputStream input = new ObjectInputStream(new FileInputStream("game5401511993038227.bin"));
//		    ObjectInputStream input2 = new ObjectInputStream(new FileInputStream("game5541511993060033.bin"));
//		    RiskGameModel newGame = (RiskGameModel) input.readObject();
//		    RiskGameModel newGame2 = (RiskGameModel) input2.readObject();
//		    assertEquals(5,newGame.getGameMap().getCountryNum());
//		    assertEquals(5,newGame.getPlayers().length);
//		} catch (Exception e) {
//		    e.printStackTrace();
//		} 
		game2.startGame();
		if (game2.getGameStage()==54) return;
		while (true){
			game2.playerTurn();
			if (game2.getGameStage()==54) break;
		}
		try {
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("game2.bin"));
		    output.writeObject(game2);
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		try {
		    @SuppressWarnings("resource")
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("game2.bin"));
		    RiskGameModel newGame2 = (RiskGameModel) input.readObject();
		    boolean p1 = game2.getPlayers()[0].getState();
		    boolean p2 = game2.getPlayers()[1].getState();
		    assertEquals(p1,newGame2.getPlayers()[0].getState());
		    assertEquals(p2,newGame2.getPlayers()[0].getState());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		System.out.println("Game Test: Serializability test finished");
	}
}
