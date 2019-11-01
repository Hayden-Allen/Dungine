
public class Enemy extends Character {
	private int goldDropChance, weaponDropChance, armorDropChance;
	
	public Enemy(GameObject go) {
		super(go);
	}
	
	public void interact(Room r, Character c) {	//TODO
		
	}
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go);
		goldDropChance = go.attribute("dcgold");
		weaponDropChance = go.attribute("dcweapon");
		armorDropChance = go.attribute("dcarmor");
	}
	public boolean weaponDrop() {
		return (int)(Math.random() * 101) >= 100 - weaponDropChance;
	}
	public boolean goldDrop() {
		return (int)(Math.random() * 101) >= 100 - goldDropChance;
	}
	public boolean armorDrop() {
		return (int)(Math.random() * 101) >= 100 - armorDropChance;
	}
}
