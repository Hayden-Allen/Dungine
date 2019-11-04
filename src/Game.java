import java.util.ArrayList;
import java.util.Scanner;

public class Game {
	private Player player;	//only one player allowed
	private ArrayList<World> worlds;
	private int currentWorld;	
	private Scanner in;	//used to get input
	
	public Game() {
		player = null;
		worlds = new ArrayList<World>();
		currentWorld = 0;	//first in list
	}
	
	public Room currentRoom() {
		return worlds.get(currentWorld).grid().get(player.wy()).get(player.wx());
	}
	public Scanner in() {	//used in Encounter
		return in;
	}
	public void draw() {	//draws the map of the current World
		worlds.get(currentWorld).draw(player);
	}
	public void addWorld(World w) {	//adds w to list of Worlds
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
	public void run() {	//game loop
		in = new Scanner(System.in);	//for player input
		
		//list of possible commands
		ParameterList cmds = Console.getParam("con.cmd");
		
		String answer = "";	//player's input
		while(true) {
			System.out.print(Console.<String>rsetting("input"));	//prompt for input
			answer = in.nextLine().trim().toLowerCase();
			
			//split input into command and arguments
			boolean space = answer.contains(" ");
			String cmd = answer.substring(0, space ? answer.indexOf(' ') : answer.length());
			if(space)
				answer = answer.substring(answer.indexOf(' ') + 1);
			
			if(cmd.equals("quit"))	//stops game
				break;
			
			Console.gfOp(cmds.getElement(cmd), answer, this);	//perform specified command
		}
		in.close();	//close player input
	}
	//moves player in given direction given number of times
	public void movePlayer(String dir, int times) {	//0 1 2 3 = up left down right
		//convert direction String to number
		int direction = 0;	//used as array index
		switch(dir) {
		case "up": direction = 0; break;
		case "left": direction = 1; break;
		case "down": direction = 2; break;
		case "right": direction = 3; break;
		default: Console.logn(Console.rsetting("invalid.dir")); return;
		}
		
		//values for each direction (0 is up so don't move in x but move -1 in y)
		int[] dx = {0, -1, 0, 1};
		int[] dy = {-1, 0, 1, 0};

		//shortcuts
		World cur = worlds.get(currentWorld);
		int wx = player.wx(), wy = player.wy();
		
		for(int i = 0; i < times; i++) {
			//resulting coordinates
			int rx = wx + dx[direction];
			int ry = wy + dy[direction];
			
			//if player can't move that way, stop where it is
			if(!cur.doorAt(wx, wy, dx[direction], dy[direction]))
				break;
			
			//otherwise, move
			currentRoom().onExit(player);	//onExit of current Room
			//change room
			player.setWx(rx);
			player.setWy(ry);		
			currentRoom().onEnter(player, this);	//onEnter of new Room
		}
		
		if(Console.<Boolean>rsetting("maponmove"))	//whether or not to redraw map after each movement
			draw();
	}
}
