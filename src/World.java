import java.util.*;

public class World extends GameObjectClass {	//stores a 2d array of Rooms
	private ArrayList<ArrayList<Room>> grid;
	private String name;	//for use in switching between different Worlds. Not implemented yet
	
	public World(Game g, GameObject go) {
		super(go);
		g.addWorld(this);	//automatically add to Game
	}
	public void fromGameObject(GameObject go) {
		name = go.<String>attribute("name");
		grid = new ArrayList<ArrayList<Room>>();	//create empty grid
		
		GameObjectList<GameObjectList<GameObject>> rooms = go.<GameObjectList<GameObject>>list("rooms");
		for(int y = 0; y < rooms.elements().size(); y++) {	//add in order that they are listed in file
			ArrayList<Room> temp = new ArrayList<Room>();
			GameObjectList<GameObject> row = rooms.elements().get(y);
			for(int x = 0; x < row.elements().size(); x++)
				temp.add(new Room(row.elements().get(x), x, y));
			grid.add(temp);
		}
	}
	public void draw(Player p) {	//draws all rooms, walls, and doors
		char corner = Console.rgraphic("room.wall.corner");
		char wallns = Console.rgraphic("room.wall.ns");	//vertical wall (north-south)
		char wallew = Console.rgraphic("room.wall.ew");	//horizontal wall (east-west)
		
		ArrayList<String> lines = new ArrayList<String>();
		for(int y = 0 ; y < grid.size(); y++) {	//fill list from top to bottom
			String[] rowLines = {"", "", "", "", ""};
			ArrayList<Room> row = grid.get(y);
			int x = 0;
			for(; x < row.size(); x++) {
				Room cur = row.get(x);	//Room at (x, y) in grid
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
					
					rowLines[i + 1] += objects[i];	//add lines of RoomObject symbols
					
					if(i == 1 && p.wx() == x && p.wy() == y)	//if player is in this Room
						//width of each Room is 6 chars, middle is 3 chars from left. 6 * x + 3
						rowLines[i + 1] = rowLines[i + 1].substring(0, 6 * x + 3) + p.symbol() + rowLines[i + 1].substring(6 * x + 4);
					
					if(x == row.size() - 1) {	//if at end of row add right walls
						if(i == 1)
							rowLines[i + 1] += doorChar(x, y, 1, 0);	//right door
						else
							rowLines[i + 1] += wallns;
					}
				}
				if(y == grid.size() - 1) {	//if on last row of grid add bottom walls
					rowLines[4] += "" + corner + wallew + wallew;	//bottom left
					rowLines[4] += doorChar(x, y, 0, 1);	//bottom door
					rowLines[4] += "" + wallew + wallew;	//bottom right
					if(x == row.size() - 1)
						rowLines[4] += corner;
				}
			}
			//if this row is longer than row above, must draw top walls
			for(int extra = x; y > 0 && extra < grid.get(y - 1).size(); extra++) {
				rowLines[0] += "" + wallew + wallew;	//bottom left
				rowLines[0] += absoluteDoorChar(x, y - 1, Room.DOWN);	//bottom door
				rowLines[0] += "" + wallew + wallew;	//bottom right
				if(x == grid.get(y - 1).size() - 1)
					rowLines[0] += corner;
			}
			for(String line : rowLines)	//add all
				if(!line.isEmpty())
					lines.add(line);
		}
		
		for(String line : lines)	//print all
			System.out.println(line);
	}
	public boolean doorAt(int x, int y, int dx, int dy) {	//whether or not the Room at (x, y) can be moved out of in given direction
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
	private char absoluteDoorChar(int x, int y, int dir) {	//Directional door character for Room at (x, y) regardless of surrounding Rooms.
															//returns |, -, v, ^, <, or >
		char closedns = Console.rgraphic("room.door.closed.ns");
		char closedew = Console.rgraphic("room.door.closed.ew");
		char n = Console.rgraphic("room.door.n");
		char e = Console.rgraphic("room.door.e");
		char w = Console.rgraphic("room.door.w");
		char s = Console.rgraphic("room.door.s");
		Room cur = grid.get(y).get(x);
		
		boolean isOpen = cur.door(dir);
		if(isOpen) {
			switch(dir) {
			case Room.UP: return n;	//^
			case Room.LEFT: return w;	//<
			case Room.DOWN: return s;	//v
			case Room.RIGHT: return e;	//>
			}
		}
		else {	//door is closed
			switch(dir) {
			case Room.UP:
			case Room.DOWN:
				return closedew;	//horizontal wall
			case Room.LEFT:
			case Room.RIGHT:
				return closedns;	//vertical wall
			}
		}
		return 'X';
	}
	private char doorChar(int x, int y, int dx, int dy) {	//TODO: make elegant	
															//Door character for Room at (x, y) taking into account surrounding Rooms
		char closedns = Console.rgraphic("room.door.closed.ns");
		char closedew = Console.rgraphic("room.door.closed.ew");
		char open = Console.rgraphic("room.door.open");
		char n = Console.rgraphic("room.door.n");
		char e = Console.rgraphic("room.door.e");
		char w = Console.rgraphic("room.door.w");
		char s = Console.rgraphic("room.door.s");
		Room cur = grid.get(y).get(x);
		
		int ox = x + dx, oy = y + dy;	
		//if Room in given direction doesn't exist
		if(oy < 0)	//above
			return cur.door(Room.UP) ? n : closedew;	//if open return ^ otherwise horizontal wall
		if(ox < 0)	//left
			return cur.door(Room.LEFT) ? w : closedns;	//if open return < otherwise vertical wall
		if(oy > grid.size() - 1)	//below
			return cur.door(Room.DOWN) ? s : closedew; 	//if open return v otherwise horizontal wall
		if(ox > grid.get(oy).size() - 1)	//right
			return cur.door(Room.RIGHT) ? e : closedns;	//if open return > otherwise vertical wall
			
		Room other = grid.get(oy).get(ox);
		if(dy < 0) {	//above
			if(cur.door(Room.UP) && other.door(Room.DOWN))	//both open
				return open;
			else if(cur.door(Room.UP))	//only this open
				return n;
			else if(other.door(Room.DOWN))	//only other open
				return s;
			else						//both closed
				return closedew;
		}
		if(dy > 0) {	//below
			if(cur.door(Room.DOWN) && other.door(Room.UP))	//both open
				return open;
			else if(cur.door(Room.DOWN))	//only this open
				return s;
			else if(other.door(Room.UP))	//only other open
				return n;
			else						//both closed
				return closedew;
		}
		if(dx < 0) {	//left
			if(cur.door(Room.LEFT) && other.door(Room.RIGHT))	//both open
				return open;
			else if(cur.door(Room.LEFT))	//only this open
				return w;
			else if(other.door(Room.RIGHT))	//only other open
				return e;
			else						//both closed
				return closedns;
		}
		if(dx > 0) {	//right
			if(cur.door(Room.RIGHT) && other.door(Room.LEFT))	//both open
				return open;
			else if(cur.door(Room.RIGHT))	//only this open
				return e;
			else if(other.door(Room.LEFT))	//only other open
				return w;
			else						//both closed
				return closedns;
		}
		
		return '\0';	//will never happen
	}
	public ArrayList<ArrayList<Room>> grid(){
		return grid;
	}
}
