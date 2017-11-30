package gamemodels;

/**
 * Interface of Strategy
 */
public interface Strategy {
	/**
	 * interface definition of reinforcement
	 * @param player the player will enter reinforcement phase
	 */
	public void reinforcementPhase(PlayerModel player);
	/**
	 * interface definition of attack
	 * @param player the player  will enter attack phase
	 * @return 1- no possible attack 2-finished by player
	 */
	public int attackPhase(PlayerModel player);
	/**
	 * interface definition of fortification
	 * @param player  the player will enter fortification phase
	 */
	public void fortificationPhase(PlayerModel player);
}
