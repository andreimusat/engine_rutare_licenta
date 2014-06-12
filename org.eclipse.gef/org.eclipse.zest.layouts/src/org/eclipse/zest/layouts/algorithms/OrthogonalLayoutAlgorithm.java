package org.eclipse.zest.layouts.algorithms;

import java.util.Random;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

public class OrthogonalLayoutAlgorithm extends AbstractLayoutAlgorithm {

	private double boundsX;
	private double boundsY;
	private double boundsWidth;
	private double boundsHeight;
	private DisplayIndependentRectangle layoutBounds = null;
	int steps = 3;
	private int numConns;

	public OrthogonalLayoutAlgorithm() {
		this(LayoutStyles.NONE);
	}

	public OrthogonalLayoutAlgorithm(int styles) {
		super(styles);
	}

	public OrthogonalLayoutAlgorithm(int styles, int numConnections) {
		super(styles);
		this.numConns = numConnections;
	}

	public void setLayoutArea(double x, double y, double width, double height) {
		layoutBounds = new DisplayIndependentRectangle(x, y, width, height);
	}

	protected boolean isValidConfiguration(boolean asynchronous,
			boolean continuous) {
		if (asynchronous && continuous) {
			return false;
		} else if (asynchronous && !continuous) {
			return true;
		} else if (!asynchronous && continuous) {
			return false;
		} else if (!asynchronous && !continuous) {
			return true;
		}

		return false;
	}

	protected void applyLayoutInternal(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider, double boundsX,
			double boundsY, double boundsWidth, double boundsHeight) {

		fireProgressEvent(1, 3);

		for (InternalRelationship relationship : relationshipsToConsider) {

			InternalNode src = relationship.getSource();
			InternalNode dst = relationship.getDestination();

			DisplayIndependentPoint srcPos = src.getInternalLocation();
			DisplayIndependentPoint dstPos = dst.getInternalLocation();

			if ((srcPos.x != dstPos.x) && (srcPos.y != dstPos.y)) {
				relationship.addBendPoint(srcPos.x, srcPos.y);
				relationship.addBendPoint(srcPos.x, dstPos.y);
				relationship.addBendPoint(dstPos.x, dstPos.y);
			}

		}

		fireProgressEvent(2, 3);

		updateLayoutLocations(entitiesToLayout);
		updateEntities(entitiesToLayout);
		updateBendPoints(relationshipsToConsider);
		fireProgressEvent(3, 3);
	}

	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider, double x, double y,
			double width, double height) {

		boundsX = x;
		boundsY = y;
		boundsWidth = width;
		boundsHeight = height;
		layoutBounds = new DisplayIndependentRectangle(boundsX, boundsY,
				boundsWidth, boundsHeight);

		Random rand = new Random();

		for (int i = 0; i < numConns; i++) {
			entitiesToLayout[i].setInternalLocation(
					boundsX + (boundsWidth / entitiesToLayout.length)
							* rand.nextDouble(),
					boundsY + (boundsHeight / entitiesToLayout.length)
							* rand.nextDouble());
		}
	}

	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider) {

	}

	protected int getTotalNumberOfLayoutSteps() {
		return steps;
	}

	protected int getCurrentLayoutStep() {
		return 0;
	}

}
