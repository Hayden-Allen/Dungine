
public class Parameter<E> extends ParameterBase {	//stores a value under a name. Used to create the registry
	private E value;	//stored data
	
	public Parameter(String key, E value) {
		super(key);
		this.value = value;
	}
	
	public E value() {
		return value;
	}
	@Override
	public String toString() {	//format for printing
		//if stored data is a lambda, replace it's toString() value with "(fn)"
		String valueString = (value instanceof GameFunction ? "(fn)" : value.toString());
		if(value instanceof String)	//surround String values with quotes
			valueString = "\"" + valueString + "\"";
		return key() + ": " + valueString;
	}
}
