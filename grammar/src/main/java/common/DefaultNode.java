package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultNode implements Node {
	
	private Node parentNode;
	
	private List<Node> children = new ArrayList<>();
	
	private String label;
	
	@Override
	public void print(final int ident) {
		for (int i = 0; i < ident; i++) {
			System.out.print("  ");
		}
		
		// output own data
		System.out.println(label);
		
		// output children
		for (Node child : children) {
			child.print(ident+1);
		}
	}

	@Override
	public Node getParentNode() {
		return parentNode;
	}
	
	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	@Override
	public Collection<Node> getChildren() {
		return children;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
