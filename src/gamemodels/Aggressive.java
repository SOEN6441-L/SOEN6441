package gamemodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gameviews.AttackPhaseView;
import gameviews.FortificationPhaseView;
import gameviews.ReinforcePhaseView;
import mapmodels.CountryModel;
import mapmodels.RiskMapModel;

public class Aggressive implements Strategy,Serializable{
	private static final long serialVersionUID = 8L;

	@Override
	public void reinforcementPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		ReinforcePhaseView reinforcementPhase = new ReinforcePhaseView(player,1);
		ArrayList<CountryModel> countryList = player.getAttackingCountry();
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
		else{
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

	@Override
	public int attackPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		if (player.getAttackingCountry().size()==0){
        	player.setAttackInfo("No more territories can attack, attack phase finished");
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
        	attackPhase.attackOneCountry(1);
        	Boolean found = false;
        	for (int i=0;i<attackPhase.localCountries.length;i++){
        		if (attackPhase.localCountries[i].getName().equals(attackPhase.selCountryNameFrom)){
        			found = true;
        			break;
        		}
        	}
        	if (!found){  
        		break;
        	}
        }
        attackPhase.dispose();	
        return 0;
	}

	@Override
	public void fortificationPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		Map <String[], Integer> candidate = new HashMap<String[], Integer>();
		if (player.getAttackingCountry().size()==0){
			
		}
		else {
			
		}
		
		
        FortificationPhaseView fortiPhase = new FortificationPhaseView(player);
        fortiPhase.setVisible(true);
        fortiPhase.dispose();			
	}

}
