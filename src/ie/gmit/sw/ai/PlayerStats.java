package ie.gmit.sw.ai;

public class PlayerStats implements Stats {
	
	public int attack;
	public int defence;

	private void Player(int playerAttack, int playerDefence){
		attack = playerAttack;
		defence = playerDefence;
	}

	@Override
	public void attackStat(int attack) {
			
	}


	@Override
	public void defenceStat(int defence) {
		
	}
}
