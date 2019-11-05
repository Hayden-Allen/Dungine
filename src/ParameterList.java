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
	public ParameterList getPath(String path, boolean write) {	//get element at given path
		path = path.toUpperCase();
		return followPath(path, path, write);	//recurse
	}
	//path relative to this node, full path, whether or not this is a write operation
	private ParameterList followPath(String path, String fullPath, boolean write) {
		if(path.isEmpty())	//end of path reached
			return this;
		
		int end = path.contains(".") ? path.indexOf(".") : path.length();	//path of next node
		String cur = path.substring(0, end);	//name of child node to get
		ParameterList pl = this.<ParameterList>getElement(cur);	//get next node in path

		//for debug printing
		int fpend = fullPath.lastIndexOf(path) == 0 ? fullPath.length() : fullPath.lastIndexOf(path) - 1;	//end of full path
		String fp = fullPath.substring(0, fpend);	//adjusted path
		
		if(pl == null)	//child doesn't exist
			throw new Error(String.format("Parameter %s has no element %s", fp, cur));
		if(write && pl.readOnly()) {	//restricted access
			System.out.printf("WARNING: Cannot edit %s; it is read only\n", fullPath);
			return null;
		}
		//follow path from child node
		return pl.followPath(end + 1 > path.length() ? "" : path.substring(end + 1), fullPath, write);
	}
}
