package org.eclipse.draw2d.examples.genetic_placing;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

	public static int WIDTH = 1280;
	public static int HEIGHT = 1024;

	public static void main(String[] args) throws Exception {
		Graph graph = GraphReader.getInstance().readGraph("test_graph");

		for (Edge e : graph.edges) {
			if (e.srcName.equals("uvm_pkg.uvm_tlm_fifo_base"))
				System.out.println(e.toString());
		}

		System.out.println();

		for (Node n : graph.nodes) {
			System.out.println(n.toString());
		}

		graph.setAllNeighbours();

		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setLayout(new FillLayout());

		FigureCanvas canvas = new FigureCanvas(shell);
		canvas.setBackground(ColorConstants.white);

		Figure diagram = new Figure();
		diagram.setLayoutManager(new XYLayout());

		NodePlacer np = new NodePlacer(graph, WIDTH, HEIGHT);
		np.placeNodes();

		for (Node n : graph.nodes) {
			if (n.equals(np.central)) {
				NodeFigure nf = new NodeFigure(n);
				diagram.add(nf, n.constraint);
			}
		}

		canvas.setContents(diagram);

		shell.setText("Diagrams");
		shell.setSize(WIDTH, HEIGHT);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}

}
