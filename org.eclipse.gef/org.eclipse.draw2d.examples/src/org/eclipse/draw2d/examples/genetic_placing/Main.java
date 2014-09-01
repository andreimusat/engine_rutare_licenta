package org.eclipse.draw2d.examples.genetic_placing;

import java.io.FileNotFoundException;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

	public static int WIDTH = 800;
	public static int HEIGHT = 600;

	public static void main(String[] args) throws FileNotFoundException {
		Graph graph = GraphReader.getInstance().readGraph("simple_graph");
		graph.createInitialEdges();

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
			NodeFigure nf = new NodeFigure(n);
			diagram.add(nf, n.constraint);
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
