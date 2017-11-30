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
 * Aggressive strategy  in reinforcement and attack and fortification phase.
 */
public class Aggressive implements Strategy,Serializable{

	private static final long serialVersionUID = 8L;

	/**
	 * Aggressive strategy  in reinforcement phase.
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
		ReinforcePhaseView reinforcementPhase = new ReinforcePhaseView(player,1);
		ArrayList<CountryModel> countryList = player.getAttackingCountry(1);
		int max = 0;
		int maxArmy= 1;
		if (countryList.size()==0){//no attacking country, reinforce strongest country
			max = 0;
			maxArmy= reinforcementPhase.localCountries[0].getNumber();
			for (int i=1;i<reinforcementPhase.localCountries.length;i++){
				if (reinforcementPhase.localCountries[i].getNumber()>maxArmy){
					maxArmy = reinforcementPhase.localCountries[i].getNumber();
					max = i;
				}
			}
		}
		else{//reinforce strongest country that can attack
			max = 0;
			maxArmy= countryList.get(0).getArmyNumber();
			for (int i=1;i<countryList.size();i++){
				if (countryList.get(i).getArmyNumber()>maxArmy){
					maxArmy = countryList.get(i).getArmyNumber();
					max = i;
				}
			}
			for (int i=0;i<reinforcementPhase.localCountries.length;i++){
				if (reinforcementPhase.localCountries[i].getName().equals(countryList.get(max).getShowName())){
					max = i;
					break;
				}
			}
		}
		reinforcementPhase.reinforceArmy(max);
		reinforcementPhase.confirm();
		reinforcementPhase.dispose();
	}

	/**
	 * Aggressive strategy  in attack phase.
	 * @param player player object
	 * @return 1-no attacking or 0- normal
	 */
	@Override
	public int attackPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		if (player.getAttackingCountry(0).size()==0){
        	return 1;
		}
        AttackPhaseView attackPhase = new AttackPhaseView(player,1);
        int max = 0;
        int maxArmy = attackPhase.localCountries[0].getNumber();
        for (int i=1;i<attackPhase.localCountries.length;i++){
        	if (attackPhase.localCountries[i].getNumber()>maxArmy){
        		max = i;
        		maxArmy = attackPhase.localCountries[i].getNumber();
        	}
        }
        RiskMapModel myMap = player.getMyGame().getGameMap();
        attackPhase.selCountryNameFrom = attackPhase.localCountries[max].getName();
        attackPhase.reloadAttacked();
        while (true) {
        	attackPhase.selCountryNameTo = attackPhase.localAdjacencyList.
        			get(myMap.findCountry(attackPhase.selCountryNameFrom)).get(0).getShowName();
        	attackPhase.attackOneCountry();
    		if (player.getAttackingCountry(0).size()==0){
            	break;
    		}
        	Boolean found = false;
        	for (int i=0;i<attackPhase.localCountries.length;i++){
        		if (attackPhase.localCountries[i].getName().equals(attackPhase.selCountryNameFrom)){
        			found = true;
        			break; 
        		}
        	}
        	if (!found){ 
        		attackPhase.finishPhase();
        		break;
        	}
        }
        attackPhase.dispose();	
        return 0;
	}

	/**
	 * Aggressive strategy  in fortification phase.
	 * @param player player object
	 */
	@Override
	public void fortificationPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		player.getMyGame().myLog.setLogStr("\n"+player.getDiscription()+" fortification begin.\n");
        player.setAttackInfo("fortification begin.");
        
        RiskMapModel myMap = player.getMyGame().getGameMap();

		Map <CountryModel [], Integer> candidates = new HashMap<CountryModel[], Integer>();
		ArrayList<CountryModel> countryList = player.getAttackingCountry(1);
		
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

		int MaxValue = -1;
		CountryModel [] solution = null;
		if (countryList.size()>0){//have attacking countries
			for (CountryModel [] candidate: candidates.keySet()){
	        	Boolean found = false;
	        	for (int i=0;i<countryList.size();i++){
	        		if (countryList.get(i).equals(candidate[1])){
	        			found = true;
	        			break;
	        		}
	        	}
	        	if (!found){  
	        		continue;
	        	}					
				if (candidates.get(candidate)>MaxValue){
					solution = candidate;
					MaxValue = candidates.get(candidate);
				}
			}
		}
		
		if (MaxValue==-1){
			for (CountryModel [] candidate: candidates.keySet()){					
				if (candidates.get(candidate)>MaxValue){
					solution = candidate;
					MaxValue = candidates.get(candidate);
				}
			}
		}

		if (MaxValue!=-1){
			int armyNumber = solution[0].getArmyNumber()-1;
			player.moveArmies(solution[1],solution[0],armyNumber);
			player.getMyGame().myLog.setLogStr("    "+player.getDiscription()+" move "+armyNumber+" armies from "+ solution[0].getShowName()+" to "+ solution[1].getShowName()+".\n");
		    player.setAttackStepInfo("Move "+armyNumber+" armies from "+ solution[0].getShowName()+" to "+ solution[1].getShowName()+".");
		}
	    player.getMyGame().myLog.setLogStr(player.getDiscription()+" fortification finished.\n");
	    player.setAttackInfo("fortification finished.");		
	}
}
