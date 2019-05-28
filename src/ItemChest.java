
public class ItemChest extends RoomObject implements Createable {
	private Item i;
	
	public ItemChest(Item i) {
		this.i = i;
		symbol = Console.symbols.get("Chest.Item");
	}
	
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go);
		
	}
	public void interact(Room r, Character c) {
		c.inv().add(i);
		r.removeObject(this);
	}
	public String string() {
		return String.format("ichest{%s %s}", super.string(), i.string());
	}
	
}
