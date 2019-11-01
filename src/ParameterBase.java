
public abstract class ParameterBase {
	protected String key;
	protected boolean readOnly;
	
	public ParameterBase(String key) {
		this.key = key.toUpperCase();
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
