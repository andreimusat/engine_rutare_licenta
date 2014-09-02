package org.eclipse.draw2d.examples.genetic_placing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class NodePlacer {

	public Graph graph;
	public Node central;
	public Rectangle restrictedZone;
	public int width;
	public int height;
	public static int THRESHOLD = 20000;
	public static int NUM_ITER = 1000;
	public static int NEGATIVE_SCORE = -100;
	public static int ZONE_SIZE = 80;
	public static int SPEED = 20;
	public static int INTERSECTION = 0;
	public static int RESTRICTED = 1;
	public static int DISTANCE = 2;

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
		central.distanceToCenter = 0;

		restrictedZone = computeRestrictedZone(central.constraint.getCenter(), ZONE_SIZE);
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

			for (Node node : graph.nodes) {
				if (!node.pinned) {
					if (central.connectedNodes.contains(node.id)) {
						node.constraint.x = randIntInRange(restrictedZone.x, restrictedZone.x + restrictedZone.width);
						node.constraint.y = randIntInRange(restrictedZone.y, restrictedZone.y + restrictedZone.height);
					} else {
						Node pair = findPair(node);
						node.pair = pair;
						if (euclidianDistanceSquared(node.constraint.getCenter(), pair.constraint.getCenter()) > THRESHOLD) {
							double angle = getAngle(node, pair);
							node.constraint.getCenter().x += SPEED * Math.cos(angle * Math.PI / 180);
							node.constraint.getCenter().y += SPEED * Math.sin(angle * Math.PI / 180);
						}

						// node intersects another one, immediately place it
						// away from it
						if ((node.violations & (1 << INTERSECTION)) == 1) {
							node.constraint.x += node.constraint.width;
							node.constraint.y += node.constraint.height;
						}

						// node is in the restricted zone, immediately move it
						// outside
						if ((node.violations & (1 << RESTRICTED)) == 1) {

						}

						if ((node.violations & (1 << DISTANCE)) == 1) {

						}

					}
				}
			}

			checkNodes();

			if (shouldStop())
				break;

			counter++;
		}
	}

	public void fitness(Node current) {
		current.fitness = 0; // reset fitness for new iteration
		for (Node n : graph.nodes) {
			if (!n.equals(current)) {
				if (current.constraint.intersects(n.constraint)) {
					current.fitness += NEGATIVE_SCORE;
					current.violations |= (1 << INTERSECTION);
				}
			}
		}

		if (isInRestrictedZone(current) && !central.connectedNodes.contains(current.id)) {
			current.fitness += NEGATIVE_SCORE;
			current.violations |= (1 << RESTRICTED);
		}

		if (current.pair != null && euclidianDistanceSquared(current.constraint.getCenter(), current.pair.constraint.getCenter()) > THRESHOLD) {
			current.fitness += NEGATIVE_SCORE;
			current.violations |= (1 << DISTANCE);
		}

		if (current.fitness >= 0)
			current.pinned = true;
	}

	public boolean isInRestrictedZone(Node n) {
		if (restrictedZone.intersects(n.constraint))
			return true;
		return false;
	}

	public boolean shouldStop() {
		for (Node node : graph.nodes) {
			if (!node.pinned)
				return false;
		}
		return true;
	}

	public int euclidianDistanceSquared(Point p1, Point p2) {
		return (int) (Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
	}

	public int euclidianDistanceSquared(int x1, int x2, int y1, int y2) {
		return (int) (Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	public int randIntInRange(int rangeMin, int rangeMax) {
		Random rand = new Random();

		int randomNum = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;

		return randomNum;
	}

	public Rectangle computeRestrictedZone(Point center, int length) {
		return new Rectangle(center.x - length, center.y - length, 2 * length, 2 * length);
	}

	public Node findPair(Node n) {
		int distance = Integer.MAX_VALUE;
		Node result = null;
		for (int id : n.connectedNodes) {
			Node neigh = graph.getNodeById(id);
			int distToNeigh = euclidianDistanceSquared(n.constraint.getCenter(), neigh.constraint.getCenter());
			if (result == null || neigh.distanceToCenter < result.distanceToCenter || distToNeigh < distance) {
				distance = distToNeigh;
				result = neigh;
			}
		}

		return result;
	}

	public double getAngle(Node n1, Node n2) {
		int deltaY = n2.constraint.getCenter().y - n1.constraint.getCenter().y;
		int deltaX = n2.constraint.getCenter().x - n1.constraint.getCenter().x;
		return (Math.atan2(deltaY, deltaX) * 180) / Math.PI;
	}
}
