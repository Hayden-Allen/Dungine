
public class Parameter<E> extends ParameterBase {
	private E value;
	
	public Parameter(String key, E value) {
		super(key);
		this.value = value;
	}
	
	public Parameter<?> create(Parser p){
		if(value instanceof String)
			return new Parameter<String>(key, p.nextString());
		if(value instanceof Integer)
			return new Parameter<Integer>(key, p.nextInt());
		return null;
	}
	public E value() {
		return value;
	}
	@Override
	public String toString() {
		String valueString = (value instanceof GameFunction ? "(fn)" : value.toString());
		if(value instanceof String)
			valueString = "\"" + valueString + "\"";
		return key + ": " + valueString;
	}
}
