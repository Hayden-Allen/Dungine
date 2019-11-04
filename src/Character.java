
//can move between and within Rooms
//have health, gold, Inventory, and StatList

public abstract class Character extends MovableRoomObject {
	private int hp, gold;	//health an currency
	private Inventory inv;	//list of Items
	private StatList stats;	//name, atk, def, and spd
	//currently equipped Items
	private Weapon equippedWeapon;
	private Armor equippedArmor;
	
	public Character(GameObject go) {
		super(go);	//send to MovableRoomObject
		//arbitrarily choose default equipment as first in list
		equippedWeapon = inv.firstWeapon();	//Weapon with lowest index in Item array
		equippedArmor = inv.firstArmor();	//Armor with lowest index in Armor array
	}
	
	public Weapon equippedWeapon() {
		return equippedWeapon;
	}
	public Armor equippedArmor() {
		return equippedArmor;
	}
	public StatList stats() {
		return stats;
	}
	public void printInv() {	//print a box
		Console.printItemBox("inventory", 	//graphics root directory
				inv.title(), 	//"Inventory-[size/maxSize]"
				inv.items(), 	//list to be printed
				this);	//send this Character to put equipped character next to equipped items
	}
	public String equip(String name) {	//find Item with given name and equip it
		for(Item i : inv.items()) {
			if(i.name().equalsIgnoreCase(name)) {	//account for user input
				if(i instanceof Weapon) {	//if Item with given name is a Weapon
					equippedWeapon = (Weapon)i;
					return i.name();	//for use in success output
				}
				else if(i instanceof Armor) {	//if Item with given name is Armor
					equippedArmor = (Armor)i;
					return i.name();	//for use in success output
				}
			}
		}
		return null;	//for use in fail output (Item not found)
	}
	public Item getItem(String name) {	//gets Item with given name
		for(Item i : inv.items())
			if(i.name().equalsIgnoreCase(name))
				return i;
		return null;
	}
	public boolean isEquipped(Item i) {	//tests if given Item is either equippedWeapon or equippedArmor
		if(equippedWeapon != null && equippedWeapon.equals(i))
			return true;
		if(equippedArmor != null && equippedArmor.equals(i))
			return true;
		return false;
	}
	public boolean isAlive() {	//whether or not this Character can continue to fight (in Encounter)
		return hp > 0;
	}
	public boolean attack(Character c) {	//this Character attacks c
		//damage done to c by this Character
		//if c.def() is greater than atk(), this will be negative
		//negative values, which end up healing c, are bounded by c's equippedArmor's floor value
		int damage = Math.max(atk() - c.def(), c.equippedArmor.floor());
		
		c.hp -= damage;	//do damage to c
		if(Console.<Boolean>rsetting("hit.print"))	//whether or not to print when damage is done
			//format String for hit statement. Can contain up to 3 tokens, but they will always be passed in this order
			Console.logn(Console.rsetting("hit.string"), name(), c.name(), damage);	
		
		return c.isAlive();	//return whether or not c survived the attack
	}
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go.object("visual"));	//send to RoomObject
		inv = new Inventory(go.object("inventory"));	//create Inventory from GameObject
		stats = new StatList(go.object("stats"));	//create StatList from GameObject
		//get hp and gold attributes
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
	public int atk() {	//base attack plus any bonuses given by equipped Items
		int bonus = 0;
		if(equippedWeapon != null)
			bonus += equippedWeapon.atk();
		if(equippedArmor != null)
			bonus += equippedArmor.atk();
		return stats.atk() + bonus;
	}
	public int def() {	//base defense plus any bonuses given by equipped Items
		int bonus = 0;
		if(equippedWeapon != null)
			bonus += equippedWeapon.def();
		if(equippedArmor != null)
			bonus += equippedArmor.def();
		return stats.def() + bonus;
	}
	public int spd() {	//base speed plus any bonuses given by equipped Items
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
