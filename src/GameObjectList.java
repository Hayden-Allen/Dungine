import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Arrays;

public class GameObjectList<E extends GameObjectBase> extends GameObjectBase {
	private ArrayList<E> elements, accepted;
	
	@SafeVarargs
	public GameObjectList(String key, E...elements) {
		this.key = key;
		accepted = new ArrayList<E>(Arrays.asList(elements));
	}
	public GameObjectList(String key, ArrayList<E> elements) {
		this.key = key;
		this.elements = elements;
	}
	
	public ArrayList<E> elements() {
		return elements;
	}
	private boolean allowed(String key) {
		for(GameObjectBase go : accepted)
			if(go.key().equals(key))
				return true;
		return false;
	}
	@SuppressWarnings("unchecked")
	public GameObjectList<E> create(Parser p, Game g) {
		Parser p2 = new Parser(p.nextBlock(), 0);
		ArrayList<E> el = new ArrayList<E>();
		
		while(p2.hasNext()) {
			String key = p2.next();
			if(key.isEmpty())	//TODO why
				break;
			else if(key.charAt(0) == '%')
				el.add(g.definition(key.substring(1)));
			else if(!allowed(key))
				throw new InputMismatchException("Element \"" + key + "\" does not belong in list \"" + this.key + '\"');
			else
				el.add((E)Console.template(key).create(p2, g));
		}
		return new GameObjectList<E>(key, el);
	}
	@Override
	public String toString() {
		String s = key + "[";
		if(elements != null)
			for(GameObjectBase gob : elements)
				s += gob.toString() + " ";
		return s.substring(0, s.length() - 1) + "]";
	}
}
