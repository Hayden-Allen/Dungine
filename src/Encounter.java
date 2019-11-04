import java.util.*;

public class Encounter {	//a battle that starts when the Player enters a Room containing Enemies
	private Player p;	//the Player that enters the Room
	private ArrayList<Enemy> enemies;	//list of Enemies in the Room
	//spoils from the battle
	private int gold;
	private ArrayList<Item> items;
	
	public Encounter(Player p, ArrayList<Enemy> enemies) {
		this.p = p;
		this.enemies = enemies;
		gold = 0;
		items = new ArrayList<Item>();
	}
	
	private void sort(ArrayList<Character> characters) {
		//sort list of Characters by speed
		//highest speed moves first
		for(int i = 0; i < characters.size(); i++)
			for(int j = i + 1; j < characters.size(); j++) {
				Character one = characters.get(i), two = characters.get(j);
				if(one.spd() < two.spd()) {
					characters.set(i, two);
					characters.set(j, one);
				}
			}
	}
	public ArrayList<Enemy> enemies(){
		return enemies;
	}
	public ArrayList<Item> items(){
		return items;
	}
	public Player p() {
		return p;
	}
	public void addGold(int g) {
		gold += g;
	}
	public void addItem(Item i) {
		items.add(i);
	}
	private String onStart() {	//String printed when the Encounter begins
		String s = Console.rsetting("encounter.start") + " ";	//start of sentence
		//maps Enemy names to number of occurrences
		HashMap<String, Integer> times = new HashMap<String, Integer>();
		for(Enemy e : enemies) {
			String name = e.name();
			if(!times.containsKey(name))	//if new, seen once
				times.put(name, 1);
			else	//if not new, seen n + 1 times
				times.put(name, times.get(name) + 1);
		}
		
		for(String name : times.keySet()) {	//for each name, go through and print it plus # of occurrences
			int n = times.get(name);
			//only print s if more than one occurrence of name
			s += n + " " + name + (n > 1 ? "s" : "") + " and ";
		}
		return s.substring(0, s.length() - 5) + "!";	//remove trailing "and"
	}
	private ArrayList<Character> initCharacters() {
		ArrayList<Character> temp = new ArrayList<Character>(enemies);
		temp.add(p);
		sort(temp);
		return temp;
	}
	public ArrayList<Enemy> run(Game g) {	//starts the Encounter
		Scanner in = g.in();	//for getting player input
		
		//create list of Enemies + player and sort them to determine turn order
		ArrayList<Character> characters = initCharacters();

		Console.logn(onStart());	//print start String
		
		//list of possible commands
		ParameterList cmds = Console.getParam("con.cmd.encounter");
		
		while(p.isAlive() && !enemies.isEmpty()) {	//while Player is alive and Enemies remain
			for(Character cur : characters){	//go through all Characters in speed order
				if(cur == p) {	//player's turn
					boolean success = false;	//whether or not the player used their turn
					while(!success) {
						Console.logn(Console.rsetting("encounter.attack"));	//input prompt
						System.out.print(Console.<String>rsetting("input"));
						String answer = in.nextLine().trim().toLowerCase();	//get input
						
						//split input into command and arguments
						boolean space = answer.contains(" ");
						String cmd = answer.substring(0, space ? answer.indexOf(' ') : answer.length());
						if(space)
							answer = answer.substring(answer.indexOf(' ') + 1);
						
						//player has used their turn if the command is hit and it was successfully carried out
						success = Console.efOp(cmds.getElement(cmd), answer, this) && cmd.equals("hit");
					}
					//account for killed Enemies
					characters = initCharacters();
				}
				else if(characters.contains(cur))	//Enemy's turn. if killed by player earlier in this round, do nothing
					if(!cur.attack(p))	//if player doesn't survive attack
						break;	//end the battle
			}
		}
			
		if(!p.isAlive()) {	//if player died
			Console.logn(Console.rsetting("encounter.defeat"));	//print defeat String
			return enemies;
		}
		
		Console.logn(Console.rsetting("encounter.victory"));	//print victory String
		
		p.addGold(gold);	//give player sum of gold from all defeated Enemies
		Console.logn(Console.rsetting("encounter.money"), gold);	//tell player how much gold they got
		
		cmds = Console.getParam("con.cmd.encounter.post");	//list of commands for picking Items
		
		boolean itemSuccess = items.isEmpty();	//if no Items were dropped, ignore
		if(!itemSuccess) 	//if Items were dropped, print them out
			Console.printItemBox("encounter", "Available Items", items, null);
		
		while(!itemSuccess) {	//while player is still picking up Items
			System.out.print(Console.<String>rsetting("input"));	//prompt for input
			String answer = in.nextLine().trim().toLowerCase();	//get input
			//split input into command and arguments
			boolean space = answer.contains(" ");
			String cmd = answer.substring(0, space ? answer.indexOf(' ') : answer.length());
			if(space)
				answer = answer.substring(answer.indexOf(' ') + 1);
		
			if(cmd.equalsIgnoreCase("leave"))	//player doesn't want to take any more Items
				break;
			
			Console.efOp(cmds.getElement(cmd), answer, this);	//perform specified command
			itemSuccess = items.isEmpty() || p.inv().isFull();	//if player can't take any more Items
		}
		return enemies;	//send back to Room so that killed Enemies can be removed
	}
}
