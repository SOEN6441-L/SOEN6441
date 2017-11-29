package gamemodels;

public interface Strategy {
	/**
	 * 
	 * @param player
	 */
	public void reinforcementPhase(PlayerModel player);
	/**
	 * 
	 * @param player
	 * @return 1- no possible attack 2-finished by player
	 */
	public int attackPhase(PlayerModel player);
	/**
	 * 
	 * @param player
	 */
	public void fortificationPhase(PlayerModel player);
}
