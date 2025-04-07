
public class Assignment<X, Y> {
	private final X x;
	private final Y y;
	public Assignment(X lhs, Y rhs) {
		this.x = lhs;
		this.y = rhs;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.x+":="+this.y;
	}
}
