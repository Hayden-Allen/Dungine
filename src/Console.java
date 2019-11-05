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
		path = path.toUpperCase();	//normalize path
		int end = path.contains(".") ? path.lastIndexOf(".") : path.length();
		ParameterList pl = registryRoot.getPath(path.substring(0, end), false);	//get next to last node, not write operation
		
		if(end == path.length())	//end of path reached
			return (E)pl;
		if(pl == null) {	//doesn't exist
			parser.err(Parser.PARAM_DNE, path);
			return null;
		}
		
		E ret = pl.<E>getElement(path.substring(end + 1));	//get element from next to last node if possible
		if(ret != null)
			return ret;
		//doesn't exist
		parser.err(Parser.PARAM_DNE, path);
		return null;
	}
	public static boolean validPath(String path) {	//whether or not the registry has a value at given path
		path = path.toUpperCase();
		int end = path.contains(".") ? path.lastIndexOf(".") : path.length();
		ParameterList pl = registryRoot.getPath(path.substring(0, end), false);
		
		if(pl != null)
			return pl.getElement(path.substring(end + 1)) != null;
		return false;
	}
	
	public static <E> GameObjectAttribute<E> goa(String key, E value){	//shortcut to create GameObjectAttribute
		return new GameObjectAttribute<E>(key, value);
	}
	@SafeVarargs
	public static <E extends GameObjectBase> GameObjectList<E> gol(String key, E...values){	//shortcut to create GameObjectList
		return new GameObjectList<E>(key, values);
	}
	public static GameObject go(String key, GameObjectBase...elements) {	//shortcut to create GameObject
		return new GameObject(key, elements);
	}
	
	public static <E> Parameter<E> p(String key, E value){	//shortcut to create Parameter
		return new Parameter<E>(key, value);
	}
	public static ParameterList pl(String key) {	//shortcut to create ParameterList
		return new ParameterList(key);
	}
	
	public static boolean echo() {	//whether or not to print debug messages
									//if con.setting.echo doesn't exist yet, returns false, otherwise returns its value
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
	public static <E> GameObjectAttribute<E> rgoa(String path){	//get GameObjectAttribute from registry
		return Console.<Parameter<GameObjectAttribute<E>>>getParam("Lang.Template.Attribute." + path).value();
	}
	public static <E extends GameObjectBase> GameObjectList<E> rgol(String path){	//get GameObjectList from registry
		return Console.<Parameter<GameObjectList<E>>>getParam("Lang.Template.List." + path).value();
	}
	public static GameObject rgo(String path) {	//get GameObject from registry
		return Console.<Parameter<GameObject>>getParam("Lang.Template.Object." + path).value();
	}
	public static GameFunction rcmd(String path) {	//get GameFunction from registry
		return Console.<Parameter<GameFunction>>getParam("Con.Cmd." + path).value();
	}
	public static EncounterFunction recmd(String path) {	//get EncounterFunction from registry
		return Console.<Parameter<EncounterFunction>>getParam("con.cmd.encounter." + path).value();
	}
	public static String rlang(String path){	//get language element from registry
		return Console.<Parameter<String>>getParam("Lang." + path).value();
	}
	public static <E> E rsetting(String path) {	//get setting from registry
		return Console.<Parameter<E>>getParam("con.setting." + path).value();
	}
	public static char rgraphic(String path) {	//get graphic character from registry
		return Console.<Parameter<java.lang.Character>>getParam("con.graphic." + path).value();
	}
	public static String rgraphics(String path) {	//get graphic string from registry
		return Console.<Parameter<String>>getParam("con.graphic." + path).value();
	}
	
	public static void init() {
		addParam("", pl("USER"));	//reserved for unrestricted language use
		
		addParam("", pl("CON"));	//stores information for console
									//TODO reorganize data for commands (put it under cmd?)
		
		addParam("CON", pl("SETTING"));	//settings (basically everything that's not a char)
		addParam("con.setting", p("echo", true));	//whether or not to print debug messages during interpretation
		addParam("con.setting", p("stutter", 20));	//millisecond delay between printed characters
		addParam("con.setting", p("input", "<< "));	//String printed as input prompt
		addParam("con.setting", p("output", ">> "));	//String printed before all console output
		addParam("con.setting", p("maponmove", false));	//whether or not to draw the map every time the player moves
		addParam("con.setting", pl("invalid"));	//various Strings to be printed when something doesn't fit given criteria
		addParam("con.setting.invalid", p("cmd", "Invalid command."));	//any input command that doesn't exist
		addParam("con.setting.invalid", p("dir", "Invalid direction."));	//invalid movement direction
		addParam("con.setting.invalid", pl("target"));
		addParam("con.setting.invalid.target", p("name", "That enemy doesn't exist."));	//unknown enemy name in encounter
		addParam("con.setting.invalid.target", p("index", "Invalid enemy index."));	//non-existent enemy index in encounter
		addParam("con.setting", pl("hit"));	//data for when a character is attacked
		addParam("con.setting.hit", p("print", true));	//whether or not to print message
		addParam("con.setting.hit", p("string", "%s hits %s for %d damage!"));	//format string to print. Passed attacker, attacked, damage in that order
		addParam("con.setting", pl("chest"));	//stuff for chests. currently only gold exists
		addParam("con.setting.chest", p("gold", "You found %d gold in the chest!"));	//format string. passed amount of gold in chest
		addParam("con.setting", pl("encounter"));	//strings for encounter
		addParam("con.setting.encounter", p("attack", "What do you do?"));	//printed at start of your turn
		addParam("con.setting.encounter", p("victory", "You are victorious!"));	//prints when you win
		addParam("con.setting.encounter", p("money", "You gain %d gold!"));	//passed amount of gold dropped by enemies
		addParam("con.setting.encounter", p("start", "You are attacked by"));	//start of sentence printed when the encounter begins
		addParam("con.setting.encounter", p("defeat", "You have been defeated."));	//when you lose
		addParam("con.setting", pl("currency"));	//in game name and symbols for currency
		addParam("con.setting.currency", p("name", "gold"));	//default is gold
		addParam("con.setting.currency", p("suffix", "G"));	//displayed in stats list
		addParam("con.setting", pl("cmd"));	//command-specific settings
		addParam("con.setting.cmd", pl("equip"));
		addParam("con.setting.cmd.equip", p("fail", "You cannot equip that item."));	//unknown item
		addParam("con.setting.cmd.equip", p("success", "Equipped %s."));	//passed name of equipped item
		addParam("con.setting.cmd", pl("desc"));
		addParam("con.setting.cmd.desc", p("fail", "You don't own that item."));	//unknown item
		addParam("con.setting.cmd.desc", p("success", "%s"));	//passed given item description
		addParam("con.setting.cmd", pl("x"));
		addParam("con.setting.cmd.x", p("invalid", "That object doesn't exist."));	//unknown object symbol
		
		addParam("CON", pl("CMD"));	//commands
		addParam("CON", pl("GRAPHIC"));	//mostly single characters that are used to format output
		addParam("con.graphic", pl("room"));	//all character used to draw world
		addParam("con.graphic.room", pl("wall"));
		addParam("con.graphic.room.wall", p("corner", '+'));
		addParam("con.graphic.room.wall", p("ns", '|'));	//vertical
		addParam("con.graphic.room.wall", p("ew", '-'));	//horizontal
		addParam("con.graphic.room", pl("door"));
		addParam("con.graphic.room.door", p("open", ' '));	//both sides are open
		addParam("con.graphic.room.door", p("n", '^'));	//bottom of room above is closed
		addParam("con.graphic.room.door", p("w", '<'));	//right of room to the left is closed
		addParam("con.graphic.room.door", p("s", 'v'));	//top of room below is closed
		addParam("con.graphic.room.door", p("e", '>'));	//left of room to the right is closed
		addParam("con.graphic.room.door", pl("closed"));	//both closed
		addParam("con.graphic.room.door.closed", p("ns", '|'));	//vertical (left and right doors)
		addParam("con.graphic.room.door.closed", p("ew", '-'));	//horizontal (top and bottom doors)
		addParam("con.graphic", pl("inventory"));	//formats inventory output ("inventory" command)
		addParam("con.graphic.inventory", p("corner", '+'));	//corner of box
		addParam("con.graphic.inventory", p("equipped", '*'));	//extra bullet character added to equipped items
		addParam("con.graphic.inventory", p("separator", " : "));	//separates names and stats (left and right columns)
		addParam("con.graphic.inventory", pl("bullet"));	//different bullets for different types of items
		addParam("con.graphic.inventory.bullet", p("weapon", 'W'));
		addParam("con.graphic.inventory.bullet", p("armor", 'A'));
		addParam("con.graphic.inventory.bullet", p("consumable", 'C'));
		addParam("con.graphic.inventory", pl("border"));	//borders of box
		addParam("con.graphic.inventory.border", p("ns", '|'));	//vertical (left and right sides of box)
		addParam("con.graphic.inventory.border", p("ew", '-'));	//horizontal (top and bottom sides of box)
		addParam("con.graphic.inventory", pl("title"));	//formats title of box ("Inventory-[size/maxSize]")
		addParam("con.graphic.inventory.title", p("divide", '/'));	//put in between current and max size
		addParam("con.graphic.inventory.title", pl("parentheses"));	//open and close size statement
		addParam("con.graphic.inventory.title.parentheses", p("open", '['));
		addParam("con.graphic.inventory.title.parentheses", p("close", ']'));
		addParam("con.graphic.inventory", pl("stat"));	//for stats list of each item (right column of box)
		addParam("con.graphic.inventory.stat", p("atk", "atk"));	//what attack stat is called
		addParam("con.graphic.inventory.stat", p("def", "def"));	//what defense stat is called
		addParam("con.graphic.inventory.stat", p("spd", "spd"));	//what speed stat is called
		addParam("con.graphic.inventory.stat", p("open", '<'));		//open parentheses for stat list
		addParam("con.graphic.inventory.stat", p("close", '>'));	//close parentheses for stat list
		addParam("con.graphic.inventory.stat", pl("separator"));
		addParam("con.graphic.inventory.stat.separator", p("name", ','));	//separates different stats
		addParam("con.graphic.inventory.stat.separator", p("value", ':'));	//separates name and value 
		addParam("con.graphic", pl("stats"));	//for "stats" command
		addParam("con.graphic.stats", p("name", "name"));	//what name stat is called
		addParam("con.graphic.stats", p("hp", "hp"));	//what health stat is called
		addParam("con.graphic.stats", p("atk", "atk"));	//what attack stat is called
		addParam("con.graphic.stats", p("def", "def"));	//what defense stat is called
		addParam("con.graphic.stats", p("spd", "spd"));	//what speed stat is called
		addParam("con.graphic.stats", p("worth", "worth"));	//net worth (gold + value of inventory)
		addParam("con.graphic.stats", p("open", '['));	//opens stat list
		addParam("con.graphic.stats", p("close", ']'));	//closes stat list
		addParam("con.graphic.stats", pl("separator"));
		addParam("con.graphic.stats.separator", p("value", ':'));	//separates name and value of each stat
		addParam("con.graphic.stats.separator", p("stat", ','));	//separates stats
		addParam("con.graphic.stats", pl("bonus"));	//bonuses gained from equipped items
		addParam("con.graphic.stats.bonus", p("open", '('));	//open bonus
		addParam("con.graphic.stats.bonus", p("close", ')'));	//close bonus
		addParam("con.graphic.stats.bonus", p("positive", '+'));	//if bonus is >= 0, display this
		addParam("con.graphic.stats.bonus", p("negative", '-'));	//if bonus is < 0, display this
		addParam("con.graphic", pl("help"));	//for "help" command
		addParam("con.graphic.help", p("corner", '+'));	//corner of box
		addParam("con.graphic.help", p("separator", " : "));	//separates two columns (commands and descriptions)
		addParam("con.graphic.help", p("required", '*'));	//denotes a required argument for a command
		addParam("con.graphic.help", p("optional", '-'));	//denotes and optional argument for a command
		addParam("con.graphic.help", pl("border"));
		addParam("con.graphic.help.border", p("ns", '|'));	//left and right of box
		addParam("con.graphic.help.border", p("ew", '-'));	//top and bottom of box
		addParam("con.graphic", pl("encounter"));	//for when the player is in a fight
		addParam("con.graphic.encounter", p("corner", '+'));	//corner of boxes
		addParam("con.graphic.encounter", p("separator", " : "));	//separates columns of boxes
		addParam("con.graphic.encounter", p("bullet", '>'));	//bullet character for boxes
		addParam("con.graphic.encounter", pl("border"));
		addParam("con.graphic.encounter.border", p("ns", '|'));	//left and right of box
		addParam("con.graphic.encounter.border", p("ew", '-'));	//top and bottom of box
		addParam("con.graphic.encounter", pl("help"));	//for "help" command when in encounter
		addParam("con.graphic.encounter.help", p("corner", '+'));	//corner of box
		addParam("con.graphic.encounter.help", p("separator", " : "));	//separates columns of box
		addParam("con.graphic.encounter.help", p("required", '*'));	//denotes a required argument for a command
		addParam("con.graphic.encounter.help", p("optional", '-'));	//denotes an optional argument for a command
		addParam("con.graphic.encounter.help", pl("border"));
		addParam("con.graphic.encounter.help.border", p("ns", '|'));	//left and right of box
		addParam("con.graphic.encounter.help.border", p("ew", '-'));	//top and bottom of box
		
		
		addParam("", pl("LANG"));	//stores all data used in interpreting the Dungine language
		addParam("lang", p("rootdir", new File("").getAbsolutePath() + "\\LocalFiles\\"));	//root filepath
		addParam("LANG", p("MAIN", rlang("rootdir") + "main.dgnh"));	//filepath of entry file
		addParam("LANG", pl("KEY"));	//header file keywords (.dgnh)
		addParam("lang.key", p("bin", "0b"));	//precedes integers to parse them in base 2
		addParam("lang.key", pl("read"));
		addParam("lang.key.read", p("header", "import"));	//interpret another header file
		addParam("lang.key.read", p("game", "read"));	//interpret a game file
		addParam("lang.key", p("param", "param"));	//manipulate the registry
		addParam("LANG", pl("TEMPLATE"));	//templates for all objects creatable in Dungine game files (.dgn)
		addParam("LANG.TEMPLATE", pl("ATTRIBUTE"));	//GameObjectAttributes
		addParam("LANG.TEMPLATE", pl("LIST"));		//GameObjectLists
		addParam("LANG.TEMPLATE", pl("OBJECT"));	//GameObjects
		
		//use shortcut commands to easily create attributes, lists, and objects
		//TODO make more abstract methods that automatically assign same name to Parameter and key of GameObjectBase
		//ex: pgoa("atk", 0) returns an object equivalent to p("atk", goa("atk", 0))
		
		//StatList
		addParam("lang.template.attribute", p("name", goa("name", "NONAME")));	//default value is "NONAME"
		addParam("lang.template.attribute", p("atk", goa("atk", 0)));
		addParam("lang.template.attribute", p("def", goa("def", 0)));
		addParam("lang.template.attribute", p("spd", goa("spd", 0)));
		//StatList object is called "stats" and has "name", "atk", "def", and "spd" attributes
		addParam("lang.template.object", p("stats", go("stats", rgoa("name"), rgoa("atk"), rgoa("def"), rgoa("spd"))));
		
		//RoomObject
		//room coordinates x must be within [0, 3] and y must be within [0, 1]
		addParam("lang.template.attribute", p("x", goa("x", 0)));
		addParam("lang.template.attribute", p("y", goa("y", 0)));
		addParam("lang.template.attribute", p("symbol", goa("symbol", 'X')));	//default symbol is 'X'
		//RoomObject object is called "visual" and has "x", "y", and "symbol" attributes
		addParam("lang.template.object", p("visual", new GameObject("visual", rgoa("x"), rgoa("y"), rgoa("symbol"))));
		
		//GoldChest
		addParam("lang.template.attribute", p("gold", goa("gold", 0)));	//default to no gold (used elsewhere as well)
		//GoldChest object is called "gchest" and has "gold" attribute and "visual" object
		addParam("lang.template.object", p("gchest", go("gchest", rgo("visual"), rgoa("gold"))));

		//Items
		addParam("lang.template.attribute", p("desc", goa("desc", "NODESC")));	//description
		addParam("lang.template.attribute", p("rarity", goa("rarity", 0)));	//rarity doesn't do anything yet but might as well add it
		addParam("lang.template.attribute", p("value", goa("value", 0)));	//monetary value of the item
		//Weapon object is called "weapon" and has "desc", "rarity", and "value" attributes and "stats" object
		addParam("lang.template.object", p("weapon", go("weapon", rgo("stats"), rgoa("desc"), rgoa("rarity"), rgoa("value"))));
		addParam("lang.template.attribute", p("floor", goa("floor", 0)));	//floor for armor. Default to neutral (no healing, no damage)
		//Armor object is called "armor" and has "desc", "rarity", "value", and "floor" attributes and "stats" object
		addParam("lang.template.object", p("armor", go("armor", rgo("stats"), rgoa("desc"), rgoa("rarity"), rgoa("value"), rgoa("floor"))));
		addParam("lang.template.attribute", p("duration", goa("duration", 1)));	//duration for consumables (not yet implemented)
		addParam("lang.template.attribute", p("self", goa("self", false)));	//whether or not a consumable affects the user (not yet implemented)
		addParam("lang.template.attribute", p("hp", goa("hp", 0)));	//amount of healing that a consumable does (not yet implemented)
		//Consumable object is called "consumable" and has "desc", "rarity", "value", "hp", "duration", and "self" attributes and "stats" object
		addParam("lang.template.object", p("consumable", go("consumable", rgo("stats"), rgoa("desc"), rgoa("rarity"), 
				rgoa("value"), rgoa("hp"), rgoa("duration"), rgoa("self"))));
		
		//Inventory
		addParam("lang.template.attribute", p("size", goa("size", 5)));	//Inventory.maxSize default to 5 (doesn't make sense to default to 0)
		//GameObjectList called "items" that can contain "weapon", "armor", or "consumable" objects. This is turned into Inventory.items
		addParam("lang.template.list", p("items", gol("items", rgo("weapon"), rgo("armor"), rgo("consumable"))));
		//Inventory object is called "inventory" and has "size" attribute and "items" list
		addParam("lang.template.object", p("inventory", go("inventory", rgoa("size"), rgol("items"))));
		
		//Enemy
		addParam("lang.template.attribute", p("maxhp", goa("maxhp", 5)));	//doesn't make sense to default to 0
		addParam("lang.template.attribute", p("dcgold", goa("dcgold", 0)));		//chance (out of 100) that an enemy will drop its gold when it dies
		addParam("lang.template.attribute", p("dcarmor", goa("dcarmor", 0)));	//chance (out of 100) that an enemy will drop its equipped armor when it dies
		addParam("lang.template.attribute", p("dcweapon", goa("dcweapon", 0)));	//chance (out of 100) that an enemy will drop its equipped weapon when it dies
		//Enemy object is called "enemy" and has "hp", "maxhp", "gold", "dcgold", "dcarmor", and "dcweapon" attributes and
		//"visual", "inventory", and "stats" objects
		addParam("lang.template.object", p("enemy", go("enemy", rgoa("hp"), rgoa("maxhp"), rgoa("gold"), rgo("visual"), 
					rgoa("dcgold"), rgoa("dcarmor"), rgoa("dcweapon"), rgo("inventory"), rgo("stats"))));

		//Room
		addParam("lang.template.attribute", p("doors", goa("doors", 0)));		//integer value for doors
		addParam("lang.template.attribute", p("onenter", goa("onenter", "")));	//text to display when player enters room
		addParam("lang.template.attribute", p("onexit", goa("onexit", "")));	//text to display when player exits room
		//combine "onenter" and "onexit" attributes to a single "text" object for organization
		addParam("lang.template.object", p("text", go("text", rgoa("onenter"), rgoa("onexit"))));
		//list of RoomObjects called "objects" that can contain "gchest" and "enemy" objects
		addParam("lang.template.list", p("objects", gol("objects", rgo("gchest"), rgo("enemy"))));
		//Room object is called "room" and contains "door" attribute, "text" object, and "objects" list
		addParam("lang.template.object", p("room", go("room", rgo("text"), rgoa("doors"), rgol("objects"))));
		
		//World (actually added under lang.tlo
		addParam("lang.template.list", p("row", gol("row", rgo("room"))));	//list of "room" objects representing a row of the world grid
		addParam("lang.template.list", p("rooms", gol("rooms", rgol("row"))));	//list of "row" objects representing the world grid
		
		addParam("LANG", pl("TLO"));	//Top level objects (can be defined outside of other objects)
		//Player object is called "player" and has "hp", "maxhp", and "gold" attributes and "visual", "stats", and "inventory" objects 
		addParam("lang.tlo", p("player", go("player", rgoa("hp"), rgoa("maxhp"), rgoa("gold"), rgo("visual"), rgo("stats"), rgo("inventory"))));
		//World object is called "world" and has "name" attribute (doesn't do anything yet) and "rooms" list
		addParam("lang.tlo", p("world", go("world", rgoa("name"), rgol("rooms"))));

		
		//commands that the player can use
		
		//draws map
		addParam("con.cmd", p("map", (GameFunction)(String s, Game g) -> 
		{
			g.draw();
		}));
		//moves specified number of times in given direction
		addParam("con.cmd", p("move", (GameFunction)(String input, Game g) ->
		{
			Scanner s = new Scanner(input);
			
			if(!s.hasNext()) {	//direction not given
				Console.logn("Specify a direction (up, left, down, right).");
				s.close();
				return;
			}
			
			String dir = s.next();	//direction string ("up", "left", "down", or "right")
			int times = 1;	//number of times to move in that direction
			if(s.hasNextInt())
				times = s.nextInt();	//if specified
			
			g.movePlayer(dir, times);
			s.close();
		}));
		//draws box containing information about the player's Inventory
		addParam("con.cmd", p("inv", (GameFunction)(String s, Game g) ->
		{
			g.player().printInv();
		}));
		//lists player's stats
		addParam("con.cmd", p("stats", (GameFunction)(String s, Game g) ->
		{
			g.player().print();
		}));
		//equips Weapon/Armor with specified name
		addParam("con.cmd", p("equip", (GameFunction)(String s, Game g) ->
		{
			String item = s;
			String success = g.player().equip(item);
			if(success != null)
				Console.logn(Console.rsetting("cmd.equip.success"), success);
			else
				Console.logn(Console.rsetting("cmd.equip.fail"));
			
		}));
		//describes Item with given name
		addParam("con.cmd", p("desc", (GameFunction)(String s, Game g) ->
		{
			String item = s;
			Item success = g.player().getItem(item);
			if(success != null)
				Console.logn(Console.rsetting("cmd.desc.success"), success.desc());
			else
				Console.logn(Console.rsetting("cmd.desc.fail"));
			
		}));
		//examines RoomObject with given char symbol
		addParam("con.cmd", p("x", (GameFunction)(String s, Game g) ->
		{
			char id = s.charAt(0);	//get first char
			Room cur = g.currentRoom();
			RoomObject ro = cur.object(id, 1);	//first occurrence of given char (TODO support indexing)
			if(ro != null)
				ro.interact(cur, g.player());
			else
				Console.logn(Console.rsetting("cmd.x.invalid"));
		}));
		//draws a box listing available commands and descriptions of those commands
		addParam("con.cmd", p("help", (GameFunction)(String s, Game g) ->
		{
			String[] cmds = {	//command names and argument lists
				"map", 
				"move " + Console.rgraphic("help.required") + "dir " + Console.rgraphic("help.optional") + "n", 
				"inv", 
				"stats",
				"equip " + Console.rgraphic("help.required") + "s",
				"desc " + Console.rgraphic("help.required") + "s",
				"x " + Console.rgraphic("help.required") + "c",
				"quit"
			};
			String[] descs = {	//command descriptions
				"draws the map",
				"moves the player n times in given direction",
				"displays all items in inventory",
				"list of your stats",
				"equips weapon/armor with given name",
				"describes item with given name",
				"interact with object with given symbol",
				"stop the game"
			};
			
			//print as non-bulleted two column box titled "Help" using "con.graphic.help" as graphical root
			Console.printBoxColumns("help", "Help", false, cmds, descs);	
		}));
		
		addParam("con.cmd", pl("encounter"));	//commands available when player is in an encounter
		//lists all enemies
		addParam("con.cmd.encounter", p("list", (EncounterFunction)(String s, Encounter e) ->
		{
			String[] lines = new String[e.enemies().size()];
			for(int i = 0; i < lines.length; i++)	//fill array with enemy names
				lines[i] = e.enemies().get(i).name();
			//print as bulleted box titled "Enemies" using "con.graphic.encounter" as graphical root
			Console.printBox("encounter", "Enemies", true, lines);
		}));
		//attack enemy with given name
		addParam("con.cmd.encounter", p("hit", (EncounterFunction)(String answer, Encounter e) ->
		{
			String targetName = answer;
			int index = 1;	//the nth occurrence of enemy with given name to be attacked
			ArrayList<Enemy> enemies = e.enemies();

			if(answer.contains(" ")) {	//specified index
				try {
					index = Integer.parseInt(answer.substring(answer.indexOf(' ') + 1));
				}
				catch(Exception ex) {
					Console.logn(Console.rsetting("invalid.target.index"));
				}
			}
			
			int valid = 0;	//occurrences of given name
			Enemy target = null;
			for(Enemy enemy : enemies) {	//for each enemy
				if(enemy.name().equalsIgnoreCase(targetName)) {	//ignore case for simplicity
					valid++;
					if(valid == index) {	//if occurrences matches specified occurrence, select current enemy
						target = enemy;
						break;
					}
				}
			}
			
			if(target == null)	//target doesn't exist
				Console.logn(Console.rsetting("invalid.target.name"));
			else if(!e.p().attack(target)) {	//if the enemy doesn't survive the player's attack
				e.enemies().remove(target);	//remove from encounter's list of enemies
				//roll to see what the enemy drops
				if(target.goldDrop())
					e.addGold(target.gold());
				if(target.armorDrop())
					e.addItem(target.equippedArmor());
				if(target.weaponDrop())
					e.addItem(target.equippedWeapon());
			}			
		}));
		//shows commands available in battle
		addParam("con.cmd.encounter", p("help", (EncounterFunction)(String s, Encounter e) ->
		{
			String[] cmds = {	//list of commands and arguments
				"list",
				"hit " + Console.rgraphic("encounter.help.required") + "s"
			};
			String[] descs = {	//descriptions of commands
				"lists all enemies",
				"hit enemy with name s"
			};
			
			//print as non-bulleted two column box titled "Help" using "con.graphic.encounter.help" as graphical root
			Console.printBoxColumns("encounter.help", "Help", false, cmds, descs);	
		}));
		
		addParam("con.cmd.encounter", pl("post"));	//commands for cleanup phase of an encounter (taking dropped items)
		//TODO allow player to do more with dropped items before picking them up (like description)
		//list all available items
		addParam("con.cmd.encounter.post", p("list", (EncounterFunction)(String s, Encounter e) ->
		{
			//print as custom bulleted two column box titled "Available Items" using "con.graphic.encounter" as graphical root
			//with no character specified as the owner of the given item list
			Console.printItemBox("encounter", "Available Items", e.items(), null);
		}));
		//take item with given name
		addParam("con.cmd.encounter.post", p("take", (EncounterFunction)(String answer, Encounter e) ->
		{
			ArrayList<Item> items = e.items();
			Player p = e.p();
			String target = answer.substring(answer.indexOf(' ') + 1);
			
			int extraGold = 0;	//all items that the player doesn't take (or can't carry) will be converted to their value in gold
			for(Item i : items)
				extraGold += i.value();	//sum up value of all items in list
			
			boolean all = target.equals("all");	//player wants to take all items
			for(int i = 0; i < items.size(); i++) {	//for each item
				Item cur = items.get(i);	//current item
				//if player doesn't wantto take all and name of current item equals target name
				//OR if player wants all and has room for them
				if((!all && cur.name().equalsIgnoreCase(target)) || (all && !p.inv().isFull())) {
					extraGold -= items.get(i).value();	//subtract value of current item from gold total
					p.inv().add(cur);	//give to player
					items.remove(i);	//remove from available list
					Console.logn("You take %s.", cur.name());	//print success message TODO add this format string to registry
					if(!all)	//player only wanted this one item
						break;
					i--;	//decrease index to avoid skipping other items
				}
			}
			//if player has no more room or decides not to take all items, give them gold value of remaining items
			if(p.inv().isFull() && !items.isEmpty()) {
				//TODO add to registry and create different strings for "can't carry" and "decided not to take"
				Console.logn("You cannot carry all of the items. You receive %d gold for the remaining ones.", extraGold);
				p.addGold(extraGold);
			}
		}));
		//lists commands available during encounter cleanup
		addParam("con.cmd.encounter.post", p("help", (EncounterFunction)(String s, Encounter e) ->
		{
			String[] cmds = {	//list of commands and arguments
				"list",
				"take " + Console.rgraphic("encounter.help.required") + "s",
				"leave"
			};
			String[] descs = {	//command descriptions
				"lists all items",
				"take item with given name or all items",
				"take no items"
			};
			
			//print as non-bulleted two column box titled "Help" using con.graphic.encounter.help as graphical root
			//TODO custom graphical root (con.graphic.encounter.post.help)
			Console.printBoxColumns("encounter.help", "Help", false, cmds, descs);	
		}));
		
		getParam("CON.CMD").lock();	//prevent language files from accessing con.cmd
		//prevent language files from adding to con.setting/graphic
		Console.<ParameterList>getParam("con.setting").lockAdditions();
		Console.<ParameterList>getParam("con.graphic").lockAdditions();
		getParam("LANG").lock();	//prevent language files from accessing con.lang
	}
	
	//called from main game loop
	//takes a function, an input string, and a game and performs the function if possible
	//otherwise, it prints out the invalid message
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
	//called from encounter loop
	//similar to gfOp
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
	//prints list of items
	//each item is bulleted according to which type it is (weapon, armor, consumable)
	//if c != null, items equipped by c have an extra character added to their bullet
	public static void printItemBox(String graphicRoot, String head, ArrayList<Item> items, Character c) {
		String[] names = new String[items.size()];
		String[] stats = new String[items.size()];
		for(int i = 0; i < items.size(); i++) {
			Item cur = items.get(i);
			names[i] = cur.formatName(c != null && c.isEquipped(cur));	//whether or not this item is equipped
			stats[i] = cur.formatStats();
		}
		
		//print non-bulleted (custom bullet provided by formatName()), two column box using given graphical root and title
		Console.printBoxColumns(graphicRoot, head, false, names, stats);
	}
	//prints a box containing two columns of strings
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
	//prints a list of strings (optionally with bullet points before them) with a box around it
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
		
		Console.logn(title);	//top of box
		
		String rowFormat = "";
		rowFormat = "%c " + (bullet ? "%c " : "") + "%-" + (length - 4 - (bullet ? 2 : 0)) + "s %c";
		
		for(String row : lines) {	//middle section of box
			if(bullet)
				Console.logn(rowFormat, borderns, bulletc, row, borderns);
			else
				Console.logn(rowFormat, borderns, row, borderns);
		}
		
		String bottom = "";
		for(int i = 0; i < length - 2; i++)
			bottom += borderew;
		bottom = corner + bottom + corner;
		
		Console.logn(bottom);	//bottom of box
	}
	//inital call to logr (prints output string)
	private static void log(String s, Object...args) {
		logr(s, false, args);
	}
	//prints string one character at a time
	//recurse determines whether or not this is the first call, and if the output string should be logged
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
	//prints given string with new line
	public static void logn(String s, Object...args) {
		log(s + "\n", args);
	}
	public static void main(String[] args) {
		init();	//initialize registry
		
		Game g = Parser.createGame();	//read game files
		g.run();	//start game
	}
}
