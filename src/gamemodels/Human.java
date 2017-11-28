package gamemodels;

import java.io.Serializable;
import java.util.ArrayList;

import gameviews.AttackPhaseView;
import gameviews.FortificationPhaseView;
import gameviews.ReinforcePhaseView;
import mapmodels.CountryModel;

public class Human implements Strategy,Serializable{
	private static final long serialVersionUID = 7L;

	@Override
	public void reinforcementPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		ReinforcePhaseView reinforcementPhase = new ReinforcePhaseView(player,0);
		reinforcementPhase.setVisible(true);
		reinforcementPhase.dispose();
	}

	@Override
	public int attackPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		ArrayList<CountryModel> attackingCountry = player.getAttackingCountry();
		if (attackingCountry.size()==0){
        	player.setAttackInfo("No more territories can attack, attack phase finished");
        	return 1;
		}
        AttackPhaseView attackPhase = new AttackPhaseView(player,0);
        attackPhase.setVisible(true);
        attackPhase.dispose();
        return 0;
	}

	@Override
	public void fortificationPhase(PlayerModel player) {
		// TODO Auto-generated method stub
        FortificationPhaseView fortiPhase = new FortificationPhaseView(player);
        fortiPhase.setVisible(true);
        fortiPhase.dispose();		
	}

}
