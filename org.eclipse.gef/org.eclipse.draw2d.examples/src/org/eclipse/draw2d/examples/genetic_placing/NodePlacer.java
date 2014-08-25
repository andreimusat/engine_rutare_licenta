package org.eclipse.draw2d.examples.genetic_placing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.draw2d.geometry.Rectangle;

public class NodePlacer {

	public Graph graph;
	public Node central;
	public int width;
	public int height;
	public static int THERSHOLD = 20;
	public static int NUM_ITER = 1000;
	public static int NEGATIVE_SCORE = -100;

	public NodePlacer(Graph g, int displayWidth, int displayHeight) {
		this.graph = g;
		this.width = displayWidth;
		this.height = displayHeight;
		for (Node n : g.nodes) {
			Random rand = new Random();
			n.constraint = new Rectangle(rand.nextInt(displayWidth), rand.nextInt(displayHeight), Node.DIMENSION, Node.DIMENSION);
		}
		this.central = null;
	}

	public void computeCentralNode() {
		int maxConns = 0;
		for (Node n : graph.nodes) {
			if (n.connectedNodes.size() > maxConns) {
				maxConns = n.connectedNodes.size();
				central = n;
			}
		}

		Rectangle currentConstraint = central.constraint;
		int constrWidth = currentConstraint.width;
		int constrHeight = currentConstraint.height;
		Rectangle newConstraint = new Rectangle((width - constrWidth) / 2, (height - constrHeight) / 2, constrWidth, constrHeight);
		central.constraint = newConstraint;
		central.pinned = true;
	}

	public void checkNodes() {
		List<Node> visited = new ArrayList<Node>();
		List<Node> toVisit = new ArrayList<Node>();
		visited.add(central);
		for (int nodeId : central.connectedNodes) {
			toVisit.add(graph.getNodeById(nodeId));
		}

		while (!toVisit.isEmpty()) {
			Node current = toVisit.remove(0);
			if (visited.contains(current))
				continue;

			visited.add(current);
			fitness(current);
			for (int nodeId : current.connectedNodes) {
				Node candidate = graph.getNodeById(nodeId);
				if (!toVisit.contains(candidate))
					toVisit.add(candidate);
			}
		}

	}

	public void placeNodes() {
		int counter = 0;
		computeCentralNode();

		while (counter < NUM_ITER) {
			checkNodes();

			for (Node node : graph.nodes) {
				if (!node.pinned) {
					node.constraint.x = new Random().nextInt(width);
					node.constraint.y = new Random().nextInt(height);
				}
			}

			if (shouldStop())
				break;

			counter++;
		}
	}

	public void fitness(Node current) {
		for (Node n : graph.nodes) {
			if (!n.equals(current)) {
				if (current.constraint.intersects(n.constraint)) {
					current.fitness += NEGATIVE_SCORE;
				}
			}
		}

		if (current.fitness >= 0)
			current.pinned = true;
	}

	public boolean shouldStop() {
		for (Node node : graph.nodes) {
			if (!node.pinned)
				return false;
		}
		return true;
	}

	public int euclidianDistanceSquared(int x1, int x2, int y1, int y2) {
		return (int) (Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
}
