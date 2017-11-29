package gamemodels;

/**
 * Interface of Strategy
 */
public interface Strategy {
	/**
	 * @param player the player will enter reinforcement phase
	 */
	public void reinforcementPhase(PlayerModel player);
	/**
	 * 
	 * @param player the player  will enter attack phase
	 * @return 1- no possible attack 2-finished by player
	 */
	public int attackPhase(PlayerModel player);
	/**
	 * 
	 * @param player  the player will enter fortification phase
	 */
	public void fortificationPhase(PlayerModel player);
}
