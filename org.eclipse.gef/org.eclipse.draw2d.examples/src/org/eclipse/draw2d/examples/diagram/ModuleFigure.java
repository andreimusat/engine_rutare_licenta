package org.eclipse.draw2d.examples.diagram;

import java.awt.Rectangle;
import java.util.Map;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class ModuleFigure extends Figure {

	static final Color BG = new Color(null, 242, 240, 255);
	static final Color INPUT = new Color(null, 0, 255, 0);
	static final Color OUTPUT = new Color(null, 255, 0, 0);
	static Font BOLD = new Font(null, "", 10, SWT.BOLD);

	public ModuleFigure(String name, Map<String, String> ports) {

		class SeparatorBorder extends MarginBorder {
			SeparatorBorder() {
				super(5, 5, 10, 5);
			}
		}

		Label header = new Label(name);
		header.setFont(BOLD);
		header.setBorder(new MarginBorder(3, 5, 3, 5));

		add(header, new Rectangle(21, 21, -1, -1));

		for (String key : ports.keySet()) {
			Figure port = new Figure();
			AbstractLayout layout = new ToolbarLayout();
			port.setLayoutManager(layout);
			port.add(new Label(key));
			port.setBorder(new SeparatorBorder());
			if ("input".equals(ports.get(key)))
				port.setForegroundColor(INPUT);
			else
				port.setForegroundColor(OUTPUT);

			add(port, new Rectangle(21, 21, -1, -1));
		}

		setBorder(new LineBorder());
		setLayoutManager(new XYLayout());

		setOpaque(true);
		setBackgroundColor(BG);
	}
}
