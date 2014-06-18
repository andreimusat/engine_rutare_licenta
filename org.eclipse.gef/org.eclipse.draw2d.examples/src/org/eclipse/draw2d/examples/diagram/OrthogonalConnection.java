package org.eclipse.draw2d.examples.diagram;

import org.eclipse.draw2d.Figure;
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

	public void figureLocationChanged() {
		PointList newPoints = router.rerouteConnection(source, destination,
				srcConstraint, dstConstraint, srcPortName, dstPortName);
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

}
