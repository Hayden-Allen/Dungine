
public abstract class MovableRoomObject extends RoomObject {
	protected int wx, wy;
	public MovableRoomObject(GameObject go) {
		super(go);
	}
	
	public void move(World w, int wx, int wy, int rx, int ry, boolean tp) { //TODO: pathing
		if(wy < 0 || wy > w.grid().size() - 1 || wx < 0 || wx > w.grid().get(wy).size() - 1)
			return;
		x = rx;
		y = ry;
		w.grid().get(this.wy).get(this.wx).removeObject(this);
		this.wx = wx;
		this.wy = wy;
		w.grid().get(this.wy).get(this.wx).addObject(this);
	}
	public void setWx(int x) {
		wx = x;
	}
	public void setWy(int y) {
		wy = y;
	}
	public int wx() {
		return wx;
	}
	public int wy() {
		return wy;
	}
}
