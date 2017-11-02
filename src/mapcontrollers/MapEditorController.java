package mapcontrollers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import mapmodels.ContinentModel;
import mapmodels.ErrorMsg;
import mapmodels.RiskMapModel;
import mapviews.BasicInfoView;
import mapviews.MapEditorView;
	
/**
 * Class acting as the MapEditorView's controller, 
 * to define action performed according to different users' action.
 * It will use method getSelectedObj() to get user's selection from the view.
 */
public class MapEditorController implements ActionListener { 
	private RiskMapModel myMapModel;
	private MapEditorView myEditorView;
	private String lastUsedContinent = "";
	private String selectedName;
	
	/**
	 * Method to add a model to this controller.
	 * @param m model
	 */
	public void addModel(RiskMapModel m){
		this.myMapModel = m;
	}

	/**
	 * Method to add a view to this controller.
	 * @param v view
	 */
	public void addView(MapEditorView v){
		this.myEditorView = v;
	}
	
	/**
	 * Method to define action performed according to different users' action.
	 * @param e the action event of user.
	 */	
	public void actionPerformed(ActionEvent e) {
		String buttonName = e.getActionCommand();
		selectedName = myEditorView.getSelectedObj();
		switch (buttonName){
		case "New Map":
			newMap();
			break;				
		case "Load Exiting Map":
			loadFromFile();
			break;
		case "New Continent":
			newContinent();
			break;
		case "New Country":
			newCountry();
			break;
		case "Save To File":
			saveToFile();
			break;
		case "Delete ":
			deleteContinent();
			break;
		case "Rename ":
			renameContinent();
			break;
		case "Control Number":
			changeContinentControl();
			break;
		case "Delete":
			deleteCountry();
			break;
		case "Rename":
			renameCountry();
			break;
		case "Move to another continent":
			moveCountry();
			break;
		case "Complete":
			myMapModel.addCompletedConnection();
			break;
		case "Clear":
			myMapModel.removeAllConnection();
			break;				
		}
	}

	/**
	 * Method to implement response to newContinentBtn, provide GUI to input new continent's name, 
	 * then call mapelements.RiskMap#addContinent to really add a continent (controlNum initialize to 0).
	 * @see mapmodels.RiskMapModel#addContinent(String, int)
	 */
	private void newContinent(){
		boolean retry = true;
		while (retry){
			String inputWord=JOptionPane.showInputDialog(null,"Input the continent name: ");
			if (inputWord!=null){
				if ((inputWord = inputWord.trim()).isEmpty()){
					JOptionPane.showMessageDialog(null,"Continnet name can't be empty");
				}
				else {
					ErrorMsg errorMsg;
					if ((errorMsg = myMapModel.addContinent(inputWord,0)).isResult()) {
						//reloadContinents();
						retry = false;
					}
					else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
				}
			}
			else retry = false;
		}	
	}

	/**
	 * Method to implement response to newCountryBtn, provide GUI to input new country's name 
	 * and choose continent belongs to, then call mapelements.RiskMap#addCountry to really add a country.
	 * @see mapmodels.RiskMapModel#addCountry(String, String, int, int)
	 */
	private void newCountry(){
		if (myMapModel.getContinents().size()==0)
			JOptionPane.showMessageDialog(null,"Country must belong to a continent, create at least one continent.");
		else{
			//Configure the ConfirmDialog
			JTextField countryInput = new JTextField();
			String continents[]= new String[myMapModel.getContinents().size()];
			int loopcount = 0, defaultIndex = 0;
			for (ContinentModel loopContinent : myMapModel.getContinents()) {
				continents[loopcount++]=loopContinent.getName();
				if (loopContinent.getName().equals(lastUsedContinent)) defaultIndex = loopcount-1; 
			}
			JComboBox<Object> continentInput = new JComboBox<Object>(continents);
			Object[] message = {
					"Input country Name:", countryInput,
					"Choose Continent Name:", continentInput
			};  
			continentInput.setSelectedIndex(defaultIndex);
			boolean retry = true;
			while (retry){
				int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if (countryInput.getText()==null||countryInput.getText().trim().isEmpty()){
						JOptionPane.showMessageDialog(null,"Country name can't be empty");
					}
					else if (continentInput.getSelectedIndex()==-1){
						JOptionPane.showMessageDialog(null,"Country must belong to a continent, choose one exiting continent or create a new one.");
					}
					else {
						ErrorMsg errorMsg;
						lastUsedContinent = (String)continentInput.getItemAt(continentInput.getSelectedIndex());
						if ((errorMsg = myMapModel.addCountry(countryInput.getText().trim(), lastUsedContinent, 0, 0)).isResult()){
							//reloadContinents();
							//reloadMatrix();
							retry = false;
						}
						else JOptionPane.showMessageDialog(null, errorMsg.getMsg());

					}
				}
				else retry = false;
			}	
		}	
	}

	/**
	 * Method to implement response to saveToFileBtn, check data validity, provide GUI to input file's name,  
	 * then call mapelements.RiskMap#saveToFile to really save map to the file.
	 * @see mapmodels.RiskMapModel#saveToFile(String)
	 * @see mapmodels.RiskMapModel#checkErrors()
	 * @see mapmodels.RiskMapModel#checkWarnings()
	 */
	private void saveToFile(){
		ErrorMsg errorMsg;
		if ((errorMsg = myMapModel.checkErrors()).isResult()){
			BasicInfoView basicInfoView = new BasicInfoView(myMapModel,myMapModel.checkWarnings(),0); //mode = 0 save file
			BasicInfoController basicInfoController = new BasicInfoController();
			basicInfoController.addModel(myMapModel);
			basicInfoController.addView(basicInfoView);
			basicInfoView.addController(basicInfoController);
			basicInfoView.setVisible(true);
			int state = basicInfoView.getState();
			basicInfoView.dispose();
			if (state==0) return;
			
			String outputFileName;
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("./src/map"));
			chooser.setDialogTitle("Save map file");
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
			if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}	
			outputFileName = chooser.getSelectedFile().getAbsolutePath().trim();
			if (outputFileName.isEmpty()){
				JOptionPane.showMessageDialog(null,"Map file name invalid");
			}
			else{
				if (outputFileName.indexOf(".map")==-1){
					outputFileName+=".map";
				}
				if ((errorMsg = myMapModel.saveToFile(outputFileName)).isResult()){
					JOptionPane.showMessageDialog(null,"Successfully save the file.");
					//setTitle("Map Editor - "+myMapModel.getRiskMapName()+" by "+myMapModel.getAuthor());
					//reloadContinents();
				}
				else JOptionPane.showMessageDialog(null,  errorMsg.getMsg());
			}
		}
		else JOptionPane.showMessageDialog(null,  errorMsg.getMsg());
	}

	/**
	 * Method to implement response to newMapBtn, create a new map object to edit.
	 */
	public void newMap(){
		if (myMapModel.isModified()){
			if (JOptionPane.showConfirmDialog(null,
					"This will discard any modification since last save, do you want to proceed?",
					"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
				return;	
		}
		myMapModel.initMapModel("Untitled");
		//reInitializeGUI();
	}

	/**
	 * Method to implement response to loadFromFileBtn, provide GUI to input file's name, call 
	 * mapelements.RiskMap#loadMapFile to load data into data structure, 
	 * call checkErrors,checkWarnings to validate data, 
	 * then refresh the main GUI.
	 * @see mapmodels.RiskMapModel#loadMapFile(String)
	 * @see mapmodels.RiskMapModel#checkErrors()
	 * @see mapmodels.RiskMapModel#checkWarnings()
	 */
	private void loadFromFile(){
		ErrorMsg errorMsg;
		if (myMapModel.isModified()){
			if (JOptionPane.showConfirmDialog(null,
					"This will discard any modification since last save, do you want to proceed?",
					"Confirm", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
				return;	
		}
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
			return;
		}			
		inputFileName = chooser.getSelectedFile().getAbsolutePath().trim();//
		if (inputFileName.isEmpty()){
			JOptionPane.showMessageDialog(null,"Map file name invalid");
		}
		else{
			RiskMapModel existingMap = new RiskMapModel();
			if ((errorMsg = existingMap.loadMapFile(inputFileName)).isResult()){//succeed load file data into our data structure
				if ((errorMsg = existingMap.checkErrors()).isResult()
						||JOptionPane.showConfirmDialog(null,errorMsg.getMsg()
								+"Do you still want to open the map and correct these errors?",
								"Confirm", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){//succeed check the errors
					int checkWarning = existingMap.checkWarnings();
					if (checkWarning>0) {
						BasicInfoView basicInfoView = new BasicInfoView(myMapModel,myMapModel.checkWarnings(),0); //mode = 0 save file
						BasicInfoController basicInfoController = new BasicInfoController();
						basicInfoController.addModel(myMapModel);
						basicInfoController.addView(basicInfoView);
						basicInfoView.addController(basicInfoController);
						basicInfoView.setVisible(true);
						int state = basicInfoView.getState();
						basicInfoView.dispose();
						if (state==0) return;
					}
					myMapModel = existingMap;
					myEditorView.changeMapModel(existingMap);
					//reloadContinents();		
					//reloadMatrix();
				}
				else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
			}
			else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
		}
	}
	
	/**
	 * Method to implement response to mDeleteContinentMenu, call 
	 * mapelements.RiskMap#deleteContinent to really delete a continent.
	 * @see mapmodels.RiskMapModel#deleteContinent(String)
	 */
	private void deleteContinent() {
		ErrorMsg errorMsg;
		if ((errorMsg=myMapModel.deleteContinent(selectedName)).isResult()){
			//reloadContinents();
		}
		else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
	}

	/**
	 * Method to implement response to mDeleteCountryMenu, call 
	 * mapelements.RiskMap#deleteCountry to really delete a country.
	 * @see mapmodels.RiskMapModel#deleteCountry(String)
	 */
	private void deleteCountry() {
		myMapModel.deleteCountry(selectedName);
		//reloadContinents();
		//reloadMatrix();
	}

	/**
	 * Method to implement response to mRenameContinentMenu, provide GUI to input new name for a continent, then call
	 * mapelements.RiskMap#renameContinent to really rename the continent.
	 * @see mapmodels.RiskMapModel#renameContinent(String, String)
	 */
	private void renameContinent() {
		boolean retry=true;
		while (retry){			
			String inputWord=JOptionPane.showInputDialog(null,"Input the new name for continent <"+selectedName+">:",selectedName);
			if (inputWord!=null){
				if (inputWord.trim().isEmpty()){
					JOptionPane.showMessageDialog(null,"Continnet name can't be empty");
				}
				else if (!inputWord.trim().equals(selectedName)) {
					ErrorMsg errorMsg;
					if ((errorMsg=myMapModel.renameContinent(selectedName,inputWord.trim())).isResult()){
						//reloadContinents();
						retry = false;
					}
					else JOptionPane.showMessageDialog(null,errorMsg.getMsg());
				}
			}
			else retry = false;
		}
	}	

	/**
	 * Method to implement response to mChangeContinentControl menu, provide GUI to input new control number for a continent, 
	 * then call mapelements.RiskMap#changeControlNum to really change a continent's control number.
	 * @see mapmodels.RiskMapModel#changeControlNum(String, int)
	 */
	private void changeContinentControl() {
		int oldControlNum = myMapModel.findContinent(selectedName).getControlNum();
		boolean retry = true;
		while(retry){
			String inputWord=JOptionPane.showInputDialog(null,"Input the new control number for continent <"+selectedName+">:",oldControlNum);
			if (inputWord!=null){
				inputWord = inputWord.trim();
				if (inputWord.isEmpty()){
					JOptionPane.showMessageDialog(null,"Control number can't be empty");
				}
				else{
					Pattern pattern = Pattern.compile("[0-9]*");  
					if (pattern.matcher(inputWord).matches()){ 
						ErrorMsg errorMsg;
						if ((errorMsg = myMapModel.changeControlNum(selectedName, Integer.parseInt(inputWord))).isResult()){
							//reloadContinents();
							retry = false;
						}
						else JOptionPane.showMessageDialog(null, errorMsg.getMsg());								
					}
					else JOptionPane.showMessageDialog(null,"Control number must be integer");
				}
			}
			else retry = false;
		}	
	}

	/**
	 * Method to implement response to mRenameCountry menu, provide GUI to input new name for a country, 
	 * then call mapelements.RiskMap#renameCountry to really rename the country.
	 * @see mapmodels.RiskMapModel#renameCountry(String, String)
	 */
	private void renameCountry() {
		boolean retry=true;
		while (retry){
			String inputWord=JOptionPane.showInputDialog(null,"Input the new name for country <"+selectedName+">:",selectedName);
			if (inputWord!=null){
				if (inputWord.trim().isEmpty()){
					JOptionPane.showMessageDialog(null,"Country name can't be empty");
				}
				else if (!inputWord.trim().equals(selectedName)) {	
					ErrorMsg errorMsg;
					if ((errorMsg=myMapModel.renameCountry(selectedName,inputWord.trim())).isResult()){
						//reloadContinents();
						//reloadMatrix();
						retry = false;
					}
					else JOptionPane.showMessageDialog(null, errorMsg.getMsg());
				}
			}
			else retry = false;
		}	
	}

	/**
	 * Method to implement response to mMoveCountry menu, provide GUI to choose a new continent for a country to move in, 
	 * then call mapelements.RiskMap#moveCountry to really move the country.
	 * @see mapmodels.RiskMapModel#moveCountry(String, String)
	 */
	private void moveCountry() {
		if (myMapModel.getContinents().size()<=1){
			JOptionPane.showMessageDialog(null,"No other continents.");
			return;
		}				
		ContinentModel oldContinent = myMapModel.findCountry(selectedName).getBelongTo();
		String continents[]= new String[myMapModel.getContinents().size()-1];
		int loopcount = 0;
		for (ContinentModel loopContinent : myMapModel.getContinents()) {
			if (!loopContinent.getName().equals(oldContinent.getName())){
				continents[loopcount++]=loopContinent.getName();
			}	
		}
		JComboBox<Object> continentInput = new JComboBox<Object>(continents);
		Object[] message = {
			"Move country <"+selectedName+"> from <"+oldContinent.getName()+"> to:  ", continentInput
		};    	
		boolean retry = true;
		while (retry){
			int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				ErrorMsg errorMsg;
				if ((errorMsg=myMapModel.moveCountry((String)continentInput.getItemAt(continentInput.getSelectedIndex()), selectedName)).isResult()){
					//reloadContinents();
					//reloadMatrix();
					retry = false;
				}	
				else JOptionPane.showMessageDialog(null, errorMsg.getMsg());
			}
			else retry = false;
		}	
	}

	/**
	 * Main method to startup the map editor.
	 * @param args command line parameters
	 */
	public static void main(String[] args) {  
		RiskMapModel myMapModel = new RiskMapModel();
		MapEditorView myEditorView = new MapEditorView();
		MapEditorController myEditorController = new MapEditorController();

		myEditorView.addModel(myMapModel);//add module to view	
		myMapModel.addObserver(myEditorView);//add view to model using Observer pattern
		myMapModel.initMapModel("Untitled");//initialize Model
		
		myEditorController.addModel(myMapModel); //add model to controller
		myEditorController.addView(myEditorView);//add view to controller
		
		myEditorView.addController(myEditorController);//add controller to view

		myEditorView.setVisible(true);
	}
}
