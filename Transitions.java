import java.util.ArrayList;

public class Transitions {
	private Location fromLocation;
	private Location toLocation;
	private Method method;
	private ArrayList<Assignment> assignments;
	private String guard;
	
	public Transitions(Location fromLoc, Location toLoc, 
			Method method, ArrayList<Assignment> assignments,
			String guard) {
		this.fromLocation = fromLoc;
		this.toLocation = toLoc;
		this.method = method;
		this.assignments = assignments;
		this.guard = guard.replace("&", "&amp;");
	}
	
	
	@Override
	public String toString() {
		String str1 = "\"" + this.fromLocation.name + "\"" + " -> "
				+ "\"" + this.toLocation.name + "\"";
		
		String str2 = (this.guard == "") ? this.method.toString() 
				: this.method.toString() + "|" + "(" + this.guard + ")";
		
		String str3 = "";
		
		for(int i=0; i<assignments.size(); i++) {
			str3 += (i==assignments.size()-1) ? assignments.get(i).toString()
					: assignments.get(i) + ",";
		}
		
		String str = (str3 == "" ) ? str1 + " [label=<? " + str2 + ">]"
				: str1 + " [label=<? " + str2 + "<BR />" + "[" + str3 + "]>]";
		return str;
	}
}
