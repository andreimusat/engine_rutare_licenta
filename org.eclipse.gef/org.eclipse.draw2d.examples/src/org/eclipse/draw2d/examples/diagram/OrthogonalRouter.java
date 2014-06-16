package org.eclipse.draw2d.examples.diagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

public class OrthogonalRouter {

	public static int DEFAULT_PADDING = 30;
	public static final int NONE = -1;
	public static final int TOP = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;

	public PointList routeConnection(IFigure source, IFigure dest,
			Rectangle srcConstraints, Rectangle dstConstraints,
			String srcPortName, String dstPortName) {

		if (!(source instanceof ModuleFigure)
				|| !(dest instanceof ModuleFigure)) {
			return null;
		}

		IFigure srcPort = null;
		IFigure dstPort = null;

		Map<IFigure, Rectangle> srcChildren = ((ModuleFigure) source).children;
		for (IFigure fig : srcChildren.keySet()) {
			for (Object element : fig.getChildren()) {
				if (element instanceof Label)
					if (srcPortName.equals(((Label) element).getText())) {
						srcPort = fig;
						break;
					}
			}
		}

		Map<IFigure, Rectangle> dstChildren = ((ModuleFigure) dest).children;
		for (IFigure fig : dstChildren.keySet()) {
			for (Object element : fig.getChildren()) {
				if (element instanceof Label)
					if (srcPortName.equals(((Label) element).getText())) {
						dstPort = fig;
						break;
					}
			}
		}

		Dimension srcDim = srcPort.getPreferredSize(-1, -1);
		Dimension dstDim = dstPort.getPreferredSize(-1, -1);

		Rectangle srcBounds = srcChildren.get(srcPort);
		Rectangle dstBounds = dstChildren.get(dstPort);

		Point srcLocation = computeFigureLocation(srcBounds, srcConstraints,
				srcDim);
		Point dstLocation = computeFigureLocation(dstBounds, dstConstraints,
				dstDim);

		PointList pl = new PointList();
		List<Rectangle> obstacles = new ArrayList<Rectangle>();
		obstacles.add(srcConstraints);
		obstacles.add(dstConstraints);
		pl.addPoint(srcLocation);
		pl.addPoint(dstLocation);

		boolean finished = false;

		while (!finished) {
			for (Rectangle rectangle : obstacles) {
				int side = NONE;

				Point p1 = pl.getPoint(pl.size() - 2);
				Point p2 = pl.getPoint(pl.size() - 1);
				Point intersection = null;

				side = checkPointOnContour(p1, rectangle);

				if (side != NONE)
					intersection = p1;

				if (intersection == null) {
					side = checkPointOnContour(p2, rectangle);
					if (side != NONE)
						intersection = p2;
				}

				if (intersection == null) {
					intersection = intersects(p1, p2, rectangle.getTopLeft(),
							rectangle.getTopRight());
					side = TOP;
				}

				if (intersection == null) {
					intersection = intersects(p1, p2, rectangle.getTopRight(),
							rectangle.getBottomRight());
					side = RIGHT;
				}

				if (intersection == null) {
					intersects(p1, p2, rectangle.getBottomRight(),
							rectangle.getBottomLeft());
					side = BOTTOM;
				}

				if (intersection == null) {
					intersects(p1, p2, rectangle.getBottomLeft(),
							rectangle.getTopLeft());
					side = LEFT;
				}

				if (intersection == null || intersection.equals(p2)) {
					finished = true;
					side = NONE;
					continue;
				}

				Point intermediate = null;

				if (intersection.equals(p1)) {
					switch (side) {
					case TOP:
						intermediate = new Point(p1.x, p1.y + DEFAULT_PADDING);
						break;
					case BOTTOM:
						intermediate = new Point(p1.x, p1.y - DEFAULT_PADDING);
						break;
					case LEFT:
						intermediate = new Point(p1.x - DEFAULT_PADDING, p1.y);
						break;
					case RIGHT:
						intermediate = new Point(p1.x + DEFAULT_PADDING, p1.y);
						break;
					default:
						break;
					}

					pl.removePoint(pl.size() - 1);
					pl.addPoint(intermediate);
					pl.addPoint(dstLocation);

					continue;
				}

				switch (side) {
				case TOP:
					Point candidate1 = new Point(intersection.x
							+ DEFAULT_PADDING, intersection.y - DEFAULT_PADDING);
					Point candidate2 = new Point(intersection.x
							- DEFAULT_PADDING, intersection.y - DEFAULT_PADDING);
					int distance1 = euclidianDistanceSquared(candidate1,
							dstLocation);
					int distance2 = euclidianDistanceSquared(candidate2,
							dstLocation);
					if (distance1 < distance2)
						intermediate = candidate1;
					else
						intermediate = candidate2;
					break;
				case BOTTOM:
					candidate1 = new Point(intersection.x + DEFAULT_PADDING,
							intersection.y + DEFAULT_PADDING);
					candidate2 = new Point(intersection.x - DEFAULT_PADDING,
							intersection.y + DEFAULT_PADDING);
					distance1 = euclidianDistanceSquared(candidate1,
							dstLocation);
					distance2 = euclidianDistanceSquared(candidate2,
							dstLocation);
					if (distance1 < distance2)
						intermediate = candidate1;
					else
						intermediate = candidate2;
					break;
				case LEFT:
					candidate1 = new Point(intersection.x - DEFAULT_PADDING,
							intersection.y + DEFAULT_PADDING);
					candidate2 = new Point(intersection.x - DEFAULT_PADDING,
							intersection.y - DEFAULT_PADDING);
					distance1 = euclidianDistanceSquared(candidate1,
							dstLocation);
					distance2 = euclidianDistanceSquared(candidate2,
							dstLocation);
					if (distance1 < distance2)
						intermediate = candidate1;
					else
						intermediate = candidate2;
					break;
				case RIGHT:
					candidate1 = new Point(intersection.x + DEFAULT_PADDING,
							intersection.y + DEFAULT_PADDING);
					candidate2 = new Point(intersection.x + DEFAULT_PADDING,
							intersection.y - DEFAULT_PADDING);
					distance1 = euclidianDistanceSquared(candidate1,
							dstLocation);
					distance2 = euclidianDistanceSquared(candidate2,
							dstLocation);
					if (distance1 < distance2)
						intermediate = candidate1;
					else
						intermediate = candidate2;
					break;
				}

				pl.removePoint(pl.size() - 1);
				pl.addPoint(intermediate);
				pl.addPoint(dstLocation);

			}
		}

		normalizePath(pl);

		return pl;
	}

	public int checkPointOnContour(Point p1, Rectangle obstacle) {
		Point topLeft = obstacle.getTopLeft();
		Point topRight = obstacle.getTopRight();
		Point bottomRight = obstacle.getBottomRight();
		Point bottomLeft = obstacle.getBottomLeft();

		if ((p1.x >= topLeft.x) && (p1.x <= topRight.x) && (p1.y == topLeft.y)) {
			return TOP;
		}

		if ((p1.x >= bottomLeft.x) && (p1.x <= bottomRight.x)
				&& (p1.y == bottomLeft.y)) {
			return BOTTOM;
		}

		if ((p1.y <= bottomLeft.y) && (p1.y >= topLeft.y)
				&& (p1.x == bottomLeft.x)) {
			return LEFT;
		}

		if ((p1.y >= bottomRight.y) && (p1.y <= topRight.y)
				&& (p1.x == bottomRight.x)) {
			return RIGHT;
		}

		return NONE;
	}

	public Point intersects(Point p1, Point p2, Point p3, Point p4) {
		Point intersection = null;

		double s1X, s1Y, s2X, s2Y;
		double s, t;

		s1X = p2.x - p1.x;
		s1Y = p2.y - p1.y;
		s2X = p4.x - p3.x;
		s2Y = p4.y - p3.y;

		s = (-s1Y * (p1.x - p3.x) + s1X * (p1.y - p3.y))
				/ (-s2X * s1Y + s1X * s2Y);
		t = (s2X * (p1.y - p3.y) - s2Y * (p1.x - p3.x))
				/ (-s2X * s1Y + s1X * s2Y);

		if ((s > 0) && (s < 1) && (t > 0) && (t < 1)) {
			intersection = new Point((int) (p1.x + (t * s1X)),
					(int) (p1.y + (t * s1Y)));
		}

		return intersection;
	}

	public void normalizePath(PointList path) {
		PointList normalized = new PointList();
		normalized.addPoint(path.getFirstPoint());
		for (int i = 0; i < path.size() - 1; i++) {
			Point p1 = path.getPoint(i);
			Point p2 = path.getPoint(i + 1);
			if (!areContinuous(p1, p2)) {
				normalized.addPoint(p1.x, p2.y);
				normalized.addPoint(p2);
			} else {
				normalized.addPoint(p2);
			}
		}

		path.removeAllPoints();
		path.addAll(normalized);
	}

	public boolean areContinuous(Point p1, Point p2) {
		return ((p1.x == p2.x) || (p1.y == p2.y));
	}

	public Point computeFigureLocation(Rectangle bounds, Rectangle constraints,
			Dimension dimension) {

		int boundsX = bounds.x;
		int boundsY = bounds.y;

		int parentX = constraints.x;
		int parentY = constraints.y;

		return new Point(boundsX + parentX, parentY + boundsY
				+ (dimension.height / 2));
	}

	public double computeAngle(Point p1, Point p2) {
		int deltaY = p2.y - p1.y;
		int deltaX = p2.x - p1.x;

		return (Math.atan2(deltaY, deltaX) * 180 / Math.PI);
	}

	public int euclidianDistanceSquared(Point p1, Point p2) {
		return (int) (Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
	}

}
