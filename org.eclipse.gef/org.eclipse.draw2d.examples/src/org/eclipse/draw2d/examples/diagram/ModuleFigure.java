package org.eclipse.draw2d.examples.diagram;

import java.util.Map;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class ModuleFigure extends Figure {

	static final Color BG = new Color(null, 242, 240, 255);
	static final Color INPUT = new Color(null, 0, 255, 0);
	static final Color OUTPUT = new Color(null, 255, 0, 0);
	static Font BOLD = new Font(null, "", 10, SWT.BOLD);
	public static int VERTICAL_PADDING = 25;

	public ModuleFigure(String name, Map<String, String> ports,
			Rectangle parentConstraints) {

		class SeparatorBorder extends MarginBorder {
			SeparatorBorder() {
				super(5, 5, 10, 5);
			}
		}
		setBorder(new LineBorder());
		setLayoutManager(new XYLayout());

		Label header = new Label(name);
		header.setFont(BOLD);
		header.setBorder(new MarginBorder(3, 5, 3, 5));

		int headerX = parentConstraints.getTop().x / 2;
		int headerY = 1;

		add(header, new Rectangle(headerX, headerY, -1, -1));

		int portX = 0;
		int portY = parentConstraints.getCenter().y / 2;

		for (String key : ports.keySet()) {
			Figure port = new Figure();
			AbstractLayout layout = new ToolbarLayout();
			port.setLayoutManager(layout);
			port.add(new Label(key));
			port.setBorder(new SeparatorBorder());
			if ("input".equals(ports.get(key))) {
				port.setForegroundColor(INPUT);
				portX = 5;
			} else {
				port.setForegroundColor(OUTPUT);
				portX = parentConstraints.getRight().x / 2 + 20;
			}

			add(port, new Rectangle(portX, portY, -1, -1));
		}

		setOpaque(true);
		setBackgroundColor(BG);
	}
}
