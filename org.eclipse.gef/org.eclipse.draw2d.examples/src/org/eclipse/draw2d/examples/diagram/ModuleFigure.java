package org.eclipse.draw2d.examples.diagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
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
	public static int VERTICAL_PADDING = 25;

	public List<IFigure> children = new ArrayList<IFigure>();

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

		Dimension labelDim = header.getPreferredSize(-1, -1);

		int headerX = ((parentConstraints.width - parentConstraints.x) / 2)
				- (labelDim.width / 4);
		int headerY = 1;

		add(header, new Rectangle(headerX, headerY, labelDim.width,
				labelDim.height));

		int portX = 0;
		int portY = (parentConstraints.height - parentConstraints.y) / 2;

		for (String key : ports.keySet()) {
			Figure port = new Figure();
			AbstractLayout layout = new ToolbarLayout();
			port.setLayoutManager(layout);
			port.add(new Label(key));
			port.setFont(NORMAL);
			port.setBorder(new SeparatorBorder());
			if ("input".equals(ports.get(key))) {
				port.setForegroundColor(INPUT);
				portX = 0;
			} else {
				port.setForegroundColor(OUTPUT);
				portX = (parentConstraints.width - parentConstraints.x);
			}

			children.add(port);

			Dimension someDim = port.getPreferredSize(-1, -1);

			add(port,
					new Rectangle(portX, portY, someDim.width, someDim.height));
		}

		setOpaque(true);
		setBackgroundColor(BG);
	}
}
