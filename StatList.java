
public class StatList implements Createable {
	private String name;
	private int atk, def, spd;
	
	public StatList(String name, int atk, int def, int spd) {
		this.name = name;
		this.atk = atk;
		this.def = def;
		this.spd = spd;
	}
	public StatList(GameObject go) {
		fromGameObject(go);
	}
	public StatList(Parser p) {
		GameObject go = Console.<GameObject>template("stats").create(p);
		fromGameObject(go);
	}
	
	public void fromGameObject(GameObject go) {
		name = go.<GameObjectAttribute<String>>element("name").value();
		atk = go.<GameObjectAttribute<Integer>>element("atk").value();
		def = go.<GameObjectAttribute<Integer>>element("def").value();
		spd = go.<GameObjectAttribute<Integer>>element("spd").value();
	}
	public String string() {	//TODO only print if value is not default
		return String.format("stats{name \"%s\" atk %d def %d spd %d}", name, atk, def, spd);
	}
	public String name() {
		return name;
	}
	public int atk() {
		return atk;
	}
	public int def() {
		return def;
	}
	public int spd() {
		return spd;
	}
}
