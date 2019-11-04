import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Console {
	public static Parser parser;	
	public static ParameterList registryRoot = new ParameterList("root");
	
	public static int addParam(String path, ParameterBase pb) {
		ParameterList pl = registryRoot.getPath(path, true);
		if(pl == null)
			return -2;	//DNE
		ParameterBase prev = pl.getElement(pb.key());
		
		if(prev != null) {	//overwrite current value if it exists
			pl.removeElement(pb.key());
			pl.addElement(pb);
		}
		else if(!pl.noNew())	//if it doesn't exist and you can create new values in this list
			pl.addElement(pb);
		else	//fail
			return -1;
		
		if(prev != null)
			return 1; //success with warning
		
		return 0;	//success
	}
	@SuppressWarnings("unchecked")
	public static <E extends ParameterBase> E getParam(String path) {
		path = path.toUpperCase();
		int end = path.contains(".") ? path.lastIndexOf(".") : path.length();
		ParameterList pl = registryRoot.getPath(path.substring(0, end), false);
		
		if(end == path.length())
			return (E)pl;
		if(pl == null)
			return null;
		
		E ret = pl.<E>getElement(path.substring(end + 1));
		if(ret != null)
			return ret;
		parser.err(Parser.PARAM_DNE, path);
		return null;
	}
	public static boolean validPath(String path) {
		path = path.toUpperCase();
		int end = path.contains(".") ? path.lastIndexOf(".") : path.length();
		ParameterList pl = registryRoot.getPath(path.substring(0, end), false);
		
		if(pl != null)
			return pl.getElement(path.substring(end + 1)) != null;
		return false;
	}
	
	public static <E> GameObjectAttribute<E> goa(String key, E value){
		return new GameObjectAttribute<E>(key, value);
	}
	@SafeVarargs
	public static <E extends GameObjectBase> GameObjectList<E> gol(String key, E...values){
		return new GameObjectList<E>(key, values);
	}
	public static GameObject go(String key, GameObjectBase...elements) {
		return new GameObject(key, elements);
	}
	
	public static <E> Parameter<E> p(String key, E value){
		return new Parameter<E>(key, value);
	}
	public static ParameterList pl(String key) {
		return new ParameterList(key);
	}
	
	public static boolean echo() {
		ParameterList con = registryRoot.getPath("con", false);
		if(con == null)
			return false;
		ParameterList setting = con.getPath("setting", false);
		if(setting == null)
			return false;
		Parameter<Boolean> echo = setting.getElement("echo");
		if(echo == null)
			return false;
		return echo.value();
	}
	public static <E> GameObjectAttribute<E> rgoa(String path){
		return Console.<Parameter<GameObjectAttribute<E>>>getParam("Lang.Template.Attribute." + path).value();
	}
	public static <E extends GameObjectBase> GameObjectList<E> rgol(String path){
		return Console.<Parameter<GameObjectList<E>>>getParam("Lang.Template.List." + path).value();
	}
	public static GameObject rgo(String path) {
		return Console.<Parameter<GameObject>>getParam("Lang.Template.Object." + path).value();
	}
	public static GameFunction rcmd(String path) {
		return Console.<Parameter<GameFunction>>getParam("Con.Cmd." + path).value();
	}
	public static EncounterFunction recmd(String path) {
		return Console.<Parameter<EncounterFunction>>getParam("con.cmd.encounter." + path).value();
	}
	public static String rlang(String path){
		return Console.<Parameter<String>>getParam("Lang." + path).value();
	}
	public static <E> E rsetting(String path) {
		return Console.<Parameter<E>>getParam("con.setting." + path).value();
	}
	public static char rgraphic(String path) {
		return Console.<Parameter<java.lang.Character>>getParam("con.graphic." + path).value();
	}
	public static String rgraphics(String path) {
		return Console.<Parameter<String>>getParam("con.graphic." + path).value();
	}
	
	public static void init() {
		addParam("", pl("USER"));
		addParam("user", p("test", "test"));
		addParam("", pl("CON"));
		addParam("CON", pl("SETTING"));
		addParam("con.setting", p("echo", true));
		addParam("con.setting", p("stutter", 20));
		addParam("con.setting", p("input", "<< "));
		addParam("con.setting", p("output", ">> "));
		addParam("con.setting", p("maponmove", false));
		addParam("con.setting", pl("invalid"));
		addParam("con.setting.invalid", p("cmd", "Invalid command."));
		addParam("con.setting.invalid", p("dir", "Invalid direction."));
		addParam("con.setting.invalid", pl("target"));
		addParam("con.setting.invalid.target", p("name", "That enemy doesn't exist."));
		addParam("con.setting.invalid.target", p("index", "Invalid enemy index."));
		addParam("con.setting", pl("hit"));
		addParam("con.setting.hit", p("print", true));
		addParam("con.setting.hit", p("string", "%s hits %s for %d damage!"));
		addParam("con.setting", pl("chest"));
		addParam("con.setting.chest", p("gold", "You found %d gold in the chest!"));
		addParam("con.setting", pl("encounter"));
		addParam("con.setting.encounter", p("attack", "What do you do?"));
		addParam("con.setting.encounter", p("victory", "You are victorious!"));
		addParam("con.setting.encounter", p("money", "You gain %d gold!"));
		addParam("con.setting.encounter", p("start", "You are attacked by"));
		addParam("con.setting.encounter", p("defeat", "You have been defeated."));
		addParam("con.setting", pl("currency"));
		addParam("con.setting.currency", p("name", "gold"));
		addParam("con.setting.currency", p("suffix", "G"));
		addParam("con.setting", pl("cmd"));
		addParam("con.setting.cmd", pl("equip"));
		addParam("con.setting.cmd.equip", p("fail", "You cannot equip that item."));
		addParam("con.setting.cmd.equip", p("success", "Equipped %s."));
		addParam("con.setting.cmd", pl("desc"));
		addParam("con.setting.cmd.desc", p("fail", "You don't own that item."));
		addParam("con.setting.cmd.desc", p("success", "%s"));
		addParam("con.setting.cmd", pl("x"));
		addParam("con.setting.cmd.x", p("invalid", "That object doesn't exist."));
		
		addParam("CON", pl("CMD"));
		addParam("CON", pl("GRAPHIC"));
		addParam("con.graphic", pl("room"));
		addParam("con.graphic.room", pl("wall"));
		addParam("con.graphic.room.wall", p("corner", '+'));
		addParam("con.graphic.room.wall", p("ns", '|'));
		addParam("con.graphic.room.wall", p("ew", '-'));
		addParam("con.graphic.room", pl("door"));
		addParam("con.graphic.room.door", p("open", ' '));
		addParam("con.graphic.room.door", p("n", '^'));
		addParam("con.graphic.room.door", p("w", '<'));
		addParam("con.graphic.room.door", p("s", 'v'));
		addParam("con.graphic.room.door", p("e", '>'));
		addParam("con.graphic.room.door", pl("closed"));
		addParam("con.graphic.room.door.closed", p("ns", '|'));
		addParam("con.graphic.room.door.closed", p("ew", '-'));
		addParam("con.graphic", pl("inventory"));
		addParam("con.graphic.inventory", p("corner", '+'));
		addParam("con.graphic.inventory", p("equipped", '*'));
		addParam("con.graphic.inventory", p("separator", " : "));
		addParam("con.graphic.inventory", pl("bullet"));
		addParam("con.graphic.inventory.bullet", p("weapon", 'W'));
		addParam("con.graphic.inventory.bullet", p("armor", 'A'));
		addParam("con.graphic.inventory.bullet", p("consumable", 'C'));
		addParam("con.graphic.inventory", pl("border"));
		addParam("con.graphic.inventory.border", p("ns", '|'));
		addParam("con.graphic.inventory.border", p("ew", '-'));
		addParam("con.graphic.inventory", pl("title"));
		addParam("con.graphic.inventory.title", p("divide", '/'));
		addParam("con.graphic.inventory.title", pl("parentheses"));
		addParam("con.graphic.inventory.title.parentheses", p("open", '['));
		addParam("con.graphic.inventory.title.parentheses", p("close", ']'));
		addParam("con.graphic.inventory", pl("stat"));
		addParam("con.graphic.inventory.stat", p("atk", "atk"));
		addParam("con.graphic.inventory.stat", p("def", "def"));
		addParam("con.graphic.inventory.stat", p("spd", "spd"));
		addParam("con.graphic.inventory.stat", p("open", '<'));
		addParam("con.graphic.inventory.stat", p("close", '>'));
		addParam("con.graphic.inventory.stat", pl("separator"));
		addParam("con.graphic.inventory.stat.separator", p("name", ','));
		addParam("con.graphic.inventory.stat.separator", p("value", ':'));
		addParam("con.graphic", pl("stats"));
		addParam("con.graphic.stats", p("name", "name"));
		addParam("con.graphic.stats", p("hp", "hp"));
		addParam("con.graphic.stats", p("atk", "atk"));
		addParam("con.graphic.stats", p("def", "def"));
		addParam("con.graphic.stats", p("spd", "spd"));
		addParam("con.graphic.stats", p("worth", "worth"));
		addParam("con.graphic.stats", p("open", '['));
		addParam("con.graphic.stats", p("close", ']'));
		addParam("con.graphic.stats", pl("separator"));
		addParam("con.graphic.stats.separator", p("value", ':'));
		addParam("con.graphic.stats.separator", p("stat", ','));
		addParam("con.graphic.stats", pl("bonus"));
		addParam("con.graphic.stats.bonus", p("open", '('));
		addParam("con.graphic.stats.bonus", p("close", ')'));
		addParam("con.graphic.stats.bonus", p("positive", '+'));
		addParam("con.graphic.stats.bonus", p("negative", '-'));
		addParam("con.graphic", pl("help"));
		addParam("con.graphic.help", p("corner", '+'));
		addParam("con.graphic.help", p("separator", " : "));
		addParam("con.graphic.help", p("required", '*'));
		addParam("con.graphic.help", p("optional", '-'));
		addParam("con.graphic.help", pl("border"));
		addParam("con.graphic.help.border", p("ns", '|'));
		addParam("con.graphic.help.border", p("ew", '-'));
		addParam("con.graphic", pl("encounter"));
		addParam("con.graphic.encounter", p("corner", '+'));
		addParam("con.graphic.encounter", p("separator", " : "));
		addParam("con.graphic.encounter", p("bullet", '>'));
		addParam("con.graphic.encounter", pl("border"));
		addParam("con.graphic.encounter.border", p("ns", '|'));
		addParam("con.graphic.encounter.border", p("ew", '-'));
		addParam("con.graphic.encounter", pl("help"));
		addParam("con.graphic.encounter.help", p("corner", '+'));
		addParam("con.graphic.encounter.help", p("separator", " : "));
		addParam("con.graphic.encounter.help", p("required", '*'));
		addParam("con.graphic.encounter.help", p("optional", '-'));
		addParam("con.graphic.encounter.help", pl("border"));
		addParam("con.graphic.encounter.help.border", p("ns", '|'));
		addParam("con.graphic.encounter.help.border", p("ew", '-'));
		
		
		addParam("", pl("LANG"));
		addParam("lang", p("rootdir", new File("").getAbsolutePath() + "\\LocalFiles\\"));
		addParam("LANG", p("MAIN", rlang("rootdir") + "main.dgnh"));
		addParam("LANG", pl("KEY"));
		addParam("lang.key", p("bin", "0b"));
		addParam("lang.key", pl("read"));
		addParam("lang.key.read", p("header", "import"));
		addParam("lang.key.read", p("game", "read"));
		addParam("lang.key", p("param", "param"));
		addParam("lang.key", p("define", "define"));
		addParam("LANG", pl("TEMPLATE"));
		addParam("LANG.TEMPLATE", pl("ATTRIBUTE"));
		addParam("LANG.TEMPLATE", pl("LIST"));
		addParam("LANG.TEMPLATE", pl("OBJECT"));
		
		//stats
		addParam("lang.template.attribute", p("name", goa("name", "NONAME")));
		addParam("lang.template.attribute", p("atk", goa("atk", 0)));
		addParam("lang.template.attribute", p("def", goa("def", 0)));
		addParam("lang.template.attribute", p("spd", goa("spd", 0)));
		addParam("lang.template.object", p("stats", go("stats", rgoa("name"), rgoa("atk"), rgoa("def"), rgoa("spd"))));
		
		//visual (RoomObject)
		addParam("lang.template.attribute", p("x", goa("x", 0)));
		addParam("lang.template.attribute", p("y", goa("y", 0)));
		addParam("lang.template.attribute", p("symbol", goa("symbol", 'X')));
		addParam("lang.template.object", p("visual", new GameObject("visual", rgoa("x"), rgoa("y"), rgoa("symbol"))));
		
		//gold chest
		addParam("lang.template.attribute", p("gold", goa("gold", 0)));
		addParam("lang.template.object", p("gchest", go("gchest", rgo("visual"), rgoa("gold"))));

		//items
		addParam("lang.template.attribute", p("desc", goa("desc", "NODESC")));
		addParam("lang.template.attribute", p("rarity", goa("rarity", 0)));
		addParam("lang.template.attribute", p("value", goa("value", 0)));
		addParam("lang.template.object", p("weapon", go("weapon", rgo("stats"), rgoa("desc"), rgoa("rarity"), rgoa("value"))));
		addParam("lang.template.attribute", p("floor", goa("floor", 0)));
		addParam("lang.template.object", p("armor", go("armor", rgo("stats"), rgoa("desc"), rgoa("rarity"), rgoa("value"), rgoa("floor"))));
		addParam("lang.template.attribute", p("duration", goa("duration", 1)));
		addParam("lang.template.attribute", p("self", goa("self", false)));
		addParam("lang.template.attribute", p("hp", goa("hp", 0)));
		addParam("lang.template.object", p("consumable", go("consumable", rgo("stats"), rgoa("desc"), rgoa("rarity"), rgoa("value"), rgoa("hp"), rgoa("duration"), rgoa("self"))));
		
		//inventory
		addParam("lang.template.attribute", p("size", goa("size", 5)));
		addParam("lang.template.list", p("items", gol("items", rgo("weapon"), rgo("armor"), rgo("consumable"))));
		addParam("lang.template.object", p("inventory", go("inventory", rgoa("size"), rgol("items"))));
		
		//characters
		addParam("lang.template.attribute", p("dcgold", goa("dcgold", 0)));
		addParam("lang.template.attribute", p("dcarmor", goa("dcarmor", 0)));
		addParam("lang.template.attribute", p("dcweapon", goa("dcweapon", 0)));
		addParam("lang.template.object", p("enemy", go("enemy", rgoa("hp"), rgoa("gold"), rgo("visual"), 
					rgoa("dcgold"), rgoa("dcarmor"), rgoa("dcweapon"), rgo("inventory"), rgo("stats"))));
		
		//room.objects
		addParam("lang.template.list", p("objects", gol("objects", rgo("gchest"), rgo("enemy"))));
		
		//room
		addParam("lang.template.attribute", p("hidden", goa("hidden", false)));
		addParam("lang.template.attribute", p("doors", goa("doors", 0)));
		addParam("lang.template.attribute", p("onenter", goa("onenter", "")));
		addParam("lang.template.attribute", p("onexit", goa("onexit", "")));
		addParam("lang.template.object", p("text", go("text", rgoa("onenter"), rgoa("onexit"))));
		addParam("lang.template.object", p("room", go("room", rgo("text"), rgoa("doors"), rgoa("hidden"), rgol("objects"))));
		
		//world
		addParam("lang.template.list", p("row", gol("row", rgo("room"))));
		addParam("lang.template.list", p("rooms", gol("rooms", rgol("row"))));
		
		addParam("LANG", pl("TLO"));
		addParam("lang.tlo", p("world", go("world", rgoa("name"), rgol("rooms"))));
		addParam("lang.tlo", p("player", go("player", rgoa("hp"), rgoa("gold"), rgo("visual"), rgo("stats"), rgo("inventory"))));

		
		
		
		addParam("con.cmd", p("map", (GameFunction)(String s, Game g) -> 
		{
			g.draw();
		}));
		addParam("con.cmd", p("move", (GameFunction)(String input, Game g) ->
		{
			Scanner s = new Scanner(input);
			
			if(!s.hasNext()) {
				Console.logn("Specify a direction (up, left, down, right).");
				s.close();
				return;
			}
			
			String dir = s.next();
			int times = 1;
			if(s.hasNextInt())
				times = s.nextInt();
			
			g.movePlayer(dir, times);
			s.close();
		}));
		addParam("con.cmd", p("inv", (GameFunction)(String s, Game g) ->
		{
			g.player().printInv();
		}));
		addParam("con.cmd", p("stats", (GameFunction)(String s, Game g) ->
		{
			g.player().print();
		}));
		addParam("con.cmd", p("equip", (GameFunction)(String s, Game g) ->
		{
			String item = s;
			String success = g.player().equip(item);
			if(success != null)
				Console.logn(Console.rsetting("cmd.equip.success"), success);
			else
				Console.logn(Console.rsetting("cmd.equip.fail"));
			
		}));
		addParam("con.cmd", p("desc", (GameFunction)(String s, Game g) ->
		{
			String item = s;
			Item success = g.player().getItem(item);
			if(success != null)
				Console.logn(Console.rsetting("cmd.desc.success"), success.desc());
			else
				Console.logn(Console.rsetting("cmd.desc.fail"));
			
		}));
		addParam("con.cmd", p("x", (GameFunction)(String s, Game g) ->
		{
			char id = s.charAt(0);
			Room cur = g.currentRoom();
			RoomObject ro = cur.object(id, 1);
			if(ro != null)
				ro.interact(cur, g.player());
			else
				Console.logn(Console.rsetting("cmd.x.invalid"));
		}));
		addParam("con.cmd", p("help", (GameFunction)(String s, Game g) ->
		{
			String[] cmds = {
				"map", 
				"move " + Console.rgraphic("help.required") + "dir " + Console.rgraphic("help.optional") + "n", 
				"inv", 
				"stats",
				"equip " + Console.rgraphic("help.required") + "s",
				"desc " + Console.rgraphic("help.required") + "s",
				"x " + Console.rgraphic("help.required") + "c",
				"quit"
			};
			String[] descs = {
				"draws the map",
				"moves the player n times in given direction",
				"displays all items in inventory",
				"list of your stats",
				"equips weapon/armor with given name",
				"describes item with given name",
				"interact with object with given symbol",
				"stop the game"
			};
			
			Console.printBoxColumns("help", "Help", false, cmds, descs);	
		}));
		
		addParam("con.cmd", pl("encounter"));
		addParam("con.cmd.encounter", p("list", (EncounterFunction)(String s, Encounter e) ->
		{
			String[] lines = new String[e.enemies().size()];
			for(int i = 0; i < lines.length; i++)
				lines[i] = e.enemies().get(i).name();
			Console.printBox("encounter", "Enemies", true, lines);
		}));
		addParam("con.cmd.encounter", p("hit", (EncounterFunction)(String answer, Encounter e) ->
		{
			String targetName = answer;
			int index = 0;
			ArrayList<Enemy> enemies = e.enemies();

			if(answer.contains(" ")) {
				try {
					index = Integer.parseInt(answer.substring(answer.indexOf(' ') + 1));
				}
				catch(Exception ex) {
					Console.logn(Console.rsetting("invalid.target.index"));
				}
			}
			
			int valid = 0;
			Enemy target = null;
			for(Enemy enemy : enemies) {
				if(enemy.name().equalsIgnoreCase(targetName)) {
					if(valid == index) {
						target = enemy;
						break;
					}
					valid++;
				}
			}
			
			if(target == null)
				Console.logn(Console.rsetting("invalid.target.name"));
			else if(!e.p().attack(target)) {
				e.enemies().remove(target);
				if(target.goldDrop())
					e.addGold(target.gold());
				if(target.armorDrop())
					e.addItem(target.equippedArmor());
				if(target.weaponDrop())
					e.addItem(target.equippedWeapon());
			}			
		}));
		addParam("con.cmd.encounter", p("help", (EncounterFunction)(String s, Encounter e) ->
		{
			String[] cmds = {
				"list",
				"hit " + Console.rgraphic("encounter.help.required") + "s"
			};
			String[] descs = {
				"lists all enemies",
				"hit enemy with name s"
			};
			
			Console.printBoxColumns("encounter.help", "Help", false, cmds, descs);	
		}));
		
		addParam("con.cmd.encounter", pl("post"));
		addParam("con.cmd.encounter.post", p("list", (EncounterFunction)(String s, Encounter e) ->
		{
			Console.printItemBox("encounter", "Available Items", e.items(), null);
		}));
		addParam("con.cmd.encounter.post", p("take", (EncounterFunction)(String answer, Encounter e) ->
		{
			ArrayList<Item> items = e.items();
			Player p = e.p();
			String target = answer.substring(answer.indexOf(' ') + 1);
			
			int extraGold = 0;
			for(Item i : items)
				extraGold += i.value();
			
			boolean all = target.equals("all");
			for(int i = 0; i < items.size(); i++) {
				Item cur = items.get(i);
				if((!all && cur.name().equalsIgnoreCase(target)) || (all && !p.inv().isFull())) {
					extraGold -= items.get(i).value();
					p.inv().add(cur);
					items.remove(i);
					Console.logn("You take %s.", cur.name());
					if(!all)
						break;
					i--;
				}
			}
			if(p.inv().isFull() && !items.isEmpty()) {
				Console.logn("You cannot carry all of the items. You receive %d gold for the remaining ones.", extraGold);
			}
		}));
		addParam("con.cmd.encounter.post", p("help", (EncounterFunction)(String s, Encounter e) ->
		{
			String[] cmds = {
				"list",
				"take " + Console.rgraphic("encounter.help.required") + "s",
				"leave"
			};
			String[] descs = {
				"lists all items",
				"take item with given name or all items",
				"take no items"
			};
			
			Console.printBoxColumns("encounter.help", "Help", false, cmds, descs);	
		}));
		
		getParam("CON.CMD").lock();
		Console.<ParameterList>getParam("con.setting").lockAdditions();
		Console.<ParameterList>getParam("con.graphic").lockAdditions();
		getParam("LANG").lock();
	}
	
	public static boolean gfOp(Parameter<GameFunction> gf, String s, Game g) {
		if(gf == null) {
			if(s.isEmpty())
				System.out.println();
			Console.logn(Console.rsetting("invalid.cmd"));
			return false;
		}
		else {
			gf.value().op(s, g);
			return true;
		}
	}
	public static boolean efOp(Parameter<EncounterFunction> ef, String s, Encounter e) {
		if(ef == null) {
			if(s.isEmpty())
				System.out.println();
			Console.logn(Console.rsetting("invalid.cmd"));
			return false;
		}
		else {
			ef.value().op(s, e);
			return true;
		}
	}
	public static void printItemBox(String graphicRoot, String head, ArrayList<Item> items, Character c) {
		String[] names = new String[items.size()];
		String[] stats = new String[items.size()];
		for(int i = 0; i < items.size(); i++) {
			Item cur = items.get(i);
			names[i] = cur.formatName(c != null && c.isEquipped(cur));
			stats[i] = cur.formatStats();
		}
		
		Console.printBoxColumns(graphicRoot, head, false, names, stats);
	}
	public static void printBoxColumns(String graphicRoot, String head, boolean bullet, String[] c1, String[] c2) {
		int maxc1Length = 0;
		for(String s : c1)
			maxc1Length = Math.max(maxc1Length, s.length());
		int maxc2Length = 0;
		for(String s : c2)
			maxc2Length = Math.max(maxc2Length, s.length());
		
		String format = "%-" + maxc1Length + "s" + "%s" + "%-" + maxc2Length + "s";
		String separator = Console.rgraphics(graphicRoot + ".separator");
		
		String[] lines = new String[c1.length];
		for(int i = 0; i < c1.length; i++)
			lines[i] = String.format(format, c1[i], separator, c2[i]);
		printBox(graphicRoot, head, bullet, lines);
	}
	public static void printBox(String graphicRoot, String head, boolean bullet, String[] lines) {
		char corner = Console.rgraphic(graphicRoot + ".corner");
		char borderew = Console.rgraphic(graphicRoot + ".border.ew"), borderns = Console.rgraphic(graphicRoot + ".border.ns");
		char bulletc = 0;
		if(bullet)
			bulletc = Console.rgraphic(graphicRoot + ".bullet");
		int titleLength = head.length() + 2;	//corner on each side
		int maxRowLength = 0;
		for(String row : lines)
			maxRowLength = Math.max(row.length() + 4 + (bullet ? 2 : 0), maxRowLength);	//wall and space on each side + optional bullet
		
		int length = Math.max(titleLength, maxRowLength);
		
		String title = "";
		int titleDelta = length - titleLength;
		for(int i = 0; i < titleDelta / 2; i++)
			title += borderew;
		title += head.replaceAll(" ", "" + borderew);
		for(int i = title.length(); i < length - 2; i++)
			title += borderew;
		title = corner + title + corner;
		
		Console.logn(title);
		
		String rowFormat = "";
		rowFormat = "%c " + (bullet ? "%c " : "") + "%-" + (length - 4 - (bullet ? 2 : 0)) + "s %c";
		
		for(String row : lines) {
			if(bullet)
				Console.logn(rowFormat, borderns, bulletc, row, borderns);
			else
				Console.logn(rowFormat, borderns, row, borderns);
		}
		
		String bottom = "";
		for(int i = 0; i < length - 2; i++)
			bottom += borderew;
		bottom = corner + bottom + corner;
		
		Console.logn(bottom);
	}
	private static void log(String s, Object...args) {
		logr(s, false, args);
	}
	private static void logr(String s, boolean recurse, Object...args) {
		if(!recurse)
			System.out.print(Console.<String>rsetting("output"));
		int arg = 0;
		try {
			for(int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if(c == '%') {
					int end = i + 1;
					for(; end < s.length() && (" %cdfsb").indexOf(s.charAt(end)) == -1; end++);
					
					if(end != s.length())
						end++;
					
					logr(String.format(s.substring(i, end), args[arg++]), true);
					i = end - 1;
					continue;
				}
				System.out.print(c);
				Thread.sleep(Console.<Integer>rsetting("stutter"));
			}
		}
		catch(InterruptedException e) {
			System.out.println("Console.log interrupted");
		}
	}
	public static void logn(String s, Object...args) {
		log(s + "\n", args);
	}
	public static void main(String[] args) {
		init();
		
		Game g = Parser.createGame();
		g.run();
	}
}
