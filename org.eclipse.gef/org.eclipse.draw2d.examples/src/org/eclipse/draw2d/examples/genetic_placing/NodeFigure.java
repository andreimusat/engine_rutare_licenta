package org.eclipse.draw2d.examples.genetic_placing;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class NodeFigure extends Figure {

	public Node node;

	public static final Color BG = new Color(null, 242, 240, 255);
	public static final Color NAME_COLOUR = new Color(null, 255, 0, 0);
	public static Font BOLD = new Font(null, "", 10, SWT.BOLD);
	public static Font NORMAL = new Font(null, "", 10, SWT.NORMAL);

	public Label header = new Label();

	public NodeFigure(Node n) {
		this.node = n;

		setBorder(new LineBorder());
		setLayoutManager(new XYLayout());

		Label header = new Label(node.name);
		header.setFont(BOLD);
		header.setBorder(new MarginBorder(3, 5, 3, 5));

		Dimension labelDim = header.getPreferredSize(-1, -1);
		int headerX = (node.constraint.width - labelDim.width) / 2;
		int headerY = (node.constraint.height - labelDim.height) / 2;
		Rectangle headerConstraints = new Rectangle(headerX, headerY, labelDim.width, labelDim.height);
		add(header, headerConstraints);

	}

}
