package org.eclipse.draw2d.examples.genetic_placing;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		Graph graph = GraphReader.getInstance().readGraph("test_graph");
		graph.createInitialEdges();
	}

}
