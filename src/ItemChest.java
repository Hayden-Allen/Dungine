import java.util.ArrayList;

public class ItemChest extends RoomObject implements Createable {
	private ArrayList<Item> items;
	
	public ItemChest(GameObject go) {
		fromGameObject(go);
	}
	
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go.<GameObject>element("visual"));
		items = new ArrayList<Item>();
		for(GameObject item : go.<GameObjectList<GameObject>>element("items").elements())
			items.add(Item.create(item));
	}
	public void interact(Room r, Character c) {
		//c.inv().add(i);
		r.removeObject(this);
	}
	public String string() {
		//return String.format("ichest{%s %s}", super.string(), i.string());
		return String.format("ichest{%s}", super.string());
	}
	
}
