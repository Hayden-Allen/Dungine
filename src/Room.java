import java.util.ArrayList;
import java.util.Iterator;

public class Room extends GameObjectClass {	//contains doors and a list of RoomObjects
	//constants representing values for open doors. All doors open is 0b1111
	public static final int UP = 0b1000, LEFT = 0b0100, DOWN = 0b0010, RIGHT = 0b0001;
	private ArrayList<RoomObject> objects;	//list of all objects
	private int doors;	//door states
	private int wx, wy;	//world coordinates of this Room
	private String onEnter, onExit;	//to be printed on entry and exit
	
	public Room(GameObject go, int wx, int wy) {
		super(go);
		this.wx = wx;
		this.wy = wy;
	}
	
	public RoomObject object(char id, int n) {	//nth occurrence of object with given name 
		int found = 0;
		for(RoomObject ro : objects) {
			if((ro.symbol() + "").equalsIgnoreCase(id + "")) {
				found++;
				if(found == n)
					return ro;
			}
		}
		return null;
	}
	public boolean door(int d) {	//whether or not the given door is open
		return (doors & d) > 0;
	}
	public void fromGameObject(GameObject go) {
		doors = go.<Integer>attribute("doors");
		
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
	public String[] objectGraphics() {	//formatted String array containing symbols of all objects at correct positions
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
		if(!onEnter.isEmpty())	//if something to print, print it
			Console.logn(onEnter);
		
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		for(RoomObject ro : objects)
			if(ro instanceof Enemy)
				enemies.add((Enemy)ro);
		
		if(!enemies.isEmpty())	//if Enemies present, start an Encounter
			enemies = (new Encounter(p, enemies)).run(g);	//assign array to result of fight
		
		//remove all enemies defeated in the Encounter
		Iterator<RoomObject> ros = objects.iterator();
		while(ros.hasNext()) {
			RoomObject cur = ros.next();
			if(cur instanceof Enemy && !enemies.contains(cur))
				ros.remove();
		}
	}
	public void onExit(Player p) {
		if(!onExit.isEmpty())	//if something to print, print it
			Console.logn(onExit);
	}
}
