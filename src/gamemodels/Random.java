package gamemodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gameviews.AttackPhaseView;
import gameviews.ReinforcePhaseView;
import mapmodels.CountryModel;
import mapmodels.RiskMapModel;

/**
 * Random strategy in the reinforcement, attack and fortification phase.
 */
public class Random implements Strategy,Serializable{

	private static final long serialVersionUID = 10L;

	/**
	 * Random strategy in the reinforcement phase
	 * @param player player object
	 */
	@Override
	public void reinforcementPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		player.calculateArmyNumber();
		player.setPhaseString("Reinforcement Phase");
		player.getMyGame().myLog.setLogStr("\n"+player.getDiscription()+" reinforcement phase begin.\n");
		player.getMyGame().myLog.setLogStr("    Totla reinforcement army is "+player.getTotalReinforcement()+"\n");
		player.getMyGame().myLog.setLogStr("        "+player.getReinforcementStr()+"\n");	
		ReinforcePhaseView reinforcementPhase = new ReinforcePhaseView(player,3);
		while (true) {
			int randomNum = (int)(Math.random()*reinforcementPhase.leftArmies);
			reinforcementPhase.armyNumberCombo.setSelectedIndex(randomNum);
			randomNum = (int)(Math.random()*reinforcementPhase.localCountries.length);
			reinforcementPhase.reinforceArmy(randomNum);
			if (reinforcementPhase.enterBtn.isVisible())
				break;
		}
		reinforcementPhase.confirm();
		reinforcementPhase.dispose();
	}

	/**
	 * Random strategy in the attack phase
	 * @param player player object
	 * @return 0 or 1
	 */
	@Override
	public int attackPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		RiskMapModel myMap = player.getMyGame().getGameMap();
		ArrayList<CountryModel> countryList = player.getAttackingCountry(0);
		if (countryList.size()==0){
        	return 1;
		}
        AttackPhaseView attackPhase = new AttackPhaseView(player,3);
        int randomNum = (int)(Math.random()*countryList.size());
        attackPhase.selCountryNameFrom = attackPhase.localCountries[randomNum].getName();
        attackPhase.reloadAttacked();
        while (true) {
        	ArrayList<CountryModel> candidates = attackPhase.localAdjacencyList.get(
        			myMap.findCountry(attackPhase.selCountryNameFrom));
        	randomNum = (int)(Math.random()*candidates.size());
        	attackPhase.selCountryNameTo = candidates.get(randomNum).getShowName();
        	attackPhase.attackOneCountry();
    		if (player.getAttackingCountry(0).size()==0){
            	break;
    		}
       		attackPhase.finishPhase();
       		break;
        }
        attackPhase.dispose();	
        return 0;
	}

	/**
	 * Random strategy in the fortification phase
	 * @param player player object
	 */
	@Override
	public void fortificationPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		player.getMyGame().myLog.setLogStr("\n"+player.getDiscription()+" fortification begin.\n");
        player.setAttackInfo("fortification begin.");
        
        RiskMapModel myMap = player.getMyGame().getGameMap();

		Map <CountryModel [], Integer> candidates = new HashMap<CountryModel[], Integer>();
		
		Map<CountryModel,ArrayList<CountryModel>> localAdjacencyList = new HashMap<CountryModel,ArrayList<CountryModel>>();
		for (CountryModel loopCountry: player.getCountries()){
			localAdjacencyList.put(loopCountry, new ArrayList<CountryModel>());
			for (CountryModel neighbour: myMap.getAdjacencyList().get(loopCountry)){
				if (neighbour.getOwner()==player){
					localAdjacencyList.get(loopCountry).add(neighbour);
				}
			}
		}
		
		for (CountryModel loopCountryFrom : player.getCountries()){
			if (loopCountryFrom.getArmyNumber()>1){
				myMap.findPath(localAdjacencyList, loopCountryFrom);
				for (CountryModel loopCountryTo : player.getCountries()){
					if (loopCountryTo.isFlagDFS()&&loopCountryTo!=loopCountryFrom){
						CountryModel [] tempArray = new CountryModel [] {loopCountryFrom,loopCountryTo};
						candidates.put(tempArray, loopCountryFrom.getArmyNumber()+loopCountryTo.getArmyNumber()-1);
					}
				}
			}	
		}
		if (candidates.size()>0){
			int randomNum = (int)(Math.random()*candidates.size());
			int j = 0;
			for (CountryModel [] solution:candidates.keySet()){
				if (j==randomNum){
					int armyNumber = solution[0].getArmyNumber()-1;
					randomNum = (int)(Math.random()*armyNumber)+1;
					player.moveArmies(solution[1],solution[0],randomNum);
					player.getMyGame().myLog.setLogStr("    "+player.getDiscription()+" move "+randomNum+" armies from "+ solution[0].getShowName()+" to "+ solution[1].getShowName()+".\n");
				    player.setAttackStepInfo("Move "+randomNum+" armies from "+ solution[0].getShowName()+" to "+ solution[1].getShowName()+".");
				    break;
				}
				j++;
			}
		}
	    player.getMyGame().myLog.setLogStr(player.getDiscription()+" fortification finished.\n");
	    player.setAttackInfo("fortification finished.");		
	}
}
