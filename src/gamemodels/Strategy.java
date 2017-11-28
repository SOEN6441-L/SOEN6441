package gamemodels;

public interface Strategy {
	public void reinforcementPhase(PlayerModel player);
	public void attackPhase(PlayerModel player);
	public void fortificationPhase(PlayerModel player);
}
