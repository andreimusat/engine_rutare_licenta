package org.eclipse.draw2d.examples.genetic_placing;

import java.util.ArrayList;
import java.util.List;

public class Graph {

	public String name;
	public List<Node> nodes;
	public List<Edge> edges;

	public Graph(String name) {
		this.name = name;
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
	}

	public void createInitialEdges() {
		for (Node n1 : nodes) {
			for (Node n2 : nodes) {
				if (n1.connectedNodes.contains(n2.id)) {
					Edge e = new Edge(n1, n2);
					if (!edges.contains(e))
						edges.add(e);
				}
			}
		}
	}

	public Node getNodeById(int id) {
		for (Node node : nodes) {
			if (node.id == id)
				return node;
		}
		return null;
	}

}
