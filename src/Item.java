
public abstract class Item extends GameObjectClass {	//something that a Character can use
	private StatList stats;	//name, attack, defense speed
	private String desc;	//description
	private int rarity, value;	//rarity (not used yet) and value
	private static final String nameFormat = "%c%c %s";	//format String for printing out name
	private static final String statsFormat = "%c%s%c %d%c %s%c %d%c %s%c %d%c";	//format String for printing out stats
	
	public Item(GameObject go) {
		super(go);
	}
	
	public static Item create(GameObject go) {
		//create new Item based on identifier of template
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
	public String formatName(boolean equipped) {	//format name String depending on whether or not this Item is equipped
		//get different bullet point characters based on which type of Item this is
		char bullet = 0;
		if(this instanceof Armor)
			bullet = Console.rgraphic("inventory.bullet.armor");
		else if(this instanceof Weapon)
			bullet = Console.rgraphic("inventory.bullet.weapon");
		else if(this instanceof Consumable)
			bullet = Console.rgraphic("inventory.bullet.consumable");
		
		return String.format(nameFormat, equipped ? Console.rgraphic("inventory.equipped") : ' ', bullet, name());
	}
	public String formatStats() {	//format stats String
		char statOpen = Console.rgraphic("inventory.stat.open"), statClose = Console.rgraphic("inventory.stat.close");
		char statSeparator = Console.rgraphic("inventory.stat.separator.name"), valueSeparator = Console.rgraphic("inventory.stat.separator.value");
		String atk = Console.rgraphics("inventory.stat.atk"), def = Console.rgraphics("inventory.stat.def");
		String spd = Console.rgraphics("inventory.stat.spd");
		
		return String.format(statsFormat, statOpen, 
				atk, valueSeparator, atk(), statSeparator, 
				def, valueSeparator, def(), statSeparator, 
				spd, valueSeparator, spd(), statClose);
	}
}
