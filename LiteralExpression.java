import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class LiteralExpression implements Expression {
	final private String symbol;
	private CompoundExpression parent;
	private HBox node;
	final boolean withJavaFXControls;
	public LiteralExpression(String symbol, boolean withJavaFXControls) {
		this.symbol = symbol;
		this.withJavaFXControls = withJavaFXControls;
		if(withJavaFXControls) {
			node = new HBox();
			Label label = new Label(symbol);
			label.setFont(ExpressionEditor.FONT);
			label.setTextFill(Color.BLACK);
			node.getChildren().add(label);
		}
	}
	/**
	 * Returns the expression's parent.
	 * @return the expression's parent
	 */
	public CompoundExpression getParent() {
		return parent;
	}
	/**
	 * Sets the parent be the specified expression.
	 * @param parent the CompoundExpression that should be the parent of the target object
	 */
	public void setParent(CompoundExpression parent) {
		this.parent = parent;
	}
	/**
	 * Creates and returns a deep copy of the expression.
	 * The entire tree rooted at the target node is copied, i.e.,
	 * the copied Expression is as deep as possible.
	 * @return the deep copy
	 */
	public Expression deepCopy() {
		return new LiteralExpression(symbol, withJavaFXControls);
	}
	/**
	 * Recursively flattens the expression as much as possible
	 * throughout the entire tree. Specifically, in every multiplicative
	 * or additive expression x whose first or last
	 * child c is of the same type as x, the children of c will be added to x, and
	 * c itself will be removed. This method modifies the expression itself.
	 */
	public void flatten() {
	}
	/**
	 * Creates a String representation by recursively printing out (using indentation) the
	 * tree represented by this expression, starting at the specified indentation level.
	 * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
	 * @return a String representation of the expression tree.
	 */	
	public String convertToString(int indentLevel) {
		StringBuffer sb = new StringBuffer("");
		Expression.indent(sb, indentLevel);
		sb.append(symbol);
		return sb.toString();
	}
	/**
	 * Returns the JavaFX node associated with this expression.
	 * It is a label with just the symbol.
	 * @return the JavaFX node associated with this expression.
	 */
	public Node getNode() {
		return node;
	}
}
