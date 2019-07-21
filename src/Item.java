
public abstract class Item implements Createable {
	protected StatList stats;
	protected int rarity, value;
	protected String desc;
	
	public Item(StatList stats, int rarity, int value, String desc) {
		this.stats = stats;
		this.rarity = rarity;
		this.value = value;
		this.desc = desc;
	}
	public Item(GameObject go) {
		fromGameObject(go);
	}
	
	public int rarity() {
		return rarity;
	}
	public int value() {
		return value;
	}
	public void fromGameObject(GameObject go) {
		stats = new StatList(go.<GameObject>element("stats"));
		rarity = go.<GameObjectAttribute<Integer>>element("rarity").value();
		value = go.<GameObjectAttribute<Integer>>element("value").value();
		desc = go.<GameObjectAttribute<String>>element("desc").value();
	}
	public StatList stats() {
		return stats;
	}
	public String desc() {
		return desc;
	}
	public static Item create(GameObject go) {
		switch(go.key()) {
		case "weapon":
			return new Weapon(go);
		case "armor":
			return new Armor(go);
		}
		return null;
	}
	public String string() {
		return String.format("%s desc \"%s\" rarity %d value %d", stats.string(), desc, rarity, value);
	}
}
