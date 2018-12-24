
public class AdditionExpression extends SimpleCompoundExpression {
	/**
	 * Constructs a new AdditionExpression.
	 */
	public AdditionExpression(boolean withJavaFXControls) {
		super(withJavaFXControls);
	}
	/**
	 * creates a copy of the tree as deep as possible, rooted at this node
	 * @return the deep copy
	 */
	public Expression deepCopy() {
		SimpleCompoundExpression e = new AdditionExpression(withJavaFXControls);
		e.addChildren(children);
		return e;
	}
	/**
	 * Determines if the given expression is an AdditionExpression
	 * @param e the expression in question
	 * @return true if e is an AdditionExpression
	 */
	public boolean sameClass(Expression e) {
		return e instanceof AdditionExpression;
	}
	/**
	 * Gets the plus symbol.
	 * @return "+"
	 */
	public String getSymbol() {
		return "+";
	}
}
