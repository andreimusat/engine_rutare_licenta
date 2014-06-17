package org.eclipse.draw2d.examples.diagram;

import org.eclipse.draw2d.Figure;

public class PortFigure extends Figure {

	private String direction;

	public PortFigure() {
	}

	public PortFigure(String dir) {
		this.direction = dir;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String dir) {
		this.direction = dir;
	}
}
