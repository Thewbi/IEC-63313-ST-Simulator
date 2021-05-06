package common;

import java.util.Collection;

public interface Node {
	
	void setLabel(String string);

	Node getParentNode();

	void setParentNode(Node parent);
	
	Collection<Node> getChildren();

	void print(int ident);

}
