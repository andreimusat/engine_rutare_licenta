package org.eclipse.draw2d.examples.diagram;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

public class OrthogonalConnection extends PolylineConnection {

	private OrthogonalRouter router;
	private Figure source;
	private Figure destination;
	private Rectangle srcConstraint;
	private Rectangle dstConstraint;
	private String srcPortName;
	private String dstPortName;

	public OrthogonalConnection() {
	}

	public OrthogonalConnection(OrthogonalRouter router, IFigure src, IFigure dst, Rectangle srcConstraint, Rectangle dstConstraint, String srcPortName, String dstPortname) {
		this.router = router;
		this.source = (Figure) src;
		this.destination = (Figure) dst;
		this.srcConstraint = srcConstraint;
		this.dstConstraint = dstConstraint;
		this.srcPortName = srcPortName;
		this.dstPortName = dstPortname;
	}

	public void updateFigure(Figure fig, Rectangle constraint) {
		if (fig.equals(source))
			srcConstraint = constraint;
		else if (fig.equals(destination))
			dstConstraint = constraint;
	}

	public void figureLocationChanged() {
		updateFiguresConstraints();
		PointList newPoints = router.rerouteConnection(source, destination, srcConstraint, dstConstraint, srcPortName, dstPortName);

		setPoints(newPoints);
	}

	public OrthogonalRouter getRouter() {
		return router;
	}

	public void setRouter(OrthogonalRouter router) {
		this.router = router;
	}

	public Figure getSource() {
		return source;
	}

	public void setSource(Figure source) {
		this.source = source;
	}

	public Figure getDestination() {
		return destination;
	}

	public void setDestination(Figure destination) {
		this.destination = destination;
	}

	public Rectangle getSrcConstraint() {
		return srcConstraint;
	}

	public void setSrcConstraint(Rectangle srcConstraint) {
		this.srcConstraint = srcConstraint;
	}

	public Rectangle getDstConstraint() {
		return dstConstraint;
	}

	public void setDstConstraint(Rectangle dstConstraint) {
		this.dstConstraint = dstConstraint;
	}

	public String getSrcPortName() {
		return srcPortName;
	}

	public void setSrcPortName(String srcPortName) {
		this.srcPortName = srcPortName;
	}

	public String getDstPortName() {
		return dstPortName;
	}

	public void setDstPortName(String dstPortName) {
		this.dstPortName = dstPortName;
	}

	public void updateFiguresConstraints() {
		srcConstraint = source.getBounds();
		dstConstraint = destination.getBounds();
	}

}
