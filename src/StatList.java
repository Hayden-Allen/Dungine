
public class StatList extends GameObjectClass {	//stores basic information
	private int atk, def, spd;	//attack, defense, and speed stats
	private String name;
	public StatList(GameObject go) {
		super(go);
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
	public void fromGameObject(GameObject go) {
		atk = go.attribute("atk");
		def = go.attribute("def");
		spd = go.attribute("spd");
		name = go.attribute("name");
	}
}
