package org.eclipse.draw2d.examples.genetic_placing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

public class Node {

	public int id;
	public String name;
	public List<Integer> connectedNodes;
	public Rectangle constraint;
	public boolean pinned;
	public static int DIMENSION = 30;
	public int fitness;
	public int distanceToCenter;

	public Node(int id, String name) {
		this.id = id;
		this.name = name;
		this.connectedNodes = new ArrayList<Integer>();
		this.pinned = false;
		this.fitness = 0;
		this.distanceToCenter = Integer.MAX_VALUE;
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
