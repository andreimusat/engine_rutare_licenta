package org.eclipse.draw2d.examples.genetic_placing;

import java.io.File;
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

	public Graph readGraph(String fileName) throws Exception {
		Scanner s = new Scanner(new File(fileName));
		String graphName = "";
		Graph g = null;
		String line = s.nextLine();
		int idCount = 0;

		if ("graph start".equals(line)) {
			line = s.nextLine();
			if (line == null) {
				s.close();
				throw new InvalidInputFormatException("File should contain graph name!");
			} else {
				graphName = line.split(":")[1].trim();
				g = new Graph(graphName);
			}

			line = s.nextLine();
			if (line == null) {
				s.close();
				throw new InvalidInputFormatException("File should contain a list of nodes");
			} else {
				if (line.trim().equals("nodes start")) {
					while (!line.trim().equals("nodes end")) {
						line = s.nextLine();
						if (line.trim().equals("nodes end"))
							continue;

						if (line == null || !line.trim().equals("node begin")) {
							s.close();
							throw new InvalidInputFormatException("Invalid node description!");
						}

						line = s.nextLine();
						String nodeName = null;
						if (line.split(":").length > 1)
							nodeName = line.split(":")[1].trim();
						else
							nodeName = "";

						line = s.nextLine();
						String nodeLabel = null;
						if (line.split(":").length > 1)
							nodeLabel = line.split(":")[1].trim();
						else
							nodeLabel = "";

						idCount++;
						g.nodes.add(new Node(idCount, nodeName, nodeLabel));

						line = s.nextLine();
						if (line == null || !line.trim().equals("end")) {
							s.close();
							throw new InvalidInputFormatException("Invalid end of node description!");
						}
					}
				}
				line = s.nextLine();
				if (line.trim().equals("edges start")) {
					while (!line.trim().equals("edges end")) {
						line = s.nextLine();
						if (line.trim().equals("edges end"))
							continue;

						if (line == null || !line.trim().equals("edge begin")) {
							s.close();
							throw new InvalidInputFormatException("Invalid edge description!");
						}

						line = s.nextLine();
						String edgeLabel = null;
						if (line.split(":").length > 1) {
							edgeLabel = line.split(":")[1].trim();
						} else {
							edgeLabel = "";
						}

						line = s.nextLine();
						String edgeSrcName = null;
						if (line.split(":").length > 1) {
							edgeSrcName = line.split(":")[1].trim();
						} else {
							edgeLabel = "";
						}

						line = s.nextLine();
						String edgeDstName = null;
						if (line.split(":").length > 1) {
							edgeDstName = line.split(":")[1].trim();
						}

						g.edges.add(new Edge(edgeSrcName, edgeDstName, edgeLabel));

						line = s.nextLine();
						if (line == null || !line.trim().equals("edge end")) {
							s.close();
							throw new InvalidInputFormatException("Invalid end of edge description!");
						}
					}
				}
			}

		} else {
			s.close();
			throw new InvalidInputFormatException("File should start with 'graph start'!");
		}

		s.close();
		return g;
	}
}
