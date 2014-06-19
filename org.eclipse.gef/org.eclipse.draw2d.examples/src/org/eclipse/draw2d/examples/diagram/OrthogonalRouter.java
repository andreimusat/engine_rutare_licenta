package org.eclipse.draw2d.examples.diagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
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

	public List<Rectangle> obstacles = new ArrayList<Rectangle>();
	public Rectangle dstConstraints;
	public Point dstLocation;

	public PointList routeConnection(IFigure source, IFigure dest, Rectangle srcConstraints, Rectangle dstConstraints, String srcPortName, String dstPortName) {

		if (!(source instanceof ModuleFigure) || !(dest instanceof ModuleFigure)) {
			return null;
		}

		this.dstConstraints = dstConstraints;
		IFigure srcPort = null;
		IFigure dstPort = null;

		Map<IFigure, Rectangle> srcChildren = ((ModuleFigure) source).children;
		for (IFigure fig : srcChildren.keySet()) {
			if (fig instanceof PortFigure) {
				if (((PortFigure) fig).getLabel().equals(srcPortName)) {
					srcPort = fig;
					break;
				}
			}
		}

		Map<IFigure, Rectangle> dstChildren = ((ModuleFigure) dest).children;
		for (IFigure fig : dstChildren.keySet()) {
			if (fig instanceof PortFigure) {
				if (((PortFigure) fig).getLabel().equals(dstPortName)) {
					dstPort = fig;
					break;
				}
			}
		}

		Dimension srcDim = srcPort.getPreferredSize(-1, -1);
		Dimension dstDim = dstPort.getPreferredSize(-1, -1);

		Rectangle srcBounds = srcChildren.get(srcPort);
		Rectangle dstBounds = dstChildren.get(dstPort);

		Point srcLocation = computeFigureLocation(srcBounds, srcConstraints, srcDim, ((PortFigure) srcPort).getDirection());
		dstLocation = computeFigureLocation(dstBounds, dstConstraints, dstDim, ((PortFigure) dstPort).getDirection());

		obstacles.add(srcConstraints);
		for (Rectangle r : CustomDiagramDrawer.constraints) {
			if (!obstacles.contains(r))
				obstacles.add(r);
		}

		PointList pl = computePath(srcConstraints, srcLocation, dstLocation);

		obstacles.clear();
		return pl;
	}

	public PointList rerouteConnection(IFigure source, IFigure dest, Rectangle srcConstraints, Rectangle dstConstraints, String srcPortName, String dstPortName) {
		if (!(source instanceof ModuleFigure) || !(dest instanceof ModuleFigure)) {
			return null;
		}

		this.dstConstraints = dstConstraints;
		IFigure srcPort = null;
		IFigure dstPort = null;

		Map<IFigure, Rectangle> srcChildren = ((ModuleFigure) source).children;
		for (IFigure fig : srcChildren.keySet()) {
			if (fig instanceof PortFigure) {
				if (((PortFigure) fig).getLabel().equals(srcPortName)) {
					srcPort = fig;
					break;
				}
			}
		}

		Map<IFigure, Rectangle> dstChildren = ((ModuleFigure) dest).children;
		for (IFigure fig : dstChildren.keySet()) {
			if (fig instanceof PortFigure) {
				if (((PortFigure) fig).getLabel().equals(dstPortName)) {
					dstPort = fig;
					break;
				}
			}
		}

		Dimension srcDim = srcPort.getPreferredSize(-1, -1);
		Dimension dstDim = dstPort.getPreferredSize(-1, -1);

		Rectangle srcBounds = srcChildren.get(srcPort);
		Rectangle dstBounds = dstChildren.get(dstPort);

		Point srcLocation = computeFigureLocation(srcBounds, srcConstraints, srcDim, ((PortFigure) srcPort).getDirection());
		dstLocation = computeFigureLocation(dstBounds, dstConstraints, dstDim, ((PortFigure) dstPort).getDirection());

		obstacles.add(srcConstraints);
		for (IFigure fig : CustomDiagramDrawer.figures) {
			if (!obstacles.contains(fig.getBounds()))
				obstacles.add(fig.getBounds());
		}

		PointList pl = computePath(srcConstraints, srcLocation, dstLocation);

		obstacles.clear();
		return pl;
	}

	public PointList computePath(Rectangle srcConstraints, Point srcLocation, Point dstLocation) {
		PointList pl = new PointList();
		pl.addPoint(srcLocation);
		pl.addPoint(dstLocation);

		boolean finished = false;

		int side = NONE;
		Point intersection = null;
		Point intermediate = null;
		Point p1 = pl.getFirstPoint();
		side = checkPointOnContour(p1, srcConstraints);

		if (side != NONE)
			intersection = p1;

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
		}

		while (!finished) {
			int clear = 0;
			for (int i = 1; i < pl.size() - 1; i++) {
				boolean modification = false;
				p1 = pl.getPoint(i);
				Point p2 = pl.getPoint(i + 1);
				for (Rectangle rectangle : obstacles) {
					Point topIntersection = null;
					Point bottomIntersection = null;
					Point leftIntersection = null;
					Point rightIntersection = null;
					side = NONE;
					intersection = null;
					intermediate = null;

					topIntersection = intersects(p1, p2, rectangle.getTopLeft(), rectangle.getTopRight());

					rightIntersection = intersects(p1, p2, rectangle.getTopRight(), rectangle.getBottomRight());

					bottomIntersection = intersects(p1, p2, rectangle.getBottomRight(), rectangle.getBottomLeft());

					leftIntersection = intersects(p1, p2, rectangle.getBottomLeft(), rectangle.getTopLeft());

					intersection = getClosestIntersection(p1, topIntersection, leftIntersection, rightIntersection, bottomIntersection);
					side = getSideOfIntersection(intersection, topIntersection, leftIntersection, rightIntersection, bottomIntersection);

					if (intersection == null) {
						side = NONE;
						side = checkPointOnContour(p2, rectangle);
						if (side != NONE)
							intersection = p2;
					}

					if (intersection == null || intersection.equals(p2)) {
						clear++;
						side = NONE;
						continue;
					}

					intermediate = computePathPoint(rectangle, intersection, pl, side);
					modification = true;
					pl.insertPoint(intermediate, i + 1);
					break;
				}
				if (modification)
					break;
			}
			if (clear == obstacles.size() * (pl.size() - 2))
				finished = true;
		}

		normalizePath(pl);
		removeExtraPoints(pl);
		shortenPath(pl);
		return pl;
	}

	public Point getClosestIntersection(Point initial, Point top, Point left, Point right, Point bottom) {
		Point result = null;

		int minDistance = Integer.MAX_VALUE;
		int distance = Integer.MAX_VALUE;

		if (top != null) {
			distance = euclidianDistanceSquared(initial, top);
			if (distance < minDistance) {
				minDistance = distance;
				result = top;
			}
		}

		if (left != null) {
			distance = euclidianDistanceSquared(initial, left);
			if (distance < minDistance) {
				minDistance = distance;
				result = left;
			}
		}

		if (right != null) {
			distance = euclidianDistanceSquared(initial, right);
			if (distance < minDistance) {
				minDistance = distance;
				result = right;
			}
		}

		if (bottom != null) {
			distance = euclidianDistanceSquared(initial, bottom);
			if (distance < minDistance) {
				minDistance = distance;
				result = bottom;
			}
		}

		return result;
	}

	public int getSideOfIntersection(Point intersection, Point top, Point left, Point right, Point bottom) {

		if (intersection == null)
			return NONE;

		if (top != null && intersection.equals(top))
			return TOP;

		if (left != null && intersection.equals(left))
			return LEFT;

		if (right != null && intersection.equals(right))
			return RIGHT;

		if (bottom != null && intersection.equals(bottom))
			return BOTTOM;

		return NONE;
	}

	public Point computePathPoint(Rectangle obstacle, Point intersection, PointList path, int side) {
		Point intermediate = null;

		Point bottomLeft = obstacle.getBottomLeft();
		Point bottomRight = obstacle.getBottomRight();
		Point topLeft = obstacle.getTopLeft();
		Point topRight = obstacle.getTopRight();

		Point bottomLeftAvoid = new Point(bottomLeft.x - DEFAULT_PADDING, bottomLeft.y + DEFAULT_PADDING);
		Point bottomRightAvoid = new Point(bottomRight.x + DEFAULT_PADDING, bottomRight.y + DEFAULT_PADDING);
		Point topLeftAvoid = new Point(topLeft.x - DEFAULT_PADDING, topLeft.y - DEFAULT_PADDING);
		Point topRightAvoid = new Point(topRight.x + DEFAULT_PADDING, topRight.y - DEFAULT_PADDING);

		switch (side) {
		case TOP:
			int distance1 = euclidianDistanceSquared(intersection, topLeft);
			int distance2 = euclidianDistanceSquared(intersection, topRight);
			if (distance1 < distance2) {
				// first candidate is topLeft, then topRight and last
				// bottomLeft
				if (!isCollision(topLeftAvoid) && !path.contains(topLeftAvoid))
					intermediate = topLeftAvoid;
				else if (!isCollision(topRightAvoid) && !path.contains(topRightAvoid))
					intermediate = topRightAvoid;
				else if (!isCollision(bottomLeftAvoid) && !path.contains(bottomLeftAvoid))
					intermediate = bottomLeftAvoid;
				else
					intermediate = new Point(topLeftAvoid.x - DEFAULT_PADDING, topLeftAvoid.y);
			} else {
				// first candidate is topLeft, then topRight and last
				// bottomLeft
				if (!isCollision(topRightAvoid) && !path.contains(topRightAvoid))
					intermediate = topRightAvoid;
				else if (!isCollision(topLeftAvoid) && !path.contains(topLeftAvoid))
					intermediate = topLeftAvoid;
				else if (!isCollision(bottomRightAvoid) && !path.contains(bottomRightAvoid))
					intermediate = bottomRightAvoid;
				else
					intermediate = new Point(topRightAvoid.x + DEFAULT_PADDING, topLeftAvoid.y);
			}
			break;
		case BOTTOM:
			distance1 = euclidianDistanceSquared(intersection, bottomLeft);
			distance2 = euclidianDistanceSquared(intersection, bottomRight);
			if (distance1 < distance2) {
				if (!isCollision(bottomLeftAvoid) && !path.contains(bottomLeftAvoid))
					intermediate = bottomLeftAvoid;
				else if (!isCollision(bottomRightAvoid) && !path.contains(bottomRightAvoid))
					intermediate = bottomRightAvoid;
				else if (!isCollision(topLeftAvoid) && !path.contains(topLeftAvoid))
					intermediate = topLeftAvoid;
				else
					intermediate = new Point(bottomLeftAvoid.x - DEFAULT_PADDING, bottomLeftAvoid.y);
			} else {
				if (!isCollision(bottomRightAvoid) && !path.contains(bottomRightAvoid))
					intermediate = bottomRightAvoid;
				else if (!isCollision(bottomLeftAvoid) && !path.contains(bottomLeftAvoid))
					intermediate = bottomLeftAvoid;
				else if (!isCollision(topLeftAvoid) && !path.contains(topLeftAvoid))
					intermediate = topLeftAvoid;
				else
					intermediate = new Point(bottomRightAvoid.x + DEFAULT_PADDING, bottomRightAvoid.y);
			}
			break;
		case LEFT:
			distance1 = euclidianDistanceSquared(intersection, bottomLeft);
			distance2 = euclidianDistanceSquared(intersection, topLeft);
			if (distance1 < distance2) {
				if (!isCollision(bottomLeftAvoid) && !path.contains(bottomLeftAvoid))
					intermediate = bottomLeftAvoid;
				else if (!isCollision(topLeftAvoid) && !path.contains(topLeftAvoid))
					intermediate = topLeftAvoid;
				else if (!isCollision(bottomRightAvoid) && !path.contains(bottomRightAvoid))
					intermediate = bottomRightAvoid;
				else
					intermediate = new Point(bottomLeftAvoid.x, bottomLeftAvoid.y + DEFAULT_PADDING);
			} else {
				if (!isCollision(topLeftAvoid) && !path.contains(topLeftAvoid))
					intermediate = topLeftAvoid;
				else if (!isCollision(bottomLeftAvoid) && !path.contains(bottomLeftAvoid))
					intermediate = bottomLeftAvoid;
				else if (!isCollision(topRightAvoid) && !path.contains(topRightAvoid))
					intermediate = topRightAvoid;
				else
					intermediate = new Point(topLeftAvoid.x, topLeftAvoid.y - DEFAULT_PADDING);
			}
			break;
		case RIGHT:
			distance1 = euclidianDistanceSquared(intersection, bottomRight);
			distance2 = euclidianDistanceSquared(intersection, topRight);
			if (distance1 < distance2) {
				if (!isCollision(bottomRightAvoid) && !path.contains(bottomRightAvoid))
					intermediate = bottomRightAvoid;
				else if (!isCollision(topRightAvoid) && !path.contains(topRightAvoid))
					intermediate = topRightAvoid;
				else if (!isCollision(bottomLeftAvoid) && !path.contains(bottomLeftAvoid))
					intermediate = bottomLeftAvoid;
				else
					intermediate = new Point(bottomRightAvoid.x, bottomRightAvoid.y + DEFAULT_PADDING);
			} else {
				if (!isCollision(topRightAvoid) && !path.contains(topRightAvoid))
					intermediate = topRightAvoid;
				else if (!isCollision(bottomRightAvoid) && !path.contains(bottomRightAvoid))
					intermediate = bottomRightAvoid;
				else if (!isCollision(topLeftAvoid) && !path.contains(topLeftAvoid))
					intermediate = topLeftAvoid;
				else
					intermediate = new Point(topRightAvoid.x, topRightAvoid.y - DEFAULT_PADDING);
			}
			break;
		}

		return intermediate;
	}

	public boolean isCollision(Point p) {
		boolean result = false;
		for (Rectangle obstacle : obstacles) {
			if (obstacle.contains(p)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public int checkPointOnContour(Point p1, Rectangle obstacle) {
		Point topLeft = obstacle.getTopLeft();
		Point topRight = obstacle.getTopRight();
		Point bottomRight = obstacle.getBottomRight();
		Point bottomLeft = obstacle.getBottomLeft();

		if ((p1.x >= topLeft.x) && (p1.x <= topRight.x) && (p1.y == topLeft.y)) {
			return TOP;
		}

		if ((p1.x >= bottomLeft.x) && (p1.x <= bottomRight.x) && (p1.y == bottomLeft.y)) {
			return BOTTOM;
		}

		if ((p1.y <= bottomLeft.y) && (p1.y >= topLeft.y) && (p1.x == bottomLeft.x)) {
			return LEFT;
		}

		if ((p1.y <= bottomRight.y) && (p1.y >= topRight.y) && (p1.x == bottomRight.x)) {
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

		s = (-s1Y * (p1.x - p3.x) + s1X * (p1.y - p3.y)) / (-s2X * s1Y + s1X * s2Y);
		t = (s2X * (p1.y - p3.y) - s2Y * (p1.x - p3.x)) / (-s2X * s1Y + s1X * s2Y);

		if ((s > 0) && (s < 1) && (t > 0) && (t < 1)) {
			intersection = new Point((int) (p1.x + (t * s1X)), (int) (p1.y + (t * s1Y)));
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
				Point normal = new Point(p1.x, p2.y);
				if (isValidMerge(normal, p1) && isValidMerge(normal, p2))
					normalized.addPoint(normal);
				else
					normalized.addPoint(p2.x, p1.y);
				normalized.addPoint(p2);
			} else {
				normalized.addPoint(p2);
			}
		}

		path.removeAllPoints();
		path.addAll(normalized);
	}

	public void removeExtraPoints(PointList path) {
		boolean done = false;
		while (!done) {
			boolean modified = false;
			for (int i = 0; i < path.size() - 2; i++) {
				Point p1 = path.getPoint(i);
				Point p2 = path.getPoint(i + 1);
				Point p3 = path.getPoint(i + 2);
				if (areColinear(p1, p2, p3)) {
					path.removePoint(i + 1);
					modified = true;
					break;
				}
			}
			if (modified)
				continue;
			done = true;
		}
	}

	public boolean areColinear(Point p1, Point p2, Point p3) {
		return (p1.y - p2.y) * (p1.x - p3.x) == (p1.y - p3.y) * (p1.x - p2.x);
	}

	public void shortenPath(PointList path) {
		boolean finished = false;
		int index = 0;
		boolean modified = false;
		while (!finished) {
			Point merged = null;
			for (int i = 1; i < path.size() - 3; i++) {
				Point start = path.getPoint(i);
				Point end = path.getPoint(i + 2);
				Point mid = path.getPoint(i + 1);
				merged = mergePoints(start, end, mid);
				if (isValidMerge(merged, path.getPoint(i - 1)) && isValidMerge(merged, path.getPoint(i + 3))) {
					index = i;
					path.removePoint(i);
					path.removePoint(i);
					path.removePoint(i);
					path.insertPoint(merged, index);
					modified = true;
					break;
				}
			}
			if (modified) {
				modified = false;
				continue;
			}
			finished = true;
		}
	}

	public Point mergePoints(Point segmentStart, Point segmentEnd, Point middle) {
		Point segmentMid = new Point((segmentStart.x + segmentEnd.x) / 2, (segmentStart.y + segmentEnd.y) / 2);
		Point merged = new Point(2 * segmentMid.x - middle.x, 2 * segmentMid.y - middle.y);

		return merged;
	}

	public boolean isValidMerge(Point p, Point previous) {
		boolean result = true;
		Rectangle limit = new Rectangle(p, previous);
		for (Rectangle obstacle : obstacles) {
			if (obstacle.equals(dstConstraints) && obstacle.contains(p)) {
				result = false;
				break;
			}

			if (obstacle.contains(p) || obstacle.intersects(limit)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public boolean areContinuous(Point p1, Point p2) {
		return ((p1.x == p2.x) || (p1.y == p2.y));
	}

	public Point computeFigureLocation(Rectangle bounds, Rectangle constraints, Dimension dimension, String direction) {

		int boundsY = bounds.y;

		int parentX = constraints.x;
		int parentY = constraints.y;

		Point result = null;

		if ("input".equals(direction)) {
			result = new Point(parentX, parentY + boundsY + (dimension.height / 2));
		} else if ("output".equals(direction)) {
			result = new Point(parentX + constraints.width, parentY + boundsY + (dimension.height / 2));
		}

		return result;
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
