import java.util.ArrayList;

public class Method {
	public String name;
	public int noOfParam; 
	public boolean isOutputMethod;
	public ArrayList<Parameter> params;
	
	public Method(String name, boolean isOutput) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.noOfParam = 0;
		this.isOutputMethod = isOutput;
		this.params = new ArrayList<Parameter>();
	}
	
	public void addParameter(Parameter param) {
		this.params.add(param);
		this.noOfParam += 1;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = "";
		if (noOfParam > 0) {
			for(int i=0; i<this.params.size(); i++) {
				str += (i==this.params.size()-1) ? this.params.get(i).toString()
						: this.params.get(i).toString() + ", ";
			}
		}
		return this.name +"[" + str + "]";
	}
}
