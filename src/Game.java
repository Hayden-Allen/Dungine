import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
	private ArrayList<World> worlds;
	private Map<String, GameObjectBase> definitions;
	private Player p;
	private int current = 0;
	
	public Game() {
		worlds = new ArrayList<World>();
		definitions = new HashMap<String, GameObjectBase>();
	}
	
	public ArrayList<World> worlds(){
		return worlds;
	}
	public Player player() {
		return p;
	}
	public void addDefinition(Parser p) {
		String type = p.next();
		GameObjectBase gob = GameObjectBase.create(type, p);
		
		if(definitions.containsKey(gob.key()))
			System.out.printf("Warning: Redefinition of \"%s\" from %s to %s\n", gob.key(), definitions.get(gob.key()).toString(), gob.toString());
	}
	public void addWorld(World w) {
		worlds.add(w);
	}
	public void print() {
		worlds.get(current).print(p);
		
	}
	public void setPlayer(Player p) {
		this.p = p;
	}
}
