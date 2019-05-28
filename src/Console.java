import java.util.*;
import java.io.*;

public class Console {
	public static ArrayList<GameObjectBase> templates;
	public static Map<String, String> keywords;
	public static Map<String, Object> parameters;
	public static Map<String, java.lang.Character> symbols;
	public static String BASE_FILEPATH, MAIN_FILEPATH, FILE_TYPE, TITLE_CARD;
	
	@SafeVarargs
	public static<E> E[] arr(E...es) {
		return es;
	}
	private static GameObject go(String key, GameObjectBase...elements) {
		return new GameObject(key, elements);
	}
	private static<E> GameObjectAttribute<E> goa(String key, E value) {
		return new GameObjectAttribute<E>(key, value);
	}	
	@SafeVarargs
	private static<E extends GameObjectBase> GameObjectList<E> gol(String key, E...elements){
		return new GameObjectList<E>(key, elements);
	}
	public static void init() {
		BASE_FILEPATH = (new File("")).getAbsolutePath();
		BASE_FILEPATH = BASE_FILEPATH.substring(0, BASE_FILEPATH.lastIndexOf("\\")) + '\\';
		MAIN_FILEPATH = "main.txt";
		FILE_TYPE = ".txt";
		TITLE_CARD = "\\-----\\   /     \\   |\\     \\   /-----\\   /--+--\\   |\\     \\   /-----\\\n" + 
					 " |    |   |     |   | \\    |   |            |      | \\    |   |\n" + 
				     " |    |   |     |   |  \\   |   |            |      |  \\   |   +-----\n" + 
					 " |    |   |     |   |   \\  |   |  /--\\      |      |   \\  |   |\n" + 
				     " |    |   |     |   |    \\ |   |     |      |      |    \\ |   |\n" + 
					 "/-----/   \\-----/   \\     \\|   \\-----/   \\--+--/   \\     \\|   \\-----/\n";

		symbols = new HashMap<String, java.lang.Character>();
		parameters = new HashMap<String, Object>();
		templates = new ArrayList<GameObjectBase>();
		keywords = new HashMap<String, String>();
		
		symbols.put("Wall.NS", '|');
		symbols.put("Wall.EW", '-');
		symbols.put("Wall.Corner", '+');
		symbols.put("Wall.Door", ' ');
		symbols.put("Wall.Door.Up", '^');
		symbols.put("Wall.Door.Down", 'v');
		symbols.put("Wall.Door.Left", '<');
		symbols.put("Wall.Door.Right", '>');
		symbols.put("Wall.Door.Closed.NS", ':');
		symbols.put("Wall.Door.Closed.EW", '=');
		symbols.put("Room.Empty", ' ');
		symbols.put("Room.Hidden.1", '\\');
		symbols.put("Room.Hidden.2", '/');
		
		parameters.put("Room.Width", 7);
		parameters.put("Room.Height", 5);
		parameters.put("Text.Speed", 2);
		
		templates.add(Console.<java.lang.Character>goa("symbol", '?'));
		templates.add(Console.<Integer>goa("x", 0));
		templates.add(Console.<Integer>goa("y", 0));
		templates.add(Console.<Integer>goa("hp", 10));
		templates.add(Console.<Integer>goa("atk", 10));
		templates.add(Console.<Integer>goa("def", 10));
		templates.add(Console.<Integer>goa("spd", 10));
		templates.add(Console.<Integer>goa("gold", 100));
		templates.add(Console.<Integer>goa("doors", 0));
		templates.add(Console.<String>goa("name", "test"));
		templates.add(Console.<Boolean>goa("hidden", false));
		
		templates.add(Console.go("visual", template("x"), template("y"), template("symbol")));
		templates.add(Console.go("gchest", template("visual"), template("gold")));
		
		templates.add(Console.<GameObject>gol("objects", template("gchest")));
		templates.add(Console.go("room", template("doors"), template("hidden"), template("objects")));
		templates.add(Console.<GameObject>gol("row", template("room")));
		templates.add(Console.<GameObjectList<GameObject>>gol("rooms", template("row")));
		templates.add(Console.go("world", template("name"), template("rooms")));
		
		templates.add(Console.go("stats", template("name"), template("atk"), template("def"), template("spd")));
		templates.add(Console.go("player", template("visual"), template("stats"), template("hp"), template("gold")));
		
		
		keywords.put("Type.String", "string");
		keywords.put("Type.Integer", "int");
		keywords.put("Type.Float", "float");
		keywords.put("Type.Object", "obj");
		keywords.put("Type.List", "list");
		
		putf("%41s\n%s\n\n", "M A D E   W I T H", TITLE_CARD);
	}
	@SuppressWarnings("unchecked")
	public static<E> E parameter(String id) {
		return (E)parameters.get(id);
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
	@SuppressWarnings("unchecked")
	public static<E extends GameObjectBase> E template(String id) {
		for(GameObjectBase gob : templates)
			if(gob.key().equals(id))
				return (E)	gob;
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		init();
		Game g = new Game();
		Parser.createGame(g);
		
		g.print();
	}
}
