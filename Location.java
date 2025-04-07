
public class Location {
	public String name;
	public boolean isIntermediate;
	
	public Location(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.isIntermediate = false;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (this.isIntermediate)
			return "\"" + this.name + "\" [shape=doublecircle ,style=dotted]";
		else
			return "\"" + this.name + "\" [shape=doublecircle ,style=solid]";
	}
}
