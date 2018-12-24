/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := A | X
 * A := A+M | M
 * M := M*M | X
 * X := (E) | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
	/**
	 * Attempts to create an expression tree -- flattened as much as possible -- from the specified String.
         * Throws a ExpressionParseException if the specified string cannot be parsed.
	 * @param str the string to parse into an expression tree
	 * @param withJavaFXControls you can just ignore this variable for R1
	 * @return the Expression object representing the parsed expression tree
	 */
	public Expression parse (String str, boolean withJavaFXControls) throws ExpressionParseException {
		// Remove spaces -- this simplifies the parsing logic
		str = str.replaceAll(" ", "");
		Expression expression = parseExpression(str, withJavaFXControls);
		if (expression == null) {
			// If we couldn't parse the string, then raise an error
			throw new ExpressionParseException("Cannot parse expression: " + str);
		}

		// Flatten the expression before returning
		expression.flatten();
		return expression;
	}
	
	/**
	 * The string can either be any kind of expression.
	 * If the string can be parsed as an M expression the M expression is returned, 
	 * but if there is a "+" then an addition expression is returned.
	 * @param str either expression to be parsed.
	 * @return the parsed expression
	 */
	protected Expression parseExpression (String str, boolean withJavaFXControls) {
		if (str == null || str.length() == 0) return null;
		final Expression mExpression = parseMExpression(str, withJavaFXControls);
		if (mExpression != null) { // If str can be parsed as an M,
			return mExpression; // Return it as an M.
		} else {
			int idxAdd = str.indexOf("+"); // Split the string at the + symbol.
			final CompoundExpression newAE = new AdditionExpression(withJavaFXControls);
			while (idxAdd != -1) {
				final Expression possibleM = parseMExpression(str.substring(0, idxAdd), withJavaFXControls);
				final Expression possibleA = parseExpression(str.substring(idxAdd + 1), withJavaFXControls);
				if (possibleM != null && possibleA != null) { // If the left side can be parsed as an M and the right side can be parsed as an E,
					newAE.addSubexpression(possibleM);
					newAE.addSubexpression(possibleA);
					return newAE; // Return the AdditionExpression with the left and right sides as children.
				}
				idxAdd = str.indexOf("+", idxAdd + 1);
			}
			return null; // If none of the pluses work, return null.
		}
		
	}
	/**
	 * The string can either be a multiplication, parenthetical, or literal expression.
	 * If the string can be parsed as an X expression the X expression is returned, 
	 * but if there is a "*" then a multiplication expression is returned.
	 * @param str either multiplication, parenthetical, or literal expression to be parsed.
	 * @return the parsed expression as an X expression or multiplication expression
	 */
	private Expression parseMExpression(String str, boolean withJavaFXControls) {
		if (str == null || str.length() == 0) return null;
		final Expression pExpression = parseXExpression(str, withJavaFXControls);
		if (pExpression != null) { // If str can be parsed as an X,
			return pExpression; // return the X parsing of str.
		} else {
			int idxMult = str.indexOf("*"); // Split str at the * symbol.
			final CompoundExpression newME = new MultiplicationExpression(withJavaFXControls);
			while (idxMult != -1) {
				final Expression possibleX = parseXExpression(str.substring(0, idxMult), withJavaFXControls);
				final Expression possibleM = parseMExpression(str.substring(idxMult + 1), withJavaFXControls);
				if (possibleX != null && possibleM != null) { // If the left side parses as an X and the right side parses as an M,
					newME.addSubexpression(possibleX);
					newME.addSubexpression(possibleM);
					return newME; // Return an M expression with the left and right side parsings as children.
				}
				idxMult = str.indexOf("*", idxMult + 1);
			}
			return null; // If none of the *'s work, return null.
		}
	}
	/**
	 * The string can either be a parenthetical expression or a literal expression.
	 * If it's a literal the literal expression is returned, but if there are parentheses then
	 * a parenthetical expression is returned.
	 * @param str either a literal expression or a parenthetical expression to be parsed
	 * @return the parsed expression as a literal expression or parenthetical expression
	 */
	private Expression parseXExpression(String str, boolean withJavaFXControls) {
		if(str == null || str.length() == 0) return null;
		final Expression lExpression = parseLExpression(str, withJavaFXControls);
		if(lExpression != null) { // If str can be parsed as an L,
			return lExpression; // Return the L parsing of str.
		}
		if(str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
			Expression aExpression = parseExpression(str.substring(1, str.length() - 1), withJavaFXControls);
			if(aExpression != null) { // If str is an E wrapped in parentheses
				ParentheticalExpression pExpression = new ParentheticalExpression(withJavaFXControls);
				pExpression.addSubexpression(aExpression);
				return pExpression; // Return a ParentheticalExpression with the E parsing of the inside as the child.
			}
		}
		return null; // Otherwise return null.
	}
	/**
	 * Converts and returns the string into a literal expression.
	 * @param str the literal expression to be parsed
	 * @return the parsed string as a literal expression
	 */
	private Expression parseLExpression(String str, boolean withJavaFXControls) {
		if(str == null || str.length() == 0) return null;
		if(str.length() == 1) {
			final int difference = str.charAt(0) - 'a'; // If str is a single letter,
			if(difference >= 0 && difference < 26) return new LiteralExpression(str, withJavaFXControls); // return a LiteralExpression with str as the symbol.
		}
		try { // If str can be parsed as an integer, return a LiteralExpression with str as the symbol.
			return new LiteralExpression("" + Integer.parseInt(str), withJavaFXControls);
		}
		catch(NumberFormatException e) {
			return null; // Otherwise return null.
		}
	}
}
