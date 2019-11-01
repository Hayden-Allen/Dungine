import java.util.ArrayList;
import java.util.Iterator;

public class Room extends GameObjectClass {
	public static final int UP = 0b1000, LEFT = 0b0100, DOWN = 0b0010, RIGHT = 0b0001;
	private ArrayList<RoomObject> objects;
	private int doors;
	private boolean hidden;	//TODO
	private int wx, wy;
	private String onEnter, onExit;
	
	public Room(GameObject go, int wx, int wy) {
		super(go);
		this.wx = wx;
		this.wy = wy;
	}
	
	public boolean door(int d) {
		return (doors & d) > 0;
	}
	public boolean hidden() {
		return hidden;
	}
	public void fromGameObject(GameObject go) {
		doors = go.<Integer>attribute("doors");
		hidden = go.<Boolean>attribute("hidden");
		GameObject text = go.object("text");
		onEnter = text.attribute("onenter");
		onExit = text.attribute("onexit");
		
		GameObjectList<GameObject> ros = go.<GameObject>list("objects");
		if(ros.elements() != null) {
			objects = new ArrayList<RoomObject>();
			for(GameObject ro : ros.elements())
				addObject(RoomObject.create(ro));
		}
	}
	public void addObject(RoomObject ro) {
		objects.add(ro);
	}
	public void addObject(MovableRoomObject ro) {
		objects.add(ro);
		ro.setWx(wx);
		ro.setWy(wy);
	}
	public void removeObject(RoomObject ro) {
		objects.remove(ro);
	}
	public String[] objectGraphics() {
		char[][] grid = new char[2][4];
		for(RoomObject ro : objects)
			grid[ro.y()][ro.x()] = ro.symbol();
		String[] lines = {"", "     ", ""};
		for(int j = 0; j < grid[0].length; j++)
			lines[0] += grid[0][j];
		for(int j = 0; j < grid[0].length; j++)
			lines[2] += grid[1][j];
		lines[0] =  lines[0].substring(0, 2) + " " + lines[0].substring(2);
		lines[2] =  lines[2].substring(0, 2) + " " + lines[2].substring(2);
		return lines;
	}
	
	public void onEnter(Player p, Game g) {
		if(!onEnter.isEmpty())
			Console.logn(onEnter);
		
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		for(RoomObject ro : objects)
			if(ro instanceof Enemy)
				enemies.add((Enemy)ro);
		
		if(!enemies.isEmpty())
			enemies = (new Encounter(p, enemies)).run(g);
		
		Iterator<RoomObject> ros = objects.iterator();
		while(ros.hasNext()) {
			RoomObject cur = ros.next();
			if(cur instanceof Enemy && !enemies.contains(cur))
				ros.remove();
		}
	}
	public void onExit(Player p) {
		if(!onExit.isEmpty())
			Console.logn(onExit);
	}
}
