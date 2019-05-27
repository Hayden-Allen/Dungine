
public abstract class Character extends RoomObject implements Createable {
	protected StatList stats;
	protected int hp, gold;
	protected Inventory inv;
	
	public StatList stats() {
		return stats;
	}
	public int hp() {
		return hp;
	}
	public int gold() {
		return gold;
	}
	public void addGold(int x) {
		gold += x;
	}
	public Inventory inv() {
		return inv;
	}
}
