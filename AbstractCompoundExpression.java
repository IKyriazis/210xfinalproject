import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public abstract class AbstractCompoundExpression implements CompoundExpression{
	private CompoundExpression parent;
	protected HBox node;
	final protected boolean withJavaFXControls;
	/**
	 * Creates a new AbstractCompoundExpression.
	 */
	public AbstractCompoundExpression(boolean withJavaFXControls) {
		if(withJavaFXControls) {
			node = new HBox();
		}
		this.withJavaFXControls = withJavaFXControls;
	}
	/**
	 * gets the parent expression
	 * @return the parent expression
	 */
	public CompoundExpression getParent() {
		return parent;
	}
	/**
	 * sets the parent to be the given CompoundExpression
	 * @param parent the new parent expression
	 */
	public void setParent(CompoundExpression parent) {
		this.parent = parent;
	}
	/**
	 * creates a copy of the tree as deep as possible, rooted at this node
	 * @return the deep copy
	 */
	public abstract Expression deepCopy();
	/**
	 * Creates a string representation of this expression.
	 * The string will be the symbol of this type of expression
	 * with all children indented on subsequent lines.
	 * @param indentLevel how far the entire string is indented
	 * @return the string representation of this expression
	 */
	public String convertToString(int indentLevel) {
		StringBuffer result = new StringBuffer("");
		Expression.indent(result, indentLevel);
		result.append(getSymbol()); // indented symbol of this operation
		appendChildren(result, indentLevel + 1);
		if(parent == null) {
			result.append("\n");
		}
		return result.toString();
		
	}
	/**
	 * Gets the operator symbol for the subclass.
	 * @return the operator symbol
	 */
	protected abstract String getSymbol();
	/**
	 * Appends string representations of this expression's children
	 * to the given string on subsequent lines.
	 * @param result the string to append the children to
	 * @param indentLevel how far the children are indented
	 */
	protected abstract void appendChildren(StringBuffer sb, int indentLevel);
	/**
	 * Recursively flattens the expression as much as possible.
	 * This method modifies the expression itself.
	 * The order of the subexpressions will not be changed.
	 */
	public abstract void flatten();
	/**
	 * Returns the JavaFX node associated with this expression.
	 * @return the JavaFX node associated with this expression.
	 */
	public Node getNode() {
		return node;
	}
	/**
	 * Gets a list of the subexpressions of this CompoundExpression.
	 * @return a list of the subexpressions
	 */
	public abstract List<Expression> getChildren();
}
