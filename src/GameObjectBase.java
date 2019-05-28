
public abstract class GameObjectBase {
	protected String key;
	
	public abstract GameObjectBase create(Parser p);
	
	private static boolean isAttrib(String s) {
		return s.equals(Console.keywords.get("Type.String")) || s.equals(Console.keywords.get("Type.Integer")) || s.equals(Console.keywords.get("Type.Float"));
	}
	private static boolean isList(String s) {
		return s.equals(Console.keywords.get("Type.List"));
	}
	private static boolean isObject(String s) {
		return s.equals(Console.keywords.get("Type.Object"));
	}
	public static GameObjectBase create(String type, Parser p) {
		if(isAttrib(type)) {
			String key = p.next();
			if(type.equals(Console.keywords.get("Type.String")))
				return new GameObjectAttribute<String>(key, p.nextString());
			if(type.equals(Console.keywords.get("Type.Integer")))
				return new GameObjectAttribute<Integer>(key, Integer.parseInt(p.next()));	//TODO: make compatible with definitions
		}
		if(isList(type)) {
			
		}
		return null;
	}
	public String key() {
		return key;
	}
}
