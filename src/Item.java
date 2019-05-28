
public abstract class Item implements Createable {
	private StatList stats;
	private int rarity, value;
	private String desc;
	
	public Item(StatList stats, int rarity, int value, String desc) {
		this.stats = stats;
		this.rarity = rarity;
		this.value = value;
		this.desc = desc;
	}
	
	public void fromGameObject(GameObject go) {
		stats = new StatList(go.<GameObject>element("stats"));
		rarity = go.<GameObjectAttribute<Integer>>element("rarity").value();
		value = go.<GameObjectAttribute<Integer>>element("value").value();
		desc = go.<GameObjectAttribute<String>>element("desc").value();
	}
}
