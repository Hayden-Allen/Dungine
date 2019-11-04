
public abstract class ParameterBase {
	private String key;	//identifier
	private boolean readOnly;	//whether or not this can be modified
	
	public ParameterBase(String key) {
		this.key = key.toUpperCase();	//automatically normalize naming convention
		this.readOnly = false;
	}
	
	public String key() {
		return key;
	}
	public void lock() {
		readOnly = true;
	}
	public void unlock() {
		readOnly = false;
	}
	public boolean readOnly() {
		return readOnly;
	}
}
