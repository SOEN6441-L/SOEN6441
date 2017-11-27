/**
 * Created by liarthur on 27/11/2017.
 */
public class LI {
    package gamecontrollers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.server.RMISocketFactory;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import gamemodels.RiskGameModel;
import gameviews.DominationView;
import gameviews.LogWindow;
import gameviews.PhaseView;
import gameviews.RiskGameView;
import mapmodels.ErrorMsg;

    /**
     * Class acting as the RiskGameView's controller,
     * to define action performed according to different users' action.
     */
    public class RiskGameController implements ActionListener {
        private static RiskGameModel myGameModel;
        private static RiskGameView gameView;
        private static MonitorInterface server = null;
        private static PhaseView phaseView;
        private static DominationView domiView;
        private static LogWindow logWindow;

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
                case "Save Game":
                    saveGame();
                    break;
                case "Load Game":
                    loadGame();
                    break;
            }
        }

        /**
         * Method to save current game to disk
         */
        public void saveGame(){
            ObjectOutputStream output = null;
            try {
                String fileName = null;
                if (myGameModel.getGameMap()!=null)
                    fileName = "game"+myGameModel.getGameMap().getRiskMapName()+myGameModel.getGameStage()+new Date().getTime()+".bin";
                else
                    fileName = "game"+myGameModel.getGameStage()+new Date().getTime()+".bin";
                output = new ObjectOutputStream(new FileOutputStream("./save/"+fileName));
                output.writeObject(myGameModel);
                JOptionPane.showMessageDialog(null,"Successfully save game to <"+fileName+">.  ");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Method to load game from disk
         */
        public void loadGame(String inputFile){
            ObjectInputStream input = null;
            try {
                input = new ObjectInputStream(new FileInputStream(inputFile));
                myGameModel = (RiskGameModel)input.readObject();

            } catch (Exception e) {
                //e.printStackTrace();
            }finally{
                try{
                    input.close();
                }catch (Exception e) {
                    //e.printStackTrace();
                }
            }
            myGameModel.addLog(logWindow,1);
            myGameModel.addObserver(phaseView);
            myGameModel.addObserver(domiView);
            myGameModel.setPhaseView(phaseView,server);
            myGameModel.setObserverLabel(phaseView.getAssignCountryLable());
            myGameModel.addObserver(gameView);
            gameView.addModel(myGameModel);
            myGameModel.setGameStage(myGameModel.getGameStage());
            myGameModel.myLog.setLogStr("Load saved game "+inputFile.trim() +" succeed\n");
        }

        /**
         * Handler used to load files.
         */
        private void loadGame() {
            String inputFileName;
            JFileChooser chooser;
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("./save"));
            chooser.setDialogTitle("Choose saved game file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileFilter(){
                @Override
                public boolean accept(File f){
                    if(f.getName().endsWith(".bin")||f.isDirectory())
                        return true;
                    else return false;
                }
                public String getDescription(){
                    return "Saved game files(*.bin)";
                }
            });
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                inputFileName = chooser.getSelectedFile().getAbsolutePath();
                if (inputFileName.trim().isEmpty()){
                    JOptionPane.showMessageDialog(null,"File name invalid");
                }
                else{
                    loadGame(inputFileName.trim());
                }
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
                myGameModel.myLog.setLogStr("Map file name invalid\n");
            }
            else{
                ErrorMsg errorMdg = null;
                if (!(errorMdg = myGameModel.loadMapFile(inputFileName.trim())).isResult()){
                    myGameModel.setGameStage(2);
                    JOptionPane.showMessageDialog(null,errorMdg.getMsg());
                    myGameModel.myLog.setLogStr(errorMdg.getMsg()+"\n");
                }
                else myGameModel.myLog.setLogStr("Load Map file "+inputFileName.trim() +" succeed\n");
            }
        }

        /**
         * Handler used to handling previous actions
         */
        private void previousStep() {
            if (myGameModel.getGameStage()>=40){//already finish putting initial armies, want to re-do it.
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
            gameView = new RiskGameView();
            server = null;
            try {
                //Obtain server reference according the server name and server host (which are determined by manager id)
                //System.setSecurityManager(new SecurityManager());
                //System.setProperty("java.security.policy", "D:\\Wangxt\\Programs\\javaworkspace\\Staff Management\\");
                final int timeoutMillis = 500;
                /**
                 * Rewrite the RMISocketFactory
                 */
                RMISocketFactory.setSocketFactory( new RMISocketFactory() {
                    /*
                     * (non-Javadoc)
                     * @see java.rmi.server.RMISocketFactory#createSocket(java.lang.String, int)
                     */
                    public Socket createSocket( String host, int port ) throws IOException {
                        Socket socket = new Socket();
                        socket.setSoTimeout(timeoutMillis);
                        socket.connect(new InetSocketAddress(host, port), timeoutMillis);
                        return socket;
                    }
                    /*
                     * (non-Javadoc)
                     * @see java.rmi.server.RMISocketFactory#createServerSocket(int)
                     */
                    public ServerSocket createServerSocket( int port ) throws IOException {
                        return new ServerSocket( port );
                    }
                } );
                server = (MonitorInterface)Naming.lookup("rmi://172.168.1.252:2020/Monitor");
            }catch (Exception e){
                //e.printStackTrace();
            }
            phaseView = new PhaseView(server);
            domiView = new DominationView(server);
            logWindow = new LogWindow(server);

            myGameModel = new RiskGameModel();
            myGameModel.addLog(logWindow,0);

            RiskGameController gameController = new RiskGameController();
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

            //start application
            gameView.setVisible(true);
        }
    }

}
