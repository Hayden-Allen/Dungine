
public abstract class Character extends MovableRoomObject {
	protected int hp, gold;
	protected Inventory inv;
	protected StatList stats;
	protected Weapon equippedWeapon;
	protected Armor equippedArmor;
	
	public Character(GameObject go) {
		super(go);
		equippedWeapon = inv.firstWeapon();
		equippedArmor = inv.firstArmor();
	}
	
	public boolean isAlive() {
		return hp > 0;
	}
	public boolean attack(Character c) {
		int damage = atk() - c.def();
		c.hp -= damage;
		if(Console.<Boolean>rsetting("hit.print"))
			Console.logn(Console.rsetting("hit.string"), name(), c.name(), damage);		
		return c.isAlive();
	}
	public static Character create(GameObject go) {
		return null;
	}
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go.object("visual"));	//send to RoomObject
		inv = new Inventory(go.object("inventory"));
		stats = new StatList(go.object("stats"));
		hp = go.attribute("hp");
		gold = go.attribute("gold");
	}
	public Inventory inv() {
		return inv;
	}
	public int hp() {
		return hp;
	}
	public int gold() {
		return gold;
	}
	public int atk() {
		int bonus = 0;
		if(equippedWeapon != null)
			bonus += equippedWeapon.atk();
		if(equippedArmor != null)
			bonus += equippedArmor.atk();
		return stats.atk() + bonus;
	}
	public int def() {
		int bonus = 0;
		if(equippedWeapon != null)
			bonus += equippedWeapon.def();
		if(equippedArmor != null)
			bonus += equippedArmor.def();
		return stats.def() + bonus;
	}
	public int spd() {
		int bonus = 0;
		if(equippedWeapon != null)
			bonus += equippedWeapon.spd();
		if(equippedArmor != null)
			bonus += equippedArmor.spd();
		return stats.spd() + bonus;
	}
	public String name() {
		return stats.name();
	}
	public void addGold(int x) {
		this.gold += x;
	}
}
