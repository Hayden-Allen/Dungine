import java.util.ArrayList;

public class Inventory extends GameObjectClass {	//stores a list of Items
	private ArrayList<Item> items;
	private int maxSize;	//maximum number of items this Inventory can hold
	
	public Inventory(GameObject go) {
		super(go);	//send to GameObjectClass
	}
	
	public boolean isFull() {	//whether or not carrying capacity is reached
		return items.size() == maxSize;
	}
	public int value() {	//gold value of all Items
		int total = 0;
		for(Item i : items)
			total += i.value();
		return total;
	}
	public Weapon firstWeapon() {	//Weapon with lowest index
		for(Item i : items)
			if(i instanceof Weapon)
				return (Weapon)i;
		return null;
	}
	public Armor firstArmor() {	//Armor with lowest index
		for(Item i : items)
			if(i instanceof Armor)
				return (Armor)i;
		return null;
	}
	public String title() {	//formatted String for title of box
		char open = Console.rgraphic("inventory.title.parentheses.open"), close = Console.rgraphic("inventory.title.parentheses.close");
		char divide = Console.rgraphic("inventory.title.divide");
		
		//"Inventory [size/maxSize]"
		return String.format("%s %c%d%c%d%c", "Inventory", open, items.size(), divide, maxSize, close);
	}
	public ArrayList<Item> items(){
		return items;
	}
	public void add(Item i) {	//if possible add i to list
		if(items.size() + 1 <= maxSize)
			items.add(i);
	}
	public void remove(String name) {	//remove Item with given name
		items.remove(get(name));
	}
	public Item get(String name) {	//get Item with given name
		for(Item i : items)
			if(i.name().equalsIgnoreCase(name))
				return i;
		return null;
	}
	public boolean contains(String name) {	//whether or not list contains Item with given name
		return get(name) != null;
	}
	public int maxSize() {
		return maxSize;
	}
	public void fromGameObject(GameObject go) {
		maxSize = go.attribute("size");
		items = new ArrayList<Item>();
		
		//list of GameObjects representing Items
		GameObjectList<GameObject> itemTemplates = go.<GameObject>list("items");
		
		//if length of template list exceeds maxSize, throw error
		if(itemTemplates.elements().size() > maxSize)
			Console.parser.err(Parser.INV_OVERFLOW, itemTemplates.elements().size(), maxSize);
		
		//otherwise create Items from each template and add to list
		for(GameObject item : itemTemplates.elements())
			items.add(Item.create(item));
	}
}
