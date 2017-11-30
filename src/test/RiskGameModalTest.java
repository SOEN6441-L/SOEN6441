package test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gamemodels.Benevolent;
import gamemodels.Cheater;
import gamemodels.Human;
import gamemodels.RiskGameModel;
import gameviews.DominationView;
import gameviews.LogWindow;
import gameviews.PhaseView;
import gameviews.RiskGameView;


import org.junit.Before;
import org.junit.Test;

import gamecontrollers.MonitorInterface;
import gamecontrollers.RiskGameController;

/**
 * This class is to test start up phase
 */
public class RiskGameModalTest {
	private RiskGameModel myGameModel;
	private RiskGameView gameView;
	private MonitorInterface server = null;
	private PhaseView phaseView;
	private DominationView domiView;
	private LogWindow logWindow;
	RiskGameController gameController;

    /**
     * set up environment
     */
    @Before
    public void setUp() {
		File outputFile = new File("./src/map/test/5.map");
		FileWriter fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("[Map]\r\nauthor=Invincible Team Four\r\nwarn=yes\r\nimage=none\r\nwrap=no\r\nscroll=none\r\n\r\n");
			fw.write("[Continents]\r\nAsia=10\r\nEurope=5\r\nAmerica=10\r\n\r\n");
			fw.write("[Territories]\r\nAsia1,0,0,Asia,Asia2,America2,America1,Europe1\r\nAsia2,0,0,Asia,America2,Asia1,America1,Europe1\r\n");
			fw.write("\r\nEurope1,0,0,Europe,Asia2,America2,Asia1,America1\r\n");
			fw.write("\r\nAmerica1,0,0,America,Asia2,America2,Asia1,Europe1\r\nAmerica2,0,0,America,Asia2,Asia1,America1,Europe1\r\n");

			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		gameView = new RiskGameView();
		server = null;

		phaseView = new PhaseView(server);
		domiView = new DominationView(server);
		logWindow = new LogWindow(server);
		
		myGameModel = new RiskGameModel();
		myGameModel.addLog(logWindow,0);

		RiskGameController.myGameModel = myGameModel;
		RiskGameController.gameView = gameView;
		RiskGameController.phaseView = phaseView;
		RiskGameController.domiView = domiView;
		RiskGameController.logWindow = logWindow;

		gameController = new RiskGameController();
		//add model and controller to views
		//phaseView.addModel(gameModel);
		gameView.addModel(myGameModel);
		gameView.addController(gameController);

		//add phase view to model
		myGameModel.addObserver(phaseView);
		myGameModel.addObserver(domiView);
		myGameModel.setPhaseView(phaseView,server);
		myGameModel.setObserverLabel(phaseView.getAssignCountryLable());
		//initialize model
		myGameModel.setPhaseString("Start Up Phase");
		myGameModel.setGameStage(0);
		//add game view to model
		myGameModel.addObserver(gameView);
    }

    /**
     * Test if players are created
     */
    @Test
    public void createPlayer(){
		myGameModel.loadMapFile("./src/map/test/5.map");
    	myGameModel.createPlayers(5);
		for (int k=0;k<5;k++)
			myGameModel.getPlayers()[k].setStrategy(new Human());
        assertEquals(5,myGameModel.getPlayers().length);
        System.out.println("Success creation of players");
    }

    /**
     * Test if countries are assigned
     */
    @Test
    public void assignCountries(){
		myGameModel.loadMapFile("./src/map/test/5.map");
    	myGameModel.createPlayers(5);
		for (int k=0;k<5;k++)
			myGameModel.getPlayers()[k].setStrategy(new Human());
    	myGameModel.assignCountriesManual();
        assertEquals(1,myGameModel.getPlayers()[0].getCountries().size());
        System.out.println("Success test of assigning armies");
    }

    /**
     * Test if startup phase is OK
     */
    @Test
    public void startup(){
		myGameModel.loadMapFile("./src/map/test/5.map");
    	myGameModel.createPlayers(5);
		for (int k=0;k<5;k++)
			myGameModel.getPlayers()[k].setStrategy(new Human());
    	myGameModel.assignCountriesManual();
    	myGameModel.putInitialArmy(1);	
        assertEquals(26,myGameModel.getPlayers()[0].getTotalArmies());
        System.out.println("Success test of startup phase");
    }
    
	/**
	 * Test the save of game class
	 */
	@Test
	public void gameSaveTest(){
		myGameModel.loadMapFile("./src/map/test/5.map");
    	myGameModel.createPlayers(5);
		for (int k=0;k<5;k++)
			myGameModel.getPlayers()[k].setStrategy(new Human());
    	myGameModel.assignCountriesManual();
    	myGameModel.putInitialArmy(1);	
		try {
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("game.bin"));
		    output.writeObject(myGameModel);
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
		
		System.out.println("Game Test: save test finished");
	}	
	
	/**
	 * Test the load of the game
	 */
	@Test
	public void gameLoadTest(){
		myGameModel.loadMapFile("./src/map/test/5.map");
    	myGameModel.createPlayers(2);
    	myGameModel.getPlayers()[0].setStrategy(new Cheater());
    	myGameModel.getPlayers()[1].setStrategy(new Benevolent());
    	myGameModel.assignCountriesManual();
    	myGameModel.putInitialArmy(1);
    	myGameModel.startGame();
		if (myGameModel.getGameStage()==54);
		while (myGameModel.getGameStage()!=54){
			myGameModel.playerTurn();
		}
		try {
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("game2.bin"));
		    output.writeObject(myGameModel);
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		try {
		    @SuppressWarnings("resource")
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("game2.bin"));
		    RiskGameModel newGame2 = (RiskGameModel) input.readObject();
		    boolean p1 = myGameModel.getPlayers()[0].getState();
		    boolean p2 = myGameModel.getPlayers()[1].getState();
		    assertEquals(p1,newGame2.getPlayers()[0].getState());
		    assertEquals(p2,newGame2.getPlayers()[1].getState());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		System.out.println("Game Test: load test finished");
	}
	
	/**
	 * Test the load of the game
	 */
	@Test
	public void tournamentTest(){
		File outputFile = new File("./tournament/test.conf");
		FileWriter fw = null;
		try{
			if (outputFile.exists()&&outputFile.isFile()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getAbsoluteFile(),false);
			fw.write("3,Europe,3D Cliff,5\r\n");
			fw.write("4,Aggressive,Random,Benevolent,Cheater\r\n");
			fw.write("4\r\n");
			fw.write("20\r\n");
			fw.close();
		}catch (IOException e) {
			//e.printStackTrace();	
		} finally {
			try {
				if (fw != null)fw.close();
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		gameController.loadTournament("./tournament/test.conf");
		assertEquals(3,gameController.tourMapNum);
		assertEquals(4,gameController.tourPlayerNum);
		assertEquals(4,gameController.tourGameNum);
		assertEquals(20,gameController.tourTurnNum);
		gameController.runTournamentManul();
		assertEquals("Cheater",gameController.tourResult[0][0]);
		System.out.println("Game Test: tournament test finished");
	}
}
