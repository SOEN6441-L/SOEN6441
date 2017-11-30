package gamemodels;

import java.io.Serializable;
import java.util.ArrayList;

import mapmodels.CountryModel;

/**
 * Cheater strategy in reinforcement, attack and fortification phase.
 */
public class Cheater implements Strategy,Serializable{

	private static final long serialVersionUID = 11L;

	/**
	 * Cheater strategy in reinforcement phase
	 * @param player player object
	 */
	@Override
	public void reinforcementPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		player.setPhaseString("Reinforcement Phase");
		player.getMyGame().myLog.setLogStr("\n"+player.getDiscription()+" reinforcement phase begin.\n");
		for (CountryModel loopCountry:player.getCountries()){
			loopCountry.setArmyNumber(loopCountry.getArmyNumber()*2);
		}
		player.addArmies(player.getTotalArmies());
		player.getMyGame().myLog.setLogStr("    "+player.getDiscription()+" double the armies number on all his territories, now has "+player.getTotalArmies()+" armies\n");
	}

	/**
	 * Cheater strategy in attack phase
	 * @param player player object
	 * @return 0 no meaning
	 */
	@Override
	public int attackPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		ArrayList<CountryModel> countryList = player.getAttackingCountry(1);
		for (CountryModel loopCountry:countryList){
			ArrayList<CountryModel> neighbours = player.getMyGame().getGameMap().getAdjacencyList().get(loopCountry);
			for (CountryModel neighbour: neighbours){
				if (neighbour.getOwner()!=player){
					PlayerModel oldPlayer = neighbour.getOwner();
					neighbour.setOwner(player);
					player.addCountry(neighbour);
		    		oldPlayer.removeCountry(neighbour);
		    		oldPlayer.addArmies(0-neighbour.getArmyNumber());
					neighbour.setArmyNumber(1);
					player.addArmies(1);
					player.getMyGame().myLog.setLogStr("    "+player.getDiscription()+" conquers "+neighbour.getShowName()+"\n");
				}
			}
		}
		//player.getMyGame().myLog.setLogStr("    "+player.getDiscription()+" conquers all his neighbours\n");
		player.getMyGame().changeDominationView();
    	player.setAttackInfo("Attack phase terminated.");
    	player.getMyGame().myLog.setLogStr(player.getDiscription()+" Attack phase terminated.\n");
		return 0;
	}

	/**
	 * Cheater strategy in fortification phase
	 * @param player player object
	 */
	@Override
	public void fortificationPhase(PlayerModel player) {
		// TODO Auto-generated method stub
		player.getMyGame().myLog.setLogStr("\n"+player.getDiscription()+" fortification begin.\n");
        player.setAttackInfo("fortification begin.");
        
        ArrayList<CountryModel> countryList = player.getAttackingCountry(1);
		for (CountryModel loopCountry:countryList){
			player.addArmies(loopCountry.getArmyNumber());
			loopCountry.setArmyNumber(loopCountry.getArmyNumber()*2);
		}

		player.getMyGame().myLog.setLogStr("    "+player.getDiscription()+" double the armies number on all his territories that has enemies around.\n");

	    player.getMyGame().myLog.setLogStr(player.getDiscription()+" fortification finished.\n");
	    player.setAttackInfo("fortification finished.");		
	}
}
