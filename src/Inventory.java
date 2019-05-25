import java.util.ArrayList;
import java.util.Arrays;

public class Inventory {
	private ArrayList<Item> items;
	private int maxSize;
	
	public Inventory(int maxSize, Item...items) {
		this.maxSize = maxSize;
		this.items = new ArrayList<Item>(Arrays.asList(items));
	}
	
	public void add(Item i) {
		if(items.size() + 1 <= maxSize)
			items.add(i);
	}
}
