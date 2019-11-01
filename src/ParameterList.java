import java.util.ArrayList;
import java.util.Arrays;

public class ParameterList extends ParameterBase {
	private ArrayList<ParameterBase> elements;
	private boolean noNew;
	
	public ParameterList(String key, ParameterBase...elements) {
		super(key);
		this.elements = 
			new ArrayList<ParameterBase>(Arrays.asList(elements));
		noNew = false;
	}
	
	public void lockAdditions() {
		noNew = true;
		for(ParameterBase pb : elements)
			if(pb instanceof ParameterList)
				((ParameterList) pb).lockAdditions();
	}
	public boolean noNew() {
		return noNew;
	}
	public void addElement(ParameterBase pb) {
		elements.add(pb);
	}
	public void removeElement(String key) {
		for(int i = 0; i < elements.size(); i++)
			if(elements.get(i).key.equals(key)) {
				elements.remove(i);
				return;
			}
	}
	@SuppressWarnings("unchecked")
	public <E extends ParameterBase> E getElement(String key) {
		for(ParameterBase pb : elements)
			if(pb.key.equalsIgnoreCase(key))
				return (E)pb;
		return null;
	}
	@Override
	public String toString() {
		String s = key + ": [";
		for(ParameterBase pb : elements)
			s += pb.toString() + ", ";
		if(s.contains(","))
			return s.substring(0, s.lastIndexOf(",")) + "]";
		return s + "]";
	}
	public ParameterList getPath(String path, boolean write) {
		path = path.toUpperCase();
		return followPath(path, path, write);
	}
	private ParameterList followPath(String path, String fullPath, boolean write) {
		if(path.isEmpty())
			return this;
		
		int end = path.contains(".") ? path.indexOf(".") : path.length();
		String cur = path.substring(0, end);
		int fpend = fullPath.lastIndexOf(path) == 0 ? fullPath.length() : fullPath.lastIndexOf(path) - 1;
		ParameterList pl = this.<ParameterList>getElement(cur);
		
		String fp = fullPath.substring(0, fpend);
		
		if(pl == null)
			throw new Error(String.format("Parameter %s has no element %s", fp, cur));
		if(write && pl.readOnly) {
			System.out.printf("WARNING: Cannot edit %s%s; it is read only\n", fp, fp.equals(cur) ? "" : "." + cur);
			return null;
		}
		return pl.followPath(end + 1 > path.length() ? "" : path.substring(end + 1), fullPath, write);
	}
}
