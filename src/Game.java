import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
	private ArrayList<World> worlds;
	public Map<String, GameObjectBase> definitions;
	private Player p;
	private int current = 0;
	
	public Game() {
		worlds = new ArrayList<World>();
		definitions = new HashMap<String, GameObjectBase>();
	}
	
	@SuppressWarnings("unchecked")
	public <E extends GameObjectBase> E definition(String key) {
		return (E)definitions.get(key);
	}
	public World currentWorld() {
		return worlds.get(current);
	}
	public Room currentRoom() {
		return currentWorld().rooms().get(p.y()).get(p.x());
	}
	public Player player() {
		return p;
	}
	public void addDefinition(Parser p) {
		String type = p.next(), key = "";
		if(GameObjectBase.isObject(type))
			key = p.next();
		
		GameObjectBase gob = GameObjectBase.create(type, p, this);
		
		if(definitions.containsKey(gob.key()))
			System.out.printf("Warning: Redefinition of \"%s\" from %s to %s\n", gob.key(), definitions.get(gob.key()).toString(), gob.toString());
		
		definitions.put((!key.isEmpty() ? key : gob.key()), gob);
	}
	public void addWorld(World w) {
		worlds.add(w);
	}
	public void move(int dx, int dy) {
		int sign = (int)Math.signum(dx);
		dx = Math.abs(dx);
		while(dx-- > 0 && currentWorld().doorAt(p.x(), p.y(), sign > 0 ? Room.RIGHT : Room.LEFT))
			p.addX(sign);
			
		sign = (int)Math.signum(dy);
		dy = Math.abs(dy);
		while(dy-- > 0 && currentWorld().doorAt(p.x(), p.y(), sign > 0 ? Room.DOWN : Room.UP))
			p.addY(sign);
	}
	public void print() {
		worlds.get(current).print(p);
		
	}
	public void setPlayer(Player p) {
		this.p = p;
	}
}
