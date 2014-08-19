package org.eclipse.draw2d.examples.genetic_placing;

import java.util.ArrayList;
import java.util.List;

public class Node {

	public int id;
	public String name;
	public List<Integer> connectedNodes;

	public Node(int id, String name) {
		this.id = id;
		this.name = name;
		connectedNodes = new ArrayList<Integer>();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node))
			return false;
		Node n = (Node) obj;
		if (this.id == n.id) {
			if (this.name == n.name) {
				for (Integer conn : n.connectedNodes) {
					if (!this.connectedNodes.contains(conn))
						return false;
				}
			} else
				return false;
		} else
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "( N: " + this.id + ", " + this.name + " )";
	}
}
