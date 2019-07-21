import java.util.*;

public class GameObject extends GameObjectBase {
	private ArrayList<GameObjectBase> elements;
	
	private GameObject(String key, ArrayList<GameObjectBase> elements) {
		this.key = key;
		this.elements = elements;
	}
	public GameObject(String key, GameObjectBase...elements) {
		this.key = key;
		this.elements = new ArrayList<GameObjectBase>(Arrays.asList(elements));
	}
	
	@SuppressWarnings("unchecked")
	public <E extends GameObjectBase> E element(String key) {
		for(GameObjectBase gob : elements)
			if(gob.key().equals(key))
				return (E)gob;
		throw new InputMismatchException("GameObject " + key() + " has no element " + key);
	}
	@Override
	
	public GameObject create(Parser p, Game g) {
		Parser p2 = new Parser(p.nextBlock(), 0);
		
		Map<String, GameObjectBase> temp = new HashMap<String, GameObjectBase>();
		for(GameObjectBase gob : elements)
			temp.put(gob.key(), gob);
		
		while(p2.hasNext()) {
			String key = p2.next();
			if(key.isEmpty())	//TODO why
				break;
			//System.out.println(key);
			if(!temp.containsKey(key))
				throw new InputMismatchException("Object \"" + this.key() + "\" has no element \"" + key + "\"");
			temp.put(key, temp.get(key).create(p2, g));
			//System.out.println(temp.get(key));
		}
		
		return new GameObject(key, new ArrayList<GameObjectBase>(temp.values()));
	}	
	@Override
	public String toString() {
		String s = key + "{";
		for(GameObjectBase gob : elements)
			s += gob.toString() + " ";
		return s.substring(0, s.length() - 1) + "}";
	}
}
