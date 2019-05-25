import java.util.*;
import java.io.*;
public class Parser {
	public static final char EOF = '\0';
	public static final String DELIMETERS = ": \t\n{}[]()" + EOF;
	private String file;
	private int index = 0;
	private Stack<java.lang.Character> parentheses;
	
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
		parentheses = new Stack<java.lang.Character>();
	}
	
	private boolean isDelim(char c) {
		return DELIMETERS.indexOf(c) != -1;
	}
	public char nextChar() {
		if(hasNext())
			return file.charAt(index++);
		return EOF;
	}
	public boolean hasNext() {
		return index < file.length();
	}
	public String next() {
		String s = "";
		char c;
		while((c = nextChar()) != EOF && !isDelim(c))
			s += c;
		return s;
	}
	public String nextString() {
		String s = "";
		char c = nextChar();
		if(c != '\"')
			throw new InputMismatchException("String literal must begin with \"");
		while((c = nextChar()) != EOF && c != '\"')
			s += c;
		return s;
	}
	
	private boolean isOpen(char c) {
		return c == '{' || c == '[' || c == '(';
	}
	private boolean isClose(char c) {
		return c == '}' || c == ']' || c == ')';
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
	public void requireParentheses() {
		char c = nextChar();
		if(!(isOpen(c) || isClose(c)))
			throw new InputMismatchException("Parentheses required");
		if(isOpen(c))
			parentheses.push(c);
		else if(parentheses.pop() != compliment(c))
			throw new InputMismatchException("Incorrect parentheses");
	}
	
	public <E> Object nextE(E e) {
		if(e instanceof Integer)
			return Integer.parseInt(next());
		if(e instanceof String)
			return nextString();
		return null;
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
				read(p.nextString() + Console.FILE_TYPE, g);
		}
	}
	public static void read(String fp, Game g) {
		
	}
	/*
	public static ArrayList<GameObjectBase> readDef(String fp) {
		ArrayList<GameObjectBase> gos = new ArrayList<GameObjectBase>();
		Parser p = new Parser(fp);
		
		while(p.hasNext()) {
			String id = p.next();
			if(id.isEmpty())	//TODO: why
				break;
			System.out.print(id + "-> ");
			gos.add(Console.template(id).create(p));
			System.out.println(gos.get(gos.size() - 1));
		}
		
		return gos;
	}
	*/
}
