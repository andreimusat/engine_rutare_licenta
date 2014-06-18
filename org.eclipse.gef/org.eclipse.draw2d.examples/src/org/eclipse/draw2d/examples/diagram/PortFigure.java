package org.eclipse.draw2d.examples.diagram;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class PortFigure extends Figure {

	private String direction;
	private Rectangle constraints;
	private String label;

	private static Dimension offset = new Dimension();

	public PortFigure() {
	}

	public PortFigure(String dir) {
		this.direction = dir;
	}

	public PortFigure(String dir, Rectangle constraints) {
		this(dir);
		this.setConstraints(constraints);
	}

	public PortFigure(String dir, Rectangle constraints, String label) {
		this(dir, constraints);
		this.setLabel(label);
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String dir) {
		this.direction = dir;
	}

	public Rectangle getConstraints() {
		return constraints;
	}

	public void setConstraints(Rectangle constraints) {
		this.constraints = constraints;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void addMouseListeners() {
		addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent event) {
				event.consume();
				offset.setWidth(event.x - getLocation().x());
				offset.setHeight(event.y - getLocation().y());
			}
		});

		addMouseMotionListener(new MouseMotionListener.Stub() {
			public void mouseDragged(MouseEvent event) {

				Rectangle rect = getBounds().getCopy();
				rect.setX(event.x - offset.width());
				rect.setY(event.y - offset.height());
				setBounds(rect);
				System.out.println(getBounds().x + " | " + getBounds().y);
				getParent().repaint();
			}
		});
	}

	public Point externalGetLocation() {
		System.out.println(getBounds().x + " | " + getBounds().y);
		return new Point(getBounds().x, getBounds().y);
	}
}
