package gamemodels;

import java.io.Serializable;

import gameviews.AttackPhaseView;
import gameviews.FortificationPhaseView;
import gameviews.ReinforcePhaseView;

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
