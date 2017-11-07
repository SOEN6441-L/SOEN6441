package gamecontrollers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import gamemodels.RiskGameModel;
import gameviews.PhaseView;
import gameviews.RiskGameView;
import mapmodels.ErrorMsg;

/**
 * Class acting as the RiskGameView's controller, 
 * to define action performed according to different users' action.
 */
public class RiskGameController implements ActionListener {
	private RiskGameModel myGameModel;
	
	/**
	 * Method to add a game model to this controller.
	 * @param m model
	 */
	public void addModel(RiskGameModel m){
		this.myGameModel = m;
	}
	
	/**
	 * Method to define action performed according to different users' action.
	 * @param e the action event of user.
	 */	
	public void actionPerformed(ActionEvent e) {
		String buttonName = e.getActionCommand();
		switch (buttonName){
		case "Step 1 - Load A Risk Map":
			loadFromFile();
			break;
		case "<< Previous":
			previousStep();
			break;
		case "Step 2 - Create Players":
			createPlayers();
			break;	
		case "Step 3 - Assign Countries":
			myGameModel.assignCountries();
			break;
		case "Step 4 - Put initial Armies":
			putInitialArmy();
			break;			
		case "Next Player":
			nextPlayer();
			break;
		case "Great ! Start Game":
			startGame();
			break;				
		case "New Game":
			newGame();
			break;				
		}
	}
	
	/**
	 * Handler used to load files.
	 */
	private void loadFromFile() { 
		//JOptionPane.showMessageDialog(null, "load from file" );
		myGameModel.setGameStage(1);//begin to load map file
		String inputFileName;
		JFileChooser chooser;
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("./src/map"));
		chooser.setDialogTitle("Choose map file");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f){
				if(f.getName().endsWith(".map")||f.isDirectory())
					return true;
				else return false;
			}
			public String getDescription(){
				return "Map files(*.map)";
			}
		});
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			myGameModel.setGameStage(3);
			return;
		}			
		inputFileName = chooser.getSelectedFile().getAbsolutePath();
		if (inputFileName.trim().isEmpty()){
			myGameModel.setGameStage(2);
			JOptionPane.showMessageDialog(null,"Map file name invalid");
		}
		else{
			ErrorMsg errorMdg = null;
			if (!(errorMdg = myGameModel.loadMapFile(inputFileName.trim())).isResult()){
				myGameModel.setGameStage(2);
				JOptionPane.showMessageDialog(null,errorMdg.getMsg());
			}
		}
	}

	/**
	 * Handler used to handling previous actions
	 */
	private void previousStep() { 
		if (myGameModel.getGameStage()>=40){//already finish puting initial armies, want to re-do it.
			myGameModel.resetPlayersInfo();	
			myGameModel.setGameStage(30);	
		}
		else if (myGameModel.getGameStage()>=30){//already assign countries, want delete them and reassign.
			if (myGameModel.getPlayers()!=null){
				myGameModel.clearPlayers();
			}	
			myGameModel.setGameStage(20);
		}	
		else if (myGameModel.getGameStage()>=20){//already create players, want delete them and recreate.
			if (myGameModel.getPlayers()!=null){
				myGameModel.deletePlayers();
			}	
			myGameModel.setGameStage(10);
		}		
		else if (myGameModel.getGameStage()>=10){//already load map, want to clear that and reload.
			if (myGameModel.getGameMap()!=null){
				myGameModel.clearGameMap();
			}	
			myGameModel.setGameStage(0);
		}
	}

	/**
	 * Handler used to create players.
	 */
	private void createPlayers() { 
		myGameModel.setGameStage(11);
		boolean retry = true;
		String[] players;
		Object[] message;
		JComboBox<Object> playerInput;
		if (myGameModel.getGameMap().getCountryNum()==1){
			players = new String[1];
			players[0] = "1 player - only for test";	
			playerInput = new JComboBox<Object>(players);
			message = new Object[]{
				"Choose how many players you want: ", playerInput
			};
			playerInput.setSelectedIndex(0);
		}
		else {
			players = new String[Math.min(6,myGameModel.getGameMap().getCountryNum())-1];
			for (int i=2;i<=players.length+1;i++) {
				players[i-2] = i+" players";
			}
			playerInput = new JComboBox<Object>(players);
			message = new Object[]{
				"Choose how many players you want (2-"+(players.length+1)+"):    ", playerInput
			};
			playerInput.setSelectedIndex(0);
		}

		while(retry){
			int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				if (playerInput.getSelectedIndex()==-1){
					JOptionPane.showMessageDialog(null,"There must be 2-6 players.");
				}
				else{
					myGameModel.createPlayers(Math.min(playerInput.getSelectedIndex()+2,
						myGameModel.getGameMap().getCountryNum()));
					retry = false;
				}
			}
			else {
				retry = false;
				myGameModel.setGameStage(12);
			}
		}
	}	
	
	/**
	 * Handler used to put initial armies
	 */
	private void putInitialArmy() { 
		myGameModel.setGameStage(31);
		if (!myGameModel.putInitialArmy()){
			myGameModel.setGameStage(32);//user canceled
		}	
	}

	/**
	 * Handler used to start the game
	 */
	private void startGame() { 
		ErrorMsg result = myGameModel.startGame();
		if (result.getResult() == 1){ //somebody wins the game
			JOptionPane.showMessageDialog(null,result.getMsg());
		}
	}

	/**
	 * Handler used to start a new game
	 */
	private void newGame() { 
		myGameModel = null;
		myGameModel = new RiskGameModel();
	}
	
	/**
	 * Handler for handling next player
	 */
	private void nextPlayer() { 
		ErrorMsg result = myGameModel.playerTurn();
		if (result.getResult()==1){//somebody win the game
			JOptionPane.showMessageDialog(null,result.getMsg());
		}
	}
	
	/**
	 * The main method of the class RiskMain
	 * @param args The array to storage the variables
	 */	
	public static void main(String[] args) {
		//create two views, one model and one controller
		RiskGameView gameView = new RiskGameView();
		PhaseView phaseView = new PhaseView();
		RiskGameModel gameModel = new RiskGameModel();
		RiskGameController gameController = new RiskGameController();
		//add model and controller to views
		phaseView.addModel(gameModel);
		gameView.addModel(gameModel);
		gameView.addController(gameController);
		//add model to controller
		gameController.addModel(gameModel);
		//add phase view to model
		gameModel.addObserver(phaseView);
		gameModel.setPhaseView(phaseView);
		gameModel.setObserverLabel(phaseView.getAssignCountryLable());
		//initialize model
		gameModel.setPhaseString("Start Up Phase");
		gameModel.setGameStage(0);
		//add game view to model
		gameModel.addObserver(gameView);

		//start application
		gameView.setVisible(true);
		phaseView.setVisible(true);
	}
}
