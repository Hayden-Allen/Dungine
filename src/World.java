import java.util.*;

public class World extends GameObjectClass {	
	private ArrayList<ArrayList<Room>> grid;
	private String name;
	
	public World(Game g, GameObject go) {
		super(go);
		g.addWorld(this);
	}
	public void fromGameObject(GameObject go) {
		name = go.<String>attribute("name");
		grid = new ArrayList<ArrayList<Room>>();
		
		GameObjectList<GameObjectList<GameObject>> rooms = go.<GameObjectList<GameObject>>list("rooms");
//		for(GameObjectList<GameObject> row : rooms.elements()) {
//			ArrayList<Room> temp = new ArrayList<Room>();
//			for(GameObject room : row.elements())
//				temp.add(new Room(room));
//			grid.add(temp);
//		}
		for(int y = 0; y < rooms.elements().size(); y++) {
			ArrayList<Room> temp = new ArrayList<Room>();
			GameObjectList<GameObject> row = rooms.elements().get(y);
			for(int x = 0; x < row.elements().size(); x++)
				temp.add(new Room(row.elements().get(x), x, y));
			grid.add(temp);
		}
	}
	public String langString() {
		return from.toString();
	}
	public void draw(Player p) {
		char corner = Console.rgraphic("room.wall.corner");
		char wallns = Console.rgraphic("room.wall.ns");
		char wallew = Console.rgraphic("room.wall.ew");
		
		ArrayList<String> lines = new ArrayList<String>();
		for(int y = 0 ; y < grid.size(); y++) {
			String[] rowLines = {"", "", "", "", ""};
			ArrayList<Room> row = grid.get(y);
			int x = 0;
			for(; x < row.size(); x++) {
				Room cur = row.get(x);
				rowLines[0] += "" + corner + wallew + wallew;	//top left
				rowLines[0] += (y > 0 && x > grid.get(y - 1).size() - 1 ? absoluteDoorChar(x, y, Room.UP) : doorChar(x, y, 0, -1));	//top door
				rowLines[0] += "" + wallew + wallew;	//top right
				if(x == row.size() - 1)
					rowLines[0] += corner;
					
				String[] objects = cur.objectGraphics();
				for(int i = 0; i < 3; i++) {
					if(i == 1)
						rowLines[i + 1] += doorChar(x, y, -1, 0);	//left door
					else
						rowLines[i + 1] += wallns;
					
					rowLines[i + 1] += objects[i];
					
					if(i == 1 && p.wx() == x && p.wy() == y)
						rowLines[i + 1] = rowLines[i + 1].substring(0, 6 * x + 3) + p.symbol() + rowLines[i + 1].substring(6 * x + 4);
					
					if(x == row.size() - 1) {
						if(i == 1)
							rowLines[i + 1] += doorChar(x, y, 1, 0);	//right door
						else
							rowLines[i + 1] += wallns;
					}
				}
				if(y == grid.size() - 1) {
					rowLines[4] += "" + corner + wallew + wallew;	//bottom left
					rowLines[4] += doorChar(x, y, 0, 1);	//bottom door
					rowLines[4] += "" + wallew + wallew;	//bottom right
					if(x == row.size() - 1)
						rowLines[4] += corner;
				}
			}
			for(int extra = x; y > 0 && extra < grid.get(y - 1).size(); extra++) {
				rowLines[0] += "" + wallew + wallew;	//bottom left
				rowLines[0] += absoluteDoorChar(x, y - 1, Room.DOWN);	//bottom door
				rowLines[0] += "" + wallew + wallew;	//bottom right
				if(x == grid.get(y - 1).size() - 1)
					rowLines[0] += corner;
			}
			for(String line : rowLines)
				if(!line.isEmpty())
					lines.add(line);
		}
		
		for(String line : lines)
			System.out.println(line);
	}
	public boolean doorAt(int x, int y, int dx, int dy) {
		if(y < 0 || y > grid.size() - 1 || x < 0 || x > grid.get(y).size() - 1)
			return false;
		Room cur = grid.get(y).get(x);
		if(dx == -1)
			return cur.door(Room.LEFT);
		if(dx == 1)
			return cur.door(Room.RIGHT);
		if(dy == -1)
			return cur.door(Room.UP);
		if(dy == 1)
			return cur.door(Room.DOWN);
		return false;
	}
	private char absoluteDoorChar(int x, int y, int dir) {
		char closedns = Console.rgraphic("room.door.closed.ns");
		char closedew = Console.rgraphic("room.door.closed.ew");
		char open = Console.rgraphic("room.door.open");
		char n = Console.rgraphic("room.door.n");
		char e = Console.rgraphic("room.door.e");
		char w = Console.rgraphic("room.door.w");
		char s = Console.rgraphic("room.door.s");
		Room cur = grid.get(y).get(x);
		
		boolean isOpen = cur.door(dir);
		if(isOpen) {
			switch(dir) {
			case Room.UP: return n;
			case Room.LEFT: return w;
			case Room.DOWN: return s;
			case Room.RIGHT: return e;
			}
		}
		else {
			switch(dir) {
			case Room.UP:
			case Room.DOWN:
				return closedew;
			case Room.LEFT:
			case Room.RIGHT:
				return closedns;
			}
		}
		return 'X';
	}
	private char doorChar(int x, int y, int dx, int dy) {	//TODO: make elegant
		char closedns = Console.rgraphic("room.door.closed.ns");
		char closedew = Console.rgraphic("room.door.closed.ew");
		char open = Console.rgraphic("room.door.open");
		char n = Console.rgraphic("room.door.n");
		char e = Console.rgraphic("room.door.e");
		char w = Console.rgraphic("room.door.w");
		char s = Console.rgraphic("room.door.s");
		Room cur = grid.get(y).get(x);
		
		int ox = x + dx, oy = y + dy;		
		if(oy < 0)
			return cur.door(Room.UP) ? n : closedew;
		if(ox < 0)
			return cur.door(Room.LEFT) ? w : closedns;
		if(oy > grid.size() - 1)
			return cur.door(Room.DOWN) ? s : closedew; 
		if(ox > grid.get(oy).size() - 1)
			return cur.door(Room.RIGHT) ? e : closedns;
			
		Room other = grid.get(oy).get(ox);
		if(dy < 0) {
			if(cur.door(Room.UP) && other.door(Room.DOWN))
				return open;
			else if(cur.door(Room.UP))
				return n;
			else if(other.door(Room.DOWN))
				return s;
			else
				return closedew;
		}
		if(dy > 0) {
			if(cur.door(Room.DOWN) && other.door(Room.UP))
				return open;
			else if(cur.door(Room.DOWN))
				return s;
			else if(other.door(Room.UP))
				return n;
			else
				return closedew;
		}
		if(dx < 0) {
			if(cur.door(Room.LEFT) && other.door(Room.RIGHT))
				return open;
			else if(cur.door(Room.LEFT))
				return w;
			else if(other.door(Room.RIGHT))
				return e;
			else
				return closedns;
		}
		if(dx > 0) {
			if(cur.door(Room.RIGHT) && other.door(Room.LEFT))
				return open;
			else if(cur.door(Room.RIGHT))
				return e;
			else if(other.door(Room.LEFT))
				return w;
			else
				return closedns;
		}
		
		return 'X';
	}
	public ArrayList<ArrayList<Room>> grid(){
		return grid;
	}
}
