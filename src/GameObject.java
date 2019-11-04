import java.util.ArrayList;
import java.util.Arrays;

//class that acts as a template for many different types of objects
//used in creating those objects from parsed input
public class GameObject extends GameObjectBase {
	private ArrayList<GameObjectBase> elements;	//list of attributes, lists, and objects that this object contains
	
	public GameObject(String key, GameObjectBase...elements) {	//identifier and list of elements. Used on intial creation
		super(key);
		this.elements = new ArrayList<GameObjectBase>(Arrays.asList(elements));
	}
	public GameObject(String key, ArrayList<GameObjectBase> elements) {	//identifier and list of elements. Used to create from a template GameObject
		super(key);
		this.elements = elements;
	}
	
	public GameObject create(Parser p) {	//create from file input
		Parser p2 = new Parser(p.file(), p.nextBlock());	//parse next block of input
		ArrayList<GameObjectBase> temp = new ArrayList<GameObjectBase>(elements);	//list of elements with default values
		
		while(p2.hasNext()) {
			String key = p2.next();
			if(key.isEmpty())
				continue;
			GameObjectBase element = element(key);
		
			if(element == null)	//if this object does not contain element with given identifier
				throw new Error("GameObject \"" + this.key + "\" has no element \"" + key + "\".");
			
			//otherwise, replace the default value with specified value
			temp.remove(element);
			temp.add(element.create(p2));
		}
		return new GameObject(key, temp);	//same identifier, customized elements
	}
	
	@SuppressWarnings("unchecked")
	public <E extends GameObjectBase> E element(String key) {	//element from list with given identifier, casted to given type
		for(GameObjectBase gob : elements)
			if(gob.key.equals(key))
				return (E)gob;
		return null;
	}
	public <E> E attribute(String key) {	//value of attribute with given identifier of given type
		GameObjectAttribute<E> goa = this.<GameObjectAttribute<E>>element(key);
		if(goa != null)
			return goa.value();
		return null;
	}
	public <E extends GameObjectBase> GameObjectList<E> list(String key) {	//list of given type with given identifier
		return this.<GameObjectList<E>>element(key);
	}
	public GameObject object(String key) {	//object with given identifier
		return this.<GameObject>element(key);
	}
	
	@Override
	public String toString() {	//format as a language String; this object could be recreated from returned String
		String s = key + ":{";	//open object
		for(GameObjectBase gob : elements)	//add all elements, separated by commas
			s += gob.toString() + ","; 
			
		if(elements.size() > 0)	//if any elements added, remove trailing omma
			s = s.substring(0, s.length() - 1);
		return s + "}";	//close object
	}
}
