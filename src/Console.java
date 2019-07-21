import java.util.*;
import java.io.*;


public class Console {
	public static ArrayList<GameObjectBase> templates;
	public static Map<String, String> keywords;
	public static Map<String, ArrayList<String>> commands;
	public static Map<String, Object> parameters;
	public static Map<String, java.lang.Character> symbols;
	public static Map<ArrayList<String>, GameFunction> commandfns;
	public static String BASE_FILEPATH, MAIN_FILEPATH, FILE_TYPE, TITLE_CARD;
	
	@SafeVarargs
	public static<E> E[] arr(E...es) {
		return es;
	}
	@SafeVarargs
	public static<E> ArrayList<E> arrl(E...es){
		return new ArrayList<E>(Arrays.asList(es));
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
		TITLE_CARD = "\n" +
					 " \\-----\\   /     \\   |\\     \\   /-----\\   /--+--\\   |\\     \\   /-----\\\n" + 
					 "  |    |   |     |   | \\    |   |            |      | \\    |   |\n" + 
				     "  |    |   |     |   |  \\   |   |            |      |  \\   |   +-----\n" + 
					 "  |    |   |     |   |   \\  |   |  /--\\      |      |   \\  |   |\n" + 
				     "  |    |   |     |   |    \\ |   |     |      |      |    \\ |   |\n" + 
					 " /-----/   \\-----/   \\     \\|   \\-----/   \\--+--/   \\     \\|   \\-----/\n";

		symbols = new HashMap<String, java.lang.Character>();
		parameters = new HashMap<String, Object>();
		templates = new ArrayList<GameObjectBase>();
		keywords = new HashMap<String, String>();
		commands = new HashMap<String, ArrayList<String>>();
		commandfns = new HashMap<ArrayList<String>, GameFunction>();
		
		symbols.put("Wall.NS", '|');
		symbols.put("Wall.EW", '-');
		symbols.put("Wall.Corner", '+');
		symbols.put("Wall.Door", ' ');
		symbols.put("Wall.Door.Up", '^');
		symbols.put("Wall.Door.Down", 'v');
		symbols.put("Wall.Door.Left", '<');
		symbols.put("Wall.Door.Right", '>');
		symbols.put("Wall.Door.Closed.NS", '|');
		symbols.put("Wall.Door.Closed.EW", '-');
		symbols.put("Room.Empty", ' ');
		symbols.put("Room.Hidden.1", '\\');
		symbols.put("Room.Hidden.2", '/');
		
		parameters.put("Room.Width", 7);
		parameters.put("Room.Height", 5);
		parameters.put("Text.Speed", 2);
		parameters.put("Error.Message", "Invalid command");
		parameters.put("Output.Start", ">> ");
		parameters.put("Input.Start", "<< ");
		parameters.put("Description.Empty", "No description");
		
		parameters.put("Draw.Move", "move");
		parameters.put("Draw.Always", "always");
		parameters.put("Draw.Never", "never");
		parameters.put("World.Draw", Console.<String>parameter("Draw.Move"));
		
		templates.add(Console.<java.lang.Character>goa("symbol", '?'));
		templates.add(Console.<Integer>goa("x", 0));
		templates.add(Console.<Integer>goa("y", 0));
		templates.add(Console.<Integer>goa("hp", 5));
		templates.add(Console.<Integer>goa("atk", 0));
		templates.add(Console.<Integer>goa("def", 0));
		templates.add(Console.<Integer>goa("spd", 0));
		templates.add(Console.<Integer>goa("gold", 0));
		templates.add(Console.<Integer>goa("doors", 0));
		templates.add(Console.<Integer>goa("rarity", 1));
		templates.add(Console.<Integer>goa("value", 0));
		templates.add(Console.<Integer>goa("size", 5));
		templates.add(Console.<Integer>goa("floor", 0));
		templates.add(Console.<String>goa("name", ""));
		templates.add(Console.<String>goa("desc", ""));
		templates.add(Console.<Boolean>goa("hidden", false));
		
		templates.add(Console.go("visual", template("x"), template("y"), template("symbol")));
		templates.add(Console.go("gchest", template("visual"), template("gold")));
		
		templates.add(Console.go("stats", template("name"), template("atk"), template("def"), template("spd")));
		templates.add(Console.go("weapon", template("stats"), template("desc"), template("rarity"), template("value")));
		templates.add(Console.go("armor", template("stats"), template("desc"), template("rarity"), template("value"), template("floor")));
		
		templates.add(Console.<GameObject>gol("items", template("weapon"), template("armor")));
		templates.add(Console.go("inventory", template("size"), template("items")));
		templates.add(Console.go("ichest", template("visual"), template("items")));
		
		templates.add(Console.go("player", template("visual"), template("stats"), template("hp"), template("gold"), template("inventory")));
		
		templates.add(Console.<GameObject>gol("objects", template("gchest"), template("ichest")));
		templates.add(Console.go("room", template("doors"), template("hidden"), template("objects")));
		templates.add(Console.<GameObject>gol("row", template("room")));
		templates.add(Console.<GameObjectList<GameObject>>gol("rooms", template("row")));
		templates.add(Console.go("world", template("name"), template("rooms")));
		
		keywords.put("Type.String", "string");
		keywords.put("Type.Integer", "int");
		keywords.put("Type.Boolean", "bool");
		keywords.put("Type.Object", "item");
		keywords.put("Type.List", "list");
		
		
		commands.put("Direction.Up", Console.<String>arrl("u", "up", "n", "north"));
		commands.put("Direction.Left", Console.<String>arrl("l", "left", "w", "west"));
		commands.put("Direction.Down", Console.<String>arrl("d", "down", "s", "south"));
		commands.put("Direction.Right", Console.<String>arrl("r", "right", "e", "east"));
		
		
		
		commandfns.put(arrl("go", "move"), (String s, Game g) -> {
			Scanner in = new Scanner(s);
			while(in.hasNext()) {
				String dir = in.next();
				int times = 1;
				if(in.hasNextInt()) 
					times = in.nextInt();
				
				for(String str : commands.get("Direction.Up"))
					if(str.equals(dir))
						g.move(0, -times);
				for(String str : commands.get("Direction.Left"))
					if(str.equals(dir)) 
						g.move(-times, 0);
				for(String str : commands.get("Direction.Down"))
					if(str.equals(dir)) 
						g.move(0, times);
				for(String str : commands.get("Direction.Right"))
					if(str.equals(dir)) 
						g.move(times, 0);
			}
			if(Console.<String>parameter("World.Draw").equals(Console.<String>parameter("Draw.Move")))
				g.print();
			in.close();
		});
		commandfns.put(arrl("inv", "inventory"), (String s, Game g) -> {	//TODO display size
			boolean stats = s.endsWith("-stats");
			Weapon w = g.player().weapon();
			Armor a = g.player().armor();
			ArrayList<String> lines = new ArrayList<String>();
			
			lines.add(String.format("Weapon: %s", (w != null ? w.stats().name() : "N/A") + (stats ? ":" + w.stats().toString() : "")));
			lines.add(String.format("Armor : %s", (a != null ? a.stats().name() : "N/A") + (stats ? ":" + a.stats().toString() : "")));
			
			for(Item i : g.player().inv().items())
				if(i != w && i != a)
					lines.add(String.format("   >%s", i.stats().name() + (stats ? ":" + i.stats().toString() : "")));
			
			box(lines);
		});
		commandfns.put(arrl("x", "examine", "look", "check", "interact"), (String s, Game g) -> {
			s = s.trim();
			char key = s.charAt(0);
			
			for(RoomObject ro : g.currentRoom().objects())
				if(ro.symbol() == key) {
					ro.interact(g.currentRoom(), g.player());
					break;
				}
		});
		commandfns.put(arrl("stats", "c", "character"), (String s, Game g) -> {
			StatList stats = g.player().stats();
			box(arrl("Name: " + stats.name(), "hp: " + g.player().hp(), String.format("atk: %d | def: %d | spd: %d", stats.atk(), stats.def(), stats.spd())));
		});
		commandfns.put(arrl("gold", "money"), (String s, Game g) -> {
			box(arrl(String.format("Gold: %dG", g.player().gold()), String.format("Net Worth: %dG", g.player().netWorth())));
		});
		commandfns.put(arrl("d", "desc", "describe", "inspect"), (String s, Game g) -> {
			s = s.trim();
			if(!g.player().inv().contains(s))
				log("You are not carring an item called \"%s\"", s);
			else {
				Item i = g.player().inv().get(s);
				StatList stats = i.stats();
				//log("   >atk %d | def %d | spd %d | rarity %d | value %d", i.stats().atk(), i.stats().def(), i.stats().spd(), i.rarity(), i.value());
				//log("   >%s", (i.desc().isEmpty() ? parameters.get("Description.Empty") : i.desc()));
				box(arrl("Name: " + stats.name(), String.format("rarity: %d | value: %d", i.rarity(), i.value()), stats.toString(), "", i.desc()));
			}
		});
		commandfns.put(arrl("map", "draw"), (String s, Game g) -> {
			g.print();
		});
		commandfns.put(arrl("clear", "cls"), (String s, Game g) -> {
			String str = "";
			for(int i = 0; i < 50; i++)
				str += "\n";
			System.out.println(str);
		});
	}
	private static void box(ArrayList<String> lines) {
		String s = "";
		
		for(int i = 0; i < lines.size(); i++) {
			if(lines.get(i).contains("\n")) {
				String[] arr = lines.get(i).split("\n");
				for(int j = 0; j < arr.length; j++)
					lines.add(i + j + 1, arr[j]);
				lines.remove(i);
				i += arr.length;
			}
		}
		int maxlen = 0;
		for(String str : lines)
			if(str.length() > maxlen)
				maxlen = str.length();
		String border = "+";
		for(int i = 0; i < maxlen; i++)
			border += "-";
		border += "+";
		s += border + "\n";
		
		String bbuffer = " ";
		for(int i = 0; i < + Console.<String>parameter("Input.Start").length() - 1; i++)
			bbuffer += " ";
		for(String str : lines) {
			String mbuffer = "";
			for(int i = str.length(); i < maxlen; i++)
				mbuffer += " ";
			s += bbuffer + "|" + str + mbuffer + "|\n";
		}
		log(s + bbuffer + border);
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
	public static void log(String s, Object...args) {
		System.out.print((String)parameters.get("Output.Start"));
		putf(s + '\n', args);
	}
	public static GameFunction command(String key) {
		for(ArrayList<String> arr : commandfns.keySet())
			if(arr.contains(key))
				return commandfns.get(arr);
		return null;
	}
	public static void start(Game g) {
		command("clear").op("", g);
		//putf("%41s\n%s\n\n", "M A D E   W I T H", TITLE_CARD);
		g.print();
	}
	public static void main(String[] args) throws IOException {
		init();
		Game g = new Game();
		Parser.createGame(g);
		
		start(g);
		
		Scanner in = new Scanner(System.in);
		String input = "";
		while(true) {	//TODO move to Parser
			if(Console.<String>parameter("World.Draw").equals(Console.<String>parameter("Draw.Always")))
				g.print();
			
			System.out.print((String)parameters.get("Input.Start"));
			input = in.nextLine();
			if(input.equals("quit"))
				break;
			
			boolean valid = false;
			for(ArrayList<String> arr : commandfns.keySet())
				for(String s : arr)
					if(s.equals(input.contains(" ") ? input.substring(0, input.indexOf(" ")) : input)) {
						commandfns.get(arr).op(input.contains(" ") ? input.substring(input.indexOf(" ") + 1) : "", g);
						valid = true;
					}
			if(!valid)
				log((String)parameters.get("Error.Message"));
		}
		in.close();
	}
}
