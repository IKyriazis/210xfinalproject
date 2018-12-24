import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public abstract class SimpleCompoundExpression extends AbstractCompoundExpression {
	final protected List<Expression> children = new LinkedList<Expression>();
	/**
	 * Constructs a new SimpleCompoundExpression.
	 */
	public SimpleCompoundExpression(boolean withJavaFXControls) {
		super(withJavaFXControls);
	}
	/**
	 * Adds all the elements of toAdd as subexpressions.
	 * @param toAdd expressions to add
	 */
	protected void addChildren(List<Expression> toAdd) {
		for (int i = 0; i < toAdd.size(); i++) {
			addSubexpression(toAdd.get(i).deepCopy());
		}
	}
	/**
	 * Recursively flattens the expression as much as possible.
	 * This method modifies the expression itself.
	 * The order of the subexpressions will not be changed.
	 */
	public void flatten() {
		final int size = children.size();
		for (int i = 0; i < size; i++) { // For every subexpression,
			children.get(0).flatten(); // Flatten it.
			if (sameClass(children.get(0))) { // If it is the same type of operation,
				final CompoundExpression removed = (CompoundExpression) children.remove(0); // delete the repetitive subexpression.
				if(withJavaFXControls) {
					node.getChildren().remove(0); // Remove the repetitive subexpression's node.
					if(node.getChildren().size() > 0) {
						node.getChildren().remove(0); // Remove the operator symbol.
					}
				}
				for(Expression grandchild : removed.getChildren()) {
					addSubexpression(grandchild); // and add its children as subexpressions.
				}
			}
			else {
				if(withJavaFXControls) {
					node.getChildren().remove(0); // Remove the repetitive subexpression's node.
					if(node.getChildren().size() > 0) {
						node.getChildren().remove(0); // Remove the operator symbol.
					}
				}
				addSubexpression(children.remove(0)); // This preserves the order of children.
			}
		}
	}
	/**
	 * Determines if the given expression is the same class as the subclass.
	 * @param e the expression in question
	 * @return true if the expression is the same class
	 */
	protected abstract boolean sameClass(Expression e);
	
	public void appendChildren(StringBuffer sb, int indentLevel) {
		for (int i = 0; i < children.size(); i++) {
			sb.append("\n"); // children on separate lines indented one more level
			sb.append(children.get(i).convertToString(indentLevel));
		}
	}
	/**
	 * Adds a new subexpression to this expression.
	 * @param subexpression the subexpression to add
	 */
	public void addSubexpression(Expression subexpression) {
		children.add(subexpression);
		subexpression.setParent(this);
		if(withJavaFXControls) {
			if(!node.getChildren().isEmpty()) {
				final Label label = new Label(getSymbol());
				label.setFont(ExpressionEditor.FONT);
				label.setTextFill(Color.BLACK);
				node.getChildren().add(label);
			}
			node.getChildren().add(subexpression.getNode());
		}
	}
	/**
	 * Returns the JavaFX node associated with this expression.
	 * It is an HBox with all the children laid out side by side in order, with the symbol in between.
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
		return children;
	}
}
