import java.util.ArrayList;
import java.util.Arrays;

public class Inventory implements Createable {
	private ArrayList<Item> items;
	private int maxSize;
	
	public Inventory(int maxSize, Item...items) {
		this.maxSize = maxSize;
		this.items = new ArrayList<Item>(Arrays.asList(items));
	}
	public Inventory(GameObject go) {
		fromGameObject(go);
	}
	
	public ArrayList<Item> items(){
		return items;
	}
	public void add(Item i) {
		if(items.size() + 1 <= maxSize)
			items.add(i);
	}
	public int maxSize() {
		return maxSize;
	}
	public boolean contains(String name) {
		return get(name) != null;
	}
	@SuppressWarnings("unchecked")
	public <E extends Item> E get(String name) {
		for(Item i : items)
			if(i.stats().name().equals(name))
				return (E)i;
		return null;
	}
	public void fromGameObject(GameObject go) {
		maxSize = go.<GameObjectAttribute<Integer>>element("size").value();
		items = new ArrayList<Item>();
		GameObjectList<GameObject> ilist =  go.<GameObjectList<GameObject>>element("items");
		if(ilist != null && ilist.elements() != null)
			for(GameObject item : ilist.elements())
				items.add(Item.create(item));
	}
	public String string() {
		return "";
	}
}
