package org.eclipse.draw2d.examples.diagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;

public class CustomDiagram {

	List<ModuleFigure> figures = new ArrayList<ModuleFigure>();

	public ModuleFigure buildDiagram(String name, Map<String, String> ports,
			Rectangle constraint) {

		ModuleFigure figure = new ModuleFigure(name, ports, constraint);

		figures.add(figure);

		return figure;
	}

}
