
public abstract class Character extends RoomObject {
	protected int hp, gold;
	protected Inventory inv;
	protected StatList stats;
	protected Armor armor;
	protected Weapon weapon;
	
	protected Armor firstArmor() {
		for(Item i : inv.items())
			if(i instanceof Armor)
				return (Armor)i;
		return null;
	}
	protected Weapon firstWeapon() {
		for(Item i : inv.items())
			if(i instanceof Weapon)
				return (Weapon)i;
		return null;
	}
	public Armor armor() {
		return armor;
	}
	public Weapon weapon() {
		return weapon;
	}
	public int gold() {
		return gold;
	}
	public int netWorth() {	//TODO store?
		int total = gold;
		for(Item i : inv.items())
			total += i.value();
		return total;
	}
	public int hp() {
		return hp;
	}
	public Inventory inv() {
		return inv;
	}
	public StatList stats() {
		return stats;
	}
	public void addGold(int x) {
		gold += x;
	}
	
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go.<GameObject>element("visual"));
		inv = new Inventory(go.<GameObject>element("inventory"));
	}
}
