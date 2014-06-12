package org.eclipse.draw2d.examples.diagram;

import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

public class CustomDiagram {

	public IFigure buildDiagram(String name, Map<String, String> ports) {

		Figure diagram = new Figure();
		diagram.setLayoutManager(new XYLayout());

		Rectangle constraint = new Rectangle(10, 10, 100, 100);
		ModuleFigure figure = new ModuleFigure(name, ports);

		diagram.add(figure, constraint);

		return diagram;
	}

}
