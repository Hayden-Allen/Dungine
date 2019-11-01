import java.util.ArrayList;
import java.util.Arrays;

public class GameObject extends GameObjectBase {
	private ArrayList<GameObjectBase> elements;
	
	public GameObject(String key, GameObjectBase...elements) {
		super(key);
		this.elements = new ArrayList<GameObjectBase>(Arrays.asList(elements));
	}
	public GameObject(String key, ArrayList<GameObjectBase> elements) {
		super(key);
		this.elements = elements;
	}
	
	public GameObject create(Parser p) {
		Parser p2 = new Parser(p.file(), p.nextBlock());
		ArrayList<GameObjectBase> temp = new ArrayList<GameObjectBase>(elements);
		
		while(p2.hasNext()) {
			String key = p2.next();
			if(key.isEmpty())
				continue;
			GameObjectBase element = element(key);
		
			if(element == null)
				throw new Error("GameObject \"" + this.key + "\" has no element \"" + key + "\".");
			
			temp.remove(element);
			temp.add(element.create(p2));
		}
		return new GameObject(key, temp);
	}
	
	@SuppressWarnings("unchecked")
	public <E extends GameObjectBase> E element(String key) {
		for(GameObjectBase gob : elements)
			if(gob.key.equals(key))
				return (E)gob;
		return null;
	}
	public <E> E attribute(String key) {
		GameObjectAttribute<E> goa = this.<GameObjectAttribute<E>>element(key);
		if(goa != null)
			return goa.value();
		return null;
	}
	public <E extends GameObjectBase> GameObjectList<E> list(String key) {
		return this.<GameObjectList<E>>element(key);
	}
	public GameObject object(String key) {
		return this.<GameObject>element(key);
	}
	
	public void add(GameObjectBase gob) {
		this.elements.add(gob);
	}
	@Override
	public String toString() {
		String s = key + ":{";
		for(GameObjectBase gob : elements)
			s += gob.toString() + ","; 
			
		if(elements.size() > 0)
			s = s.substring(0, s.length() - 1);
		return s + "}";
	}
}
