package org.eclipse.draw2d.examples.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class CustomDiagramDrawer {

	private static Map<String, String> ports = new HashMap<String, String>();

	public static List<IFigure> figures = new ArrayList<IFigure>();

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

		Rectangle constraint = new Rectangle(400, 20, 100, 100);

		IFigure fig = cd.buildDiagram("mod", ports, constraint);
		figures.add(fig);

		canvas.setContents(diagram);

		diagram.add(fig, constraint);

		Rectangle constraint2 = new Rectangle(400, 150, 150, 150);

		IFigure fig2 = cd.buildDiagram("anotherMod", ports, constraint2);
		figures.add(fig2);

		diagram.add(fig2, constraint2);

		OrthogonalRouter router = new OrthogonalRouter();

		PointList pl = router.routeConnection(fig, fig2, constraint,
				constraint2, "a", "a");

		PolylineConnection assoc = new PolylineConnection();
		PolygonDecoration containment = new PolygonDecoration();
		assoc.setTargetDecoration(containment);
		assoc.setPoints(pl);
		diagram.add(assoc);

		PointList pl2 = router.routeConnection(fig2, fig, constraint2,
				constraint, "b", "b");

		PolylineConnection assoc2 = new PolylineConnection();
		PolygonDecoration containment2 = new PolygonDecoration();
		assoc2.setTargetDecoration(containment2);
		assoc2.setPoints(pl2);
		diagram.add(assoc2);

		shell.setText("Diagrams");
		shell.setSize(800, 600);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}
}
