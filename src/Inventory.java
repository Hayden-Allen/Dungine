import java.util.ArrayList;
import java.util.Arrays;

public class Inventory extends GameObjectClass {
	private ArrayList<Item> items;
	private int maxSize;
	
	private String title, bottom;
	private static final String rowFormat = "%c %c %s %c%s%c %d%c %s%c %d%c %s%c %d%c %c";
	private int rowLength;
	
	public Inventory(GameObject go) {
		super(go);
	}
	
	public int value() {
		int total = 0;
		for(Item i : items)
			total += i.value();
		return total;
	}
	public Weapon firstWeapon() {
		for(Item i : items)
			if(i instanceof Weapon)
				return (Weapon)i;
		return null;
	}
	public Armor firstArmor() {
		for(Item i : items)
			if(i instanceof Armor)
				return (Armor)i;
		return null;
	}
	private void format() {
		char borderew = Console.rgraphic("inventory.border.ew");
		char corner = Console.rgraphic("inventory.corner");
		char open = Console.rgraphic("inventory.title.parentheses.open"), close = Console.rgraphic("inventory.title.parentheses.close");
		char divide = Console.rgraphic("inventory.title.divide");
		char placeholder = 'A';	//single character placeholder for formatting
		String atk = Console.rgraphics("inventory.stat.atk"), def = Console.rgraphics("inventory.stat.def");
		String spd = Console.rgraphics("inventory.stat.spd");
		
		int maxNameLength = 0;
		for(Item i : items) {
			String row = String.format(rowFormat,
					placeholder, placeholder, i.name(), placeholder, 
					atk, placeholder, i.atk(), placeholder, 
					def, placeholder, i.def(), placeholder, 
					spd, placeholder, i.spd(), placeholder, placeholder);
			maxNameLength = Math.max(row.length(), maxNameLength);
		}
		
		int minTitleLength = String.format(placeholder + "Inventory" + placeholder + placeholder + "%d" + placeholder + "%d" + placeholder + placeholder, items.size(), maxSize).length();
		rowLength = Math.max(maxNameLength, minTitleLength);
		
		int titleDelta = rowLength - minTitleLength;
		title = corner + "";
		for(int i = 0; i < titleDelta / 2; i++)
			title += borderew;
		title += String.format("Inventory%c%c%d%c%d%c", borderew, open, items.size(), divide, maxSize, close);
		for(int i = title.length(); i < rowLength - 1; i++)
			title += borderew;
		title += corner;
		
		bottom = corner + "";
		for(int i = 0; i < rowLength - 2; i++)
			bottom += borderew;
		bottom += corner;	
	}
	public void print() {
		Console.logn(title);
		for(Item i : items)
			printItem(i);
		Console.logn(bottom);
	}
	private void printItem(Item i) {
		char borderns = Console.rgraphic("inventory.border.ns"), bullet = 0;
		char statOpen = Console.rgraphic("inventory.stat.open"), statClose = Console.rgraphic("inventory.stat.close");
		char statSeparator = Console.rgraphic("inventory.stat.separator.name"), valueSeparator = Console.rgraphic("inventory.stat.separator.value");
		String atk = Console.rgraphics("inventory.stat.atk"), def = Console.rgraphics("inventory.stat.def");
		String spd = Console.rgraphics("inventory.stat.spd");
		
		if(i instanceof Armor)
			bullet = Console.rgraphic("inventory.bullet.armor");
		else if(i instanceof Weapon)
			bullet = Console.rgraphic("inventory.bullet.weapon");
		else if(i instanceof Consumable)
			bullet = Console.rgraphic("inventory.bullet.consumable");
		
		String s = String.format(rowFormat,
				borderns, bullet, i.name(), statOpen, 
				atk, valueSeparator, i.atk(), statSeparator, 
				def, valueSeparator, i.def(), statSeparator, 
				spd, valueSeparator, i.spd(), statClose, borderns);
		while(s.length() < rowLength)
			s = s.substring(0, 4 + i.name().length()) + ' ' + s.substring(4 + i.name().length());
		Console.logn(s);
	}
	public ArrayList<Item> items(){
		return items;
	}
	public void add(Item i) {
		if(items.size() + 1 <= maxSize) {
			items.add(i);
			format();
		}
	}
	public void remove(String name) {
		items.remove(get(name));
		format();
	}
	public Item get(String name) {
		for(Item i : items)
			if(i.name().equals(name))
				return i;
		return null;
	}
	public boolean contains(String name) {
		return get(name) != null;
	}
	public int maxSize() {
		return maxSize;
	}
	public void fromGameObject(GameObject go) {
		maxSize = go.attribute("size");
		items = new ArrayList<Item>();
		
		for(GameObject item : go.<GameObject>list("items").elements())
			items.add(Item.create(item));
		
		format();
	}
}
