import java.util.ArrayList;
import java.util.Arrays;

public class World implements Createable {
	private ArrayList<ArrayList<Room>> rooms;
	private String id;
	
	public World(String id, Room[]...rooms) {
		this.rooms = new ArrayList<ArrayList<Room>>();
		for(Room[] r : rooms)
			this.rooms.add(new ArrayList<Room>(Arrays.asList(r)));
		this.id = id;
	}
	public World(GameObject go) {
		fromGameObject(go);
	}
	
	public void fromGameObject(GameObject go) {	//TODO
		id = go.<GameObjectAttribute<String>>element("id").value();
	}
	public String string() {
		return String.format("world{id %s}", id);
	}
	
	private boolean doorAt(int x, int y, int d) {
		if(y < 0 || y > rooms.size() - 1 || x < 0 || x > rooms.get(y).size() - 1)
			return false;
		return rooms.get(y).get(x).door(d);
	}
	private String[] printRoom(Room r, int x, int y) {	//TODO: mod
		int width = Console.<Integer>parameter("Room.Width"), height = Console.<Integer>parameter("Room.Height"), row = 0;
		String[] rows = new String[height];
		String tb = "+";
		
		for(int i = 0; i < width - 2; i++)
			tb += Console.symbols.get("Wall.EW");
		tb += Console.symbols.get("Wall.Corner");
		rows[row++] = tb;
		
		for(int i = 0; i < height - 2; i++) {
			String s = "" + Console.symbols.get("Wall.NS");
			for(int j = 0; j < width - 2; j++) {
				RoomObject ro = r.objectAt(j, i);
				if(r.hidden())
					s += Console.symbols.get("Room.Hidden." + ((i + j) % 2 == 0 ? "1" : "2"));
				else if(ro != null)
					s += ro.symbol();
				else
					s += Console.symbols.get("Room.Empty");
			}
			s += Console.symbols.get("Wall.NS");
			rows[row++] = s;
		}
		rows[row] = tb;
		
		int mid = height / 2, bot = rows.length - 1;
		int[] indices = {0, bot, mid, mid}, bits = {Room.UP, Room.DOWN, Room.LEFT, Room.RIGHT}, bits2 = {Room.DOWN, Room.UP, Room.RIGHT, Room.LEFT};	//UDLR
		int[] dx = {0, 0, -1, 1}, dy = {-1, 1, 0, 0};
		int[] ends = {width / 2, width / 2, 0, rows[mid].length() - 1}, starts = {width / 2 + 1, width / 2 + 1, 1, rows[mid].length()};
		String[] suffices = {"Up", "Down", "Left", "Right"}, suffices2 = {"Down", "Up", "Right", "Left"};
		
		for(int i = 0; i < 4; i++) {
			char c = 0;
			if(r.door(bits[i]))
				c = doorAt(x + dx[i], y + dy[i], bits2[i]) ? Console.symbols.get("Wall.Door") : Console.symbols.get("Wall.Door." + suffices[i]);
			else if(doorAt(x + dx[i], y + dy[i], bits2[i]))
				c = Console.symbols.get("Wall.Door" + suffices2[i]);
			if(c != 0)
				rows[indices[i]] = rows[indices[i]].substring(0, ends[i]) + c + rows[indices[i]].substring(starts[i]);
		}
		return rows;
	}
	public void print() {	//TODO: store to make more efficient
		ArrayList<String> rows = new ArrayList<String>();
		
		for(int i = 0; i < rooms.size(); i++) {
			ArrayList<String> row = new ArrayList<String>();
			
			for(int j = 0; j < rooms.get(i).size(); j++) {
				String[] s = printRoom(rooms.get(i).get(j), j, i);
				if(row.isEmpty())
					row = new ArrayList<String>(Arrays.asList(s));
				else
					for(int k = 0; k < s.length; k++)
						row.set(k, row.get(k).substring(0, row.get(k).length() - 1) + s[k]);
			}
			for(String s : row)
				rows.add(s);
		}
		int height = Console.<Integer>parameter("Room.Height");
		for(int i = height - 1; i < rows.size() - 1; i += height)
			rows.remove(i--);	//account for removal
			
		for(String s : rows)
			System.out.println(s);
	}
}
