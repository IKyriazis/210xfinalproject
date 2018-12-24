import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Tester {
	@Before
	public void setup() {
		
	}
	@Test
	public void testAdditionExpression() {
		AdditionExpression add = new AdditionExpression(false);
		add.addSubexpression(new LiteralExpression("1", false));
		AdditionExpression addSub = new AdditionExpression(false);
		addSub.addSubexpression(new LiteralExpression("3", false));
		addSub.addSubexpression(new LiteralExpression("4", false));
		add.addSubexpression(addSub);
		add.addSubexpression(new LiteralExpression("2", false));
		System.out.println("Add:\n" + add.convertToString(0));
		assertEquals(add, addSub.getParent());
		add.flatten();
		System.out.println("Add flattened:\n" + add.convertToString(0));
		AdditionExpression copy = (AdditionExpression) add.deepCopy();
		assertEquals(add.convertToString(0), copy.convertToString(0));
		assertEquals(false, add == copy);
	}
	@Test
	public void testMultiplicationExpression() {
		MultiplicationExpression add = new MultiplicationExpression(false);
		add.addSubexpression(new LiteralExpression("1", false));
		MultiplicationExpression addSub = new MultiplicationExpression(false);
		addSub.addSubexpression(new LiteralExpression("3", false));
		addSub.addSubexpression(new LiteralExpression("4", false));
		add.addSubexpression(addSub);
		add.addSubexpression(new LiteralExpression("2", false));
		System.out.println("Multiplication:\n" + add.convertToString(0));
		assertEquals(add, addSub.getParent());
		add.flatten();
		System.out.println("Multiplication flattened:\n" + add.convertToString(0));
		MultiplicationExpression copy = (MultiplicationExpression) add.deepCopy();
		assertEquals(add.convertToString(0), copy.convertToString(0));
		assertEquals(false, add == copy);
	}
	@Test
	public void testBothExpression() {
		AdditionExpression a = new AdditionExpression(false);
		AdditionExpression b = new AdditionExpression(false);
		b.addSubexpression(new LiteralExpression("1", false));
		b.addSubexpression(new LiteralExpression("2", false));
		MultiplicationExpression c = new MultiplicationExpression(false);
		c.addSubexpression(new LiteralExpression("3", false));
		AdditionExpression d = new AdditionExpression(false);
		d.addSubexpression(new LiteralExpression("4", false));
		d.addSubexpression(new LiteralExpression("5", false));
		d.addSubexpression(new LiteralExpression("6", false));
		AdditionExpression e = new AdditionExpression(false);
		e.addSubexpression(new LiteralExpression("7", false));
		e.addSubexpression(new LiteralExpression("9", false));
		c.addSubexpression(d);
		d.addSubexpression(e);
		b.addSubexpression(c);
		a.addSubexpression(b);
		assertEquals(a, b.getParent());
		System.out.println("Both mult and add: ");
		System.out.println(a.convertToString(0));
		System.out.println("Mult and add flattened:");
		a.flatten();
		System.out.println(a.convertToString(0));
		AdditionExpression copy = (AdditionExpression) a.deepCopy();
		assertEquals(a.convertToString(0), copy.convertToString(0));
		assertEquals(false, a == copy);
		
		
	}
	@Test
	public void testParenthetical() {
		ParentheticalExpression a = new ParentheticalExpression(false);
		AdditionExpression b = new AdditionExpression(false);
		AdditionExpression c = new AdditionExpression(false);
		c.addSubexpression(new LiteralExpression("3", false));
		c.addSubexpression(new LiteralExpression("4", false));
		b.addSubexpression(new LiteralExpression("1", false));
		b.addSubexpression(new LiteralExpression("2", false));
		b.addSubexpression(c);
		a.addSubexpression(b);
		System.out.println("Parenthetical:");
		System.out.println(a.convertToString(0));
		a.flatten();
		System.out.println("Parenthetical flattened: \n" + a.convertToString(0));
		ParentheticalExpression copy = (ParentheticalExpression) a.deepCopy();
		assertEquals(a.convertToString(0), copy.convertToString(0));
		assertEquals(false, a == copy);
	}
	/*
	@Test
	public void testParser() {
		SimpleExpressionParser parser = new SimpleExpressionParser();
		ParentheticalExpression pA = new ParentheticalExpression();
		ParentheticalExpression pB = new ParentheticalExpression();
		pA.addSubexpression(pB);
		
		LiteralExpression lA = new LiteralExpression("2");
		AdditionExpression aA = new AdditionExpression();
		pB.addSubexpression(lA);
		AdditionExpression aB = new AdditionExpression();
		aA.addSubexpression(aB);
		ParentheticalExpression pC = new ParentheticalExpression();
		LiteralExpression lB = new LiteralExpression("3");
		aB.addSubexpression(pC);
		aB.addSubexpression(lB);
		ParentheticalExpression pD = new ParentheticalExpression();
		aC.addSubexpression(pD);
		ParentheticalExpression pE = new ParentheticalExpression();
		aD.addSubexpression(pE);
		LiteralExpression lC = new LiteralExpression("z");
		aE.addSubexpression(lC);
		assertEquals(parser.parseExpression("((2+(((z)))+3))"), (Expression) pA);
		
	}
	*/
	
	
}
