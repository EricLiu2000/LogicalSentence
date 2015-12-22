package sentence;

public interface LogicalExpression {
	public String getExpression();
	public int getNumVars();
	public boolean valid();
	public boolean satisfiable();
	public boolean contingent();
	public int equivalent(LogicalExpression expression);
	public int entails(LogicalExpression expression);
	public boolean[] getTruthTable();
}
