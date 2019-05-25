import java.util.*;
import java.util.function.*;
import java.io.*;

public class Console {
	public static Map<String, java.lang.Character> symbols;
	public static Map<String, Object> parameters;
	public static ArrayList<GameObjectBase> templates;
	public static String BASE_FILEPATH, MAIN_FILEPATH, FILE_TYPE;
	public static Map<String, String> keywords;
	
	@SuppressWarnings("unchecked")
	public static<E> E parameter(String id) {
		return (E)parameters.get(id);
	}
	@SafeVarargs
	public static<E> E[] arr(E...es) {
		return es;
	}
	public static GameObjectBase template(String id) {
		for(GameObjectBase gob : templates)
			if(gob.key().equals(id))
				return gob;
		return null;
	}
	public static void init() {
		BASE_FILEPATH = (new File("")).getAbsolutePath();
		BASE_FILEPATH = BASE_FILEPATH.substring(0, BASE_FILEPATH.lastIndexOf("\\")) + '\\';
		MAIN_FILEPATH = "main.txt";
		FILE_TYPE = ".txt";

		symbols = new HashMap<String, java.lang.Character>();
		parameters = new HashMap<String, Object>();
		templates = new ArrayList<GameObjectBase>();
		keywords = new HashMap<String, String>();
		
		symbols.put("Chest.Gold", 'C');
		symbols.put("Chest.Item", 'C');
		symbols.put("Wall.NS", '|');
		symbols.put("Wall.EW", '-');
		symbols.put("Wall.Corner", '+');
		symbols.put("Wall.Door", ' ');
		symbols.put("Wall.Door.Up", '^');
		symbols.put("Wall.Door.Down", 'v');
		symbols.put("Wall.Door.Left", '<');
		symbols.put("Wall.Door.Right", '>');
		symbols.put("Room.Empty", ' ');
		symbols.put("Room.Hidden.1", '\\');
		symbols.put("Room.Hidden.2", '/');
		
		parameters.put("Room.Width", 7);
		parameters.put("Room.Height", 5);
		parameters.put("Text.Speed", 25);
		
		templates.add(Console.<Integer>goa("hp", 10));
		templates.add(Console.<String>goa("name", "test"));
		
		keywords.put("Type.String", "string");
		keywords.put("Type.Integer", "int");
		keywords.put("Type.Float", "float");
		keywords.put("Type.Object", "obj");
		keywords.put("Type.List", "list");
	}
	private static<E> GameObjectAttribute<E> goa(String key, E value) {
		return new GameObjectAttribute<E>(key, value);
	}
	public static void putf(String s, Object...args) {
		int arg = 0;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '%') {
				int j = i + 1;
				while(!("%bcdfs").contains(""+s.charAt(j++)));
				putf(String.format(s.substring(i, j), args[arg++]));
				i = j - 1;
			}
			else
				System.out.print(c);
			try {Thread.sleep(Console.<Integer>parameter("Text.Speed"));}
			catch(InterruptedException e) {e.printStackTrace();}
		}
	}
	public static void main(String[] args) throws IOException {
		init();
		
		GoldChest c1 = new GoldChest(3, 1, 10);
		Room r1 = new Room(Room.UP | Room.LEFT | Room.DOWN | Room.RIGHT, false, c1), r2 = new Room(Room.ALL, true);
		World w1 = new World("world1", arr(r1, r2), arr(r2, r2));
		
//		Parser p = new Parser("helloworld.txt");
//		GameObjectAttribute<Integer> goa = new GameObjectAttribute<Integer>("hp", 10);
//		GameObjectAttribute<String> goa2 = new GameObjectAttribute<String>("name", "");
//		//System.out.println(goa.create(p));
		//System.out.println(goa2.create(p));
		
		//System.out.println(Parser.readDef("helloworld.txt"));
		
		Game g = new Game();
		Parser.createGame(g);
		
		w1.print();
	}
}
