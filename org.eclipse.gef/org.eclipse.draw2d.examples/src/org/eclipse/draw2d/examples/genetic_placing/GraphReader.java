package org.eclipse.draw2d.examples.genetic_placing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphReader {

	private static GraphReader instance;

	private GraphReader() {
	}

	public static GraphReader getInstance() {
		if (instance == null)
			instance = new GraphReader();

		return instance;
	}

	public Graph readGraph(String fileName) throws FileNotFoundException {
		List<Node> nodes = new ArrayList<Node>();
		Scanner s = new Scanner(new File(fileName));
		String graphName = "";
		Graph g = null;
		String line = s.nextLine();
		if ("graph start".equals(line.trim())) {
			graphName = s.nextLine().split(":")[1].trim();
			g = new Graph(graphName);
			line = s.nextLine();
			if ("nodes start".equals(line.trim())) {
				while (s.hasNextLine()) {
					line = s.nextLine();
					if ("node start".equals(line.trim())) {
						String nodeName = s.nextLine().split(":")[1].trim();
						int id = Integer.parseInt(s.nextLine().split(":")[1].trim());
						Node n = new Node(id, nodeName);
						String[] connectedNodes = s.nextLine().split(":")[1].trim().split(",");
						for (String conn : connectedNodes) {
							n.connectedNodes.add(Integer.parseInt(conn));
						}
						nodes.add(n);
					} else if ("node end".equals(line.trim()))
						continue;
				}
			}
		}
		g.nodes = nodes;
		s.close();
		return g;
	}
}
