import java.util.*;

public class Game {
	private Player p;
	private ArrayList<World> worlds;
	private Map<String, GameObjectBase> definitions;
	
	public Game() {
		worlds = new ArrayList<World>();
		definitions = new HashMap<String, GameObjectBase>();
	}
	
	public void setPlayer(Player p) {
		this.p = p;
	}
	public void addWorld(World w) {
		worlds.add(w);
	}
	public void addDefinition(Parser p) {
		String type = p.next();
		GameObjectBase gob = GameObjectBase.create(type, p);
		
		if(definitions.containsKey(gob.key()))
			System.out.printf("Warning: Redefinition of \"%s\" from %s to %s\n", gob.key(), definitions.get(gob.key()).toString(), gob.toString());
	}
}
