import java.util.*;
import java.io.*;
public class Parser {
	public static final char EOF = '\0';
	public static final String DELIMETERS = ": \t\n{}[]()" + EOF;
	private int index = 0;
	private String file;
	
	public Parser(String path) {//TODO: change the way file is stored to allow for errors w/ line numbers
		try {
			file = "";
			Scanner s = new Scanner(new File(Console.BASE_FILEPATH + path));
			while(s.hasNext())
				file += s.nextLine() + '\n';
			file += EOF;
			s.close();
		}
		catch(IOException e) {e.printStackTrace();}
	}
	public Parser(String s, int start) {	//TODO: start unnecessary but conflicting headers
		file = s + EOF;
		index = start;
	}
	
	public boolean hasNext() {
		return index < file.length();
	}
	private boolean isClose(char c) {
		return c == '}' || c == ']' || c == ')';
	}
	private boolean isDelim(char c) {
		return DELIMETERS.indexOf(c) != -1;
	}
	private boolean isOpen(char c) {
		return c == '{' || c == '[' || c == '(';
	}
	private char compliment(char c) {
		if(c == '{')
			return '}';
		if(c == '[')
			return ']';
		if(c == '(')
			return ')';
		return '\0';
	}
	public char nextChar() {
		if(hasNext())
			return file.charAt(index++);
		return EOF;
	}
	public char nextNonDelim() {
		char c = nextChar();
		while(isDelim(c))
			c = nextChar();
		return c;
	}
	public char nextParentheses() {
		char c = nextChar();
		while(!("()[]{}".contains(""+c)))
			c = nextChar();
		return c;
	}
	public <E> Object nextE(E e) {	//TODO account for definitions
		if(e instanceof Integer) {
			String s = next();
			if(s.startsWith("0b"))
				return Integer.parseInt(s.substring(2), 2);
			return Integer.parseInt(s);
		}
		if(e instanceof String)
			return nextString();
		if(e instanceof Boolean)
			return Boolean.parseBoolean(next());
		if(e instanceof java.lang.Character)
			return nextNonDelim();
		return null;
	}
	public String next() {
		if(hasNext()) {
			String s = "";
			char c;
			while((c = nextChar()) != EOF && !isDelim(c))
				s += c;
			if(s.isEmpty())
				return next();
			index--;
			return s;
		}
		return "";
	}
	public String nextBlock() {
		String s = "";
		Stack<java.lang.Character> parentheses = new Stack<java.lang.Character>();
		
		parentheses.push(nextParentheses());
		while(!parentheses.isEmpty()) {
			char c = nextChar();
			s += c;
			if(isOpen(c))
				parentheses.push(c);
			else if(isClose(c)) {
				if(compliment(parentheses.pop()) != c) {
					System.out.println(s);
					throw new InputMismatchException("Invalid parentheses");
				}
			}
		}
		return s.substring(0, s.length() - 1);
	}
	public String nextString() {
		String s = "";
		char c = nextNonDelim();
		if(c != '\"')
			throw new InputMismatchException("String literal must begin with \"");
		while((c = nextChar()) != EOF && c != '\"')
			s += c;
		return s;
	}
	public static void createGame(Game g) {
		readHeader(Console.MAIN_FILEPATH, g);
	}
	public static void readHeader(String fp, Game g) {
		Parser p = new Parser(fp);
		
		while(p.hasNext()) {
			String cmd = p.next();
			if(cmd.equals("import"))
				readHeader(p.nextString() + Console.FILE_TYPE, g);
			else if(cmd.equals("read"))
				readFile(p.nextString() + Console.FILE_TYPE, g);
			else if(cmd.equals("define"))
				g.addDefinition(p);
			else if(cmd.equals("default")) {
				String key = p.next();
				Console.symbols.put(key, p.nextNonDelim());
			}
		}
	}
	public static void readFile(String fp, Game g) {
		Parser p = new Parser(fp);
		
		while(p.hasNext()) {
			String key = p.next();
			
			if(key.equals("player"))
				g.setPlayer(new Player(Console.<GameObject>template("player").create(p)));
			if(key.equals("world"))
				g.addWorld(new World(Console.<GameObject>template("world").create(p)));
		}
	}
}
