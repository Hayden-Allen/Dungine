import java.util.ArrayList;
import java.util.Arrays;

public class Room {
	public static final int UP = 0b0001, LEFT = 0b0010, DOWN = 0b0100, RIGHT = 0b1000, ALL = 0b1111;
	private int doors;
	private boolean hidden;
	private ArrayList<RoomObject> objects;
	
	public Room(int doors, boolean hidden, RoomObject...objects) {
		this.doors = doors;
		this.hidden = hidden;
		if(objects == null)
			objects = new RoomObject[0];
		this.objects = new ArrayList<RoomObject>(Arrays.asList(objects));
	}
	
	public boolean door(int d) {
		return(doors & d) > 0;
	}
	public boolean hidden() {
		return hidden;
	}
	public void removeObject(RoomObject ro) {
		objects.remove(ro);
	}
	public RoomObject objectAt(int x, int y) {
		for(RoomObject ro : objects)
			if(ro.x() == x && ro.y() == y)
				return ro;
		return null;
	}
}
