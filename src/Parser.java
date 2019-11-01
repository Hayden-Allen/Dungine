import java.util.*;
import java.io.*;
public class Parser {
	public static final int STRING = 0, DEFINE_DNE = 1, BLOCK = 2, PAREN_MATCH = 3, INVALID_CMD = 4, TLO = 5, PARAM_DNE = 6, FILE_END = 7;
	public static final int INVALID_TYPE = 8, PARAM_FAIL = 9, PARAM_WARNING = 10;
	
	private static final char LINE_DELIM = '\n', EOF = '\0', STRING_DELIM = '\"', DEF_DELIM = '%', ELEM_DELIM = ',', ID_DELIM = ':';
	private static final String PARENTHESES = "[]{}", DELIMETERS = PARENTHESES + LINE_DELIM + DEF_DELIM + ELEM_DELIM + ID_DELIM;
	private String data, file;
	private int line = 1, index = 0;
	
	public Parser(String file, boolean header) {
		try {			
			Scanner in = new Scanner(new File(file));
			String data = "";
			while(in.hasNextLine())
				data += in.nextLine() + LINE_DELIM;
			data = data.substring(0, data.length() - 1) + EOF;	//remove trailing \n
			in.close();
						
			if(!header) {
				boolean inString = false;	//remove non-string whitespace
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
	public Parser(String file, String data) {
		this.file = file;
		this.data = data;
	}
	public String file() {
		return file;
	}
	public char nextChar() {
		if(index > data.length() - 1)
			err(FILE_END);
		char c = data.charAt(index++);
			
		if(c == LINE_DELIM) {
			line++;
			//return nextChar();
		}
		return c;
	}
	public char peek() {
		int i = index, l = line;
		char c = nextChar();
		index = i;
		line = l;
		return c;
	}
	public String next() {
		String s = "";
		
		while(hasNext()) {
			char c = nextChar();
			if(DELIMETERS.indexOf(c) != -1 || c == ' ')	//for headers only
				break;
			s += c;
		}
		
		if(s.isBlank() && hasNext())
			return next();
		
		return s;
	}
	public String data() {
		return data;
	}
	public String nextString() {
		if(peek() == DEF_DELIM) {
			nextChar(); //eat %
			return this.<String>checkDef(next());
		}
			
		String s = "";
		if(nextChar() != STRING_DELIM)	//eat "
			err(STRING, "start");
		while(true) {
			char c = nextChar();
			if(c == STRING_DELIM)
				break;
			s += c;
		}
		return s;
	}
	public int nextInt() {
		if(peek() == DEF_DELIM) {
			nextChar(); //eat %
			return this.<Integer>checkDef(next().trim());
		}
		String n = next().trim();
		if(n.startsWith(Console.rlang("key.bin")))
			return Integer.parseInt(n.substring(2), 2);		
		return Integer.parseInt(n);
	}
	public boolean nextBoolean() {
		if(peek() == DEF_DELIM) {
			nextChar(); //eat %
			return this.<Boolean>checkDef(next().trim());
		}
		return Boolean.parseBoolean(next().trim());
	}
	public String nextBlock() {
		Stack<java.lang.Character> stack = new Stack<java.lang.Character>();
		char c = nextChar();
		if(!isOpen(c))
			err(BLOCK);
		stack.push(c);
		
		String s = c + "";
		while(!stack.isEmpty()) {
			c = nextChar();
			if(isOpen(c))
				stack.push(c);
			else if(isClose(c)) {
				if(c != compliment(stack.pop()))
					err(PAREN_MATCH);
			}
			s += c;
		}
		return s;
	}
	public void err(int code, Object...args) {
		String s = String.format("\nError in file %s (line %d):\n", file, line);
		switch(code) {
		case FILE_END: throw new Error(s + "end of file.");
		case INVALID_TYPE: throw new Error(s + "invalid type \'" + args[0] + "\'.");
		case PARAM_DNE: throw new Error(s + "Parameter \"" + args[0] + "\" does not exist.");
		case PARAM_FAIL: throw new Error(s + "You can not create new values in path \"" + args[0] + "\".");
		case PARAM_WARNING:
			if(Console.echo())
				System.out.printf("WARNING: Override {%s%s%s} from {%s} to {%s}\n", args[0], args[1], args[2], args[3], args[4]); 
		break;
		}
	}
	public boolean hasNext() {
		return index < data.length();
	}
	public <E> E checkDef(String path) {
		Parameter<E> p = Console.<Parameter<E>>getParam("USER." + path);
		if(p == null)
			err(DEFINE_DNE);
		return p.value();
	}
	public static Game createGame() {
		Parser p = new Parser(Console.rlang("Main"), true);
		Console.parser = p;
		
		Game g = new Game();
		p.readHeader(g);
		return g;
	}
	public void readHeader(Game g) {
		while(hasNext()) {
			String cmd = next();
			if(cmd.equals(Console.rlang("key.read.header"))) {
				Parser p = new Parser(nextString() + ".dgnh", true);
				p.readHeader(g);
			}
			else if(cmd.equals(Console.rlang("key.read.game"))) {
				Parser p = new Parser(nextString() + ".dgn", false);
				p.readGF(g);
			}
			else if(cmd.equals(Console.rlang("key.param"))) {
				String fullPath = next().toUpperCase();
				String path = fullPath.substring(0, fullPath.lastIndexOf('.'));
				String name = fullPath.substring(fullPath.lastIndexOf('.') + 1);
				
				char type = nextChar();
				nextChar(); //eat ' '
				
				int result = 0;	//-2, -1, 0, 1 = DNE, noNew, success, warning
				Object value = null;
				
				Parameter<Object> old = null;
				if(Console.validPath(fullPath))
					old = Console.getParam(fullPath);
				
				switch(type) {
				case 'i':
					value = nextInt();
					result = Console.addParam(path, new Parameter<Integer>(name, (Integer)value)); 
				break;
				case 's': 
					value = nextString();
					result = Console.addParam(path, new Parameter<String>(name, (String)value)); 
				break;
				case 'b': 
					value = nextBoolean();
					result = Console.addParam(path, new Parameter<Boolean>(name, (Boolean)value)); 
				break;
				case 'c': 
					value = nextChar();
					result = Console.addParam(path, new Parameter<java.lang.Character>(name, (java.lang.Character)value));
				break;
				default: err(INVALID_TYPE, type);
				}
				
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
	public void readGF(Game g) {
		while(hasNext()) {
			String key = next();
			switch(key) {
			case "world": new World(g, Console.<Parameter<GameObject>>getParam("lang.tlo.world").value().create(this)); break;
			case "player": new Player(g, Console.<Parameter<GameObject>>getParam("lang.tlo.player").value().create(this)); break;
			default: err(TLO);
			}
		}
	}
	
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