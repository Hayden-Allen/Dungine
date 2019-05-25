
public abstract class Character extends RoomObject {
	private StatList stats;
	private int hp, gold;
	private Inventory inv;
	
	public void addGold(int x) {
		gold += x;
	}
	public Inventory inv() {
		return inv;
	}
}
