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
 * Benevolent strategy in reinforcement and attack and fortification phase
 */
public class Benevolent implements Strategy,Serializable{

	private static final long serialVersionUID = 9L;
	private String country;

	/**
	 * Benevolent strategy in reinforcement phase.
	 * @param player player object
	 */
	@Override
	public void reinforcementPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		country = null;
		player.calculateArmyNumber();
		player.setPhaseString("Reinforcement Phase");
		player.getMyGame().myLog.setLogStr("\n"+player.getDiscription()+" reinforcement phase begin.\n");
		player.getMyGame().myLog.setLogStr("    Totla reinforcement army is "+player.getTotalReinforcement()+"\n");
		player.getMyGame().myLog.setLogStr("        "+player.getReinforcementStr()+"\n");	
		ReinforcePhaseView reinforcementPhase = new ReinforcePhaseView(player,2);
		int min = 0;
		int minArmy= reinforcementPhase.localCountries[0].getNumber();
		for (int i=1;i<reinforcementPhase.localCountries.length;i++){
			if (reinforcementPhase.localCountries[i].getNumber()<minArmy){
				minArmy = reinforcementPhase.localCountries[i].getNumber();
				min = i;
			}
		}
		country = reinforcementPhase.localCountries[min].getName();
		reinforcementPhase.reinforceArmy(min);
		reinforcementPhase.confirm();
		reinforcementPhase.dispose();
	}

	/**
	 * Benevolent strategy in attack phase.
	 * @param player player object
	 * @return 1 or 0
	 */
	@Override
	public int attackPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		if (player.getAttackingCountry(0).size()==0){
        	return 1;
		}
        AttackPhaseView attackPhase = new AttackPhaseView(player,1);
        attackPhase.finishPhase();
        attackPhase.dispose();	
        return 0;
	}

	/**
	 * Benevolent strategy in fortification phase.
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
			if (!loopCountryFrom.getShowName().equals(country)&&loopCountryFrom.getArmyNumber()>1){
				myMap.findPath(localAdjacencyList, loopCountryFrom);
				for (CountryModel loopCountryTo : player.getCountries()){
					if (loopCountryTo.isFlagDFS()&&loopCountryTo!=loopCountryFrom&&!loopCountryTo.getShowName().equals(country) ){
						CountryModel [] tempArray = new CountryModel [] {loopCountryFrom,loopCountryTo};
						candidates.put(tempArray, loopCountryFrom.getArmyNumber()+loopCountryTo.getArmyNumber()-1);
					}
				}
			}	
		}

		int minValue = 1000000;
		CountryModel [] solution = null;
		for (CountryModel [] candidate: candidates.keySet()){					
			if (candidate[1].getArmyNumber()<minValue){
				solution = candidate;
				minValue = candidate[1].getArmyNumber();
			}
		}

		if (solution!=null){
			int armyNumber = solution[0].getArmyNumber()-1;
			player.moveArmies(solution[1],solution[0],armyNumber);
			player.getMyGame().myLog.setLogStr("    "+player.getDiscription()+" move "+armyNumber+" armies from "+ solution[0].getShowName()+" to "+ solution[1].getShowName()+".\n");
		    player.setAttackStepInfo("Move "+armyNumber+" armies from "+ solution[0].getShowName()+" to "+ solution[1].getShowName()+".");
		}
	    player.getMyGame().myLog.setLogStr(player.getDiscription()+" fortification finished.\n");
	    player.setAttackInfo("fortification finished.");		
	}
}
