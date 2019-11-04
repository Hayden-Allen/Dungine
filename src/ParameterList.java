import java.util.ArrayList;
import java.util.Arrays;

public class ParameterList extends ParameterBase {	//stores a list of Parameters and other ParameterLists
	private ArrayList<ParameterBase> elements;	//stored Parameters and ParameterLists
	private boolean noNew;	//whether or not new elements can be added to the list
	
	public ParameterList(String key, ParameterBase...elements) {
		super(key);
		this.elements = new ArrayList<ParameterBase>(Arrays.asList(elements));
		noNew = false;
	}
	
	public void lockAdditions() {	//prevent new values from being added to this list or any of its elements
		noNew = true;
		for(ParameterBase pb : elements)
			if(pb instanceof ParameterList)
				((ParameterList) pb).lockAdditions();
	}
	public boolean noNew() {
		return noNew;
	}
	public void addElement(ParameterBase pb) {	//add element to list
		elements.add(pb);
	}
	public void removeElement(String key) {	//remove element from list
		for(int i = 0; i < elements.size(); i++)
			if(elements.get(i).key().equals(key)) {
				elements.remove(i);
				return;
			}
	}
	@SuppressWarnings("unchecked")
	public <E extends ParameterBase> E getElement(String key) {	//get element with given name
		for(ParameterBase pb : elements)
			if(pb.key().equalsIgnoreCase(key))
				return (E)pb;
		return null;
	}
	@Override
	public String toString() {	//format for output
		String s = key() + ": [";	//open list
		for(ParameterBase pb : elements)	//all elements separated by commas
			s += pb.toString() + ", ";
		if(s.contains(","))	//remove trailing comma
			return s.substring(0, s.lastIndexOf(",")) + "]";
		return s + "]";	//close list
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
		if(write && pl.readOnly()) {
			System.out.printf("WARNING: Cannot edit %s%s; it is read only\n", fp, fp.equals(cur) ? "" : "." + cur);
			return null;
		}
		return pl.followPath(end + 1 > path.length() ? "" : path.substring(end + 1), fullPath, write);
	}
}
