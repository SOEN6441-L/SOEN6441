package gamemodels;

public interface Strategy {
	/**
	 * Method reinforcementPhase()
	 * @param player
	 */
	public void reinforcementPhase(PlayerModel player);
	/**
	 * Method attackPhase()
	 * @param player
	 * @return 1- no possible attack 2-finished by player
	 */
	public int attackPhase(PlayerModel player);
	/**
	 * Method fortificationPhase()
	 * @param player
	 */
	public void fortificationPhase(PlayerModel player);
}
