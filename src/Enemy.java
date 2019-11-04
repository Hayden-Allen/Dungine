
public class Enemy extends Character {	//a Character that the player can fight in an Encounter
	//integers from [0, 100]
	private int goldDropChance, weaponDropChance, armorDropChance;
	
	public Enemy(GameObject go) {
		super(go);	//send to Character
	}
	
	public void interact(Room r, Character c) {	//TODO
		
	}
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go);	//send to Character
		//get Enemy-specific values
		goldDropChance = go.attribute("dcgold");
		weaponDropChance = go.attribute("dcweapon");
		armorDropChance = go.attribute("dcarmor");
	}
	//determine whether or not this Enemy drops their weapon/gold/armor when they die
	public boolean weaponDrop() {
		return (int)(Math.random() * 100) >= 100 - weaponDropChance;	//[0, 99]
	}
	public boolean goldDrop() {
		return (int)(Math.random() * 100) >= 100 - goldDropChance;
	}
	public boolean armorDrop() {
		return (int)(Math.random() * 100) >= 100 - armorDropChance;
	}
}
