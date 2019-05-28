
public abstract class Character extends RoomObject implements Createable {
	protected int hp, gold;
	protected Inventory inv;
	protected StatList stats;
	
	public int gold() {
		return gold;
	}
	public int hp() {
		return hp;
	}
	public Inventory inv() {
		return inv;
	}
	public StatList stats() {
		return stats;
	}
	public void addGold(int x) {
		gold += x;
	}
}
