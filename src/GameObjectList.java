import java.util.ArrayList;
import java.util.Arrays;

public class GameObjectList<E extends GameObjectBase> extends GameObjectBase {
	private ArrayList<E> accepted, elements;
	
	@SafeVarargs
	public GameObjectList(String key, E...accepted) {
		super(key);
		this.accepted = new ArrayList<E>(Arrays.asList(accepted));
		this.elements = new ArrayList<E>();
	}
	private GameObjectList(String key, ArrayList<E> accepted, ArrayList<E> elements) {
		super(key);
		this.accepted = accepted;
		this.elements = elements;
	}
	
	@SuppressWarnings("unchecked")
	public GameObjectList<E> create(Parser p) {
		Parser p2 = new Parser(p.file(), p.nextBlock());
		ArrayList<E> temp = new ArrayList<E>();
		while(p2.hasNext()) {
			String key = p2.next();
			if(key.isEmpty())	//TODO why
				continue;
			
			E element = acceptedElement(key);
			if(element == null)
				throw new Error("GameObjectList \"" + this.key + "\" has no element \"" + key + "\".");		
			temp.add((E)element.create(p2));
		}
		return new GameObjectList<E>(key, accepted, temp);
	}
	private E acceptedElement(String key) {
		for(E e : accepted)
			if(e.key.equals(key))
				return e;
		return null;
	}
	public ArrayList<E> elements() {
		return elements;
	}
	@Override
	public String toString() {
		String s = key + ":[";
		for(GameObjectBase gob : elements)
			s += gob.toString() + ",";
		if(s.contains(","))
			s = s.substring(0, s.lastIndexOf(","));
		return s + "]";
	}
}
