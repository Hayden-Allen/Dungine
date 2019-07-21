import java.util.ArrayList;
import java.util.Arrays;

public class Room implements Createable {
	public static final int UP = 0b1000, LEFT = 0b0100, DOWN = 0b0010, RIGHT = 0b0001, ALL = 0b1111;
	private ArrayList<RoomObject> objects;
	private boolean hidden;
	private int doors;
	
	public Room(int doors, boolean hidden, RoomObject...objects) {
		this.doors = doors;
		this.hidden = hidden;
		if(objects == null)
			objects = new RoomObject[0];
		this.objects = new ArrayList<RoomObject>(Arrays.asList(objects));
	}
	public Room(GameObject go) {
		fromGameObject(go);
	}
	
	public ArrayList<RoomObject> objects(){
		return objects;
	}
	public boolean door(int d) {
		return(doors & d) > 0;
	}
	public void fromGameObject(GameObject go) {	//TODO lists
		doors = go.<GameObjectAttribute<Integer>>element("doors").value();
		hidden = go.<GameObjectAttribute<Boolean>>element("hidden").value();
		
		GameObjectList<GameObject> gobjs = go.<GameObjectList<GameObject>>element("objects");
		if(gobjs.elements() != null) {
			objects = new ArrayList<RoomObject>();
			for(GameObject object : go.<GameObjectList<GameObject>>element("objects").elements())
				objects.add(RoomObject.create(object));
		}
	}
	public boolean hidden() {
		return hidden;
	}
	public RoomObject objectAt(int x, int y) {
		if(objects != null)
			for(RoomObject ro : objects)
				if(ro.x() == x && ro.y() == y)
					return ro;
		return null;
	}
	public void removeObject(RoomObject ro) {
		objects.remove(ro);
	}
	public String string() {	//TODO objects
		return String.format("room{doors %d hidden %b}", doors, hidden);
	}
}
