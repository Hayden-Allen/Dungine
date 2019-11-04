import java.util.ArrayList;
import java.util.Arrays;

//a list of a certain type of templates
public class GameObjectList<E extends GameObjectBase> extends GameObjectBase {
	private ArrayList<E> accepted, elements;	//accepted templates, stored templates
	
	@SafeVarargs
	public GameObjectList(String key, E...accepted) {	//used for initial creation
		super(key);
		this.accepted = new ArrayList<E>(Arrays.asList(accepted));
		this.elements = new ArrayList<E>();
	}
	private GameObjectList(String key, ArrayList<E> accepted, ArrayList<E> elements) {	//used for creation from a template GameObjectList
		super(key);
		this.accepted = accepted;
		this.elements = elements;
	}
	
	@SuppressWarnings("unchecked")
	public GameObjectList<E> create(Parser p) {
		Parser p2 = new Parser(p.file(), p.nextBlock());	//get next block of input
		ArrayList<E> temp = new ArrayList<E>();	//list of elements
		while(p2.hasNext()) {
			String key = p2.next();
			if(key.isEmpty())	//TODO why
				continue;
			
			E element = acceptedElement(key);
			if(element == null)	//if key is not accepted, stop program
				throw new Error("GameObjectList \"" + this.key + "\" has no element \"" + key + "\".");
			//otherwise create element from template and add to list
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
	public String toString() {	//languaget string for this list
		String s = key + ":[";	//open list
		for(GameObjectBase gob : elements)	//add all elements separated by commas
			s += gob.toString() + ",";
		if(s.contains(","))	//remove trailing comma
			s = s.substring(0, s.lastIndexOf(","));
		return s + "]";	//close list
	}
}
