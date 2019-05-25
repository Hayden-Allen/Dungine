
public abstract class RoomObject {
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
}
