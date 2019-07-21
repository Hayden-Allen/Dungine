
public abstract class GameObjectBase {
	protected String key;
	
	public abstract GameObjectBase create(Parser p, Game g);
	
	public static boolean isAttrib(String s) {
		return s.equals(Console.keywords.get("Type.String")) || s.equals(Console.keywords.get("Type.Integer")) || s.equals(Console.keywords.get("Type.Float"));
	}
	public static boolean isList(String s) {
		return s.equals(Console.keywords.get("Type.List"));
	}
	public static boolean isObject(String s) {
		return s.equals(Console.keywords.get("Type.Object"));
	}
	public static GameObjectBase create(String type, Parser p, Game g) {
		String key = p.next();
		if(isAttrib(type)) {
			if(type.equals(Console.keywords.get("Type.String")))
				return new GameObjectAttribute<String>(key, p.nextString());
			if(type.equals(Console.keywords.get("Type.Integer")))
				return new GameObjectAttribute<Integer>(key, Integer.parseInt(p.next()));	//TODO: make compatible with definitions
		}
		if(isList(type)) {
			
		}
		if(isObject(type)) {
			return Console.<GameObject>template(key).create(p, g);
		}
		return null;
	}
	public String key() {
		return key;
	}
}
