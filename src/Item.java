
public abstract class Item extends GameObjectClass {
	private StatList stats;
	private String desc;
	private int rarity, value;
	
	public Item(GameObject go) {
		super(go);
	}
	
	public static Item create(GameObject go) {
		switch(go.key()) {
		case "weapon": return new Weapon(go);
		case "armor": return new Armor(go);
		case "consumable": return new Consumable(go);
		}
		throw new Error("Invalid Item type \"" + go.key() + "\"");
	}
	public void fromGameObject(GameObject go) {
		stats = new StatList(go.object("stats"));
		desc = go.<String>attribute("desc");
		rarity = go.<Integer>attribute("rarity");
		value = go.<Integer>attribute("value");
	}
	public String name() {
		return stats.name();
	}
	public String desc() {
		return desc;
	}
	public int atk() {
		return stats.atk();
	}
	public int def() {
		return stats.def();
	}
	public int spd() {
		return stats.spd();
	}
	public int rarity() {
		return rarity;
	}
	public int value() {
		return value;
	}
}
