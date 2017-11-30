package gamemodels;

import java.io.Serializable;
import java.util.ArrayList;

import gameviews.AttackPhaseView;
import gameviews.FortificationPhaseView;
import gameviews.ReinforcePhaseView;
import mapmodels.CountryModel;

/**
 * Human strategy in reinforcement and attack and fortification phase
 */
public class Human implements Strategy,Serializable{

	private static final long serialVersionUID = 7L;

	/**
	 * Human strategy in reinforcement phase
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
		ReinforcePhaseView reinforcementPhase = new ReinforcePhaseView(player,0);
		reinforcementPhase.setVisible(true);
		reinforcementPhase.dispose();
	}

	/**
	 * Human strategy in attack phase
	 * @param player player object
	 * @return 1 or 0
	 */
	@Override
	public int attackPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		ArrayList<CountryModel> attackingCountry = player.getAttackingCountry(0);
		if (attackingCountry.size()==0){
        	player.setAttackInfo("No more territories can attack, attack phase finished");
        	return 1;
		}
        AttackPhaseView attackPhase = new AttackPhaseView(player,0);
        attackPhase.setVisible(true);
        attackPhase.dispose();
        return 0;
	}

	/**
	 * Human strategy in fortification phase
	 * @param player player object
	 */
	@Override
	public void fortificationPhase(PlayerModel player) {
		// TODO Auto-generated method stub
        FortificationPhaseView fortiPhase = new FortificationPhaseView(player);
        fortiPhase.setVisible(true);
        fortiPhase.dispose();		
	}

}
