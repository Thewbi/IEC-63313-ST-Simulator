package com.st.grammar;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import common.DefaultNode;
import common.Node;

public class ASTListener extends StructuredTextBaseListener {

    private int indent;
	
	private Node rootNode;
	
	private Node currentNode;
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override 
	public void enterEveryRule(ParserRuleContext ctx) {

        for (int i = 0; i < indent; i++)
        {
            System.out.print("  ");
        }

        System.out.print(ctx.getClass().getSimpleName() + " " + ctx.getStart().getText());
        System.out.println("");
		
		// TO DEBUG UNCOMMENT THIS:
		descend();
		// currentNode.setLabel(ctx.getText());
		// currentNode.setLabel(ctx.getClass().getSimpleName() + " " + ctx.getStart().getText());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override 
	public void exitEveryRule(ParserRuleContext ctx) {
		// TO DEBUG UNCOMMENT THIS
		ascend();
	}
	
	@Override 
	public void visitTerminal(TerminalNode node) { 
		descend();
		currentNode.setLabel("TERMINAL: " + node.getText());
		ascend();
	}
	
	private void descend() {
        indent++;

		Node parent = getCurrentNode();
		
		currentNode = createNode();
		
		parent.getChildren().add(currentNode);
		currentNode.setParentNode(parent);
	}

	private void ascend() {
        indent--;

		if (currentNode != null) {
			currentNode = currentNode.getParentNode();
		}
	}
	
	public Node getRootNode() {
		if (rootNode == null) {
			rootNode = createNode();
			rootNode.setLabel("ROOT");
		}
		return rootNode;
	}

	/**
	 * add your own implementation or add a reference to a factory that creates your nodes!
	 * 
	 * @return a new node
	 */
	private Node createNode() {
		return new DefaultNode();
	}

	public Node getCurrentNode() {
		if (currentNode == null) {
			currentNode = getRootNode();
		}
		return currentNode;
	}

}
