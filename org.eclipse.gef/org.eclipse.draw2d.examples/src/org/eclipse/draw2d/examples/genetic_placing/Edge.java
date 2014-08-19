package org.eclipse.draw2d.examples.genetic_placing;

public class Edge {

	public Node start;
	public Node end;

	public Edge(Node s, Node e) {
		this.start = s;
		this.end = e;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Edge))
			return false;
		Edge e = (Edge) obj;
		return ((this.start.equals(e.start) && this.end.equals(e.end)) || (this.start.equals(e.end) && this.end.equals(e.start)));
	}

	@Override
	public String toString() {
		return "( E: " + this.start.toString() + " -> " + this.end.toString() + " )";
	}
}
