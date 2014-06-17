package org.eclipse.draw2d.examples.diagram;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class ModuleFigure extends Figure {

	static final Color BG = new Color(null, 242, 240, 255);
	static final Color INPUT = new Color(null, 0, 255, 0);
	static final Color OUTPUT = new Color(null, 255, 0, 0);
	static Font BOLD = new Font(null, "", 10, SWT.BOLD);
	static Font NORMAL = new Font(null, "", 10, SWT.NORMAL);
	public static int MAGIC_CONSTANT = 20;

	public Map<IFigure, Rectangle> children = new HashMap<IFigure, Rectangle>();

	private static Dimension offset = new Dimension();

	public ModuleFigure(String name, Map<String, String> ports,
			Rectangle parentConstraints) {

		setBorder(new LineBorder());
		setLayoutManager(new XYLayout());

		Label header = new Label(name);
		header.setFont(BOLD);
		header.setBorder(new MarginBorder(3, 5, 3, 5));

		Dimension labelDim = header.getPreferredSize(-1, -1);
		int headerX = (parentConstraints.width - labelDim.width) / 2;
		int headerY = 1;
		Rectangle headerConstraints = new Rectangle(headerX, headerY,
				labelDim.width, labelDim.height);
		children.put(header, headerConstraints);
		add(header, headerConstraints);

		int portX = 0;
		int portY = 0;
		int defaultPortY = (parentConstraints.height) / 2;

		for (String key : ports.keySet()) {
			PortFigure port = new PortFigure();
			AbstractLayout layout = new ToolbarLayout();
			port.setLayoutManager(layout);
			port.add(new Label(key));
			port.setFont(NORMAL);
			port.setBorder(new SeparatorBorder(5, 5, 10, 5));
			Dimension someDim = port.getPreferredSize(-1, -1);
			portY = defaultPortY - someDim.height / 2;
			if ("input".equals(ports.get(key))) {
				port.setForegroundColor(INPUT);
				portX = 0;
				port.setDirection("input");
			} else {
				port.setForegroundColor(OUTPUT);
				portX = (parentConstraints.width - (someDim.width / 2) - (MAGIC_CONSTANT / 2));
				port.setDirection("output");
			}

			Rectangle portConstraints = new Rectangle(portX, portY,
					someDim.width, someDim.height);
			children.put(port, portConstraints);
			add(port, portConstraints);
		}

		setOpaque(true);
		setBackgroundColor(BG);

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
				getParent().repaint();
			}
		});
	}

	class SeparatorBorder extends MarginBorder {
		SeparatorBorder(int tPadding, int lPadding, int bPadding, int rPadding) {
			super(tPadding, lPadding, bPadding, rPadding);
		}
	}
}
