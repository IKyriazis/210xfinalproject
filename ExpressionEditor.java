import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ExpressionEditor extends Application {
	
	public static void main (String[] args) {
		launch(args);
	}

	/**
	 * Mouse event handler for the entire pane that constitutes the ExpressionEditor
	 */
	private class MouseEventHandler implements EventHandler<MouseEvent> {
		
		boolean wasDragged = false;
		Node copyNode;
		final double[] initialMousePos = new double[2];
		/**
		 * positions of the dragged expression relative to the parent node
		 * in each possible place it could be dropped
		 */
		private double[] rearrangementXPos;
		int minIdx;
		
		/**
		 * Creates a new MouseEventHandler.
		 * @param pane_ the pane that this handler operates on
		 * @param rootExpression_ the full expression displayed in pane_
		 */
		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
		}
		
		/**
		 * Gets the position of the given node relative to the expression pane.
		 * @param n the node in question
		 * @return the position relative to the expression pane
		 */
		private double[] realPos(Node n) {
			double[] coordinates = new double[2];
			while (n != expressionPane) {
				coordinates[0] += n.getLayoutX();
				coordinates[1] += n.getLayoutY();
				n = n.getParent();
			}
			return coordinates;
		}
		
		/**
		 * Selects the focus if the mouse is clicked.
		 * Enables drag-and-drop behavior on the focus if the mouse is dragged.
		 */
		public void handle (MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				final double[] realPos = realPos(focusNode);
				if(!focusNode.contains(event.getSceneX() - realPos[0], event.getSceneY() - realPos[1])) {
					// If the mouse is clicked outside the focus, delete the focus.
					((Pane) focusNode).setBorder(Expression.NO_BORDER);
					focusExpression = rootExpression;
					focusNode = rootNode;
				}
				if(focusNode != rootNode) {
					// If there is a focus, prepare to drag and drop.
					makeCopy();
					setColor((HBox) focusNode, Expression.GHOST_COLOR);
					initialMousePos[0] = event.getSceneX();
					initialMousePos[1] = event.getSceneY();
				}
				wasDragged = false;
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				if(focusNode != rootNode) {
					drag(event);
					wasDragged = true;
				}
			} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				if(expressionPane.getChildren().size() == 2) {
					expressionPane.getChildren().remove(1); // Remove the copy node.
				}
				setColor((HBox) focusNode, Color.BLACK);
				if(wasDragged) {
					drop();
				} else {
					setFocus(event); // Select the focus.
				}
			}
		}
		 
		/**
		 * Makes a copy of the focus expression to be dragged and dropped.
		 */
		private void makeCopy() {
			copyNode = focusExpression.deepCopy().getNode();
			expressionPane.getChildren().add(copyNode);
			final double[] realPos = realPos(focusNode);
			copyNode.setLayoutX(realPos[0]);
			copyNode.setLayoutY(realPos[1]);
			// Fill rearrangementXPos with the appropriate positions.
			final List<Expression> siblings = focusExpression.getParent().getChildren();
			rearrangementXPos = new double[siblings.size()];
			int siblingsIdx = 0;
			for(int i = 0; i < rearrangementXPos.length - 1; i++) {
				if(siblings.get(siblingsIdx) == focusExpression) {
					siblingsIdx++;
				}
				rearrangementXPos[i + 1] = rearrangementXPos[i] +
						siblings.get(siblingsIdx).getNode().prefWidth(-1) + // width of the sibling
						((HBox) focusNode.getParent()).getChildren().get(1).prefWidth(-1); // width of the operator
				siblingsIdx++;
			}
		}
		
		/**
		 * Sets the color of the given HBox to the given color.
		 * @param n HBox to be modified
		 * @param c new color of the HBox
		 */
		private void setColor(HBox n, Color c) {
			for(Node child : n.getChildren()) {
				if(child instanceof Label) {
					((Label) child).setTextFill(c);
				} else {
					setColor((HBox) child, c);
				}
			}
		}
		
		/**
		 * Causes the copy node to follow the mouse while it is dragged.
		 * Also rearranges the siblings of the focus to keep the focus as close to the copy node as possible.
		 * @param event the mouse location
		 */
		private void drag(MouseEvent event) {
			// Copy node follows the mouse.
			copyNode.setTranslateX(event.getSceneX() - initialMousePos[0]);
			copyNode.setTranslateY(event.getSceneY() - initialMousePos[1]);
			// Calculate which position the focus node could be in to stay closest to the copy node.
			if(!(focusExpression.getParent() instanceof ParentheticalExpression)) {
				minIdx = 0;
				final double realXPos = realPos(focusNode.getParent())[0];
				double minDistance = Math.abs(copyNode.getLayoutX() + copyNode.getTranslateX() - realXPos);
				for(int i = 1; i < rearrangementXPos.length; i++) {
					final double nextDistance = Math.abs(copyNode.getLayoutX() + copyNode.getTranslateX() - realXPos - rearrangementXPos[i]);
					if(nextDistance < minDistance) {
						minIdx = i;
						minDistance = nextDistance;
					}
				}
				//System.out.println(minIdx);
				// Move the focus node closer to the copy node.
				final List<Node> siblingNodes = ((HBox) focusNode.getParent()).getChildren();
				int focusNodeIdx = siblingNodes.indexOf(focusNode);
				siblingNodes.remove(focusNodeIdx);
				if(focusNodeIdx < 2 * minIdx) { // The focus node is to the left of where it should be.
					while(focusNodeIdx != 2 * minIdx) {
						siblingNodes.add(focusNodeIdx, siblingNodes.remove(focusNodeIdx + 1));
						focusNodeIdx += 2;
					}
				} else { // The focus node is to the right of where it should be.
					while(focusNodeIdx != 2 * minIdx) {
						siblingNodes.add(focusNodeIdx - 1, 
								siblingNodes.remove(focusNodeIdx - 2));
						focusNodeIdx -= 2;
					}
				}
				siblingNodes.add(focusNodeIdx, focusNode);
			}
		}
		
		/**
		 * After the mouse is released, changes the order of the siblings in the underlying Expression instance.
		 */
		private void drop() {
			if(!(focusExpression.getParent() instanceof ParentheticalExpression)) {
				final List<Expression> siblings = focusExpression.getParent().getChildren();
				siblings.remove(focusExpression);
				siblings.add(minIdx, focusExpression);
			}
		}
		
		/**
		 * Selects the focus when the mouse is clicked.
		 * @param event the mouse location
		 */
		private void setFocus(MouseEvent event) {
			if(focusExpression instanceof LiteralExpression) {
				// If the focus is a literal expression, delete the focus.
				((Pane) focusNode).setBorder(Expression.NO_BORDER);
				focusExpression = rootExpression;
				focusNode = rootNode;
			} else {
				boolean validFocus = false;
				for(Expression e : ((CompoundExpression) focusExpression).getChildren()) {
					// Does e contain the mouse click?
					final double[] realPos = realPos(e.getNode());
					if(e.getNode().contains(event.getSceneX() - realPos[0], event.getSceneY() - realPos[1])) {	
						// Make e the new focus.
						validFocus = true;
						((Pane) focusNode).setBorder(Expression.NO_BORDER);
						focusExpression = e;
						focusNode = e.getNode();
						((Pane) focusNode).setBorder(Expression.RED_BORDER);
						break;
					}
				}
				if(!validFocus) {
					// Delete the focus if the mouse click was not contained in any of the focus' children.
					((Pane) focusNode).setBorder(Expression.NO_BORDER);
					focusExpression = rootExpression;
					focusNode = rootNode;
				}
			}
		}
	}
	
	
	
	
	

	/**
	 * Size of the GUI
	 */
	private static final int WINDOW_WIDTH = 1800, WINDOW_HEIGHT = 500;

	/**
	 * Initial expression shown in the textbox
	 */
	private static final String EXAMPLE_EXPRESSION = "2*x+3*y+4*z+(7+6*z)";
	
	/**
	 * Font used in the label displayed in the pane.
	 */
	public static final Font FONT = new Font("Verdana", 100);
	
	/**
	 * Parser used for parsing expressions.
	 */
	private final ExpressionParser expressionParser = new SimpleExpressionParser();
	private Node focusNode;
	private Expression focusExpression;
	private Node rootNode;
	private Expression rootExpression;
	private final Pane expressionPane = new Pane();
	@Override
	public void start (Stage primaryStage) {
		primaryStage.setTitle("Expression Editor");

		// Add the textbox and Parser button
		final Pane queryPane = new HBox();
		final TextField textField = new TextField(EXAMPLE_EXPRESSION);
		final Button button = new Button("Parse");
		queryPane.getChildren().add(textField);

		// Add the callback to handle when the Parse button is pressed	
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle (MouseEvent e) {
				// Try to parse the expression
				try {
					// Success! Add the expression's Node to the expressionPane
					rootExpression = expressionParser.parse(textField.getText(), true);
					focusExpression = rootExpression;
					rootNode = rootExpression.getNode();
					focusNode = rootNode;
					System.out.println(rootExpression.convertToString(0));
					expressionPane.getChildren().clear();
					expressionPane.getChildren().add(rootNode);
					rootNode.setLayoutX(WINDOW_WIDTH/4);
					rootNode.setLayoutY(WINDOW_HEIGHT/2);
					
					

					// If the parsed expression is a CompoundExpression, then register some callbacks
					if (rootExpression instanceof CompoundExpression) {
						((Pane) rootExpression.getNode()).setBorder(Expression.NO_BORDER);
						final MouseEventHandler eventHandler = new MouseEventHandler(expressionPane, (CompoundExpression) rootExpression);
						expressionPane.setOnMousePressed(eventHandler);
						expressionPane.setOnMouseDragged(eventHandler);
						expressionPane.setOnMouseReleased(eventHandler);
					}
				} catch (ExpressionParseException epe) {
					// If we can't parse the expression, then mark it in red
					textField.setStyle("-fx-text-fill: red");
				}
			}
		});
		queryPane.getChildren().add(button);

		// Reset the color to black whenever the user presses a key
		textField.setOnKeyPressed(e -> textField.setStyle("-fx-text-fill: black"));
		
		final BorderPane root = new BorderPane();
		root.setTop(queryPane);
		root.setCenter(expressionPane);

		primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
		primaryStage.show();
	}
}
