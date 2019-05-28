import java.util.InputMismatchException;

public abstract class RoomObject implements Createable {
	protected char symbol;
	protected int x, y;
	
	public abstract void interact(Room r, Character c);
	public char symbol() {
		return symbol;
	}
	public int x() {
		return x;
	}
	public int y() {
		return y;
	}
	public static RoomObject create(GameObject go) {
		switch(go.key()) {
		case "gchest":
			return new GoldChest(go);
		}
		throw new InputMismatchException("Unable to create RoomObject with key \"" + go.key() + "\"");
	}
	public String string() {
		return String.format("symbol %c x %d y %d", symbol, x, y);
	}
	public void fromGameObject(GameObject go) {
		symbol = go.<GameObjectAttribute<java.lang.Character>>element("symbol").value();
		x = go.<GameObjectAttribute<Integer>>element("x").value();
		y = go.<GameObjectAttribute<Integer>>element("y").value();
	}
}
