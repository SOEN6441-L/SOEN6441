package gamemodels;

import java.io.Serializable;
import java.util.ArrayList;

import gameviews.AttackPhaseView;
import gameviews.FortificationPhaseView;
import gameviews.ReinforcePhaseView;
import mapmodels.CountryModel;

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
	public void attackPhase(PlayerModel player) {
		// TODO Auto-generated method stub
        AttackPhaseView attackPhase = new AttackPhaseView(player);
        attackPhase.setVisible(true);
        attackPhase.dispose();		
	}

	@Override
	public void fortificationPhase(PlayerModel player) {
		// TODO Auto-generated method stub
        FortificationPhaseView fortiPhase = new FortificationPhaseView(player);
        fortiPhase.setVisible(true);
        fortiPhase.dispose();			
	}

}
