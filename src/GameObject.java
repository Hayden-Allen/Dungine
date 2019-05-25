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
	
	public GameObjectBase element(String key) {
		for(GameObjectBase gob : elements)
			if(gob.key().equals(key))
				return gob;
		throw new InputMismatchException("GameObject " + key() + " has no element " + key);
	}
	public GameObject create(Parser p) {	//TODO
		ArrayList<GameObjectBase> temp = new ArrayList<GameObjectBase>(elements);
		
		p.requireParentheses();
		
		
		
		return new GameObject(key, temp);
	}
}
