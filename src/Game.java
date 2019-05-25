import java.util.*;

public class Game {
	private ArrayList<World> worlds;
	private Set<String> imports, reads;
	private Map<String, GameObjectBase> definitions;
	
	public Game() {
		worlds = new ArrayList<World>();
		imports = new HashSet<String>();
		reads = new HashSet<String>();
		definitions = new HashMap<String, GameObjectBase>();
	}
	
	public Set<String> imports(){
		return imports;
	}
	public Set<String> reads(){
		return reads;
	}
	public void addDefinition(Parser p) {
		String type = p.next();
		GameObjectBase gob = GameObjectBase.create(type, p);
		
		if(definitions.containsKey(gob.key()))
			System.out.printf("Warning: Redefinition of \"%s\" from %s to %s\n", gob.key(), definitions.get(gob.key()).toString(), gob.toString());
	}
}
