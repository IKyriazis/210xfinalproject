import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ParentheticalExpression extends AbstractCompoundExpression {
	private Expression child;
	/**
	 * Creates a new ParentheticalExpression.
	 */
	public ParentheticalExpression(boolean withJavaFXControls) {
		super(withJavaFXControls);
	}
	/**
	 * Gets the parentheses symbol.
	 * @return "()"
	 */
	public String getSymbol() {
		return "()";
	}
	/**
	 * Sets the single child to be the given subexpression.
	 * @param subexpression the subexpression to add
	 */
	public void addSubexpression(Expression subexpression) {
		child = subexpression;
		subexpression.setParent(this);
		if(withJavaFXControls) {
			if(node.getChildren().isEmpty()) {
				final Label leftParenthesis = new Label("(");
				leftParenthesis.setFont(ExpressionEditor.FONT);
				leftParenthesis.setTextFill(Color.BLACK);
				final Label rightParenthesis = new Label(")");
				rightParenthesis.setFont(ExpressionEditor.FONT);
				rightParenthesis.setTextFill(Color.BLACK);
				node.getChildren().add(leftParenthesis);
				node.getChildren().add(child.getNode());
				node.getChildren().add(rightParenthesis);
			}
			else {
				node.getChildren().set(1, child.getNode());
			}
		}
	}
	/**
	 * creates a copy of the tree as deep as possible, rooted at this node
	 * @return the deep copy
	 */
	public Expression deepCopy() {
		CompoundExpression e = new ParentheticalExpression(withJavaFXControls);
		e.addSubexpression(child.deepCopy());
		return e;
	}
	/**
	 * Recursively flattens the expression as much as possible.
	 * This method modifies the expression itself.
	 * The order of the subexpressions will not be changed.
	 */
	public void flatten() {
		child.flatten();
	}
	/**
	 * Appends string representations of this expression's children
	 * to the given string on subsequent lines.
	 * @param result the string to append the children to
	 * @param indentLevel how far the children are indented
	 */
	public void appendChildren(StringBuffer sb, int indentLevel) {
		sb.append("\n");
		sb.append(child.convertToString(indentLevel));
	}
	/**
	 * Returns the JavaFX node associated with this expression.
	 * It is an HBox with the child in between parenthesis
	 * @return the JavaFX node associated with this expression.
	 */
	public Node getNode() {
		return node;
	}
	/**
	 * Gets a list of the subexpressions of this CompoundExpression.
	 * @return a list of the subexpressions
	 */
	public List<Expression> getChildren() {
		final List<Expression> list = new ArrayList<Expression>();
		list.add(child);
		return list;
	}
}
