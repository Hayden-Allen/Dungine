import java.util.*;
import java.io.*;

public class Parser {	//reads game data from files to create a Game
	//error codes
	public static final int STRING = 0, DEFINE_DNE = 1, BLOCK = 2, PAREN_MATCH = 3, INVALID_CMD = 4, TLO = 5, PARAM_DNE = 6, FILE_END = 7;
	public static final int INVALID_TYPE = 8, PARAM_FAIL = 9, PARAM_WARNING = 10, INV_OVERFLOW = 11;
	public static final int RO_OUT_OF_BOUNDS_X = 12, RO_OUT_OF_BOUNDS_Y = 13;
	
	//language constants
	private static final char LINE_DELIM = '\n', EOF = '\0', STRING_DELIM = '\"', DEF_DELIM = '%', ELEM_DELIM = ',', ID_DELIM = ':';
	private static final String PARENTHESES = "[]{}", DELIMITERS = PARENTHESES + LINE_DELIM + DEF_DELIM + ELEM_DELIM + ID_DELIM;
	private String file, data; //name of the file and contents of the file
	private int line = 1, index = 0;	//line # and character index
	
	public Parser(String file, boolean header) {	//read file with given filepath
		try {			
			Scanner in = new Scanner(new File(file));
			String data = "";
			while(in.hasNextLine())	//read in all lines of the file
				data += in.nextLine() + LINE_DELIM;
			data = data.substring(0, data.length() - 1) + EOF;	//remove trailing \n
			in.close();
						
			if(!header) {	//if game file, remove all non-string whitespace
				boolean inString = false;
				for(int i = 0; i < data.length(); i++) {
					char c = data.charAt(i);
					if(c == '\"')
						inString = !inString;
					if((c == ' ' || c == '\t') && !inString) {
						data = data.substring(0, i) + data.substring(Math.min(data.length(), i + 1));
						i--;
					}
				}
			}
			this.data = data;
			this.file = file;
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Parser(String file, String data) {	//used in GameObject creation
		this.file = file;
		this.data = data;
	}
	public String file() {
		return file;
	}
	public char nextChar() {	//returns character at current index and increments index by 1
		if(index > data.length() - 1)
			err(FILE_END);
		char c = data.charAt(index++);
			
		if(c == LINE_DELIM)	//if character was \n, increment line count
			line++;
		return c;
	}
	public char peek() {	//get next character and then reset state
		int i = index, l = line;
		char c = nextChar();
		index = i;
		line = l;
		return c;
	}
	public String next() {	//block of characters between current position and next delimiter
		String s = "";
		
		while(hasNext()) {
			char c = nextChar();
			if(DELIMITERS.indexOf(c) != -1 || c == ' ')	//for headers only (game files are stripped of whitespace)
				break;
			s += c;
		}
		
		if(s.isBlank() && hasNext())	//if started on delimiter
			return next();
		
		return s;
	}
	public String data() {
		return data;
	}
	public String nextString() {	//returns block of characters between two quotation marks
		if(peek() == DEF_DELIM) {	//check if value should be gotten from the registry
			nextChar(); //eat %
			return this.<String>checkDef(next());	//check the registry for a definition
		}
			
		String s = "";
		if(nextChar() != STRING_DELIM)	//eat "
			err(STRING, "start");	//strings must start with "
		while(true) {
			char c = nextChar();
			if(c == STRING_DELIM)	//once another " is hit, stop
				break;
			s += c;
		}
		return s;
	}
	public int nextInt() {	//next integer value
		if(peek() == DEF_DELIM) {	//check if value should be gotten from the registry
			nextChar(); //eat %
			return this.<Integer>checkDef(next().trim());
		}
		String n = next().trim();
		String bin = Console.rlang("key.bin");
		if(n.startsWith(bin))	//binary number (used for doors)
			return Integer.parseInt(n.substring(bin.length()), 2);		//parse in base 2	
		return Integer.parseInt(n);
	}
	public boolean nextBoolean() {	//next boolean value
		if(peek() == DEF_DELIM) {	//check if value should be gotten from the registry
			nextChar(); //eat %
			return this.<Boolean>checkDef(next().trim());
		}
		return Boolean.parseBoolean(next().trim());
	}
	public String nextBlock() {	//next string of characters enclosed between two complimentary parentheses
		Stack<java.lang.Character> stack = new Stack<java.lang.Character>();	//opening parentheses
		char c = nextChar();
		if(!isOpen(c))	//block must start with opening parentheses
			err(BLOCK);
		stack.push(c);	//add to stack
		
		String s = c + "";
		while(!stack.isEmpty()) {
			c = nextChar();
			if(isOpen(c))	//push all open parentheses to stack
				stack.push(c);
			else if(isClose(c)) {	//if close is not equal to compliment of top of stack, throw error
				if(c != compliment(stack.pop()))
					err(PAREN_MATCH);
			}
			s += c;
		}
		return s;
	}
	public void err(int code, Object...args) {	//formats and prints a string containing error information
		String s = String.format("\nError in file %s (line %d):\n", file, line);
		switch(code) {
		case FILE_END: throw new Error(s + "end of file.");	//attempted use of nonexistent input
		case INVALID_TYPE: throw new Error(s + "invalid type \'" + args[0] + "\'.");	//invalid parameter type
		case PARAM_DNE: throw new Error(s + "Parameter \"" + args[0] + "\" does not exist.");	//parameter with given path doesn't exist
		//parameter with given path has noNew set to true
		case PARAM_FAIL: throw new Error(s + "You can not create new values in path \"" + args[0] + "\".");	
		case PARAM_WARNING:	//if able, warn about parameter overrides
			if(Console.echo())
				System.out.printf("WARNING: Override {%s%s%s} from {%s} to {%s}\n", args[0], args[1], args[2], args[3], args[4]); 
		break;
		//Inventory's item list is longer than its maxSize
		case INV_OVERFLOW: throw new Error(s + "Item list of size " + args[0] + " too large for inventory of size " + args[1] + ".");
		//RoomObject coordinates are invalid
		case RO_OUT_OF_BOUNDS_X: throw new Error(s + "x coordinate " + args[0] + " is out of bounds for [0, 3].");
		case RO_OUT_OF_BOUNDS_Y: throw new Error(s = "y coordinate " + args[0] + " is ut of bounds for [0, 1].");
		}
	}
	public boolean hasNext() {	//whether or not there is more data in the file
		return index < data.length();
	}
	public <E> E checkDef(String path) {	//check user subtree of registry for a value
		Parameter<E> p = Console.<Parameter<E>>getParam("USER." + path);
		if(p == null)
			err(DEFINE_DNE);	//path leads nowhere
		return p.value();
	}
	public static Game createGame() {
		Parser p = new Parser(Console.rlang("Main"), true);	//Parser on main filepath, is a header
		Console.parser = p;	//used for error reporting from methods where reference to this is otherwise unavailable
		
		Game g = new Game();	//create empty Game
		p.readHeader(g);	//put data from files into g
		return g;
	}
	public void readHeader(Game g) {	//ready header file (.dgnh)
		while(hasNext()) {
			String cmd = next();
			if(cmd.equals(Console.rlang("key.read.header"))) {	//another header file
				Parser p = new Parser(nextString() + ".dgnh", true);	//new Parser for new header
				Console.parser = p;
				p.readHeader(g);	//pass same Game
				Console.parser = this;
			}
			else if(cmd.equals(Console.rlang("key.read.game"))) {	//game file
				Parser p = new Parser(nextString() + ".dgn", false);	//new Parser for game file
				Console.parser = p;
				p.readGF(g);
				Console.parser = this;
			}
			else if(cmd.equals(Console.rlang("key.param"))) {	//registry definition/override
				String fullPath = next().toUpperCase();	//path to value
				String path = fullPath.substring(0, fullPath.lastIndexOf('.'));	//value's parent
				String name = fullPath.substring(fullPath.lastIndexOf('.') + 1);	//value's name
				
				char type = nextChar();
				nextChar(); //eat space between path and data type
				
				int result = 0;	//-2, -1, 0, 1 = DNE, noNew, success, warning
				Object value = null;
				
				Parameter<Object> old = null;
				if(Console.validPath(fullPath))	//if value at given path already exists, store it
					old = Console.getParam(fullPath);
				
				switch(type) {
				case 'i':	//int
					value = nextInt();
					result = Console.addParam(path, new Parameter<Integer>(name, (Integer)value)); 
				break;
				case 's':	//String
					value = nextString();
					result = Console.addParam(path, new Parameter<String>(name, (String)value)); 
				break;
				case 'b':	//boolean 
					value = nextBoolean();
					result = Console.addParam(path, new Parameter<Boolean>(name, (Boolean)value)); 
				break;
				case 'c':	//char
					value = nextChar();
					result = Console.addParam(path, new Parameter<java.lang.Character>(name, (java.lang.Character)value));
				break;
				default: err(INVALID_TYPE, type);	//unsupported type
				}
				
				//handle result from creation/overwrite operation
				switch(result) {
				case -2: err(PARAM_DNE, path); break;
				case -1: err(PARAM_FAIL, path); break;
				case 1: err(PARAM_WARNING, 
						path, path.isEmpty() ? "" : ".", name, old, new Parameter<Object>(name, value)); 
				break;
				}
			}
		}
	}
	public void readGF(Game g) {	//read game file (.dgn)
		while(hasNext()) {
			String key = next();
			//only two top left objects currently implemented
			switch(key) {
			case "world": new World(g, Console.<Parameter<GameObject>>getParam("lang.tlo.world").value().create(this)); break;
			case "player": new Player(g, Console.<Parameter<GameObject>>getParam("lang.tlo.player").value().create(this)); break;
			default: err(TLO);
			}
		}
	}
	
	//for use in nextBlock()
	private boolean isOpen(char c) {
		return c == '{' || c == '[';
	}
	private boolean isClose(char c) {
		return c == '}' || c == ']';
	}
	private char compliment(char c) {
		if(isOpen(c))
			return (c == '{' ? '}' : ']');
		else if(isClose(c))
			return (c == '}' ? '{' : '[');
		return '&';
	}
}