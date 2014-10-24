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

	public Node getNodeById(int id) {
		for (Node node : nodes) {
			if (node.id == id)
				return node;
		}
		return null;
	}

	public Integer getNodeIdByName(String nodeName) {
		for (Node node : nodes) {
			if (node.name.equals(nodeName))
				return node.id;
		}

		return -1;
	}

	public void setAllNeighbours() {
		for (Node n : nodes) {
			for (Edge e : edges) {
				if (e.srcName.equals(n.name)) {
					Integer nodeIdByName = getNodeIdByName(e.dstName);
					if (!n.connectedNodes.contains(nodeIdByName))
						n.connectedNodes.add(nodeIdByName);
				} else if (e.dstName.equals(n.name)) {
					Integer nodeIdByName = getNodeIdByName(e.srcName);
					if (!n.connectedNodes.contains(nodeIdByName))
						n.connectedNodes.add(nodeIdByName);
				}
			}
		}
	}

}
