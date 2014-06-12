package org.eclipse.draw2d.examples.diagram;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class CustomDiagramDrawer {

	private static Map<String, String> ports = new HashMap<String, String>();

	public static void main(String[] args) {
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setLayout(new FillLayout());

		FigureCanvas canvas = new FigureCanvas(shell);
		canvas.setBackground(ColorConstants.white);

		ports.put("a", "input");
		ports.put("b", "output");

		CustomDiagram cd = new CustomDiagram();
		Figure diagram = new Figure();
		diagram.setLayoutManager(new XYLayout());
		canvas.setContents(diagram);

		IFigure fig = cd.buildDiagram("mod", ports);

		canvas.setContents(diagram);

		diagram.add(fig, new Rectangle(20, 20, -1, -1));

		shell.setText("Diagrams");
		shell.setSize(500, 200);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}

}
