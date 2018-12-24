
public class MultiplicationExpression extends SimpleCompoundExpression {
	/**
	 * Constructs a new MultiplicationExpression.
	 */
	public MultiplicationExpression(boolean withJavaFXControls) {
		super(withJavaFXControls);
	}
	/**
	 * creates a copy of the tree as deep as possible, rooted at this node
	 * @return the deep copy
	 */
	public Expression deepCopy() {
		SimpleCompoundExpression e = new MultiplicationExpression(withJavaFXControls);
		e.addChildren(children);
		return e;
	}
	/**
	 * Determines if the given expression is an MultiplicationExpression
	 * @param e the expression in question
	 * @return true if e is an MultiplicationExpression
	 */
	public boolean sameClass(Expression e) {
		return e instanceof MultiplicationExpression;
	}
	/**
	 * Gets the times symbol.
	 * @return "*"
	 */
	public String getSymbol() {
		return "·";
	}
}
