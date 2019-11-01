import java.util.*;

public class Game {
	private Player player;
	private ArrayList<World> worlds;
	private int currentWorld;
	private Scanner in;
	
	public Game() {
		player = null;
		worlds = new ArrayList<World>();
		currentWorld = 0;
	}
	
	public Scanner in() {
		return in;
	}
	public void draw() {
		worlds.get(currentWorld).draw(player);
	}
	public void addWorld(World w) {
		worlds.add(w);
	}
	public Player player() {
		return player;
	}
	public void setPlayer(Player p) {
		player = p;
	}
	public ArrayList<World> worlds(){
		return worlds;
	}
	public void run() {
		in = new Scanner(System.in);
		
		String invalid = Console.<String>rsetting("invalid.cmd");
		ParameterList cmds = Console.getParam("con.cmd");
		
		String answer = "";
		while(true) {
			System.out.print(Console.<String>rsetting("input"));
			answer = in.nextLine().trim().toLowerCase();
			String cmd = answer.substring(0, answer.contains(" ") ? answer.indexOf(' ') : answer.length());
			
			if(cmd.equals("quit"))
				break;
			Parameter<GameFunction> fn = cmds.getElement(cmd);
			if(fn == null) {
				if(cmd.isEmpty())
					System.out.println();
				Console.logn(invalid);
			}
			else 
				fn.value().op(answer, this);
		}
		in.close();
	}
	public void movePlayer(String dir, int times) {	//0 1 2 3 = up left down right
		int direction = 0;
		switch(dir) {
		case "up": direction = 0; break;
		case "left": direction = 1; break;
		case "down": direction = 2; break;
		case "right": direction = 3; break;
		default: Console.logn(Console.rsetting("invalid.dir")); return;
		}
		
		int[] dx = {0, -1, 0, 1};
		int[] dy = {-1, 0, 1, 0};

		World cur = worlds.get(currentWorld);
		boolean enter = false;
		for(int i = 0; i < times; i++) {
			int rx = player.wx + dx[direction];
			int ry = player.wy + dy[direction];
			
			if(!cur.doorAt(player.wx, player.wy, dx[direction], dy[direction]))
				break;
			
			cur.grid().get(player.wy).get(player.wx).onExit(player);
			player.wx = rx;
			player.wy = ry;
			enter = true;
		}
		
		if(Console.<Boolean>rsetting("maponmove"))
			draw();
		
		if(enter)
			cur.grid().get(player.wy).get(player.wx).onEnter(player, this);
	}
}
