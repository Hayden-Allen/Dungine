
public class ItemChest extends RoomObject {
	private Item i;
	
	public ItemChest(Item i) {
		this.i = i;
		symbol = Console.symbols.get("Chest.Item");
	}
	
	public void interact(Room r, Character c) {
		c.inv().add(i);
		r.removeObject(this);
	}
}
