import java.util.*;

public class Encounter {
	private Player p;
	private ArrayList<Enemy> enemies;
	
	public Encounter(Player p, ArrayList<Enemy> enemies) {
		this.p = p;
		this.enemies = enemies;
	}
	private void sort(ArrayList<Character> characters) {
		for(int i = 0; i < characters.size(); i++)
			for(int j = i + 1; j < characters.size(); j++) {
				Character one = characters.get(i), two = characters.get(j);
				if(one.spd() < two.spd()) {
					Character temp = one;
					one = two;
					two = temp;
				}
			}
	}
	public ArrayList<Enemy> run(Game g) {
		Scanner in = g.in();
		ArrayList<Character> characters = new ArrayList<Character>(enemies);
		characters.add(p);
		sort(characters);
		
		int gold = 0;
		ArrayList<Item> items = new ArrayList<Item>();
		
		while(p.isAlive() && !enemies.isEmpty()) {
			Iterator<Character> it = characters.iterator();
			while(it.hasNext()){	//in speed order
				Character cur = it.next();
				if(cur == p) {
					boolean success = false;
					while(!success) {
						Console.logn(Console.rsetting("encounter.attack"));
						System.out.print(Console.<String>rsetting("input"));
						String answer = in.nextLine().trim().toLowerCase();
						if(answer.equals("run")) {
							if(characters.get(0) == p) {	//player is faster than all monsters
								Console.logn(Console.rsetting("encounter.flee.success"));
								break;
							}
							else
								Console.logn(Console.rsetting("encounter.flee.fail"));
						}
						
						boolean space = answer.contains(" ");
						String cmd = answer.substring(0, space ? answer.indexOf(' ') : answer.length());
						
						if(space)
							answer = answer.substring(answer.indexOf(' ') + 1);
						switch(cmd) {
						case "list":
							for(Enemy e : enemies)
								Console.logn("\t>%s", e.name());
						break;
						case "hit":
							String targetName = answer;
							int index = 0;
	
							if(answer.contains(" ")) {
								try {
									index = Integer.parseInt(answer.substring(answer.indexOf(' ') + 1));
								}
								catch(Exception e) {
									Console.logn(Console.rsetting("invalid.target.index"));
								}
							}
							
							int valid = 0;
							Enemy target = null;
							for(Enemy e : enemies) {
								if(e.name().equalsIgnoreCase(targetName)) {
									if(valid == index) {
										target = e;
										break;
									}
									valid++;
								}
							}
							
							if(target == null)
								Console.logn(Console.rsetting("invalid.target.name"));
							else if(!p.attack(target)) {
								enemies.remove(target);
								if(target.goldDrop())
									gold += target.gold();
								if(target.armorDrop())
									items.add(target.equippedArmor);
								if(target.weaponDrop())
									items.add(target.equippedWeapon);
							}
						}					
					break;
					}
				}
			}
		}
		p.addGold(gold);
		Console.logn(Console.rsetting("encounter.money"), gold);
		Console.logn(Console.rsetting("encounter.victory"));
		return enemies;
	}
}
