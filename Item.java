
public abstract class Item {
	private StatList stats;
	private int rarity, value;
	private String desc;
	
	public Item(StatList stats, int rarity, int value, String desc) {
		this.stats = stats;
		this.rarity = rarity;
		this.value = value;
		this.desc = desc;
	}
}
